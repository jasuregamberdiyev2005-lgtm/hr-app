package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun EmployeePortalScreen(
  currentUser: AuthUser,
  employees: List<Employee>,
  attendanceList: List<DailyAttendance>,
  leaveRequests: List<LeaveRequest>,
  onSelfCheckIn: (AttendanceStatus) -> Unit,
  onRequestLeave: () -> Unit,
  onRequestDocument: () -> Unit = {},
  onNavigateToAnnouncements: () -> Unit = {},
  onNavigateToKpi: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  val employee = remember(currentUser, employees) {
    employees.find { it.id == currentUser.employeeId } ?: employees.first()
  }

  val todayAttendance = remember(attendanceList, employee) {
    attendanceList.find { it.employeeId == employee.id }
  }

  val myLeaveRequests = remember(leaveRequests, employee) {
    leaveRequests.filter { it.employeeId == employee.id }
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(CorporateBg)
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
    contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)
  ) {
    // 1. Employee Profile Card
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CorporatePrimary)
      ) {
        Column(modifier = Modifier.padding(20.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
          ) {
            EmployeeAvatar(
              firstName = employee.firstName,
              lastName = employee.lastName,
              colorHex = 0xFFFFFFFF,
              size = 56
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
              Text(
                text = employee.fullName,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White
              )
              Text(
                text = employee.position,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.9f)
              )
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = "${employee.department} • ${employee.email}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.75f)
              )
            }
          }

          Spacer(modifier = Modifier.height(16.dp))
          HorizontalDivider(color = Color.White.copy(alpha = 0.2f))
          Spacer(modifier = Modifier.height(12.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column {
              Text(text = "Telefon", color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp)
              Text(text = employee.phone, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            }
            Column {
              Text(text = "Ish boshlagan", color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp)
              Text(text = employee.joinDate, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            }
            Column {
              Text(text = "Holat", color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp)
              Text(text = employee.status.label, color = Color(0xFF86EFAC), fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
          }
        }
      }
    }

    // 2. Tezkor Xizmatlar & Portallar
    item {
      Column {
        Text(
          text = "Tezkor xizmatlar",
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          // Document / Spravka Request
          Card(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(12.dp))
              .clickable { onRequestDocument() },
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
          ) {
            Column(
              modifier = Modifier.padding(12.dp),
              horizontalAlignment = Alignment.Start
            ) {
              Box(
                modifier = Modifier
                  .size(34.dp)
                  .clip(RoundedCornerShape(8.dp))
                  .background(CorporatePrimaryContainer),
                contentAlignment = Alignment.Center
              ) {
                Icon(Icons.Default.Description, contentDescription = null, tint = CorporatePrimary, modifier = Modifier.size(18.dp))
              }
              Spacer(modifier = Modifier.height(8.dp))
              Text("Spravka olish", fontWeight = FontWeight.Bold, fontSize = 13.sp)
              Text("Ish joyi & daromad", fontSize = 10.sp, color = CorporateTextSecondary)
            }
          }

          // Announcements & Birthdays
          Card(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(12.dp))
              .clickable { onNavigateToAnnouncements() },
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
          ) {
            Column(
              modifier = Modifier.padding(12.dp),
              horizontalAlignment = Alignment.Start
            ) {
              Box(
                modifier = Modifier
                  .size(34.dp)
                  .clip(RoundedCornerShape(8.dp))
                  .background(Color(0xFFFCE7F3)),
                contentAlignment = Alignment.Center
              ) {
                Icon(Icons.Default.Campaign, contentDescription = null, tint = Color(0xFFDB2777), modifier = Modifier.size(18.dp))
              }
              Spacer(modifier = Modifier.height(8.dp))
              Text("E'lonlar & Bayram", fontWeight = FontWeight.Bold, fontSize = 13.sp)
              Text("Yangiliklar & tabrik", fontSize = 10.sp, color = CorporateTextSecondary)
            }
          }

          // KPI Tasks
          Card(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(12.dp))
              .clickable { onNavigateToKpi() },
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
          ) {
            Column(
              modifier = Modifier.padding(12.dp),
              horizontalAlignment = Alignment.Start
            ) {
              Box(
                modifier = Modifier
                  .size(34.dp)
                  .clip(RoundedCornerShape(8.dp))
                  .background(Color(0xFFFEF3C7)),
                contentAlignment = Alignment.Center
              ) {
                Icon(Icons.Default.Flag, contentDescription = null, tint = Color(0xFFD97706), modifier = Modifier.size(18.dp))
              }
              Spacer(modifier = Modifier.height(8.dp))
              Text("Mening KPI", fontWeight = FontWeight.Bold, fontSize = 13.sp)
              Text("Vazifalar progressi", fontSize = 10.sp, color = CorporateTextSecondary)
            }
          }
        }
      }
    }

    // 3. Today's Attendance Check-in
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
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.AccessTime, contentDescription = null, tint = CorporatePrimary)
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Bugungi davomatingiz",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
              )
            }
            todayAttendance?.let {
              AttendanceStatusBadge(status = it.status)
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = if (todayAttendance?.checkInTime != null) {
              "Siz bugun soat ${todayAttendance.checkInTime} da tizimda qayd etilgansiz."
            } else {
              "Bugun ish boshlaganingizni belgilang:"
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          Spacer(modifier = Modifier.height(12.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Button(
              onClick = { onSelfCheckIn(AttendanceStatus.PRESENT) },
              modifier = Modifier
                .weight(1f)
                .testTag("self_checkin_present"),
              colors = ButtonDefaults.buttonColors(containerColor = StatusSuccessText)
            ) {
              Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Keldim", fontSize = 12.sp)
            }

            FilledTonalButton(
              onClick = { onSelfCheckIn(AttendanceStatus.REMOTE) },
              modifier = Modifier
                .weight(1f)
                .testTag("self_checkin_remote")
            ) {
              Icon(Icons.Default.Laptop, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Masofaviy", fontSize = 12.sp)
            }

            OutlinedButton(
              onClick = { onSelfCheckIn(AttendanceStatus.LATE) },
              modifier = Modifier
                .weight(1f)
                .testTag("self_checkin_late")
            ) {
              Text("Kechikdim", fontSize = 12.sp)
            }
          }
        }
      }
    }

    // 3. My Payslip
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
              text = "Mening oylik maoshim (Sentabr)",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "Hisoblangan",
              style = MaterialTheme.typography.labelSmall,
              color = StatusSuccessText,
              fontWeight = FontWeight.Bold
            )
          }

          Spacer(modifier = Modifier.height(14.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text("Asosiy maosh:", style = MaterialTheme.typography.bodyMedium)
            Text(formatUzs(employee.baseSalary), fontWeight = FontWeight.SemiBold)
          }

          Spacer(modifier = Modifier.height(6.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text("Bonus & Rag'batlantirish:", style = MaterialTheme.typography.bodyMedium)
            Text(
              "+${formatUzs(employee.bonus)}",
              fontWeight = FontWeight.SemiBold,
              color = StatusSuccessText
            )
          }

          Spacer(modifier = Modifier.height(6.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text("Ushlab qolingan (soliq/jarima):", style = MaterialTheme.typography.bodyMedium)
            Text(
              "-${formatUzs(employee.deduction)}",
              fontWeight = FontWeight.SemiBold,
              color = MaterialTheme.colorScheme.error
            )
          }

          Spacer(modifier = Modifier.height(10.dp))
          HorizontalDivider()
          Spacer(modifier = Modifier.height(10.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Sof qo'lga tegadigan summa:",
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold,
              color = CorporatePrimary
            )
            Text(
              text = formatUzs(employee.netSalary),
              style = MaterialTheme.typography.titleLarge,
              fontWeight = FontWeight.Bold,
              color = CorporatePrimary
            )
          }
        }
      }
    }

    // 4. My Leave Requests
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
              text = "Mening arizalarim (${myLeaveRequests.size})",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold
            )
            Button(
              onClick = onRequestLeave,
              contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
            ) {
              Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Yangi ariza", fontSize = 12.sp)
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          if (myLeaveRequests.isEmpty()) {
            Text(
              text = "Sizda hali arizalar mavjud emas. Yuqoridagi tugma orqali ta'til so'rovi yuborishingiz mumkin.",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          } else {
            myLeaveRequests.forEach { req ->
              Card(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 4.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
              ) {
                Column(modifier = Modifier.padding(10.dp)) {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Text(
                      text = "${req.leaveType.label} (${req.daysCount} kun)",
                      fontWeight = FontWeight.Bold,
                      fontSize = 13.sp
                    )
                    LeaveStatusBadge(status = req.status)
                  }
                  Spacer(modifier = Modifier.height(4.dp))
                  Text(
                    text = "Muddat: ${req.startDate} — ${req.endDate}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                  if (req.reason.isNotBlank()) {
                    Text(
                      text = "Sabab: \"${req.reason}\"",
                      fontSize = 12.sp,
                      color = MaterialTheme.colorScheme.onSurfaceVariant
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
}
