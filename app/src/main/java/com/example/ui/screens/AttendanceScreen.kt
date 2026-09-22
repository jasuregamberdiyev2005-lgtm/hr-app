package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun AttendanceScreen(
  currentUser: AuthUser? = null,
  attendanceList: List<DailyAttendance>,
  onUpdateStatus: (employeeId: String, newStatus: AttendanceStatus) -> Unit,
  onMarkAllPresent: () -> Unit,
  onExportReport: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  val isEmployee = currentUser?.role == UserRole.EMPLOYEE
  val isDirector = currentUser?.role == UserRole.DIRECTOR
  val isHR = currentUser?.role == UserRole.HR_MANAGER

  var selectedViewMode by remember { mutableIntStateOf(0) } // 0: Kunlik jadval, 1: Oylik kalendar
  var selectedCalendarDay by remember { mutableIntStateOf(17) } // Default: today (17-Sep)

  val presentCount = attendanceList.count { it.status == AttendanceStatus.PRESENT }
  val lateCount = attendanceList.count { it.status == AttendanceStatus.LATE }
  val remoteCount = attendanceList.count { it.status == AttendanceStatus.REMOTE }
  val absentCount = attendanceList.count { it.status == AttendanceStatus.ABSENT }
  val onLeaveCount = attendanceList.count { it.status == AttendanceStatus.ON_LEAVE }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    // Role banner
    Surface(
      color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = if (isEmployee) Icons.Default.PersonPinCircle else Icons.Default.FactCheck,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.primary,
          modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = when {
            isEmployee -> "Shaxsiy va umumiy jamoa davomati jadvali (Faqat ko'rish)"
            isHR -> "HR boshqaruvi: Kunlik davomatni belgilash va oylik tabel nazorati"
            isDirector -> "Direktor nazorati: Kompaniya intizomi va davomat tahlili"
            else -> "Davomat tizimi"
          },
          style = MaterialTheme.typography.bodySmall,
          fontWeight = FontWeight.Medium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    // Mode Switcher Tab Row
    TabRow(
      selectedTabIndex = selectedViewMode,
      containerColor = MaterialTheme.colorScheme.surface,
      contentColor = CorporatePrimary
    ) {
      Tab(
        selected = selectedViewMode == 0,
        onClick = { selectedViewMode = 0 },
        text = { Text("Kunlik davomat jadvali", fontWeight = FontWeight.SemiBold) },
        icon = { Icon(Icons.Default.Today, contentDescription = null) }
      )
      Tab(
        selected = selectedViewMode == 1,
        onClick = { selectedViewMode = 1 },
        text = { Text("Oylik kalendar ko'rinishi", fontWeight = FontWeight.SemiBold) },
        icon = { Icon(Icons.Default.CalendarMonth, contentDescription = null) }
      )
    }

    if (selectedViewMode == 0) {
      // 1. Daily Attendance View
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
      ) {
        // Stats Overview Card
        item {
          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Column {
                  Text(
                    text = "Bugungi holat: 17-Sentabr, 2026",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                  )
                  Text(
                    text = "Jami ${attendanceList.size} nafar xodimdan",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }
                if (!isEmployee) {
                  Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    OutlinedButton(
                      onClick = onExportReport,
                      contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                      shape = RoundedCornerShape(10.dp),
                      modifier = Modifier.testTag("export_attendance_button")
                    ) {
                      Icon(Icons.Default.FileDownload, contentDescription = null, modifier = Modifier.size(16.dp))
                      Spacer(modifier = Modifier.width(4.dp))
                      Text("Eksport", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    if (isHR) {
                      Button(
                        onClick = onMarkAllPresent,
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("mark_all_present_button")
                      ) {
                        Icon(Icons.Default.DoneAll, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Keldi", fontSize = 11.sp)
                      }
                    }
                  }
                }
              }

              Spacer(modifier = Modifier.height(14.dp))

              // Pill stats
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                AttendanceMiniStat(label = "Keldi", count = presentCount, color = StatusSuccessText, bgColor = StatusSuccessBg, modifier = Modifier.weight(1f))
                AttendanceMiniStat(label = "Kechikdi", count = lateCount, color = StatusWarningText, bgColor = StatusWarningBg, modifier = Modifier.weight(1f))
                AttendanceMiniStat(label = "Masofaviy", count = remoteCount, color = StatusPurpleText, bgColor = StatusPurpleBg, modifier = Modifier.weight(1f))
                AttendanceMiniStat(label = "Kelmadi", count = absentCount, color = StatusDangerText, bgColor = StatusDangerBg, modifier = Modifier.weight(1f))
              }
            }
          }
        }

        item {
          Text(
            text = "Xodimlar davomati",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
          )
        }

        // Attendance list items
        items(attendanceList, key = { it.employeeId }) { item ->
          DailyAttendanceCard(
            item = item,
            canChangeStatus = isHR,
            onSelectStatus = { newStatus -> onUpdateStatus(item.employeeId, newStatus) }
          )
        }
      }
    } else {
      // 2. Monthly Calendar View
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
      ) {
        item {
          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = "Sentabr 2026",
                  style = MaterialTheme.typography.titleLarge,
                  fontWeight = FontWeight.Bold
                )
                Text(
                  text = "Oylik o'rtacha: 91.4%",
                  style = MaterialTheme.typography.bodySmall,
                  fontWeight = FontWeight.SemiBold,
                  color = StatusSuccessText
                )
              }

              Spacer(modifier = Modifier.height(16.dp))

              // Weekdays header
              val weekdays = listOf("Du", "Se", "Ch", "Pa", "Ju", "Sh", "Ya")
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
              ) {
                weekdays.forEachIndexed { index, day ->
                  Text(
                    text = day,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = if (index >= 5) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.width(36.dp),
                    textAlign = TextAlign.Center
                  )
                }
              }

              Spacer(modifier = Modifier.height(10.dp))

              // 30 days grid
              // September 2026 starts on Tuesday (offset 1 day)
              val daysInMonth = 30
              val offset = 1 // Tuesday start

              Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                var currentDay = 1
                for (week in 0..4) {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                  ) {
                    for (dayOfWeek in 0..6) {
                      val isCurrentMonth = (week == 0 && dayOfWeek >= offset) || (week > 0 && currentDay <= daysInMonth)
                      if (isCurrentMonth && currentDay <= daysInMonth) {
                        val dayNumber = currentDay
                        val isSelected = dayNumber == selectedCalendarDay
                        val isToday = dayNumber == 17
                        val isWeekend = dayOfWeek >= 5

                        Box(
                          modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(
                              when {
                                isSelected -> CorporatePrimary
                                isToday -> CorporatePrimaryContainer
                                else -> Color.Transparent
                              }
                            )
                            .border(
                              width = if (isToday && !isSelected) 1.5.dp else 0.dp,
                              color = CorporatePrimary,
                              shape = CircleShape
                            )
                            .clickable { selectedCalendarDay = dayNumber },
                          contentAlignment = Alignment.Center
                        ) {
                          Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                              text = "$dayNumber",
                              fontSize = 13.sp,
                              fontWeight = if (isToday || isSelected) FontWeight.Bold else FontWeight.Normal,
                              color = when {
                                isSelected -> Color.White
                                isWeekend -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                else -> MaterialTheme.colorScheme.onSurface
                              }
                            )
                            if (!isWeekend) {
                              Box(
                                modifier = Modifier
                                  .size(4.dp)
                                  .clip(CircleShape)
                                  .background(
                                    if (isSelected) Color.White
                                    else if (dayNumber > 17) Color.LightGray
                                    else StatusSuccessText
                                  )
                              )
                            }
                          }
                        }
                        currentDay++
                      } else {
                        Spacer(modifier = Modifier.size(38.dp))
                      }
                    }
                  }
                }
              }
            }
          }
        }

        // Selected Day Details Card
        item {
          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = "$selectedCalendarDay-Sentabr kunlik hisoboti",
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.Bold
                )
                if (selectedCalendarDay == 17) {
                  StatusBadge(text = "Bugun", backgroundColor = CorporatePrimaryContainer, textColor = CorporatePrimary)
                } else if (selectedCalendarDay > 17) {
                  StatusBadge(text = "Kelgusi kun", backgroundColor = Color(0xFFF1F5F9), textColor = Color(0xFF64748B))
                } else {
                  StatusBadge(text = "O'tgan kun", backgroundColor = StatusSuccessBg, textColor = StatusSuccessText)
                }
              }

              Spacer(modifier = Modifier.height(12.dp))

              if (selectedCalendarDay > 17) {
                Text(
                  text = "Bu sana uchun hali davomat ma'lumotlari kiritilmagan.",
                  style = MaterialTheme.typography.bodyMedium,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              } else {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Text("Ishga kelganlar:", style = MaterialTheme.typography.bodyMedium)
                  Text("9 ta (90%)", fontWeight = FontWeight.Bold, color = StatusSuccessText)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Text("Kechikkanlar:", style = MaterialTheme.typography.bodyMedium)
                  Text("1 ta", fontWeight = FontWeight.Bold, color = StatusWarningText)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Text("Ta'tilda / Masofaviy:", style = MaterialTheme.typography.bodyMedium)
                  Text("2 ta", fontWeight = FontWeight.Bold, color = CorporatePrimary)
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
fun AttendanceMiniStat(
  label: String,
  count: Int,
  color: Color,
  bgColor: Color,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(8.dp))
      .background(bgColor)
      .padding(vertical = 8.dp, horizontal = 6.dp),
    contentAlignment = Alignment.Center
  ) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      Text(text = "$count", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = color)
      Text(text = label, fontSize = 11.sp, color = color, maxLines = 1)
    }
  }
}

@Composable
fun DailyAttendanceCard(
  item: DailyAttendance,
  canChangeStatus: Boolean = true,
  onSelectStatus: (AttendanceStatus) -> Unit
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = item.employeeName,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = item.department,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          if (item.checkInTime != null) {
            Icon(
              imageVector = Icons.Default.AccessTime,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = item.checkInTime,
              style = MaterialTheme.typography.bodySmall,
              fontWeight = FontWeight.SemiBold,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(8.dp))
          }
          AttendanceStatusBadge(status = item.status)
        }
      }

      if (canChangeStatus) {
        Spacer(modifier = Modifier.height(12.dp))

        // Clickable quick toggle status pills
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          listOf(
            AttendanceStatus.PRESENT,
            AttendanceStatus.LATE,
            AttendanceStatus.REMOTE,
            AttendanceStatus.ABSENT
          ).forEach { st ->
            val isCurrent = item.status == st
            OutlinedButton(
              onClick = { onSelectStatus(st) },
              modifier = Modifier
                .weight(1f)
                .height(34.dp),
              contentPadding = PaddingValues(horizontal = 4.dp),
              colors = ButtonDefaults.outlinedButtonColors(
                containerColor = if (isCurrent) CorporatePrimaryContainer else Color.Transparent,
                contentColor = if (isCurrent) CorporatePrimary else MaterialTheme.colorScheme.onSurfaceVariant
              ),
              border = ButtonDefaults.outlinedButtonBorder.takeIf { !isCurrent }
            ) {
              Text(
                text = st.label,
                fontSize = 11.sp,
                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                maxLines = 1
              )
            }
          }
        }
      }
    }
  }
}
