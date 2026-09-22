package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AuthUser
import com.example.model.KpiTask
import com.example.model.UserRole
import com.example.ui.theme.*

@Composable
fun KpiScreen(
  kpiTasks: List<KpiTask>,
  currentUser: AuthUser,
  onUpdateProgress: (String, Int) -> Unit,
  onRateTask: (String, Float) -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedDeptFilter by remember { mutableStateOf("Barchasi") }
  val departments = listOf("Barchasi", "IT & Dasturlash", "HR & Kadrlar", "Marketing & PR", "Moliya")

  val filteredTasks = remember(kpiTasks, selectedDeptFilter, currentUser) {
    val roleTasks = if (currentUser.role == UserRole.EMPLOYEE) {
      kpiTasks.filter { it.employeeId == currentUser.employeeId }
    } else {
      kpiTasks
    }

    if (selectedDeptFilter == "Barchasi") {
      roleTasks
    } else {
      roleTasks.filter { it.department.contains(selectedDeptFilter, ignoreCase = true) }
    }
  }

  val avgScore = remember(kpiTasks) {
    val rated = kpiTasks.filter { it.score > 0f }
    if (rated.isEmpty()) 0.0 else rated.map { it.score.toDouble() }.average()
  }

  val completedCount = remember(kpiTasks) {
    kpiTasks.count { it.progressPercent >= 100 }
  }

  Box(modifier = modifier.fillMaxSize().background(CorporateBg)) {
    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding = PaddingValues(16.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // 1. KPI Scoreboard Banner
      item {
        Card(
          modifier = Modifier.fillMaxWidth().testTag("kpi_scoreboard_banner"),
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = Color.Transparent),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .background(
                Brush.horizontalGradient(
                  colors = listOf(
                    Color(0xFF1E3A8A),
                    Color(0xFF2563EB),
                    Color(0xFF3B82F6)
                  )
                )
              )
              .padding(20.dp)
          ) {
            Column {
              Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Column {
                  Text(
                    text = "🎯 Kompaniya KPI va Baholash",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                  )
                  Text(
                    text = "Oylik maqsadlar, vazifalar va samaradorlik reytingi",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.85f)
                  )
                }

                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White.copy(alpha = 0.2f))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                      imageVector = Icons.Default.Star,
                      contentDescription = null,
                      tint = Color(0xFFFBBF24),
                      modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                      text = String.format("%.1f", avgScore),
                      fontWeight = FontWeight.Bold,
                      fontSize = 16.sp,
                      color = Color.White
                    )
                  }
                }
              }

              Spacer(modifier = Modifier.height(16.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
              ) {
                KpiStatItem(
                  title = "Vazifalar",
                  value = kpiTasks.size.toString(),
                  modifier = Modifier.weight(1f)
                )
                KpiStatItem(
                  title = "Bajarilgan",
                  value = "$completedCount ta",
                  modifier = Modifier.weight(1f)
                )
                KpiStatItem(
                  title = "Samaradorlik",
                  value = "${(avgScore * 20).toInt()}%",
                  modifier = Modifier.weight(1f)
                )
              }
            }
          }
        }
      }

      // 2. Department Filters (if Director or HR)
      if (currentUser.role != UserRole.EMPLOYEE) {
        item {
          LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(vertical = 4.dp)
          ) {
            items(departments) { dept ->
              val isSelected = selectedDeptFilter == dept
              FilterChip(
                selected = isSelected,
                onClick = { selectedDeptFilter = dept },
                label = {
                  Text(
                    text = dept,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                  )
                },
                colors = FilterChipDefaults.filterChipColors(
                  selectedContainerColor = CorporatePrimary,
                  selectedLabelColor = Color.White
                ),
                shape = RoundedCornerShape(20.dp)
              )
            }
          }
        }
      }

      // 3. Task List
      if (filteredTasks.isEmpty()) {
        item {
          Card(
            modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
          ) {
            Column(
              modifier = Modifier.fillMaxWidth().padding(32.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Icon(
                imageVector = Icons.Default.Checklist,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = CorporateTextSecondary
              )
              Spacer(modifier = Modifier.height(10.dp))
              Text(
                text = "KPI vazifalar topilmadi",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
              )
            }
          }
        }
      } else {
        items(filteredTasks, key = { it.id }) { task ->
          KpiTaskCard(
            task = task,
            currentUser = currentUser,
            onUpdateProgress = { newProgress -> onUpdateProgress(task.id, newProgress) },
            onRateTask = { score -> onRateTask(task.id, score) }
          )
        }
      }

      item {
        Spacer(modifier = Modifier.height(72.dp))
      }
    }
  }
}

@Composable
private fun KpiStatItem(
  title: String,
  value: String,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(10.dp))
      .background(Color.White.copy(alpha = 0.15f))
      .padding(vertical = 8.dp, horizontal = 10.dp),
    contentAlignment = Alignment.Center
  ) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      Text(text = value, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
      Text(text = title, color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp)
    }
  }
}

@Composable
fun KpiTaskCard(
  task: KpiTask,
  currentUser: AuthUser,
  onUpdateProgress: (Int) -> Unit,
  onRateTask: (Float) -> Unit
) {
  val animatedProgress by animateFloatAsState(
    targetValue = task.progressPercent / 100f,
    animationSpec = tween(durationMillis = 600),
    label = "kpi_progress"
  )

  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(38.dp)
              .clip(CircleShape)
              .background(CorporatePrimaryContainer),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Flag,
              contentDescription = null,
              tint = CorporatePrimary,
              modifier = Modifier.size(20.dp)
            )
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = task.title,
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "${task.employeeName} • ${task.department}",
              style = MaterialTheme.typography.labelSmall,
              color = CorporateTextSecondary
            )
          }
        }

        // Score Badge
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (task.score >= 4.5f) Color(0xFFD1FAE5) else Color(0xFFFEF3C7))
            .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Star,
              contentDescription = null,
              tint = if (task.score >= 4.5f) Color(0xFF047857) else Color(0xFFB45309),
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
              text = if (task.score > 0) "${task.score}" else "Kutilmoqda",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = if (task.score >= 4.5f) Color(0xFF047857) else Color(0xFFB45309)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = task.targetDescription,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Spacer(modifier = Modifier.height(12.dp))

      // Progress bar and percentage
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(
          text = "Bajarilish ko'rsatkichi",
          fontSize = 11.sp,
          color = CorporateTextSecondary
        )
        Text(
          text = "${task.progressPercent}%",
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          color = if (task.progressPercent == 100) StatusSuccessText else CorporatePrimary
        )
      }

      Spacer(modifier = Modifier.height(6.dp))

      LinearProgressIndicator(
        progress = { animatedProgress },
        modifier = Modifier
          .fillMaxWidth()
          .height(8.dp)
          .clip(RoundedCornerShape(4.dp)),
        color = if (task.progressPercent == 100) StatusSuccessText else CorporatePrimary,
        trackColor = CorporatePrimaryContainer
      )

      // Employee quick actions to increase progress
      if (currentUser.role == UserRole.EMPLOYEE && currentUser.employeeId == task.employeeId) {
        Spacer(modifier = Modifier.height(12.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          OutlinedButton(
            onClick = { onUpdateProgress(task.progressPercent + 10) },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(vertical = 4.dp)
          ) {
            Text("+10%", fontSize = 11.sp, fontWeight = FontWeight.Bold)
          }
          OutlinedButton(
            onClick = { onUpdateProgress(task.progressPercent + 25) },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(vertical = 4.dp)
          ) {
            Text("+25%", fontSize = 11.sp, fontWeight = FontWeight.Bold)
          }
          Button(
            onClick = { onUpdateProgress(100) },
            modifier = Modifier.weight(1.2f),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = StatusSuccessText),
            contentPadding = PaddingValues(vertical = 4.dp)
          ) {
            Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(2.dp))
            Text("100% Tugatildi", fontSize = 11.sp, fontWeight = FontWeight.Bold)
          }
        }
      }

      // Director / HR rating buttons
      if (currentUser.role != UserRole.EMPLOYEE) {
        Spacer(modifier = Modifier.height(12.dp))
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
        Spacer(modifier = Modifier.height(8.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(
            text = "Baholash (Baho qo'yish):",
            fontSize = 11.sp,
            color = CorporateTextSecondary,
            fontWeight = FontWeight.Medium
          )

          Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            listOf(4.0f, 4.5f, 4.8f, 5.0f).forEach { scoreVal ->
              val isSelected = task.score == scoreVal
              AssistChip(
                onClick = { onRateTask(scoreVal) },
                label = {
                  Text(
                    text = "★ $scoreVal",
                    fontSize = 10.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                  )
                },
                colors = AssistChipDefaults.assistChipColors(
                  containerColor = if (isSelected) Color(0xFFFEF3C7) else Color.Transparent
                )
              )
            }
          }
        }
      }
    }
  }
}
