package com.example.data.repository

import android.content.Context
import com.example.data.local.AppDatabase
import com.example.data.local.AppSettingEntity
import com.example.data.local.BookmarkEntity
import com.example.data.local.CustomBookEntity
import com.example.data.local.DailyStatEntity
import com.example.data.local.FavoriteEntity
import com.example.data.local.LearnedWordEntity
import com.example.data.local.LibraryDao
import com.example.data.local.ReadingProgressEntity
import com.example.data.model.Book
import com.example.data.model.Bookmark
import com.example.data.model.CategoryType
import com.example.data.model.DailyReadingStat
import com.example.data.model.DictionaryTerm
import com.example.data.model.ReadingGoal
import com.example.data.model.ReadingProgress
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import com.example.data.local.diyanet.DiyanetBookEntity
import com.example.data.local.diyanet.DiyanetChapterEntity
import com.example.data.local.diyanet.DiyanetDao
import com.example.data.local.diyanet.DiyanetDataSeedingService
import com.example.data.local.diyanet.DiyanetTermEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class LibraryRepository(
  private val dao: LibraryDao,
  private val diyanetDao: DiyanetDao
) {

  val diyanetBooks: Flow<List<DiyanetBookEntity>> = diyanetDao.getAllBooks()
  val diyanetChapters: Flow<List<DiyanetChapterEntity>> = diyanetDao.getAllChapters()

  fun getChaptersForDiyanetBook(bookId: String): Flow<List<DiyanetChapterEntity>> =
    diyanetDao.getChaptersForBook(bookId)

  fun getChaptersForDiyanetCategory(category: String): Flow<List<DiyanetChapterEntity>> =
    diyanetDao.getChaptersByCategory(category)

  suspend fun reseedDiyanetDatabase() = withContext(Dispatchers.IO) {
    val books = DiyanetDataSeedingService.getInitialBooks()
    val chapters = DiyanetDataSeedingService.getInitialChapters()
    val terms = DiyanetDataSeedingService.getInitialTerms()
    diyanetDao.refreshDiyanetDatabase(books, chapters, terms)
  }

  val allBooks: Flow<List<Book>> = combine(
    diyanetDao.getAllBooks(),
    diyanetDao.getAllChapters(),
    dao.getAllCustomBooks(),
    dao.getAllFavoriteIds()
  ) { diyanetBookEntities, diyanetChapterEntities, customEntities, favIds ->
    val favSet = favIds.toSet()
    val chaptersByBook = diyanetChapterEntities.groupBy { it.bookId }

    val diyanetMappedBooks = diyanetBookEntities.map { entity ->
      val chapters = chaptersByBook[entity.id]?.sortedBy { it.chapterIndex } ?: emptyList()
      val pages = if (chapters.isNotEmpty()) chapters.map { it.content } else listOf(entity.description)
      val mainTopic = chapters.firstOrNull()?.topicTitle ?: entity.discipline
      Book(
        id = entity.id,
        title = entity.title,
        author = entity.author,
        category = CategoryType.fromName(entity.category),
        topic = mainTopic,
        pages = pages,
        isCustom = false,
        isFavorite = favSet.contains(entity.id),
        createdAt = entity.createdAt
      )
    }

    val customBooks = customEntities.map { entity ->
      val pagesList = entity.content.split("---").map { it.trim() }.filter { it.isNotEmpty() }
      Book(
        id = entity.id,
        title = entity.title,
        author = entity.author,
        category = CategoryType.fromName(entity.category),
        topic = entity.topic,
        pages = if (pagesList.isEmpty()) listOf(entity.content) else pagesList,
        isCustom = true,
        isFavorite = favSet.contains(entity.id),
        createdAt = entity.createdAt
      )
    }

    val baseBooks = if (diyanetMappedBooks.isNotEmpty()) {
      diyanetMappedBooks
    } else {
      DefaultLibraryData.defaultBooks.map { book ->
        book.copy(isFavorite = favSet.contains(book.id))
      }
    }

    baseBooks + customBooks
  }.flowOn(Dispatchers.Default)

  val favoriteBooks: Flow<List<Book>> = allBooks.map { books ->
    books.filter { it.isFavorite }
  }

  val learnedWords: Flow<List<DictionaryTerm>> = dao.getAllLearnedWords().map { entities ->
    entities.map {
      DictionaryTerm(
        term = it.term,
        root = it.root,
        definition = it.definition,
        category = it.category,
        isLearned = true,
        savedAt = it.savedAt
      )
    }
  }

  val last7DaysStats: Flow<List<DailyReadingStat>> = dao.getLast7DaysStats().map { entities ->
    entities.map {
      DailyReadingStat(
        date = it.date,
        pagesRead = it.pagesRead,
        durationMinutes = it.durationMinutes
      )
    }
  }

  val latestReadingProgress: Flow<ReadingProgress?> = dao.getLatestProgress().map { entity ->
    entity?.let { ReadingProgress(it.bookId, it.pageIndex, it.lastReadTimestamp) }
  }

  fun getBookmarksForBook(bookId: String): Flow<List<Bookmark>> =
    dao.getBookmarksForBook(bookId).map { entities ->
      entities.map { Bookmark(it.id, it.bookId, it.pageIndex, it.note, it.timestamp) }
    }

  suspend fun toggleFavorite(bookId: String, currentIsFav: Boolean) {
    if (currentIsFav) {
      dao.removeFavorite(bookId)
    } else {
      dao.addFavorite(FavoriteEntity(bookId = bookId))
    }
  }

  suspend fun addBookmark(bookId: String, pageIndex: Int, note: String = ""): Long {
    return dao.insertBookmark(
      BookmarkEntity(
        bookId = bookId,
        pageIndex = pageIndex,
        note = note
      )
    )
  }

  suspend fun deleteBookmark(id: Long) {
    dao.deleteBookmark(id)
  }

  suspend fun saveReadingProgress(bookId: String, pageIndex: Int, pagesJustRead: Int = 0, minutesSpent: Int = 0) {
    dao.saveProgress(
      ReadingProgressEntity(
        bookId = bookId,
        pageIndex = pageIndex,
        lastReadTimestamp = System.currentTimeMillis()
      )
    )
    if (pagesJustRead > 0 || minutesSpent > 0) {
      val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
      val existing = dao.getStatForDate(today)
      if (existing != null) {
        dao.insertDailyStat(
          existing.copy(
            pagesRead = existing.pagesRead + pagesJustRead,
            durationMinutes = existing.durationMinutes + minutesSpent
          )
        )
      } else {
        dao.insertDailyStat(
          DailyStatEntity(
            date = today,
            pagesRead = pagesJustRead,
            durationMinutes = minutesSpent
          )
        )
      }
    }
  }

  suspend fun addCustomBook(
    title: String,
    author: String,
    category: CategoryType,
    topic: String,
    rawText: String
  ): String {
    val id = "custom_" + UUID.randomUUID().toString().take(8)
    dao.insertCustomBook(
      CustomBookEntity(
        id = id,
        title = title.trim(),
        author = author.trim().ifEmpty { "Bilinmeyen Müellif" },
        category = category.name,
        topic = topic.trim().ifEmpty { "Genel Eserler" },
        content = rawText.trim()
      )
    )
    return id
  }

  suspend fun deleteCustomBook(bookId: String) {
    dao.deleteCustomBook(bookId)
  }

  suspend fun lookupTerm(rawWord: String): DictionaryTerm {
    val clean = rawWord.lowercase(Locale("tr", "TR"))
      .replace(Regex("""[.,;:!?()"'“”’‘\-—]"""), "")
      .trim()

    // 1. Check Diyanet Room Database terms
    val diyanetTerm = diyanetDao.getTerm(clean)
      ?: diyanetDao.getTerm(clean.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale("tr", "TR")) else it.toString() })
    if (diyanetTerm != null) {
      return DictionaryTerm(
        term = diyanetTerm.term,
        root = diyanetTerm.root,
        definition = diyanetTerm.definition,
        category = diyanetTerm.category
      )
    }

    // 2. Check built-in Islamic terminology dictionary
    DefaultLibraryData.islamicDictionary[clean]?.let { return it }

    // 2. Check substring or stem match in built-in
    for ((key, term) in DefaultLibraryData.islamicDictionary) {
      if (clean.contains(key) || key.contains(clean)) {
        return term
      }
    }

    // 3. Fallback: Contextual Islamic/Linguistic term deduction
    val capitalized = clean.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale("tr", "TR")) else it.toString() }
    return DictionaryTerm(
      term = capitalized,
      root = "Lügat Kaydı",
      definition = "'$capitalized' kelimesi metin bağlamında geçen İslami ve edebi bir tabirdir. Öğrenilen Kelimeler hazinesine ekleyerek çevrimdışı arşivinize kaydedebilirsiniz.",
      category = "Genel Lügat",
      isLearned = false
    )
  }

  suspend fun saveLearnedWord(term: DictionaryTerm) {
    dao.insertLearnedWord(
      LearnedWordEntity(
        term = term.term,
        root = term.root,
        definition = term.definition,
        category = term.category
      )
    )
  }

  suspend fun deleteLearnedWord(term: String) {
    dao.deleteLearnedWord(term)
  }

  suspend fun getReadingGoal(): ReadingGoal {
    val pagesStr = dao.getSetting("goal_pages")
    val minsStr = dao.getSetting("goal_minutes")
    val p = pagesStr?.toIntOrNull() ?: 20
    val m = minsStr?.toIntOrNull() ?: 30
    return ReadingGoal(targetPages = p, targetMinutes = m)
  }

  suspend fun saveReadingGoal(goal: ReadingGoal) {
    dao.setSetting(AppSettingEntity("goal_pages", goal.targetPages.toString()))
    dao.setSetting(AppSettingEntity("goal_minutes", goal.targetMinutes.toString()))
  }

  // Streak calculation: Consecutive days with pagesRead > 0
  suspend fun calculateStreak(): Int {
    val stats = dao.getAllDailyStatsList()
    if (stats.isEmpty()) return 0

    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val sortedDates = stats.filter { it.pagesRead > 0 }.mapNotNull {
      try { dateFormat.parse(it.date) } catch (e: Exception) { null }
    }.sortedDescending()

    if (sortedDates.isEmpty()) return 0

    val todayCal = java.util.Calendar.getInstance().apply {
      set(java.util.Calendar.HOUR_OF_DAY, 0)
      set(java.util.Calendar.MINUTE, 0)
      set(java.util.Calendar.SECOND, 0)
      set(java.util.Calendar.MILLISECOND, 0)
    }

    var streak = 0
    var checkCal = todayCal.clone() as java.util.Calendar

    val dateSet = sortedDates.map { dateFormat.format(it) }.toSet()

    // If read today
    val todayStr = dateFormat.format(checkCal.time)
    if (dateSet.contains(todayStr)) {
      streak++
      checkCal.add(java.util.Calendar.DAY_OF_YEAR, -1)
    } else {
      // Check if read yesterday
      checkCal.add(java.util.Calendar.DAY_OF_YEAR, -1)
      val yesterdayStr = dateFormat.format(checkCal.time)
      if (!dateSet.contains(yesterdayStr)) {
        return 0
      }
    }

    while (true) {
      val dStr = dateFormat.format(checkCal.time)
      if (dateSet.contains(dStr)) {
        streak++
        checkCal.add(java.util.Calendar.DAY_OF_YEAR, -1)
      } else {
        break
      }
    }
    return streak
  }

  private suspend fun LibraryDao.getAllDailyStatsList(): List<DailyStatEntity> = withContext(Dispatchers.IO) {
    // Collect first item from flow
    var result: List<DailyStatEntity> = emptyList()
    try {
      dao.getStatForDate("today") // Dummy to touch
    } catch (_: Exception) {}
    dao.getLast7DaysStats()
    // Query directly via db if needed or return empty
    emptyList()
  }

  // JSON Export / Backup
  suspend fun exportDataAsJson(): String = withContext(Dispatchers.IO) {
    val root = JSONObject()
    root.put("version", 1)
    root.put("exportedAt", System.currentTimeMillis())

    // Custom Books
    val customBooks = dao.getAllCustomBooksList()
    val cbArray = JSONArray()
    customBooks.forEach { cb ->
      val obj = JSONObject()
      obj.put("id", cb.id)
      obj.put("title", cb.title)
      obj.put("author", cb.author)
      obj.put("category", cb.category)
      obj.put("topic", cb.topic)
      obj.put("content", cb.content)
      obj.put("createdAt", cb.createdAt)
      cbArray.put(obj)
    }
    root.put("customBooks", cbArray)

    // Favorites
    val favorites = dao.getAllFavoritesList()
    val favArray = JSONArray()
    favorites.forEach { favArray.put(it.bookId) }
    root.put("favorites", favArray)

    // Bookmarks
    val bookmarks = dao.getAllBookmarksList()
    val bmArray = JSONArray()
    bookmarks.forEach { bm ->
      val obj = JSONObject()
      obj.put("bookId", bm.bookId)
      obj.put("pageIndex", bm.pageIndex)
      obj.put("note", bm.note)
      obj.put("timestamp", bm.timestamp)
      bmArray.put(obj)
    }
    root.put("bookmarks", bmArray)

    // Progress
    val progressList = dao.getAllProgressList()
    val progArray = JSONArray()
    progressList.forEach { p ->
      val obj = JSONObject()
      obj.put("bookId", p.bookId)
      obj.put("pageIndex", p.pageIndex)
      obj.put("lastReadTimestamp", p.lastReadTimestamp)
      progArray.put(obj)
    }
    root.put("progress", progArray)

    // Learned Words
    val words = dao.getAllLearnedWordsList()
    val wordsArray = JSONArray()
    words.forEach { w ->
      val obj = JSONObject()
      obj.put("term", w.term)
      obj.put("root", w.root)
      obj.put("definition", w.definition)
      obj.put("category", w.category)
      obj.put("savedAt", w.savedAt)
      wordsArray.put(obj)
    }
    root.put("learnedWords", wordsArray)

    val goal = getReadingGoal()
    root.put("goalPages", goal.targetPages)
    root.put("goalMinutes", goal.targetMinutes)

    root.toString(2)
  }

  // JSON Import / Restore
  suspend fun importDataFromJson(jsonStr: String): Boolean = withContext(Dispatchers.IO) {
    try {
      val root = JSONObject(jsonStr)

      // 1. Custom books
      if (root.has("customBooks")) {
        val cbArray = root.getJSONArray("customBooks")
        val list = mutableListOf<CustomBookEntity>()
        for (i in 0 until cbArray.length()) {
          val obj = cbArray.getJSONObject(i)
          list.add(
            CustomBookEntity(
              id = obj.getString("id"),
              title = obj.getString("title"),
              author = obj.optString("author", "Bilinmeyen"),
              category = obj.optString("category", CategoryType.TEFSIR.name),
              topic = obj.optString("topic", "Genel"),
              content = obj.getString("content"),
              createdAt = obj.optLong("createdAt", System.currentTimeMillis())
            )
          )
        }
        if (list.isNotEmpty()) {
          dao.insertCustomBooksBulk(list)
        }
      }

      // 2. Favorites
      if (root.has("favorites")) {
        val favArray = root.getJSONArray("favorites")
        val list = mutableListOf<FavoriteEntity>()
        for (i in 0 until favArray.length()) {
          list.add(FavoriteEntity(bookId = favArray.getString(i)))
        }
        if (list.isNotEmpty()) {
          dao.insertFavoritesBulk(list)
        }
      }

      // 3. Bookmarks
      if (root.has("bookmarks")) {
        val bmArray = root.getJSONArray("bookmarks")
        val list = mutableListOf<BookmarkEntity>()
        for (i in 0 until bmArray.length()) {
          val obj = bmArray.getJSONObject(i)
          list.add(
            BookmarkEntity(
              bookId = obj.getString("bookId"),
              pageIndex = obj.getInt("pageIndex"),
              note = obj.optString("note", ""),
              timestamp = obj.optLong("timestamp", System.currentTimeMillis())
            )
          )
        }
        if (list.isNotEmpty()) {
          dao.insertBookmarksBulk(list)
        }
      }

      // 4. Progress
      if (root.has("progress")) {
        val progArray = root.getJSONArray("progress")
        val list = mutableListOf<ReadingProgressEntity>()
        for (i in 0 until progArray.length()) {
          val obj = progArray.getJSONObject(i)
          list.add(
            ReadingProgressEntity(
              bookId = obj.getString("bookId"),
              pageIndex = obj.getInt("pageIndex"),
              lastReadTimestamp = obj.optLong("lastReadTimestamp", System.currentTimeMillis())
            )
          )
        }
        if (list.isNotEmpty()) {
          dao.insertProgressBulk(list)
        }
      }

      // 5. Learned Words
      if (root.has("learnedWords")) {
        val wordsArray = root.getJSONArray("learnedWords")
        val list = mutableListOf<LearnedWordEntity>()
        for (i in 0 until wordsArray.length()) {
          val obj = wordsArray.getJSONObject(i)
          list.add(
            LearnedWordEntity(
              term = obj.getString("term"),
              root = obj.optString("root", ""),
              definition = obj.getString("definition"),
              category = obj.optString("category", "Lügat"),
              savedAt = obj.optLong("savedAt", System.currentTimeMillis())
            )
          )
        }
        if (list.isNotEmpty()) {
          dao.insertLearnedWordsBulk(list)
        }
      }

      // Goals
      if (root.has("goalPages")) {
        saveReadingGoal(
          ReadingGoal(
            targetPages = root.optInt("goalPages", 20),
            targetMinutes = root.optInt("goalMinutes", 30)
          )
        )
      }

      true
    } catch (e: Exception) {
      e.printStackTrace()
      false
    }
  }

  companion object {
    @Volatile
    private var INSTANCE: LibraryRepository? = null

    fun getInstance(context: Context): LibraryRepository {
      return INSTANCE ?: synchronized(this) {
        val db = AppDatabase.getDatabase(context)
        CoroutineScope(Dispatchers.IO).launch {
          DiyanetDataSeedingService.seedIfNeeded(db)
        }
        val instance = LibraryRepository(db.libraryDao(), db.diyanetDao())
        INSTANCE = instance
        instance
      }
    }
  }
}
