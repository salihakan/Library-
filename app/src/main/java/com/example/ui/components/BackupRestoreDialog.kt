package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.LibraryViewModel
import com.example.ui.theme.EmeraldPrimary
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackupRestoreDialog(
  viewModel: LibraryViewModel,
  onDismiss: () -> Unit
) {
  val context = LocalContext.current
  val coroutineScope = rememberCoroutineScope()
  var selectedTab by remember { mutableIntStateOf(0) }
  var exportJsonText by remember { mutableStateOf("") }
  var isExporting by remember { mutableStateOf(false) }

  var importJsonText by remember { mutableStateOf("") }
  var isImporting by remember { mutableStateOf(false) }
  var importStatus by remember { mutableStateOf<String?>(null) }

  LaunchedEffect(selectedTab) {
    if (selectedTab == 0) {
      isExporting = true
      exportJsonText = viewModel.getExportJsonString()
      isExporting = false
    }
  }

  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = MaterialTheme.colorScheme.surface,
    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
    modifier = Modifier.testTag("backup_restore_sheet")
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
          text = "Yedekleme & İçe/Dışa Aktarma (.JSON)",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold
        )
        IconButton(onClick = onDismiss) {
          Icon(Icons.Default.Close, contentDescription = "Kapat")
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      TabRow(
        selectedTabIndex = selectedTab,
        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
      ) {
        Tab(
          selected = selectedTab == 0,
          onClick = { selectedTab = 0 },
          text = { Text("Dışa Aktar (Yedek)") },
          icon = { Icon(Icons.Default.CloudDownload, contentDescription = null) }
        )
        Tab(
          selected = selectedTab == 1,
          onClick = { selectedTab = 1 },
          text = { Text("İçe Aktar (Yükle)") },
          icon = { Icon(Icons.Default.CloudUpload, contentDescription = null) }
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      if (selectedTab == 0) {
        // EXPORT SECTION
        Card(
          colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
          ),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Text(
              text = "Okuma geçmişi, eklenen özel kitaplar, yer imleri, notlar, favoriler ve lügat kelimeleri JSON formatında paketlendi.",
              style = MaterialTheme.typography.bodySmall
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (isExporting) {
          CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
          OutlinedTextField(
            value = exportJsonText,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
              .fillMaxWidth()
              .height(140.dp),
            shape = RoundedCornerShape(12.dp)
          )

          Spacer(modifier = Modifier.height(16.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            OutlinedButton(
              onClick = {
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("IslamicLibraryBackup", exportJsonText)
                clipboard.setPrimaryClip(clip)
              },
              modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .testTag("copy_json_btn"),
              shape = RoundedCornerShape(12.dp)
            ) {
              Icon(Icons.Default.ContentCopy, contentDescription = null)
              Spacer(modifier = Modifier.width(6.dp))
              Text("Kopyala")
            }

            Button(
              onClick = {
                val sendIntent = Intent().apply {
                  action = Intent.ACTION_SEND
                  putExtra(Intent.EXTRA_TEXT, exportJsonText)
                  type = "application/json"
                }
                val shareIntent = Intent.createChooser(sendIntent, "Yedek Dosyasını Paylaş")
                context.startActivity(shareIntent)
              },
              colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
              modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .testTag("share_json_btn"),
              shape = RoundedCornerShape(12.dp)
            ) {
              Icon(Icons.Default.Share, contentDescription = null)
              Spacer(modifier = Modifier.width(6.dp))
              Text("Paylaş")
            }
          }
        }
      } else {
        // IMPORT SECTION
        Card(
          colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
          ),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Text(
              text = "Daha önce aldığınız .JSON yedek metnini buraya yapıştırıp geri yükleyebilirsiniz.",
              style = MaterialTheme.typography.bodySmall
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
          value = importJsonText,
          onValueChange = { importJsonText = it },
          placeholder = { Text("{\"version\": 1, \"customBooks\": ... }") },
          modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .testTag("import_json_input"),
          shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.End
        ) {
          OutlinedButton(
            onClick = {
              val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
              val text = clipboard.primaryClip?.getItemAt(0)?.text?.toString()
              if (!text.isNullOrBlank()) {
                importJsonText = text
              }
            },
            shape = RoundedCornerShape(8.dp)
          ) {
            Icon(Icons.Default.ContentPaste, contentDescription = null)
            Spacer(modifier = Modifier.width(6.dp))
            Text("Panodan Yapıştır")
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        importStatus?.let {
          Text(
            text = it,
            style = MaterialTheme.typography.bodySmall,
            color = if (it.contains("başarı")) EmeraldPrimary else MaterialTheme.colorScheme.error,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
          onClick = {
            if (importJsonText.isBlank()) return@Button
            isImporting = true
            viewModel.importJsonData(importJsonText) { success ->
              isImporting = false
              importStatus = if (success) "Yedek başarıyla geri yüklendi!" else "Geçersiz JSON verisi!"
            }
          },
          enabled = importJsonText.isNotBlank() && !isImporting,
          colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("restore_json_btn"),
          shape = RoundedCornerShape(12.dp)
        ) {
          if (isImporting) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
          } else {
            Icon(Icons.Default.CloudUpload, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Yedeği Geri Yükle")
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
          onClick = {
            viewModel.reseedDiyanetDatabase()
            onDismiss()
          },
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(12.dp)
        ) {
          Text("Diyanet Külliyatlarını Veritabanına Eşitle / Doldur", fontSize = 12.sp)
        }
      }
    }
  }
}
