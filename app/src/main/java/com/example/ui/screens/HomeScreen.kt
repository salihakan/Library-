package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Book
import com.example.data.model.CategoryType
import com.example.ui.AppScreen
import com.example.ui.LibraryViewModel
import com.example.ui.components.BackupRestoreDialog
import com.example.ui.theme.EmeraldContainerLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.ImperialGold
import com.example.ui.theme.ImperialGoldDark
import com.example.ui.theme.ImperialGoldLight
import com.example.ui.theme.ParchmentBg
import com.example.ui.theme.ParchmentBorder
import com.example.ui.theme.ParchmentCard
import com.example.ui.theme.RoyalEmerald
import com.example.ui.theme.RoyalEmeraldDark
import com.example.ui.theme.RoyalEmeraldLight
import com.example.ui.theme.WarmCopper

data class ChapterTopicItem(
  val bookId: String,
  val bookTitle: String,
  val bookAuthor: String,
  val pageIndex: Int,
  val topicTitle: String,
  val snippet: String,
  val totalPages: Int,
  val isFavorite: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
  viewModel: LibraryViewModel,
  onOpenReader: (String, Int) -> Unit
) {
  val allBooks by viewModel.allBooks.collectAsState()
  val selectedCategory by viewModel.selectedCategory.collectAsState()
  val selectedBookId by viewModel.selectedBookId.collectAsState()
  val latestProgress by viewModel.latestProgress.collectAsState()
  val streak by viewModel.streak.collectAsState()

  var showBackupDialog by remember { mutableStateOf(false) }

  // Books in selected category
  val categoryBooks = remember(allBooks, selectedCategory) {
    allBooks.filter { it.category == selectedCategory }
  }

  // Active book resolution for left panel
  val activeSelectedBook = remember(categoryBooks, selectedBookId) {
    categoryBooks.find { it.id == selectedBookId }
  }

  // Filtered books to generate topic items
  val booksToDisplay = remember(categoryBooks, activeSelectedBook) {
    if (activeSelectedBook != null) listOf(activeSelectedBook)
    else categoryBooks
  }

  // Flattened chapters/topics for the right panel
  val chapterTopics = remember(booksToDisplay) {
    val list = mutableListOf<ChapterTopicItem>()
    booksToDisplay.forEach { book ->
      book.pages.forEachIndexed { pageIdx, pageText ->
        val topic = extractTopicFromPage(pageText, book.topic, pageIdx)
        val snippet = extractSnippetFromPage(pageText)
        list.add(
          ChapterTopicItem(
            bookId = book.id,
            bookTitle = book.title,
            bookAuthor = book.author,
            pageIndex = pageIdx,
            topicTitle = topic,
            snippet = snippet,
            totalPages = book.pageCount,
            isFavorite = book.isFavorite
          )
        )
      }
    }
    list
  }

  // Last read book resolution
  val latestBook = remember(allBooks, latestProgress) {
    latestProgress?.let { p -> allBooks.find { it.id == p.bookId } }
  }

  Scaffold(
    bottomBar = {
      // Elevated Royal Floating Bottom Navigation Bar
      RoyalIslamicBottomNavigation(
        selectedCategory = selectedCategory,
        onCategorySelect = { viewModel.selectCategory(it) }
      )
    },
    containerColor = ParchmentBg
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {

      // 1. ROYAL ATELIER APP BAR & HERO HEADER
      Surface(
        color = RoyalEmeraldDark,
        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
        shadowElevation = 8.dp,
        modifier = Modifier.fillMaxWidth()
      ) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .background(
              Brush.verticalGradient(
                colors = listOf(RoyalEmerald, RoyalEmeraldDark)
              )
            )
            .padding(top = 12.dp, bottom = 14.dp, start = 16.dp, end = 16.dp)
        ) {
          Column {
            // Top Row: Title, Emblem & Action Icons
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                // Calligraphic Arch Emblem
                Box(
                  modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                      Brush.linearGradient(listOf(ImperialGold, WarmCopper))
                    )
                    .border(1.5.dp, ImperialGoldLight, RoundedCornerShape(14.dp)),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.Default.AutoStories,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                  )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                  Text(
                    text = "İslâm Kütüphanesi",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = ImperialGoldLight,
                    letterSpacing = 0.5.sp
                  )
                  Text(
                    text = "Dârü'l-Kütüb & Muhtasar Okuyucu",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.8f)
                  )
                }
              }

              // Streak Chip Badge
              Surface(
                color = Color.Black.copy(alpha = 0.35f),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, ImperialGold.copy(alpha = 0.5f))
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Icon(
                    imageVector = Icons.Default.LocalFireDepartment,
                    contentDescription = null,
                    tint = ImperialGold,
                    modifier = Modifier.size(16.dp)
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(
                    text = "$streak Gün",
                    color = Color.White,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Quick Actions Ribbon
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              HeaderActionChip(
                icon = Icons.Default.Search,
                label = "Ara",
                testTag = "home_search_btn",
                onClick = { viewModel.navigateTo(AppScreen.Search) }
              )
              HeaderActionChip(
                icon = Icons.Default.Favorite,
                label = "Favoriler",
                testTag = "home_favorites_btn",
                onClick = { viewModel.navigateTo(AppScreen.Favorites) }
              )
              HeaderActionChip(
                icon = Icons.Default.BarChart,
                label = "İstatistik",
                testTag = "home_stats_btn",
                onClick = { viewModel.navigateTo(AppScreen.Stats) }
              )
              HeaderActionChip(
                icon = Icons.Default.Translate,
                label = "Lügat",
                testTag = "home_learned_words_btn",
                onClick = { viewModel.navigateTo(AppScreen.LearnedWords) }
              )
              HeaderActionChip(
                icon = Icons.Default.Add,
                label = "Eser Ekle",
                testTag = "home_add_book_btn",
                onClick = { viewModel.navigateTo(AppScreen.AddBook) }
              )
              HeaderActionChip(
                icon = Icons.Default.CloudSync,
                label = "Yedek",
                testTag = "home_backup_btn",
                onClick = { showBackupDialog = true }
              )
            }
          }
        }
      }

      // 2. HERO "SON OKUNAN" MANUSCRIPT CARD (Sol: Geçtiği Kitap | Sağ: Konu)
      if (latestBook != null && latestProgress != null) {
        val totalPages = latestBook.pageCount
        val curPage = latestProgress!!.pageIndex
        val progressRatio = if (totalPages > 0) ((curPage + 1).toFloat() / totalPages).coerceIn(0f, 1f) else 0f
        val progressPercent = (progressRatio * 100).toInt()
        val latestPageText = latestBook.pages.getOrElse(curPage) { "" }
        val currentTopic = extractTopicFromPage(latestPageText, latestBook.topic, curPage)

        Card(
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = ParchmentCard),
          elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .border(1.dp, ParchmentBorder, RoundedCornerShape(18.dp))
            .testTag("last_read_card")
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(12.dp)
          ) {
            // Label Header: Sol tarafta Geçtiği Kitap, Sağ tarafta Konu
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
              ) {
                Surface(
                  color = RoyalEmerald.copy(alpha = 0.12f),
                  shape = RoundedCornerShape(6.dp)
                ) {
                  Text(
                    text = "GEÇTİĞİ KİTAP: ${latestBook.title}",
                    style = MaterialTheme.typography.labelSmall,
                    color = RoyalEmeraldDark,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                  )
                }
              }

              Spacer(modifier = Modifier.width(6.dp))

              Surface(
                color = ImperialGold.copy(alpha = 0.2f),
                shape = RoundedCornerShape(6.dp)
              ) {
                Text(
                  text = "KONU: $currentTopic",
                  style = MaterialTheme.typography.labelSmall,
                  color = ImperialGoldDark,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis
                )
              }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically
            ) {
              // Spine Graphic
              Box(
                modifier = Modifier
                  .width(36.dp)
                  .height(48.dp)
                  .clip(RoundedCornerShape(6.dp))
                  .background(
                    Brush.horizontalGradient(
                      listOf(RoyalEmeraldDark, RoyalEmerald, RoyalEmeraldLight)
                    )
                  )
                  .border(1.dp, ImperialGold.copy(alpha = 0.6f), RoundedCornerShape(6.dp)),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Bookmark,
                  contentDescription = null,
                  tint = ImperialGoldLight,
                  modifier = Modifier.size(20.dp)
                )
              }

              Spacer(modifier = Modifier.width(10.dp))

              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = "${latestBook.author} • Sayfa ${curPage + 1} / $totalPages (%$progressPercent)",
                  style = MaterialTheme.typography.bodySmall,
                  color = Color.Gray
                )
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                  progress = { progressRatio },
                  modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .clip(RoundedCornerShape(3.dp)),
                  color = ImperialGold,
                  trackColor = ParchmentBorder
                )
              }

              Spacer(modifier = Modifier.width(10.dp))

              Button(
                onClick = { onOpenReader(latestBook.id, curPage) },
                colors = ButtonDefaults.buttonColors(containerColor = RoyalEmerald),
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                modifier = Modifier.testTag("resume_reading_btn")
              ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(15.dp), tint = ImperialGoldLight)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Devam", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
              }
            }
          }
        }
      }

      // 3. ÇİFT PANELLİ DÜZEN (Sol Panel: Geçtiği Kitap | Sağ Panel: Konu & Fasıllar)
      Row(
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 8.dp)
      ) {

        // SOL PANEL: GEÇTİĞİ KİTAP (Eserler Listesi)
        Surface(
          color = Color.White.copy(alpha = 0.7f),
          shape = RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, ParchmentBorder),
          modifier = Modifier
            .fillMaxHeight()
            .width(140.dp)
            .testTag("left_panel_books")
        ) {
          LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp, horizontal = 6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            item {
              Row(
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Box(
                  modifier = Modifier
                    .size(7.dp)
                    .clip(CircleShape)
                    .background(ImperialGold)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "GEÇTİĞİ KİTAP",
                  style = MaterialTheme.typography.labelSmall,
                  color = RoyalEmerald,
                  fontWeight = FontWeight.ExtraBold,
                  letterSpacing = 0.6.sp,
                  fontSize = 10.sp
                )
              }
            }

            // Tümü (All Books)
            item {
              val isAllSelected = selectedBookId == null
              ModernBookSelectTile(
                title = "Tüm Eserler",
                subtitle = "${categoryBooks.size} Eser",
                count = categoryBooks.sumOf { it.pageCount },
                isSelected = isAllSelected,
                onClick = { viewModel.selectBook(null) }
              )
            }

            // Category Books
            items(categoryBooks) { book ->
              val isSelected = selectedBookId == book.id
              ModernBookSelectTile(
                title = book.title,
                subtitle = book.author,
                count = book.pageCount,
                isSelected = isSelected,
                onClick = { viewModel.selectBook(book.id) }
              )
            }
          }
        }

        Spacer(modifier = Modifier.width(6.dp))

        // SAĞ PANEL: KONU & FASILLAR (Her kartta Sol: Geçtiği Kitap | Sağ: Konu)
        Surface(
          color = Color.White.copy(alpha = 0.9f),
          shape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, ParchmentBorder),
          modifier = Modifier
            .fillMaxHeight()
            .weight(1f)
            .testTag("right_panel_topics")
        ) {
          if (chapterTopics.isEmpty()) {
            Box(
              modifier = Modifier.fillMaxSize(),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "Bu eserde kayıtlı konu bulunamadı.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
              )
            }
          } else {
            LazyColumn(
              modifier = Modifier.fillMaxSize(),
              contentPadding = PaddingValues(8.dp),
              verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              item {
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 2.dp),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = if (activeSelectedBook != null) "Kitap: ${activeSelectedBook.title}" else "Tüm Konular ve Fasıllar",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = RoyalEmerald,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                  )
                  Surface(
                    color = EmeraldContainerLight,
                    shape = RoundedCornerShape(8.dp)
                  ) {
                    Text(
                      text = "${chapterTopics.size} Konu",
                      style = MaterialTheme.typography.labelSmall,
                      color = RoyalEmeraldDark,
                      fontWeight = FontWeight.Bold,
                      modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                  }
                }
              }

              itemsIndexed(chapterTopics) { index, item ->
                DualTopicCard(
                  item = item,
                  onReadClick = { onOpenReader(item.bookId, item.pageIndex) },
                  onToggleFavorite = { viewModel.toggleFavorite(item.bookId) }
                )
              }
            }
          }
        }
      }
    }
  }

  if (showBackupDialog) {
    BackupRestoreDialog(
      viewModel = viewModel,
      onDismiss = { showBackupDialog = false }
    )
  }
}

@Composable
fun DualTopicCard(
  item: ChapterTopicItem,
  onReadClick: () -> Unit,
  onToggleFavorite: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("topic_card_${item.bookId}_${item.pageIndex}"),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = ParchmentCard.copy(alpha = 0.8f)),
    border = androidx.compose.foundation.BorderStroke(1.dp, ParchmentBorder)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp)
    ) {
      // 1. ÜST SATIR: SOL TARAF GEÇTİĞİ KİTAP | SAĞ TARAF KONU
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
      ) {
        // SOL TARAF: Geçtiği Kitap
        Column(modifier = Modifier.weight(1f)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = "GEÇTİĞİ KİTAP:",
              style = MaterialTheme.typography.labelSmall,
              color = RoyalEmerald,
              fontWeight = FontWeight.ExtraBold,
              fontSize = 9.sp,
              letterSpacing = 0.5.sp
            )
            if (item.bookTitle.contains("Diyanet", ignoreCase = true) || item.bookAuthor.contains("Diyanet", ignoreCase = true)) {
              Spacer(modifier = Modifier.width(4.dp))
              Surface(
                color = RoyalEmerald.copy(alpha = 0.12f),
                shape = RoundedCornerShape(3.dp)
              ) {
                Text(
                  text = "DİYANET",
                  color = RoyalEmeraldDark,
                  fontSize = 8.sp,
                  fontWeight = FontWeight.ExtraBold,
                  modifier = Modifier.padding(horizontal = 3.dp, vertical = 1.dp)
                )
              }
            }
          }
          Text(
            text = item.bookTitle,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E2824),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
          Text(
            text = item.bookAuthor,
            style = MaterialTheme.typography.labelSmall,
            color = Color.Gray,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontSize = 10.sp
          )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // SAĞ TARAF: Konu
        Column(
          horizontalAlignment = Alignment.End,
          modifier = Modifier.weight(1.1f)
        ) {
          Surface(
            color = ImperialGold.copy(alpha = 0.22f),
            shape = RoundedCornerShape(6.dp)
          ) {
            Text(
              text = "KONU:",
              style = MaterialTheme.typography.labelSmall,
              color = ImperialGoldDark,
              fontWeight = FontWeight.ExtraBold,
              fontSize = 9.sp,
              modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
            )
          }
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = item.topicTitle,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF6B4E03),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            fontSize = 11.sp
          )
        }
      }

      Spacer(modifier = Modifier.height(6.dp))

      // 2. ORTA SATIR: METİN ALINTISI / ÖZET
      Text(
        text = item.snippet,
        style = MaterialTheme.typography.bodySmall,
        color = Color(0xFF3C4641),
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        fontSize = 11.sp,
        lineHeight = 15.sp
      )

      Spacer(modifier = Modifier.height(8.dp))

      // 3. ALT SATIR: Sayfa, Favori ve Doğrudan Okuma Butonu
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Surface(
            color = RoyalEmerald.copy(alpha = 0.08f),
            shape = RoundedCornerShape(4.dp)
          ) {
            Text(
              text = "Sayfa ${item.pageIndex + 1} / ${item.totalPages}",
              style = MaterialTheme.typography.labelSmall,
              color = RoyalEmerald,
              fontWeight = FontWeight.Bold,
              fontSize = 10.sp,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }

          IconButton(
            onClick = onToggleFavorite,
            modifier = Modifier.size(28.dp)
          ) {
            Icon(
              imageVector = if (item.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
              contentDescription = "Favori",
              tint = if (item.isFavorite) Color(0xFFD32F2F) else Color.Gray,
              modifier = Modifier.size(16.dp)
            )
          }
        }

        Button(
          onClick = onReadClick,
          colors = ButtonDefaults.buttonColors(containerColor = RoyalEmerald),
          shape = RoundedCornerShape(8.dp),
          contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
          modifier = Modifier.height(28.dp)
        ) {
          Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = null, modifier = Modifier.size(12.dp), tint = ImperialGoldLight)
          Spacer(modifier = Modifier.width(4.dp))
          Text("Oku", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
      }
    }
  }
}

@Composable
fun ModernBookSelectTile(
  title: String,
  subtitle: String,
  count: Int,
  isSelected: Boolean,
  onClick: () -> Unit
) {
  Surface(
    color = if (isSelected) RoyalEmerald else Color.Transparent,
    shape = RoundedCornerShape(10.dp),
    border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, ImperialGold.copy(alpha = 0.7f)) else null,
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() }
  ) {
    Column(
      modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = title,
          style = MaterialTheme.typography.bodySmall,
          fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
          color = if (isSelected) Color.White else Color(0xFF1E2824),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
          modifier = Modifier.weight(1f),
          fontSize = 11.sp
        )
        Spacer(modifier = Modifier.width(4.dp))
        Box(
          modifier = Modifier
            .size(18.dp)
            .clip(CircleShape)
            .background(if (isSelected) ImperialGold else Color.Gray.copy(alpha = 0.18f)),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "$count",
            color = if (isSelected) Color.Black else Color(0xFF4A5550),
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }
      Text(
        text = subtitle,
        style = MaterialTheme.typography.labelSmall,
        color = if (isSelected) ImperialGoldLight else Color.Gray,
        fontSize = 10.sp,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )
      if (title.contains("Diyanet", ignoreCase = true) || subtitle.contains("Diyanet", ignoreCase = true)) {
        Spacer(modifier = Modifier.height(2.dp))
        Surface(
          color = if (isSelected) ImperialGold.copy(alpha = 0.35f) else RoyalEmerald.copy(alpha = 0.12f),
          shape = RoundedCornerShape(3.dp)
        ) {
          Text(
            text = "DİYANET NEŞRİ",
            color = if (isSelected) Color.White else RoyalEmeraldDark,
            fontSize = 8.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
          )
        }
      }
    }
  }
}

@Composable
fun HeaderActionChip(
  icon: ImageVector,
  label: String,
  testTag: String,
  onClick: () -> Unit
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .clip(RoundedCornerShape(12.dp))
      .clickable { onClick() }
      .padding(horizontal = 6.dp, vertical = 4.dp)
      .testTag(testTag)
  ) {
    Box(
      modifier = Modifier
        .size(34.dp)
        .clip(CircleShape)
        .background(Color.White.copy(alpha = 0.15f))
        .border(1.dp, ImperialGold.copy(alpha = 0.4f), CircleShape),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = icon,
        contentDescription = label,
        tint = ImperialGoldLight,
        modifier = Modifier.size(18.dp)
      )
    }
    Spacer(modifier = Modifier.height(4.dp))
    Text(
      text = label,
      style = MaterialTheme.typography.labelSmall,
      fontSize = 10.sp,
      color = Color.White.copy(alpha = 0.9f)
    )
  }
}

@Composable
fun RoyalIslamicBottomNavigation(
  selectedCategory: CategoryType,
  onCategorySelect: (CategoryType) -> Unit
) {
  Surface(
    color = RoyalEmeraldDark,
    shadowElevation = 12.dp,
    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
    modifier = Modifier.fillMaxWidth()
  ) {
    LazyRow(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp, vertical = 6.dp)
        .testTag("bottom_category_nav"),
      horizontalArrangement = Arrangement.spacedBy(6.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      items(CategoryType.entries) { category ->
        val isSelected = selectedCategory == category
        Surface(
          color = if (isSelected) ImperialGold else Color.Transparent,
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .clickable { onCategorySelect(category) }
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = getCategoryIcon(category),
              contentDescription = category.title,
              tint = if (isSelected) Color.Black else ImperialGoldLight.copy(alpha = 0.8f),
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = category.title,
              style = MaterialTheme.typography.labelSmall,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
              color = if (isSelected) Color.Black else Color.White,
              fontSize = 11.sp
            )
          }
        }
      }
    }
  }
}

fun getCategoryIcon(category: CategoryType): ImageVector = when (category) {
  CategoryType.TEFSIR -> Icons.Default.MenuBook
  CategoryType.HADIS -> Icons.Default.AutoStories
  CategoryType.FIKIH -> Icons.Default.AccountBalance
  CategoryType.SIYER -> Icons.Default.Person
  CategoryType.ISLAM_TARIHI -> Icons.Default.Public
  CategoryType.AKAID -> Icons.Default.Shield
  CategoryType.AHLAK -> Icons.Default.Spa
}

fun extractTopicFromPage(pageContent: String, fallbackTopic: String, pageIndex: Int): String {
  val lines = pageContent.lines().map { it.trim() }.filter { it.isNotEmpty() }
  for (line in lines) {
    if (line.contains("KONU:", ignoreCase = true)) {
      return line.substringAfter("KONU:").substringAfter("konu:").trim()
    }
    if (line.startsWith("1.") || line.startsWith("2.") || line.startsWith("3.") || line.startsWith("BÖLÜM") || line.startsWith("HADİS") || line.startsWith("BÂB")) {
      return line.take(45)
    }
  }
  return "$fallbackTopic (${pageIndex + 1}. Fasıl)"
}

fun extractSnippetFromPage(pageContent: String): String {
  val clean = pageContent.lines()
    .map { it.trim() }
    .filter { it.isNotEmpty() && !it.startsWith("BİSMİLLÂH") && !it.contains("KONU:") }
    .joinToString(" ")
  return if (clean.length > 120) clean.take(120) + "..." else clean
}
