package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
fun DashboardScreen(
  currentUser: AuthUser? = null,
  employees: List<Employee>,
  departments: List<Department>,
  attendanceList: List<DailyAttendance>,
  leaveRequests: List<LeaveRequest>,
  onNavigateToTab: (HrmTab) -> Unit,
  onAddEmployeeClick: () -> Unit,
  onAddLeaveClick: () -> Unit,
  onApproveLeave: (String) -> Unit,
  onRejectLeave: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val totalEmployees = employees.size
  val totalDepartments = departments.size
  val totalPayroll = employees.sumOf { it.netSalary }
  val isDirector = currentUser?.role == UserRole.DIRECTOR
  val presentCount = attendanceList.count { it.status == AttendanceStatus.PRESENT || it.status == AttendanceStatus.LATE || it.status == AttendanceStatus.REMOTE }
  val attendanceRate = if (attendanceList.isNotEmpty()) {
    ((presentCount.toFloat() / attendanceList.size.toFloat()) * 100).toInt()
  } else 0
  val pendingLeaves = leaveRequests.filter { it.status == LeaveStatus.PENDING }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
    contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)
  ) {
    // Welcome Greeting Card
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CorporatePrimary)
      ) {
        Box(modifier = Modifier.padding(20.dp)) {
          Column {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(
                  text = if (isDirector) "Assalomu alaykum, Hurmatli Rahbar!" else "Assalomu alaykum, HR Menejer!",
                  color = Color.White,
                  style = MaterialTheme.typography.titleLarge,
                  fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = if (isDirector) "Kompaniya strategik ko'rsatkichlari va moliya nazorati"
                         else "Bugungi xodimlar boshqaruvi va tahliliy hisoboti",
                  color = Color.White.copy(alpha = 0.85f),
                  style = MaterialTheme.typography.bodyMedium
                )
              }
              Icon(
                imageVector = if (isDirector) Icons.Default.BusinessCenter else Icons.Default.AdminPanelSettings,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.9f),
                modifier = Modifier.size(36.dp)
              )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Quick status pills in header
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(8.dp))
                  .background(Color.White.copy(alpha = 0.2f))
                  .padding(horizontal = 10.dp, vertical = 6.dp)
              ) {
                Text(
                  text = "Ish kuni: 17-Sentabr",
                  color = Color.White,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Medium
                )
              }
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(8.dp))
                  .background(Color.White.copy(alpha = 0.2f))
                  .padding(horizontal = 10.dp, vertical = 6.dp)
              ) {
                Text(
                  text = if (isDirector) "Rol: Bosh Direktor" else "Rol: HR Boshqaruvi",
                  color = Color.White,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Medium
                )
              }
            }
          }
        }
      }
    }

    // 4 Key Metrics (KPIs)
    item {
      Text(
        text = if (isDirector) "Kompaniya asosiy ko'rsatkichlari" else "Kadrlar asosiy ko'rsatkichlari",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground
      )
    }

    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        StatCard(
          title = "Jami xodimlar",
          value = "$totalEmployees ta",
          subtitle = "+2 bu oyda",
          icon = Icons.Default.People,
          iconBgColor = Color(0xFFDBEAFE),
          iconColor = Color(0xFF1D4ED8),
          modifier = Modifier.weight(1f)
        )
        if (isDirector) {
          StatCard(
            title = "Oylik fondi",
            value = "${totalPayroll / 1_000_000} mln",
            subtitle = "Sentabr oyi",
            icon = Icons.Default.Payments,
            iconBgColor = Color(0xFFDCFCE7),
            iconColor = Color(0xFF16A34A),
            modifier = Modifier.weight(1f)
          )
        } else {
          StatCard(
            title = "Bo'limlar",
            value = "$totalDepartments ta",
            subtitle = "Barcha faol",
            icon = Icons.Default.Business,
            iconBgColor = Color(0xFFE0F2FE),
            iconColor = Color(0xFF0284C7),
            modifier = Modifier.weight(1f)
          )
        }
      }
    }

    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        StatCard(
          title = "Bugungi davomat",
          value = "$attendanceRate%",
          subtitle = "$presentCount/$totalEmployees keldi",
          icon = Icons.Default.FactCheck,
          iconBgColor = Color(0xFFDCFCE7),
          iconColor = Color(0xFF16A34A),
          modifier = Modifier.weight(1f)
        )
        StatCard(
          title = "Yangi arizalar",
          value = "${pendingLeaves.size} ta",
          subtitle = if (pendingLeaves.isNotEmpty()) "Kutilmoqda" else "Ko'rib chiqilgan",
          icon = Icons.Default.DateRange,
          iconBgColor = if (pendingLeaves.isNotEmpty()) Color(0xFFFEF3C7) else Color(0xFFF1F5F9),
          iconColor = if (pendingLeaves.isNotEmpty()) Color(0xFFD97706) else Color(0xFF64748B),
          modifier = Modifier.weight(1f)
        )
      }
    }

    // Quick Actions adapted by role
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = if (isDirector) "Rahbar tezkor amallari" else "HR tezkor amallari",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(12.dp))
          if (isDirector) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              FilledTonalButton(
                onClick = { onNavigateToTab(HrmTab.PAYROLL) },
                modifier = Modifier.weight(1f).testTag("quick_action_director_payroll")
              ) {
                Icon(Icons.Default.Payments, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Ish haqi", fontSize = 12.sp)
              }
              FilledTonalButton(
                onClick = { onNavigateToTab(HrmTab.KPI) },
                modifier = Modifier.weight(1f).testTag("quick_action_director_kpi")
              ) {
                Icon(Icons.Default.Star, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("KPI baholash", fontSize = 12.sp)
              }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              OutlinedButton(
                onClick = { onNavigateToTab(HrmTab.DEPARTMENTS) },
                modifier = Modifier.weight(1f).testTag("quick_action_director_depts")
              ) {
                Icon(Icons.Default.Business, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Bo'limlar byudjeti", fontSize = 12.sp)
              }
              OutlinedButton(
                onClick = { onNavigateToTab(HrmTab.ANNOUNCEMENTS) },
                modifier = Modifier.weight(1f).testTag("quick_action_director_announcements")
              ) {
                Icon(Icons.Default.Campaign, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Rasmiy e'lon", fontSize = 12.sp)
              }
            }
          } else {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              FilledTonalButton(
                onClick = onAddEmployeeClick,
                modifier = Modifier
                  .weight(1f)
                  .testTag("quick_action_add_employee")
              ) {
                Icon(Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Xodim qo'shish", fontSize = 12.sp)
              }

              FilledTonalButton(
                onClick = { onNavigateToTab(HrmTab.ATTENDANCE) },
                modifier = Modifier
                  .weight(1f)
                  .testTag("quick_action_attendance")
              ) {
                Icon(Icons.Default.FactCheck, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Davomat (Tabel)", fontSize = 12.sp)
              }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              OutlinedButton(
                onClick = { onNavigateToTab(HrmTab.LEAVES) },
                modifier = Modifier.weight(1f)
              ) {
                Icon(Icons.Default.DateRange, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Arizalar (${pendingLeaves.size})", fontSize = 12.sp)
              }
              OutlinedButton(
                onClick = { onNavigateToTab(HrmTab.DOCUMENTS) },
                modifier = Modifier.weight(1f)
              ) {
                Icon(Icons.Default.Description, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Spravka berish", fontSize = 12.sp)
              }
            }
          }
        }
      }
    }

    // Chart 1: Bo'limlar bo'yicha xodimlar taqsimoti
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
                text = "Bo'limlar bo'yicha xodimlar",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "Tashkilot xodimlarining bo'linmalar kesimi",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
            TextButton(onClick = { onNavigateToTab(HrmTab.DEPARTMENTS) }) {
              Text("Bo'limlar")
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          // Visual bar representation
          val maxDeptEmployees = (departments.maxOfOrNull { it.employeeCount } ?: 1).coerceAtLeast(1)
          departments.forEach { dept ->
            val ratio = dept.employeeCount.toFloat() / maxDeptEmployees.toFloat()
            Column(modifier = Modifier.padding(vertical = 6.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(
                  text = dept.name,
                  style = MaterialTheme.typography.bodySmall,
                  fontWeight = FontWeight.SemiBold
                )
                Text(
                  text = "${dept.employeeCount} ta (${if (totalEmployees > 0) (dept.employeeCount * 100 / totalEmployees) else 0}%)",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
              Spacer(modifier = Modifier.height(4.dp))
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .height(10.dp)
                  .clip(RoundedCornerShape(5.dp))
                  .background(MaterialTheme.colorScheme.surfaceVariant)
              ) {
                Box(
                  modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(fraction = ratio.coerceIn(0.05f, 1f))
                    .clip(RoundedCornerShape(5.dp))
                    .background(CorporatePrimary)
                )
              }
            }
          }
        }
      }
    }

    // Chart 2: Davomat dinamikasi (Haftalik grafik)
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
                text = "Haftalik davomat dinamikasi",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "Oxirgi 5 kunlik ko'rsatkich (%)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
            TextButton(onClick = { onNavigateToTab(HrmTab.ATTENDANCE) }) {
              Text("Batafsil")
            }
          }

          Spacer(modifier = Modifier.height(20.dp))

          // Attendance Bar Chart
          val weeklyDays = listOf(
            Triple("Dush", 95, true),
            Triple("Sesh", 90, true),
            Triple("Chor", 85, true),
            Triple("Pay", 92, true),
            Triple("Juma", attendanceRate, false)
          )

          Row(
            modifier = Modifier
              .fillMaxWidth()
              .height(130.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
          ) {
            weeklyDays.forEach { (day, rate, _) ->
              Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier.fillMaxHeight()
              ) {
                Text(
                  text = "$rate%",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (rate >= 90) StatusSuccessText else CorporatePrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                  modifier = Modifier
                    .width(32.dp)
                    .height((80 * (rate / 100f)).dp)
                    .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                    .background(
                      if (rate >= 90) Color(0xFF10B981) else Color(0xFF2563EB)
                    )
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                  text = day,
                  fontSize = 12.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          }
        }
      }
    }

    // Pending Leaves Section
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
              Text(
                text = "Kutilayotgan arizalar",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
              )
              Spacer(modifier = Modifier.width(8.dp))
              Badge(containerColor = Color(0xFFFEF3C7), contentColor = Color(0xFFB45309)) {
                Text("${pendingLeaves.size}")
              }
            }
            TextButton(onClick = { onNavigateToTab(HrmTab.LEAVES) }) {
              Text("Barchasi")
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          if (pendingLeaves.isEmpty()) {
            Text(
              text = "Hozirda yangi kutilayotgan arizalar mavjud emas.",
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.padding(vertical = 8.dp)
            )
          } else {
            pendingLeaves.take(3).forEach { req ->
              Card(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 4.dp),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
              ) {
                Column(modifier = Modifier.padding(12.dp)) {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Column {
                      Text(
                        text = req.employeeName,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                      )
                      Text(
                        text = "${req.leaveType.label} • ${req.daysCount} kun (${req.startDate} - ${req.endDate})",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                      )
                    }
                    LeaveStatusBadge(status = req.status)
                  }

                  if (req.reason.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                      text = "\"${req.reason}\"",
                      style = MaterialTheme.typography.bodySmall,
                      color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                  }

                  Spacer(modifier = Modifier.height(8.dp))

                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    OutlinedButton(
                      onClick = { onRejectLeave(req.id) },
                      modifier = Modifier.height(34.dp),
                      contentPadding = PaddingValues(horizontal = 10.dp)
                    ) {
                      Text("Rad etish", fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                      onClick = { onApproveLeave(req.id) },
                      modifier = Modifier.height(34.dp),
                      contentPadding = PaddingValues(horizontal = 12.dp)
                    ) {
                      Text("Tasdiqlash", fontSize = 12.sp)
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
}
