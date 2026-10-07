package com.example.data.local

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.local.diyanet.DiyanetBookEntity
import com.example.data.local.diyanet.DiyanetChapterEntity
import com.example.data.local.diyanet.DiyanetDao
import com.example.data.local.diyanet.DiyanetDataSeedingService
import com.example.data.local.diyanet.DiyanetTermEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "custom_books")
data class CustomBookEntity(
  @PrimaryKey val id: String,
  val title: String,
  val author: String,
  val category: String,
  val topic: String,
  val content: String,
  val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "favorites")
data class FavoriteEntity(
  @PrimaryKey val bookId: String,
  val addedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val bookId: String,
  val pageIndex: Int,
  val note: String,
  val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "reading_progress")
data class ReadingProgressEntity(
  @PrimaryKey val bookId: String,
  val pageIndex: Int,
  val lastReadTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "learned_words")
data class LearnedWordEntity(
  @PrimaryKey val term: String,
  val root: String,
  val definition: String,
  val category: String,
  val savedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "daily_stats")
data class DailyStatEntity(
  @PrimaryKey val date: String,
  val pagesRead: Int,
  val durationMinutes: Int
)

@Entity(tableName = "app_settings")
data class AppSettingEntity(
  @PrimaryKey val key: String,
  val value: String
)

@Dao
interface LibraryDao {
  // Custom Books
  @Query("SELECT * FROM custom_books ORDER BY createdAt DESC")
  fun getAllCustomBooks(): Flow<List<CustomBookEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertCustomBook(book: CustomBookEntity)

  @Query("DELETE FROM custom_books WHERE id = :id")
  suspend fun deleteCustomBook(id: String)

  // Favorites
  @Query("SELECT bookId FROM favorites")
  fun getAllFavoriteIds(): Flow<List<String>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun addFavorite(favorite: FavoriteEntity)

  @Query("DELETE FROM favorites WHERE bookId = :bookId")
  suspend fun removeFavorite(bookId: String)

  @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE bookId = :bookId)")
  fun isFavorite(bookId: String): Flow<Boolean>

  // Bookmarks & Notes
  @Query("SELECT * FROM bookmarks WHERE bookId = :bookId ORDER BY pageIndex ASC, timestamp DESC")
  fun getBookmarksForBook(bookId: String): Flow<List<BookmarkEntity>>

  @Query("SELECT * FROM bookmarks ORDER BY timestamp DESC")
  fun getAllBookmarks(): Flow<List<BookmarkEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertBookmark(bookmark: BookmarkEntity): Long

  @Query("DELETE FROM bookmarks WHERE id = :id")
  suspend fun deleteBookmark(id: Long)

  @Query("DELETE FROM bookmarks WHERE bookId = :bookId AND pageIndex = :pageIndex AND (note IS NULL OR note = '')")
  suspend fun removeSimpleBookmark(bookId: String, pageIndex: Int)

  // Reading Progress
  @Query("SELECT * FROM reading_progress ORDER BY lastReadTimestamp DESC")
  fun getAllProgress(): Flow<List<ReadingProgressEntity>>

  @Query("SELECT * FROM reading_progress WHERE bookId = :bookId LIMIT 1")
  suspend fun getProgressForBook(bookId: String): ReadingProgressEntity?

  @Query("SELECT * FROM reading_progress ORDER BY lastReadTimestamp DESC LIMIT 1")
  fun getLatestProgress(): Flow<ReadingProgressEntity?>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun saveProgress(progress: ReadingProgressEntity)

  // Learned Words
  @Query("SELECT * FROM learned_words ORDER BY savedAt DESC")
  fun getAllLearnedWords(): Flow<List<LearnedWordEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertLearnedWord(word: LearnedWordEntity)

  @Query("DELETE FROM learned_words WHERE term = :term")
  suspend fun deleteLearnedWord(term: String)

  // Daily Stats
  @Query("SELECT * FROM daily_stats ORDER BY date DESC LIMIT 7")
  fun getLast7DaysStats(): Flow<List<DailyStatEntity>>

  @Query("SELECT * FROM daily_stats ORDER BY date DESC")
  fun getAllDailyStats(): Flow<List<DailyStatEntity>>

  @Query("SELECT * FROM daily_stats WHERE date = :date LIMIT 1")
  suspend fun getStatForDate(date: String): DailyStatEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertDailyStat(stat: DailyStatEntity)

  // Settings
  @Query("SELECT value FROM app_settings WHERE `key` = :key LIMIT 1")
  suspend fun getSetting(key: String): String?

  @Query("SELECT value FROM app_settings WHERE `key` = :key LIMIT 1")
  fun getSettingFlow(key: String): Flow<String?>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun setSetting(setting: AppSettingEntity)

  // Bulk Operations for JSON Restore
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertCustomBooksBulk(books: List<CustomBookEntity>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertFavoritesBulk(favorites: List<FavoriteEntity>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertBookmarksBulk(bookmarks: List<BookmarkEntity>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertProgressBulk(progressList: List<ReadingProgressEntity>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertLearnedWordsBulk(words: List<LearnedWordEntity>)

  @Query("SELECT * FROM custom_books")
  suspend fun getAllCustomBooksList(): List<CustomBookEntity>

  @Query("SELECT * FROM favorites")
  suspend fun getAllFavoritesList(): List<FavoriteEntity>

  @Query("SELECT * FROM bookmarks")
  suspend fun getAllBookmarksList(): List<BookmarkEntity>

  @Query("SELECT * FROM reading_progress")
  suspend fun getAllProgressList(): List<ReadingProgressEntity>

  @Query("SELECT * FROM learned_words")
  suspend fun getAllLearnedWordsList(): List<LearnedWordEntity>
}

@Database(
  entities = [
    CustomBookEntity::class,
    FavoriteEntity::class,
    BookmarkEntity::class,
    ReadingProgressEntity::class,
    LearnedWordEntity::class,
    DailyStatEntity::class,
    AppSettingEntity::class,
    DiyanetBookEntity::class,
    DiyanetChapterEntity::class,
    DiyanetTermEntity::class
  ],
  version = 2,
  exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
  abstract fun libraryDao(): LibraryDao
  abstract fun diyanetDao(): DiyanetDao

  companion object {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          AppDatabase::class.java,
          "islamic_library.db"
        )
          .fallbackToDestructiveMigration()
          .addCallback(object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
              super.onCreate(db)
              CoroutineScope(Dispatchers.IO).launch {
                INSTANCE?.let { DiyanetDataSeedingService.seedIfNeeded(it) }
              }
            }

            override fun onOpen(db: SupportSQLiteDatabase) {
              super.onOpen(db)
              CoroutineScope(Dispatchers.IO).launch {
                INSTANCE?.let { DiyanetDataSeedingService.seedIfNeeded(it) }
              }
            }
          })
          .build()
        INSTANCE = instance
        instance
      }
    }
  }
}
