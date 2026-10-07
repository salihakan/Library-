package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CategoryType
import com.example.ui.LibraryViewModel
import com.example.ui.theme.EmeraldPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBookScreen(
  viewModel: LibraryViewModel,
  onBack: () -> Unit
) {
  var title by remember { mutableStateOf("") }
  var author by remember { mutableStateOf("") }
  var selectedCategory by remember { mutableStateOf(CategoryType.TEFSIR) }
  var topic by remember { mutableStateOf("Genel İnceleme") }
  var content by remember { mutableStateOf("") }
  var categoryDropdownExpanded by remember { mutableStateOf(false) }

  val pageCount = remember(content) {
    val list = content.split("---").map { it.trim() }.filter { it.isNotEmpty() }
    if (list.isEmpty()) 1 else list.size
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text("Yeni Eser Ekle (Manuel Giriş)", fontWeight = FontWeight.Bold)
        },
        navigationIcon = {
          IconButton(onClick = onBack, modifier = Modifier.testTag("add_book_back_btn")) {
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
        .verticalScroll(rememberScrollState())
        .padding(20.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

      Card(
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Text(
            text = "📖 PDF/EPUB gereksinimi olmadan kendi yazılarınızı, risale bölümlerini veya ilmi notlarınızı kütüphaneye ekleyebilirsiniz.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "Sayfaları birbirinden ayırmak için aralarına '---' (üç tire) yazınız.",
            style = MaterialTheme.typography.labelSmall,
            color = EmeraldPrimary,
            fontWeight = FontWeight.Bold
          )
        }
      }

      // 1. Kitap Başlığı
      OutlinedTextField(
        value = title,
        onValueChange = { title = it },
        label = { Text("Kitap / Eser Başlığı *") },
        placeholder = { Text("Örn: Hülasatü'l-Beyan Tefsiri") },
        singleLine = true,
        modifier = Modifier
          .fillMaxWidth()
          .testTag("add_book_title_input"),
        shape = RoundedCornerShape(12.dp)
      )

      // 2. Yazar / Müellif
      OutlinedTextField(
        value = author,
        onValueChange = { author = it },
        label = { Text("Müellif / Yazar") },
        placeholder = { Text("Örn: İmam Gazali / Kendi Notum") },
        singleLine = true,
        modifier = Modifier
          .fillMaxWidth()
          .testTag("add_book_author_input"),
        shape = RoundedCornerShape(12.dp)
      )

      // 3. Kategori Seçimi (Spinner / Dropdown)
      Column {
        Text(
          text = "Kategori:",
          style = MaterialTheme.typography.labelMedium,
          color = EmeraldPrimary,
          fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedButton(
          onClick = { categoryDropdownExpanded = true },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("category_spinner_btn"),
          shape = RoundedCornerShape(12.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "${selectedCategory.title} (${selectedCategory.subtitle})",
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurface
            )
            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
          }
        }

        DropdownMenu(
          expanded = categoryDropdownExpanded,
          onDismissRequest = { categoryDropdownExpanded = false }
        ) {
          CategoryType.entries.forEach { cat ->
            DropdownMenuItem(
              text = { Text("${cat.title} - ${cat.subtitle}") },
              onClick = {
                selectedCategory = cat
                categoryDropdownExpanded = false
              }
            )
          }
        }
      }

      // 4. Konu Başlığı
      OutlinedTextField(
        value = topic,
        onValueChange = { topic = it },
        label = { Text("Konu / İhtisas Alanı") },
        placeholder = { Text("Örn: Tefsir Usulü, Fıkıh Kaideleri") },
        singleLine = true,
        modifier = Modifier
          .fillMaxWidth()
          .testTag("add_book_topic_input"),
        shape = RoundedCornerShape(12.dp)
      )

      // 5. Metin & İçerik
      Column {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Eser Metni & Sayfalar *",
            style = MaterialTheme.typography.labelMedium,
            color = EmeraldPrimary,
            fontWeight = FontWeight.SemiBold
          )
          Text(
            text = "Tahmini Sayfa: $pageCount",
            style = MaterialTheme.typography.labelSmall,
            color = EmeraldPrimary,
            fontWeight = FontWeight.Bold
          )
        }
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
          value = content,
          onValueChange = { content = it },
          placeholder = {
            Text(
              "Birinci sayfa metni buraya...\n\n---\n\nİkinci sayfa metni buraya...\n\n---\n\nÜçüncü sayfa metni..."
            )
          },
          modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .testTag("add_book_content_input"),
          shape = RoundedCornerShape(12.dp)
        )
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Submit Button
      Button(
        onClick = {
          viewModel.addNewBook(
            title = title,
            author = author,
            category = selectedCategory,
            topic = topic,
            content = content,
            onSuccess = onBack
          )
        },
        enabled = title.isNotBlank() && content.isNotBlank(),
        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
        modifier = Modifier
          .fillMaxWidth()
          .height(52.dp)
          .testTag("submit_new_book_btn"),
        shape = RoundedCornerShape(14.dp)
      ) {
        Icon(Icons.Default.Add, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text("Kütüphaneye Ekle", fontSize = 16.sp, fontWeight = FontWeight.Bold)
      }
    }
  }
}
