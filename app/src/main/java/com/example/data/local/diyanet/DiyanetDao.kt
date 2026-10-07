package com.example.data.local.diyanet

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface DiyanetDao {

  // ==========================================
  // KİTAPLAR (BOOKS)
  // ==========================================
  @Query("SELECT * FROM diyanet_books ORDER BY createdAt ASC")
  fun getAllBooks(): Flow<List<DiyanetBookEntity>>

  @Query("SELECT * FROM diyanet_books WHERE category = :category ORDER BY createdAt ASC")
  fun getBooksByCategory(category: String): Flow<List<DiyanetBookEntity>>

  @Query("SELECT * FROM diyanet_books WHERE isOfficialDiyanet = 1 ORDER BY createdAt ASC")
  fun getOfficialDiyanetBooks(): Flow<List<DiyanetBookEntity>>

  @Query("SELECT * FROM diyanet_books WHERE id = :id LIMIT 1")
  suspend fun getBookById(id: String): DiyanetBookEntity?

  @Query("SELECT COUNT(*) FROM diyanet_books")
  suspend fun getBookCount(): Int

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertBooks(books: List<DiyanetBookEntity>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertBook(book: DiyanetBookEntity)

  // ==========================================
  // BÖLÜMLER & FASILLAR (CHAPTERS & TOPICS)
  // ==========================================
  @Query("SELECT * FROM diyanet_chapters WHERE bookId = :bookId ORDER BY chapterIndex ASC")
  fun getChaptersForBook(bookId: String): Flow<List<DiyanetChapterEntity>>

  @Query("SELECT * FROM diyanet_chapters WHERE bookId = :bookId ORDER BY chapterIndex ASC")
  suspend fun getChaptersForBookList(bookId: String): List<DiyanetChapterEntity>

  @Query("SELECT * FROM diyanet_chapters WHERE category = :category ORDER BY orderIndex ASC, chapterIndex ASC")
  fun getChaptersByCategory(category: String): Flow<List<DiyanetChapterEntity>>

  @Query("SELECT * FROM diyanet_chapters ORDER BY orderIndex ASC, chapterIndex ASC")
  fun getAllChapters(): Flow<List<DiyanetChapterEntity>>

  @Query("SELECT * FROM diyanet_chapters WHERE id = :chapterId LIMIT 1")
  suspend fun getChapterById(chapterId: String): DiyanetChapterEntity?

  @Query("SELECT COUNT(*) FROM diyanet_chapters")
  suspend fun getChapterCount(): Int

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertChapters(chapters: List<DiyanetChapterEntity>)

  // ==========================================
  // ARAMA (SEARCH)
  // ==========================================
  @Query("""
    SELECT * FROM diyanet_chapters 
    WHERE topicTitle LIKE '%' || :query || '%' 
       OR content LIKE '%' || :query || '%' 
       OR subTopic LIKE '%' || :query || '%'
       OR sourceReference LIKE '%' || :query || '%'
    ORDER BY chapterIndex ASC
  """)
  fun searchChapters(query: String): Flow<List<DiyanetChapterEntity>>

  // ==========================================
  // LÜGAT (TERMS)
  // ==========================================
  @Query("SELECT * FROM diyanet_terms ORDER BY term ASC")
  fun getAllTerms(): Flow<List<DiyanetTermEntity>>

  @Query("SELECT * FROM diyanet_terms WHERE term = :term LIMIT 1")
  suspend fun getTerm(term: String): DiyanetTermEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertTerms(terms: List<DiyanetTermEntity>)

  // ==========================================
  // TOPLU SİLME & YENİDEN YÜKLEME
  // ==========================================
  @Query("DELETE FROM diyanet_books")
  suspend fun clearBooks()

  @Query("DELETE FROM diyanet_chapters")
  suspend fun clearChapters()

  @Query("DELETE FROM diyanet_terms")
  suspend fun clearTerms()

  @Transaction
  suspend fun refreshDiyanetDatabase(
    books: List<DiyanetBookEntity>,
    chapters: List<DiyanetChapterEntity>,
    terms: List<DiyanetTermEntity>
  ) {
    clearChapters()
    clearBooks()
    insertBooks(books)
    insertChapters(chapters)
    insertTerms(terms)
  }
}
