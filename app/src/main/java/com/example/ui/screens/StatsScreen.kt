package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DailyReadingStat
import com.example.ui.LibraryViewModel
import com.example.ui.theme.EmeraldContainerLight
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldMedium
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(
  viewModel: LibraryViewModel,
  onBack: () -> Unit
) {
  val stats by viewModel.last7DaysStats.collectAsState()
  val goal by viewModel.readingGoal.collectAsState()
  val streak by viewModel.streak.collectAsState()

  var targetPagesSlider by remember(goal) { mutableFloatStateOf(goal.targetPages.toFloat()) }
  var targetMinutesSlider by remember(goal) { mutableFloatStateOf(goal.targetMinutes.toFloat()) }

  val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
  val todayStat = stats.find { it.date == todayStr } ?: DailyReadingStat(todayStr, 0, 0)

  val todayPagesProgress = (todayStat.pagesRead.toFloat() / goal.targetPages.coerceAtLeast(1)).coerceIn(0f, 1f)
  val todayMinutesProgress = (todayStat.durationMinutes.toFloat() / goal.targetMinutes.coerceAtLeast(1)).coerceIn(0f, 1f)

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            "Okuma İstatistikleri & Hedef",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
          )
        },
        navigationIcon = {
          IconButton(onClick = onBack, modifier = Modifier.testTag("stats_back_btn")) {
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
      verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

      // 1. Streak Card (Kesintisiz Seri)
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = EmeraldPrimary),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("streak_card")
      ) {
        Row(
          modifier = Modifier.padding(20.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(54.dp)
              .clip(CircleShape)
              .background(GoldPrimary),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.LocalFireDepartment,
              contentDescription = "Seri",
              tint = Color.White,
              modifier = Modifier.size(32.dp)
            )
          }
          Spacer(modifier = Modifier.width(16.dp))
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "Kesintisiz Okuma Serisi",
              style = MaterialTheme.typography.labelMedium,
              color = EmeraldContainerLight
            )
            Row(verticalAlignment = Alignment.Bottom) {
              Text(
                text = "$streak",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
              )
              Text(
                text = " Gün Düzenli Okuma",
                style = MaterialTheme.typography.titleSmall,
                color = Color.White.copy(alpha = 0.9f),
                modifier = Modifier.padding(bottom = 4.dp, start = 6.dp)
              )
            }
            Text(
              text = if (streak > 0) "Maaşallah! İlim yolculuğunuz kesintisiz sürüyor." else "Bugün okumaya başlayarak seriyi başlatın!",
              style = MaterialTheme.typography.bodySmall,
              color = EmeraldContainerLight.copy(alpha = 0.8f)
            )
          }
        }
      }

      // 2. Today's Goal Progress
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(20.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.TrackChanges, contentDescription = null, tint = EmeraldPrimary)
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Bugünkü Hedef Durumu",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
              )
            }
            Text(
              text = "%${((todayPagesProgress + todayMinutesProgress) / 2f * 100).toInt()}",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = EmeraldPrimary
            )
          }

          Spacer(modifier = Modifier.height(16.dp))

          // Pages Progress
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(
              text = "Okunan Sayfa: ${todayStat.pagesRead} / ${goal.targetPages}",
              style = MaterialTheme.typography.bodyMedium,
              fontWeight = FontWeight.Medium
            )
            Text(
              text = "%${(todayPagesProgress * 100).toInt()}",
              style = MaterialTheme.typography.bodySmall,
              color = EmeraldMedium,
              fontWeight = FontWeight.Bold
            )
          }
          Spacer(modifier = Modifier.height(6.dp))
          LinearProgressIndicator(
            progress = { todayPagesProgress },
            modifier = Modifier
              .fillMaxWidth()
              .height(10.dp)
              .clip(RoundedCornerShape(5.dp)),
            color = EmeraldPrimary,
            trackColor = Color.Gray.copy(alpha = 0.2f)
          )

          Spacer(modifier = Modifier.height(14.dp))

          // Minutes Progress
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(
              text = "Okuma Süresi: ${todayStat.durationMinutes} / ${goal.targetMinutes} dk",
              style = MaterialTheme.typography.bodyMedium,
              fontWeight = FontWeight.Medium
            )
            Text(
              text = "%${(todayMinutesProgress * 100).toInt()}",
              style = MaterialTheme.typography.bodySmall,
              color = GoldPrimary,
              fontWeight = FontWeight.Bold
            )
          }
          Spacer(modifier = Modifier.height(6.dp))
          LinearProgressIndicator(
            progress = { todayMinutesProgress },
            modifier = Modifier
              .fillMaxWidth()
              .height(10.dp)
              .clip(RoundedCornerShape(5.dp)),
            color = GoldPrimary,
            trackColor = Color.Gray.copy(alpha = 0.2f)
          )
        }
      }

      // 3. 7 Günlük Okuma Grafiği (Last 7 Days Bar Chart)
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(20.dp)) {
          Text(
            text = "7 Günlük Okuma Grafiği",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = "Son 7 günde okunan sayfa adetleri:",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
          )

          Spacer(modifier = Modifier.height(20.dp))

          // 7-day Bar Chart Visualizer
          SevenDaysBarChart(stats = stats, goalPages = goal.targetPages)
        }
      }

      // 4. Goal Adjustment Sliders
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(20.dp)) {
          Text(
            text = "Günlük Hedef Belirleme",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.height(14.dp))

          // Daily Page Goal SeekBar
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.AutoStories, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(20.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Günlük Sayfa Hedefi", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
            }
            Text(
              text = "${targetPagesSlider.toInt()} Sayfa",
              style = MaterialTheme.typography.bodyLarge,
              fontWeight = FontWeight.Bold,
              color = EmeraldPrimary
            )
          }
          Slider(
            value = targetPagesSlider,
            onValueChange = {
              targetPagesSlider = it
              viewModel.updateReadingGoal(it.toInt(), targetMinutesSlider.toInt())
            },
            valueRange = 5f..100f,
            steps = 18,
            colors = SliderDefaults.colors(thumbColor = EmeraldPrimary, activeTrackColor = EmeraldPrimary),
            modifier = Modifier.testTag("goal_pages_slider")
          )

          Spacer(modifier = Modifier.height(14.dp))

          // Daily Minutes Goal SeekBar
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.AccessTime, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(20.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Günlük Süre Hedefi", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
            }
            Text(
              text = "${targetMinutesSlider.toInt()} Dakika",
              style = MaterialTheme.typography.bodyLarge,
              fontWeight = FontWeight.Bold,
              color = GoldPrimary
            )
          }
          Slider(
            value = targetMinutesSlider,
            onValueChange = {
              targetMinutesSlider = it
              viewModel.updateReadingGoal(targetPagesSlider.toInt(), it.toInt())
            },
            valueRange = 10f..120f,
            steps = 21,
            colors = SliderDefaults.colors(thumbColor = GoldPrimary, activeTrackColor = GoldPrimary),
            modifier = Modifier.testTag("goal_minutes_slider")
          )
        }
      }
    }
  }
}

@Composable
fun SevenDaysBarChart(
  stats: List<DailyReadingStat>,
  goalPages: Int
) {
  val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
  val dayFormat = SimpleDateFormat("EEE", Locale("tr", "TR"))

  // Build the last 7 days list
  val last7Days = remember(stats) {
    val list = mutableListOf<DailyReadingStat>()
    val cal = Calendar.getInstance()
    cal.add(Calendar.DAY_OF_YEAR, -6)
    val statMap = stats.associateBy { it.date }

    for (i in 0..6) {
      val dStr = dateFormat.format(cal.time)
      val existing = statMap[dStr]
      list.add(existing ?: DailyReadingStat(dStr, 0, 0))
      cal.add(Calendar.DAY_OF_YEAR, 1)
    }
    list
  }

  val maxPages = (last7Days.maxOfOrNull { it.pagesRead } ?: goalPages).coerceAtLeast(goalPages).coerceAtLeast(10)

  Row(
    modifier = Modifier
      .fillMaxWidth()
      .height(180.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.Bottom
  ) {
    last7Days.forEach { item ->
      val ratio = (item.pagesRead.toFloat() / maxPages).coerceIn(0.06f, 1f)
      val dayLabel = try {
        val parsed = dateFormat.parse(item.date)
        dayFormat.format(parsed ?: Date())
      } catch (_: Exception) {
        item.date.takeLast(2)
      }

      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier.weight(1f)
      ) {
        Text(
          text = if (item.pagesRead > 0) "${item.pagesRead}" else "",
          style = MaterialTheme.typography.labelSmall,
          color = EmeraldPrimary,
          fontWeight = FontWeight.Bold,
          fontSize = 11.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
          modifier = Modifier
            .width(22.dp)
            .height((130 * ratio).dp)
            .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
            .background(if (item.pagesRead >= goalPages) EmeraldPrimary else EmeraldMedium.copy(alpha = 0.6f))
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = dayLabel,
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          fontSize = 11.sp
        )
      }
    }
  }
}
