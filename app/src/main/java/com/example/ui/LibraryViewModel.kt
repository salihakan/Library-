package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.Book
import com.example.data.model.Bookmark
import com.example.data.model.CategoryType
import com.example.data.model.DailyReadingStat
import com.example.data.model.DictionaryTerm
import com.example.data.model.ReaderFont
import com.example.data.model.ReaderSettings
import com.example.data.model.ReaderTheme
import com.example.data.model.ReadingGoal
import com.example.data.model.ReadingProgress
import com.example.data.repository.LibraryRepository
import com.example.util.TtsManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface AppScreen {
  data object Home : AppScreen
  data class Reader(val bookId: String, val initialPage: Int = 0) : AppScreen
  data object Search : AppScreen
  data object Favorites : AppScreen
  data object Stats : AppScreen
  data object AddBook : AppScreen
  data object LearnedWords : AppScreen
}

data class SearchResultItem(
  val book: Book,
  val matchedPage: Int,
  val snippet: String
)

class LibraryViewModel(application: Application) : AndroidViewModel(application) {

  val repository = LibraryRepository.getInstance(application)
  val ttsManager = TtsManager(application)

  private val _currentScreen = MutableStateFlow<AppScreen>(AppScreen.Home)
  val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

  private val _selectedCategory = MutableStateFlow(CategoryType.TEFSIR)
  val selectedCategory: StateFlow<CategoryType> = _selectedCategory.asStateFlow()

  private val _selectedTopic = MutableStateFlow<String?>(null)
  val selectedTopic: StateFlow<String?> = _selectedTopic.asStateFlow()

  private val _selectedBookId = MutableStateFlow<String?>(null)
  val selectedBookId: StateFlow<String?> = _selectedBookId.asStateFlow()

  // Books reactive flow
  val allBooks: StateFlow<List<Book>> = repository.allBooks
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val favoriteBooks: StateFlow<List<Book>> = repository.favoriteBooks
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val latestProgress: StateFlow<ReadingProgress?> = repository.latestReadingProgress
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

  val learnedWords: StateFlow<List<DictionaryTerm>> = repository.learnedWords
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val last7DaysStats: StateFlow<List<DailyReadingStat>> = repository.last7DaysStats
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  // Reading Goal
  private val _readingGoal = MutableStateFlow(ReadingGoal(targetPages = 20, targetMinutes = 30))
  val readingGoal: StateFlow<ReadingGoal> = _readingGoal.asStateFlow()

  private val _streak = MutableStateFlow(0)
  val streak: StateFlow<Int> = _streak.asStateFlow()

  // Reader Specific State
  private val _activeBookId = MutableStateFlow<String?>(null)
  val activeBookId: StateFlow<String?> = _activeBookId.asStateFlow()

  private val _currentPageIndex = MutableStateFlow(0)
  val currentPageIndex: StateFlow<Int> = _currentPageIndex.asStateFlow()

  private val _readerSettings = MutableStateFlow(ReaderSettings())
  val readerSettings: StateFlow<ReaderSettings> = _readerSettings.asStateFlow()

  private val _readerSearchQuery = MutableStateFlow("")
  val readerSearchQuery: StateFlow<String> = _readerSearchQuery.asStateFlow()

  private val _activeLookupTerm = MutableStateFlow<DictionaryTerm?>(null)
  val activeLookupTerm: StateFlow<DictionaryTerm?> = _activeLookupTerm.asStateFlow()

  private val _isLookingUp = MutableStateFlow(false)
  val isLookingUp: StateFlow<Boolean> = _isLookingUp.asStateFlow()

  // Global Search
  private val _globalSearchQuery = MutableStateFlow("")
  val globalSearchQuery: StateFlow<String> = _globalSearchQuery.asStateFlow()

  private val _messageSnackbar = MutableStateFlow<String?>(null)
  val messageSnackbar: StateFlow<String?> = _messageSnackbar.asStateFlow()

  // Reading session timing
  private var sessionStartTime: Long = 0L

  init {
    viewModelScope.launch {
      val goal = repository.getReadingGoal()
      _readingGoal.value = goal
      refreshStreak()
    }
  }

  fun navigateTo(screen: AppScreen) {
    if (screen !is AppScreen.Reader && _currentScreen.value is AppScreen.Reader) {
      stopReadingSession()
    }
    _currentScreen.value = screen
  }

  fun selectCategory(category: CategoryType) {
    _selectedCategory.value = category
    _selectedTopic.value = null // reset topic filter on category change
    _selectedBookId.value = null
  }

  fun selectTopic(topic: String?) {
    _selectedTopic.value = topic
  }

  fun selectBook(bookId: String?) {
    _selectedBookId.value = bookId
  }

  fun reseedDiyanetDatabase() {
    viewModelScope.launch {
      repository.reseedDiyanetDatabase()
      _messageSnackbar.value = "Diyanet külliyatları Room veritabanına kaydedildi."
    }
  }

  fun toggleFavorite(bookId: String) {
    viewModelScope.launch {
      val book = allBooks.value.find { it.id == bookId }
      val currentFav = book?.isFavorite ?: false
      repository.toggleFavorite(bookId, currentFav)
    }
  }

  fun openBook(bookId: String, initialPage: Int = 0) {
    _activeBookId.value = bookId
    _currentPageIndex.value = initialPage
    _readerSearchQuery.value = ""
    sessionStartTime = System.currentTimeMillis()
    _currentScreen.value = AppScreen.Reader(bookId, initialPage)
  }

  fun onPageChanged(newIndex: Int) {
    val book = currentActiveBook() ?: return
    val clamped = newIndex.coerceIn(0, (book.pages.size - 1).coerceAtLeast(0))
    if (clamped != _currentPageIndex.value) {
      _currentPageIndex.value = clamped
      if (ttsManager.isSpeaking) {
        ttsManager.speak(book.pages.getOrElse(clamped) { "" })
      }
      viewModelScope.launch {
        repository.saveReadingProgress(book.id, clamped, pagesJustRead = 1, minutesSpent = 1)
        refreshStreak()
      }
    }
  }

  fun toggleTts() {
    val book = currentActiveBook() ?: return
    val pageText = book.pages.getOrElse(_currentPageIndex.value) { "" }
    ttsManager.toggle(pageText)
  }

  fun setTtsSpeed(speed: Float) {
    _readerSettings.value = _readerSettings.value.copy(ttsSpeed = speed)
    ttsManager.setRate(speed)
  }

  fun setReaderTheme(theme: ReaderTheme) {
    _readerSettings.value = _readerSettings.value.copy(theme = theme)
  }

  fun setReaderFontSize(sizeSp: Float) {
    val clamped = sizeSp.coerceIn(12f, 32f)
    _readerSettings.value = _readerSettings.value.copy(fontSizeSp = clamped)
  }

  fun setReaderFont(font: ReaderFont) {
    _readerSettings.value = _readerSettings.value.copy(font = font)
  }

  fun setReaderLineSpacing(multiplier: Float) {
    _readerSettings.value = _readerSettings.value.copy(lineSpacingMultiplier = multiplier)
  }

  fun setReaderSearchQuery(query: String) {
    _readerSearchQuery.value = query
  }

  fun addBookmarkForCurrentPage(note: String = "") {
    val book = currentActiveBook() ?: return
    val page = _currentPageIndex.value
    viewModelScope.launch {
      repository.addBookmark(book.id, page, note)
      _messageSnackbar.value = if (note.isEmpty()) "Sayfa ${page + 1} yer imlerine eklendi" else "Notunuz sayfaya kaydedildi"
    }
  }

  fun deleteBookmark(id: Long) {
    viewModelScope.launch {
      repository.deleteBookmark(id)
      _messageSnackbar.value = "Yer imi kaldırıldı"
    }
  }

  fun lookupWord(word: String) {
    if (word.isBlank()) return
    viewModelScope.launch {
      _isLookingUp.value = true
      val term = repository.lookupTerm(word)
      _activeLookupTerm.value = term
      _isLookingUp.value = false
    }
  }

  fun closeLookupCard() {
    _activeLookupTerm.value = null
  }

  fun saveActiveLookupWord() {
    val term = _activeLookupTerm.value ?: return
    viewModelScope.launch {
      repository.saveLearnedWord(term)
      _activeLookupTerm.value = term.copy(isLearned = true)
      _messageSnackbar.value = "'${term.term}' öğrenilen kelimelere eklendi"
    }
  }

  fun deleteLearnedWord(term: String) {
    viewModelScope.launch {
      repository.deleteLearnedWord(term)
      _messageSnackbar.value = "Kelime listeden silindi"
    }
  }

  fun setGlobalSearchQuery(q: String) {
    _globalSearchQuery.value = q
  }

  fun updateReadingGoal(pages: Int, minutes: Int) {
    val newGoal = ReadingGoal(targetPages = pages, targetMinutes = minutes)
    _readingGoal.value = newGoal
    viewModelScope.launch {
      repository.saveReadingGoal(newGoal)
      _messageSnackbar.value = "Günlük hedef güncellendi"
    }
  }

  fun addNewBook(
    title: String,
    author: String,
    category: CategoryType,
    topic: String,
    content: String,
    onSuccess: () -> Unit
  ) {
    if (title.isBlank() || content.isBlank()) {
      _messageSnackbar.value = "Lütfen başlık ve metin giriniz"
      return
    }
    viewModelScope.launch {
      repository.addCustomBook(title, author, category, topic, content)
      _messageSnackbar.value = "Yeni eser kütüphaneye başarıyla eklendi"
      onSuccess()
    }
  }

  fun exportJson(): String? {
    var result: String? = null
    // synchronous helper or launch
    return result
  }

  suspend fun getExportJsonString(): String {
    return repository.exportDataAsJson()
  }

  fun importJsonData(json: String, onResult: (Boolean) -> Unit) {
    viewModelScope.launch {
      val success = repository.importDataFromJson(json)
      if (success) {
        _messageSnackbar.value = "Yedek başarıyla geri yüklendi"
      } else {
        _messageSnackbar.value = "Yedek dosyası okunamadı veya hatalı"
      }
      onResult(success)
    }
  }

  fun clearSnackbar() {
    _messageSnackbar.value = null
  }

  private fun stopReadingSession() {
    ttsManager.stop()
    val book = currentActiveBook()
    if (book != null && sessionStartTime > 0) {
      val elapsedMinutes = ((System.currentTimeMillis() - sessionStartTime) / 60000L).toInt().coerceAtLeast(1)
      viewModelScope.launch {
        repository.saveReadingProgress(book.id, _currentPageIndex.value, pagesJustRead = 1, minutesSpent = elapsedMinutes)
        refreshStreak()
      }
    }
    sessionStartTime = 0
  }

  private fun refreshStreak() {
    viewModelScope.launch {
      _streak.value = repository.calculateStreak()
    }
  }

  fun currentActiveBook(): Book? {
    val id = _activeBookId.value ?: return null
    return allBooks.value.find { it.id == id }
  }

  override fun onCleared() {
    super.onCleared()
    ttsManager.release()
  }
}
