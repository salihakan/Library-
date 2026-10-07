package com.example.data.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.ReaderDarkBg
import com.example.ui.theme.ReaderDarkText
import com.example.ui.theme.ReaderLightBg
import com.example.ui.theme.ReaderLightText
import com.example.ui.theme.ReaderNightBg
import com.example.ui.theme.ReaderNightText
import com.example.ui.theme.ReaderSepiaBg
import com.example.ui.theme.ReaderSepiaText

enum class CategoryType(val title: String, val subtitle: String) {
  TEFSIR("Tefsir", "Kur'an Mealleri ve Tefsir Usulü"),
  HADIS("Hadis", "Hadis-i Şerifler ve Kütüb-i Sitte"),
  FIKIH("Fıkıh", "İslam İlmihali ve Şer'i Hükümler"),
  SIYER("Siyer", "Asr-ı Saadet ve Nebi'nin (s.a.v.) Hayatı"),
  ISLAM_TARIHI("İslam Tarihi", "Peygamberler ve İslam Medeniyeti"),
  AKAID("Akaid", "İslam İnanç Esasları ve Kelam"),
  AHLAK("Ahlak", "Tasavvuf, Nefis Terbiyesi ve Hikmet");

  companion object {
    fun fromName(name: String): CategoryType {
      return entries.find { it.name.equals(name, ignoreCase = true) || it.title.equals(name, ignoreCase = true) }
        ?: TEFSIR
    }
  }
}

data class Book(
  val id: String,
  val title: String,
  val author: String,
  val category: CategoryType,
  val topic: String,
  val pages: List<String>,
  val isCustom: Boolean = false,
  val isFavorite: Boolean = false,
  val createdAt: Long = System.currentTimeMillis()
) {
  val pageCount: Int get() = pages.size.coerceAtLeast(1)
}

data class Bookmark(
  val id: Long = 0,
  val bookId: String,
  val pageIndex: Int,
  val note: String = "",
  val timestamp: Long = System.currentTimeMillis()
)

data class ReadingProgress(
  val bookId: String,
  val pageIndex: Int,
  val lastReadTimestamp: Long = System.currentTimeMillis()
)

data class DictionaryTerm(
  val term: String,
  val root: String,
  val definition: String,
  val category: String,
  val isLearned: Boolean = false,
  val savedAt: Long = System.currentTimeMillis()
)

data class ReadingGoal(
  val targetPages: Int = 20,
  val targetMinutes: Int = 30
)

data class DailyReadingStat(
  val date: String, // Format: YYYY-MM-DD
  val pagesRead: Int = 0,
  val durationMinutes: Int = 0
)

enum class ReaderTheme(val label: String, val bg: Color, val text: Color) {
  LIGHT("Açık", ReaderLightBg, ReaderLightText),
  SEPIA("Sepya", ReaderSepiaBg, ReaderSepiaText),
  DARK("Koyu", ReaderDarkBg, ReaderDarkText),
  NIGHT("Gece", ReaderNightBg, ReaderNightText)
}

enum class ReaderFont(val label: String) {
  AMIRI("Amiri (Klasik)"),
  SERIF("Serif"),
  SANS_SERIF("Sans-Serif")
}

data class ReaderSettings(
  val fontSizeSp: Float = 18f,
  val lineSpacingMultiplier: Float = 1.6f,
  val theme: ReaderTheme = ReaderTheme.LIGHT,
  val font: ReaderFont = ReaderFont.AMIRI,
  val ttsSpeed: Float = 1.0f
)
