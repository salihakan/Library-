package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.AppScreen
import com.example.ui.LibraryViewModel
import com.example.ui.screens.AddBookScreen
import com.example.ui.screens.FavoritesScreen
import com.example.ui.screens.GlobalSearchScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LearnedWordsScreen
import com.example.ui.screens.ReaderScreen
import com.example.ui.screens.StatsScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        IslamicLibraryApp()
      }
    }
  }
}

@Composable
fun IslamicLibraryApp(
  viewModel: LibraryViewModel = viewModel()
) {
  val currentScreen by viewModel.currentScreen.collectAsState()
  val allBooks by viewModel.allBooks.collectAsState()
  val snackbarMessage by viewModel.messageSnackbar.collectAsState()

  val snackbarHostState = remember { SnackbarHostState() }

  LaunchedEffect(snackbarMessage) {
    snackbarMessage?.let {
      snackbarHostState.showSnackbar(it)
      viewModel.clearSnackbar()
    }
  }

  // Handle hardware back button
  BackHandler(enabled = currentScreen !is AppScreen.Home) {
    viewModel.navigateTo(AppScreen.Home)
  }

  Scaffold(
    snackbarHost = { SnackbarHost(snackbarHostState) },
    modifier = Modifier.fillMaxSize()
  ) { innerPadding ->
    Surface(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding),
      color = MaterialTheme.colorScheme.background
    ) {
      when (val screen = currentScreen) {
        is AppScreen.Home -> {
          HomeScreen(
            viewModel = viewModel,
            onOpenReader = { bookId, page ->
              viewModel.openBook(bookId, page)
            }
          )
        }

        is AppScreen.Reader -> {
          val book = allBooks.find { it.id == screen.bookId }
          if (book != null) {
            ReaderScreen(
              viewModel = viewModel,
              book = book,
              initialPage = screen.initialPage,
              onBack = { viewModel.navigateTo(AppScreen.Home) }
            )
          } else {
            // Fallback to home if book not found
            LaunchedEffect(Unit) {
              viewModel.navigateTo(AppScreen.Home)
            }
          }
        }

        is AppScreen.Search -> {
          GlobalSearchScreen(
            viewModel = viewModel,
            onOpenBookAtPage = { bookId, page ->
              viewModel.openBook(bookId, page)
            },
            onBack = { viewModel.navigateTo(AppScreen.Home) }
          )
        }

        is AppScreen.Favorites -> {
          FavoritesScreen(
            viewModel = viewModel,
            onOpenBook = { bookId ->
              viewModel.openBook(bookId, 0)
            },
            onBack = { viewModel.navigateTo(AppScreen.Home) }
          )
        }

        is AppScreen.Stats -> {
          StatsScreen(
            viewModel = viewModel,
            onBack = { viewModel.navigateTo(AppScreen.Home) }
          )
        }

        is AppScreen.AddBook -> {
          AddBookScreen(
            viewModel = viewModel,
            onBack = { viewModel.navigateTo(AppScreen.Home) }
          )
        }

        is AppScreen.LearnedWords -> {
          LearnedWordsScreen(
            viewModel = viewModel,
            onBack = { viewModel.navigateTo(AppScreen.Home) }
          )
        }
      }
    }
  }
}
