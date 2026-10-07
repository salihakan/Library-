package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.NoteAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Book
import com.example.data.model.ReaderFont
import com.example.ui.LibraryViewModel
import com.example.ui.components.AddPageNoteDialog
import com.example.ui.components.DictionaryBottomSheet
import com.example.ui.components.ReaderBookmarksSheet
import com.example.ui.components.ReaderContentsSheet
import com.example.ui.components.ReaderSettingsSheet
import com.example.ui.theme.EmeraldContainerLight
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderScreen(
  viewModel: LibraryViewModel,
  book: Book,
  initialPage: Int,
  onBack: () -> Unit
) {
  val currentPageIndex by viewModel.currentPageIndex.collectAsState()
  val settings by viewModel.readerSettings.collectAsState()
  val isSpeaking = viewModel.ttsManager.isSpeaking
  val searchQuery by viewModel.readerSearchQuery.collectAsState()
  val activeLookupTerm by viewModel.activeLookupTerm.collectAsState()

  val bookmarks by viewModel.repository.getBookmarksForBook(book.id).collectAsState(initial = emptyList())

  var showMenu by remember { mutableStateOf(false) }
  var showSettingsSheet by remember { mutableStateOf(false) }
  var showContentsSheet by remember { mutableStateOf(false) }
  var showBookmarksSheet by remember { mutableStateOf(false) }
  var showAddNoteDialog by remember { mutableStateOf(false) }
  var isSearchFieldVisible by remember { mutableStateOf(false) }

  val isCurrentPageBookmarked = bookmarks.any { it.pageIndex == currentPageIndex }

  val pageCount = book.pageCount
  val currentPageContent = book.pages.getOrElse(currentPageIndex) { "" }

  val scrollState = rememberScrollState()

  // Reset scroll when page changes
  LaunchedEffect(currentPageIndex) {
    scrollState.scrollTo(0)
  }

  val fontFam = when (settings.font) {
    ReaderFont.AMIRI -> FontFamily.Serif
    ReaderFont.SERIF -> FontFamily.Serif
    ReaderFont.SANS_SERIF -> FontFamily.SansSerif
  }

  Scaffold(
    topBar = {
      Column {
        TopAppBar(
          title = {
            Column {
              Text(
                text = book.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = settings.theme.text
              )
              Text(
                text = "${book.author} • Sayfa ${currentPageIndex + 1} / $pageCount",
                style = MaterialTheme.typography.labelSmall,
                color = settings.theme.text.copy(alpha = 0.7f)
              )
            }
          },
          navigationIcon = {
            IconButton(onClick = onBack, modifier = Modifier.testTag("reader_back_btn")) {
              Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Geri",
                tint = settings.theme.text
              )
            }
          },
          actions = {
            // 1. TTS Button
            IconButton(
              onClick = { viewModel.toggleTts() },
              modifier = Modifier
                .testTag("reader_tts_btn")
                .then(
                  if (isSpeaking) Modifier
                    .clip(CircleShape)
                    .background(GoldPrimary.copy(alpha = 0.3f))
                  else Modifier
                )
            ) {
              Icon(
                imageVector = if (isSpeaking) Icons.Default.Stop else Icons.Default.VolumeUp,
                contentDescription = if (isSpeaking) "Durdur" else "Sesli Oku",
                tint = if (isSpeaking) GoldPrimary else settings.theme.text
              )
            }

            // 2. Bookmark Button
            IconButton(
              onClick = {
                viewModel.addBookmarkForCurrentPage()
              },
              modifier = Modifier.testTag("reader_bookmark_btn")
            ) {
              Icon(
                imageVector = if (isCurrentPageBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                contentDescription = "Yer İmi",
                tint = if (isCurrentPageBookmarked) EmeraldPrimary else settings.theme.text
              )
            }

            // 3. In-book search icon
            IconButton(
              onClick = { isSearchFieldVisible = !isSearchFieldVisible },
              modifier = Modifier.testTag("reader_search_toggle_btn")
            ) {
              Icon(
                Icons.Default.Search,
                contentDescription = "Kitap İçi Ara",
                tint = if (searchQuery.isNotEmpty() || isSearchFieldVisible) EmeraldPrimary else settings.theme.text
              )
            }

            // 4. Overflow Menu
            IconButton(
              onClick = { showMenu = true },
              modifier = Modifier.testTag("reader_overflow_menu_btn")
            ) {
              Icon(
                Icons.Default.MoreVert,
                contentDescription = "Daha Fazla",
                tint = settings.theme.text
              )
            }

            DropdownMenu(
              expanded = showMenu,
              onDismissRequest = { showMenu = false }
            ) {
              DropdownMenuItem(
                text = { Text("İçindekiler") },
                leadingIcon = { Icon(Icons.Default.FormatListBulleted, contentDescription = null) },
                onClick = {
                  showMenu = false
                  showContentsSheet = true
                }
              )
              DropdownMenuItem(
                text = { Text("Yer İşaretleri & Notlar (${bookmarks.size})") },
                leadingIcon = { Icon(Icons.Default.Bookmark, contentDescription = null) },
                onClick = {
                  showMenu = false
                  showBookmarksSheet = true
                }
              )
              DropdownMenuItem(
                text = { Text("Okuma Ayarları (Tema, Font, Hız)") },
                leadingIcon = { Icon(Icons.Default.Settings, contentDescription = null) },
                onClick = {
                  showMenu = false
                  showSettingsSheet = true
                }
              )
            }
          },
          colors = TopAppBarDefaults.topAppBarColors(
            containerColor = settings.theme.bg
          )
        )

        // Search Bar (if visible)
        AnimatedVisibility(visible = isSearchFieldVisible) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .background(settings.theme.bg)
              .padding(horizontal = 16.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            OutlinedTextField(
              value = searchQuery,
              onValueChange = { viewModel.setReaderSearchQuery(it) },
              placeholder = { Text("Sayfa içinde kelime ara...", fontSize = 14.sp) },
              singleLine = true,
              trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                  IconButton(onClick = { viewModel.setReaderSearchQuery("") }) {
                    Icon(Icons.Default.Close, contentDescription = "Temizle")
                  }
                }
              },
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = EmeraldPrimary,
                unfocusedBorderColor = Color.Gray.copy(alpha = 0.4f)
              ),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier
                .weight(1f)
                .testTag("reader_search_input")
            )
          }
        }
      }
    },
    bottomBar = {
      // Bottom Bar with Prev, SeekBar, Next
      Surface(
        color = settings.theme.bg,
        shadowElevation = 8.dp,
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            IconButton(
              onClick = { viewModel.onPageChanged(currentPageIndex - 1) },
              enabled = currentPageIndex > 0,
              modifier = Modifier.testTag("reader_prev_page_btn")
            ) {
              Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Önceki Sayfa",
                tint = if (currentPageIndex > 0) settings.theme.text else Color.Gray.copy(alpha = 0.3f)
              )
            }

            Text(
              text = "Sayfa ${currentPageIndex + 1} / $pageCount",
              style = MaterialTheme.typography.labelLarge,
              fontWeight = FontWeight.Bold,
              color = settings.theme.text
            )

            IconButton(
              onClick = { viewModel.onPageChanged(currentPageIndex + 1) },
              enabled = currentPageIndex < pageCount - 1,
              modifier = Modifier.testTag("reader_next_page_btn")
            ) {
              Icon(
                Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Sonraki Sayfa",
                tint = if (currentPageIndex < pageCount - 1) settings.theme.text else Color.Gray.copy(alpha = 0.3f)
              )
            }
          }

          if (pageCount > 1) {
            Slider(
              value = currentPageIndex.toFloat(),
              onValueChange = { viewModel.onPageChanged(it.toInt()) },
              valueRange = 0f..(pageCount - 1).toFloat(),
              steps = (pageCount - 2).coerceAtLeast(0),
              colors = SliderDefaults.colors(
                thumbColor = EmeraldPrimary,
                activeTrackColor = EmeraldPrimary,
                inactiveTrackColor = Color.Gray.copy(alpha = 0.3f)
              ),
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .testTag("reader_page_seekbar")
            )
          }
        }
      }
    },
    floatingActionButton = {
      // Yellow FAB for adding page note
      FloatingActionButton(
        onClick = { showAddNoteDialog = true },
        containerColor = Color(0xFFFFC107), // Yellow FAB as explicitly specified
        contentColor = Color.Black,
        shape = CircleShape,
        modifier = Modifier.testTag("add_note_fab")
      ) {
        Icon(
          imageVector = Icons.Default.NoteAdd,
          contentDescription = "Sayfaya Not Ekle"
        )
      }
    },
    containerColor = settings.theme.bg
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .background(settings.theme.bg)
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .verticalScroll(scrollState)
          .padding(horizontal = 24.dp, vertical = 20.dp)
      ) {
        // Subtle hint bar for word click lookup
        Card(
          colors = CardDefaults.cardColors(
            containerColor = settings.theme.text.copy(alpha = 0.05f)
          ),
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "💡 İpucu: Istılah manası ve Arapça kökünü görmek için metindeki herhangi bir kelimeye dokunun.",
              style = MaterialTheme.typography.labelSmall,
              color = settings.theme.text.copy(alpha = 0.7f),
              fontSize = 11.sp
            )
          }
        }

        // Ornamental Calligraphic Basmala on Page 1
        if (currentPageIndex == 0) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(bottom = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Surface(
              color = settings.theme.text.copy(alpha = 0.04f),
              shape = RoundedCornerShape(16.dp),
              border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.4f))
            ) {
              Text(
                text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                style = MaterialTheme.typography.titleLarge,
                color = settings.theme.text.copy(alpha = 0.9f),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 10.dp)
              )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "Rahmân ve Rahîm olan Allah'ın adıyla",
              style = MaterialTheme.typography.labelSmall,
              color = settings.theme.text.copy(alpha = 0.6f)
            )
          }
        }

        // Reader Body Text with word-click and search highlight
        ClickableHighlightedText(
          text = currentPageContent,
          searchQuery = searchQuery,
          textColor = settings.theme.text,
          fontSize = settings.fontSizeSp.sp,
          lineHeight = (settings.fontSizeSp * settings.lineSpacingMultiplier).sp,
          fontFamily = fontFam,
          onWordClick = { word ->
            viewModel.lookupWord(word)
          }
        )

        Spacer(modifier = Modifier.height(80.dp)) // Padding for FAB
      }
    }
  }

  // Modals & Sheets
  if (showSettingsSheet) {
    ReaderSettingsSheet(
      settings = settings,
      onThemeChange = { viewModel.setReaderTheme(it) },
      onFontSizeChange = { viewModel.setReaderFontSize(it) },
      onFontChange = { viewModel.setReaderFont(it) },
      onLineSpacingChange = { viewModel.setReaderLineSpacing(it) },
      onTtsSpeedChange = { viewModel.setTtsSpeed(it) },
      onDismiss = { showSettingsSheet = false }
    )
  }

  if (showContentsSheet) {
    ReaderContentsSheet(
      pages = book.pages,
      currentPageIndex = currentPageIndex,
      onSelectPage = { viewModel.onPageChanged(it) },
      onDismiss = { showContentsSheet = false }
    )
  }

  if (showBookmarksSheet) {
    ReaderBookmarksSheet(
      bookmarks = bookmarks,
      currentPageIndex = currentPageIndex,
      onSelectPage = { viewModel.onPageChanged(it) },
      onDeleteBookmark = { viewModel.deleteBookmark(it) },
      onDismiss = { showBookmarksSheet = false }
    )
  }

  if (showAddNoteDialog) {
    AddPageNoteDialog(
      pageIndex = currentPageIndex,
      onDismiss = { showAddNoteDialog = false },
      onSaveNote = { note ->
        viewModel.addBookmarkForCurrentPage(note)
      }
    )
  }

  activeLookupTerm?.let { term ->
    DictionaryBottomSheet(
      term = term,
      onDismiss = { viewModel.closeLookupCard() },
      onSaveLearnedWord = { viewModel.saveActiveLookupWord() }
    )
  }
}

@Composable
fun ClickableHighlightedText(
  text: String,
  searchQuery: String,
  textColor: Color,
  fontSize: androidx.compose.ui.unit.TextUnit,
  lineHeight: androidx.compose.ui.unit.TextUnit,
  fontFamily: FontFamily,
  onWordClick: (String) -> Unit
) {
  val lines = text.split("\n")

  Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
    lines.forEach { line ->
      if (line.isBlank()) {
        Spacer(modifier = Modifier.height(10.dp))
      } else {
        val words = line.split(" ")
        // Use flow row or paragraph tokens
        TextFlowParagraph(
          words = words,
          searchQuery = searchQuery,
          textColor = textColor,
          fontSize = fontSize,
          lineHeight = lineHeight,
          fontFamily = fontFamily,
          onWordClick = onWordClick
        )
      }
    }
  }
}

@Composable
private fun TextFlowParagraph(
  words: List<String>,
  searchQuery: String,
  textColor: Color,
  fontSize: androidx.compose.ui.unit.TextUnit,
  lineHeight: androidx.compose.ui.unit.TextUnit,
  fontFamily: FontFamily,
  onWordClick: (String) -> Unit
) {
  // AnnotatedString for the whole line to keep proper justification/flow,
  // or individual words in an inline layout. Using AnnotatedString with LinkAnnotation or clickable row:
  // With ClickableText or annotated string spans:
  val annotatedString = buildAnnotatedString {
    words.forEachIndexed { index, word ->
      val cleanWord = word.trim()
      val isHighlighted = searchQuery.isNotBlank() && cleanWord.contains(searchQuery, ignoreCase = true)

      val start = length
      if (isHighlighted) {
        withStyle(
          style = SpanStyle(
            background = Color(0xFFFFEB3B), // Yellow highlight for search matches
            color = Color.Black,
            fontWeight = FontWeight.Bold
          )
        ) {
          append(word)
        }
      } else {
        append(word)
      }
      val end = length
      addStringAnnotation(
        tag = "WORD",
        annotation = cleanWord,
        start = start,
        end = end
      )
      if (index < words.size - 1) {
        append(" ")
      }
    }
  }

  androidx.compose.foundation.text.ClickableText(
    text = annotatedString,
    style = androidx.compose.ui.text.TextStyle(
      color = textColor,
      fontSize = fontSize,
      lineHeight = lineHeight,
      fontFamily = fontFamily,
      textAlign = TextAlign.Start
    ),
    onClick = { offset ->
      annotatedString.getStringAnnotations(tag = "WORD", start = offset, end = offset)
        .firstOrNull()?.let { annotation ->
          onWordClick(annotation.item)
        }
    }
  )
}
