package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Bookmark
import com.example.data.model.ReaderFont
import com.example.data.model.ReaderSettings
import com.example.data.model.ReaderTheme
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderSettingsSheet(
  settings: ReaderSettings,
  onThemeChange: (ReaderTheme) -> Unit,
  onFontSizeChange: (Float) -> Unit,
  onFontChange: (ReaderFont) -> Unit,
  onLineSpacingChange: (Float) -> Unit,
  onTtsSpeedChange: (Float) -> Unit,
  onDismiss: () -> Unit
) {
  val sheetState = rememberModalBottomSheetState()

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = MaterialTheme.colorScheme.surface,
    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
    modifier = Modifier.testTag("reader_settings_sheet")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 24.dp)
        .padding(bottom = 32.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Okuma Ayarları",
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.Bold
        )
        IconButton(onClick = onDismiss) {
          Icon(Icons.Default.Close, contentDescription = "Kapat")
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // 1. Theme Selection
      Text(
        text = "Tema Seçimi",
        style = MaterialTheme.typography.labelLarge,
        color = EmeraldPrimary,
        fontWeight = FontWeight.SemiBold
      )
      Spacer(modifier = Modifier.height(8.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        ReaderTheme.entries.forEach { theme ->
          val isSelected = settings.theme == theme
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(12.dp))
              .clickable { onThemeChange(theme) }
              .background(if (isSelected) EmeraldPrimary.copy(alpha = 0.1f) else Color.Transparent)
              .padding(vertical = 8.dp)
          ) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(theme.bg)
                .border(
                  width = if (isSelected) 3.dp else 1.dp,
                  color = if (isSelected) EmeraldPrimary else Color.Gray.copy(alpha = 0.4f),
                  shape = CircleShape
                ),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "A",
                color = theme.text,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
              )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = theme.label,
              style = MaterialTheme.typography.labelSmall,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
              color = if (isSelected) EmeraldPrimary else MaterialTheme.colorScheme.onSurface
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(18.dp))

      // 2. Font Size (12px - 32px)
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.FormatSize, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(20.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Yazı Boyutu",
            style = MaterialTheme.typography.labelLarge,
            color = EmeraldPrimary,
            fontWeight = FontWeight.SemiBold
          )
        }
        Text(
          text = "${settings.fontSizeSp.toInt()} sp",
          style = MaterialTheme.typography.bodyMedium,
          fontWeight = FontWeight.Bold
        )
      }
      Slider(
        value = settings.fontSizeSp,
        onValueChange = onFontSizeChange,
        valueRange = 12f..32f,
        steps = 19,
        colors = SliderDefaults.colors(thumbColor = EmeraldPrimary, activeTrackColor = EmeraldPrimary),
        modifier = Modifier.testTag("font_size_slider")
      )

      Spacer(modifier = Modifier.height(12.dp))

      // 3. Font Family
      Text(
        text = "Yazı Tipi (Font)",
        style = MaterialTheme.typography.labelLarge,
        color = EmeraldPrimary,
        fontWeight = FontWeight.SemiBold
      )
      Spacer(modifier = Modifier.height(8.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        ReaderFont.entries.forEach { font ->
          val selected = settings.font == font
          FilterChip(
            selected = selected,
            onClick = { onFontChange(font) },
            label = { Text(font.label) },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = EmeraldPrimary,
              selectedLabelColor = Color.White
            )
          )
        }
      }

      Spacer(modifier = Modifier.height(18.dp))

      // 4. TTS Speed (0.75x - 1.5x)
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.Speed, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(20.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Sesli Okuma (TTS) Hızı",
            style = MaterialTheme.typography.labelLarge,
            color = EmeraldPrimary,
            fontWeight = FontWeight.SemiBold
          )
        }
        Text(
          text = "%.2fx".format(settings.ttsSpeed),
          style = MaterialTheme.typography.bodyMedium,
          fontWeight = FontWeight.Bold
        )
      }
      Slider(
        value = settings.ttsSpeed,
        onValueChange = onTtsSpeedChange,
        valueRange = 0.75f..1.5f,
        steps = 3,
        colors = SliderDefaults.colors(thumbColor = GoldPrimary, activeTrackColor = GoldPrimary),
        modifier = Modifier.testTag("tts_speed_slider")
      )
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderContentsSheet(
  pages: List<String>,
  currentPageIndex: Int,
  onSelectPage: (Int) -> Unit,
  onDismiss: () -> Unit
) {
  val sheetState = rememberModalBottomSheetState()

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = MaterialTheme.colorScheme.surface,
    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 24.dp)
        .padding(bottom = 32.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "İçindekiler & Sayfalar (${pages.size} Sayfa)",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold
        )
        IconButton(onClick = onDismiss) {
          Icon(Icons.Default.Close, contentDescription = "Kapat")
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      LazyColumn(
        modifier = Modifier.height(360.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        itemsIndexed(pages) { index, content ->
          val isCurrent = index == currentPageIndex
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .clickable {
                onSelectPage(index)
                onDismiss()
              },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
              containerColor = if (isCurrent) EmeraldPrimary.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
            )
          ) {
            Row(
              modifier = Modifier.padding(14.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(32.dp)
                  .clip(CircleShape)
                  .background(if (isCurrent) EmeraldPrimary else Color.Gray.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = "${index + 1}",
                  color = if (isCurrent) Color.White else MaterialTheme.colorScheme.onSurface,
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp
                )
              }
              Spacer(modifier = Modifier.width(12.dp))
              Text(
                text = content.take(90).replace("\n", " ").trim() + "...",
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                modifier = Modifier.weight(1f)
              )
            }
          }
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderBookmarksSheet(
  bookmarks: List<Bookmark>,
  currentPageIndex: Int,
  onSelectPage: (Int) -> Unit,
  onDeleteBookmark: (Long) -> Unit,
  onDismiss: () -> Unit
) {
  val sheetState = rememberModalBottomSheetState()

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = MaterialTheme.colorScheme.surface,
    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 24.dp)
        .padding(bottom = 32.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Yer İşaretleri & Notlar (${bookmarks.size})",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold
        )
        IconButton(onClick = onDismiss) {
          Icon(Icons.Default.Close, contentDescription = "Kapat")
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      if (bookmarks.isEmpty()) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "Henüz yer imi veya not eklenmemiş.\nÜst bardaki yer imi simgesi veya sağ alttaki sarı buton ile not ekleyebilirsiniz.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
          )
        }
      } else {
        LazyColumn(
          modifier = Modifier.height(340.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          items(bookmarks) { bm ->
            Card(
              modifier = Modifier
                .fillMaxWidth()
                .clickable {
                  onSelectPage(bm.pageIndex)
                  onDismiss()
                },
              shape = RoundedCornerShape(12.dp),
              colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
              )
            ) {
              Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                  imageVector = if (bm.note.isNotEmpty()) Icons.Default.EditNote else Icons.Default.Bookmark,
                  contentDescription = null,
                  tint = if (bm.note.isNotEmpty()) GoldPrimary else EmeraldPrimary,
                  modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                  Text(
                    text = "Sayfa ${bm.pageIndex + 1}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                  )
                  if (bm.note.isNotEmpty()) {
                    Text(
                      text = bm.note,
                      style = MaterialTheme.typography.bodySmall,
                      color = MaterialTheme.colorScheme.onSurfaceVariant,
                      maxLines = 2,
                      overflow = TextOverflow.Ellipsis
                    )
                  }
                }
                IconButton(
                  onClick = { onDeleteBookmark(bm.id) },
                  modifier = Modifier.size(36.dp)
                ) {
                  Icon(
                    Icons.Default.DeleteOutline,
                    contentDescription = "Sil",
                    tint = Color.Red.copy(alpha = 0.7f),
                    modifier = Modifier.size(20.dp)
                  )
                }
              }
            }
          }
        }
      }
    }
  }
}

@Composable
fun AddPageNoteDialog(
  pageIndex: Int,
  onDismiss: () -> Unit,
  onSaveNote: (String) -> Unit
) {
  var noteText by remember { mutableStateOf("") }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.EditNote, contentDescription = null, tint = GoldPrimary)
        Spacer(modifier = Modifier.width(8.dp))
        Text("Sayfa ${pageIndex + 1} İçin Not Ekle")
      }
    },
    text = {
      Column {
        Text(
          text = "Bu sayfada dikkatinizi çeken fıkhi, itikadi veya tefsir mülahazalarını not alabilirsiniz:",
          style = MaterialTheme.typography.bodySmall,
          color = Color.Gray
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
          value = noteText,
          onValueChange = { noteText = it },
          placeholder = { Text("Notunuzu buraya yazınız...") },
          modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .testTag("page_note_input"),
          shape = RoundedCornerShape(12.dp)
        )
      }
    },
    confirmButton = {
      Button(
        onClick = {
          onSaveNote(noteText)
          onDismiss()
        },
        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
        modifier = Modifier.testTag("save_page_note_btn")
      ) {
        Text("Kaydet", color = Color.Black, fontWeight = FontWeight.Bold)
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Vazgeç")
      }
    }
  )
}
