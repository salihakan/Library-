package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.LibraryViewModel
import com.example.ui.SearchResultItem
import com.example.ui.theme.EmeraldContainerLight
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GlobalSearchScreen(
  viewModel: LibraryViewModel,
  onOpenBookAtPage: (String, Int) -> Unit,
  onBack: () -> Unit
) {
  val query by viewModel.globalSearchQuery.collectAsState()
  val allBooks by viewModel.allBooks.collectAsState()

  val searchResults = remember(query, allBooks) {
    if (query.trim().length < 2) emptyList()
    else {
      val q = query.trim().lowercase()
      val results = mutableListOf<SearchResultItem>()
      allBooks.forEach { book ->
        // Check title & author
        if (book.title.lowercase().contains(q) || book.author.lowercase().contains(q) || book.topic.lowercase().contains(q)) {
          results.add(
            SearchResultItem(
              book = book,
              matchedPage = 0,
              snippet = "Başlık veya Yazar Eşleşmesi: ${book.title} (${book.author}) - ${book.topic}"
            )
          )
        }
        // Check in page contents
        book.pages.forEachIndexed { pageIndex, content ->
          val contentLower = content.lowercase()
          val matchIndex = contentLower.indexOf(q)
          if (matchIndex != -1) {
            val start = (matchIndex - 40).coerceAtLeast(0)
            val end = (matchIndex + q.length + 80).coerceAtMost(content.length)
            val snippet = (if (start > 0) "... " else "") +
              content.substring(start, end).replace("\n", " ").trim() +
              (if (end < content.length) " ..." else "")

            results.add(
              SearchResultItem(
                book = book,
                matchedPage = pageIndex,
                snippet = snippet
              )
            )
          }
        }
      }
      results.take(50)
    }
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text("Külliyatta Arama", fontWeight = FontWeight.Bold)
        },
        navigationIcon = {
          IconButton(onClick = onBack, modifier = Modifier.testTag("search_back_btn")) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Geri")
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = MaterialTheme.colorScheme.surface
        )
      )
    }
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .padding(horizontal = 20.dp)
    ) {
      Spacer(modifier = Modifier.height(10.dp))

      OutlinedTextField(
        value = query,
        onValueChange = { viewModel.setGlobalSearchQuery(it) },
        placeholder = { Text("Sure, hadis, fıkıh, yazar veya metin içi ara...") },
        leadingIcon = {
          Icon(Icons.Default.Search, contentDescription = null, tint = EmeraldPrimary)
        },
        trailingIcon = {
          if (query.isNotEmpty()) {
            IconButton(onClick = { viewModel.setGlobalSearchQuery("") }) {
              Icon(Icons.Default.Close, contentDescription = "Temizle")
            }
          }
        },
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = EmeraldPrimary,
          unfocusedBorderColor = Color.Gray.copy(alpha = 0.3f)
        ),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("global_search_input")
      )

      Spacer(modifier = Modifier.height(16.dp))

      if (query.isNotBlank() && searchResults.isNotEmpty()) {
        Text(
          text = "${searchResults.size} sonuç bulundu:",
          style = MaterialTheme.typography.labelMedium,
          color = EmeraldPrimary,
          fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(10.dp))
      }

      if (query.isBlank()) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
              imageVector = Icons.Default.Search,
              contentDescription = null,
              tint = Color.Gray.copy(alpha = 0.4f),
              modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
              text = "Tüm külliyatta kitap adı, yazar ve sayfa metinlerinde arama yapabilirsiniz.",
              style = MaterialTheme.typography.bodyMedium,
              color = Color.Gray,
              modifier = Modifier.padding(horizontal = 32.dp),
              textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
          }
        }
      } else if (searchResults.isEmpty()) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "'$query' ile eşleşen bir eser veya sayfa bulunamadı.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
          )
        }
      } else {
        LazyColumn(
          modifier = Modifier.weight(1f),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          items(searchResults) { item ->
            Card(
              modifier = Modifier
                .fillMaxWidth()
                .clickable {
                  onOpenBookAtPage(item.book.id, item.matchedPage)
                }
                .testTag("search_result_${item.book.id}"),
              shape = RoundedCornerShape(14.dp),
              colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
              )
            ) {
              Column(modifier = Modifier.padding(14.dp)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                  ) {
                    Icon(
                      Icons.Default.MenuBook,
                      contentDescription = null,
                      tint = EmeraldPrimary,
                      modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                      Text(
                        text = "GEÇTİĞİ KİTAP: ${item.book.title}",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                      )
                      Text(
                        text = "Müellif: ${item.book.author}",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                      )
                    }
                  }
                  Spacer(modifier = Modifier.width(8.dp))
                  Column(horizontalAlignment = Alignment.End) {
                    Surface(
                      color = GoldPrimary.copy(alpha = 0.18f),
                      shape = RoundedCornerShape(6.dp)
                    ) {
                      Text(
                        text = "KONU: ${item.book.topic}",
                        color = Color(0xFF7A5805),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                      )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                      text = "Sayfa ${item.matchedPage + 1}",
                      color = EmeraldDark,
                      style = MaterialTheme.typography.labelSmall,
                      fontWeight = FontWeight.SemiBold
                    )
                  }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Highlighted snippet
                val snippetAnnotated = buildAnnotatedString {
                  val q = query.lowercase()
                  val snippetLower = item.snippet.lowercase()
                  var currentPos = 0
                  while (currentPos < item.snippet.length) {
                    val idx = snippetLower.indexOf(q, currentPos)
                    if (idx == -1) {
                      append(item.snippet.substring(currentPos))
                      break
                    } else {
                      append(item.snippet.substring(currentPos, idx))
                      withStyle(
                        SpanStyle(
                          background = Color(0xFFFFEB3B),
                          fontWeight = FontWeight.Bold,
                          color = Color.Black
                        )
                      ) {
                        append(item.snippet.substring(idx, idx + q.length))
                      }
                      currentPos = idx + q.length
                    }
                  }
                }

                Text(
                  text = snippetAnnotated,
                  style = MaterialTheme.typography.bodySmall,
                  lineHeight = 18.sp,
                  color = MaterialTheme.colorScheme.onSurface
                )
              }
            }
          }
        }
      }
    }
  }
}
