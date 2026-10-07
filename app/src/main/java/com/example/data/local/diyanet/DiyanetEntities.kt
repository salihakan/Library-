package com.example.data.local.diyanet

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Diyanet İşleri Başkanlığı ve İslam Külliyatı kitap/eser varlığı.
 */
@Entity(
  tableName = "diyanet_books",
  indices = [
    Index(value = ["category"]),
    Index(value = ["isOfficialDiyanet"])
  ]
)
data class DiyanetBookEntity(
  @PrimaryKey val id: String,
  val title: String,
  val author: String,
  val publisher: String, // Örn: "Diyanet İşleri Başkanlığı Yayınları"
  val category: String,  // TEFSIR, HADIS, FIKIH, SIYER, ISLAM_TARIHI, AKAID, AHLAK
  val discipline: String,// "Kur'an Yolu ve Tefsir Usulü", "Hadis-i Şerif Külliyatı", "İslam Fıkhı ve İlmihali" vb.
  val description: String,
  val totalVolumes: Int = 1,
  val publicationYear: Int = 2024,
  val isbn: String = "",
  val isOfficialDiyanet: Boolean = true,
  val createdAt: Long = System.currentTimeMillis()
)

/**
 * Bir esere ait bölümler, fasıllar ve konular.
 * Sağ paneldeki konu ve fasıl mimarisini doğrudan modelleyen ana yapı.
 */
@Entity(
  tableName = "diyanet_chapters",
  foreignKeys = [
    ForeignKey(
      entity = DiyanetBookEntity::class,
      parentColumns = ["id"],
      childColumns = ["bookId"],
      onDelete = ForeignKey.CASCADE
    )
  ],
  indices = [
    Index(value = ["bookId"]),
    Index(value = ["category"]),
    Index(value = ["bookId", "chapterIndex"])
  ]
)
data class DiyanetChapterEntity(
  @PrimaryKey val id: String,
  val bookId: String,
  val chapterIndex: Int, // 0, 1, 2...
  val topicTitle: String, // "1. Konu: Fâtiha Sûresi ve İstiane Tefsiri"
  val subTopic: String = "",
  val arabicTitle: String = "", // Arapça başlık, besmele veya hadis metni
  val content: String, // Faslın tam metni
  val pageNumber: Int = 1,
  val sourceReference: String = "", // "Bakara Sûresi 255. Âyet", "Buhârî, Bed'ü'l-Vahy 1"
  val category: String, // Kitabın kategorisiyle uyumlu (Tefsir, Hadis, Fıkıh vb.)
  val orderIndex: Int = 0
)

/**
 * Diyanet İslami terimler ve lügat varlığı.
 */
@Entity(
  tableName = "diyanet_terms",
  indices = [
    Index(value = ["category"])
  ]
)
data class DiyanetTermEntity(
  @PrimaryKey val term: String,
  val root: String,
  val definition: String,
  val category: String,
  val sourceBook: String = "DİA / Diyanet Neşriyatı"
)
