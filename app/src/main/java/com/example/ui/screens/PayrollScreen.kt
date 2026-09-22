package com.example.ui.screens

import androidx.compose.foundation.background
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
fun PayrollScreen(
  currentUser: AuthUser? = null,
  employees: List<Employee>,
  searchQuery: String,
  onSearchChange: (String) -> Unit,
  onEditPayroll: (Employee) -> Unit,
  onExportReport: () -> Unit = {},
  onNavigateToDocuments: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  val isEmployee = currentUser?.role == UserRole.EMPLOYEE
  val isDirector = currentUser?.role == UserRole.DIRECTOR

  // If Employee, show personal payslip view
  if (isEmployee) {
    val myEmployee = remember(employees, currentUser) {
      employees.find { it.id == currentUser?.employeeId } ?: employees.first()
    }
    PersonalPayslipView(
      employee = myEmployee,
      onRequestCertificate = onNavigateToDocuments,
      modifier = modifier
    )
    return
  }

  // Otherwise, Director and HR view
  val totalBaseSalary = employees.sumOf { it.baseSalary }
  val totalBonus = employees.sumOf { it.bonus }
  val totalDeduction = employees.sumOf { it.deduction }
  val totalPayroll = employees.sumOf { it.netSalary }
  val averageSalary = if (employees.isNotEmpty()) totalPayroll / employees.size else 0L

  val filteredEmployees = remember(employees, searchQuery) {
    val query = searchQuery.trim().lowercase()
    if (query.isEmpty()) employees
    else employees.filter {
      it.fullName.lowercase().contains(query) ||
        it.position.lowercase().contains(query) ||
        it.department.lowercase().contains(query)
    }
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp),
    contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)
  ) {
    // Role banner
    item {
      Surface(
        color = if (isDirector) Color(0xFFEFF6FF) else Color(0xFFF0FDF4),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.padding(12.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = if (isDirector) Icons.Default.BusinessCenter else Icons.Default.AdminPanelSettings,
            contentDescription = null,
            tint = if (isDirector) Color(0xFF1D4ED8) else Color(0xFF15803D),
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = if (isDirector) "Direktor nazorati: Kompaniya oylik maosh fondi va moliyaviy hisoboti"
                   else "HR boshqaruvi: Xodimlar oylik maoshlari va bonus/chegirmalarini hisoblash",
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = if (isDirector) Color(0xFF1E40AF) else Color(0xFF166534)
          )
        }
      }
    }

    // KPI summary
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CorporatePrimary)
      ) {
        Column(modifier = Modifier.padding(20.dp)) {
          Text(
            text = "Sentabr oylik ish haqi fondi",
            color = Color.White.copy(alpha = 0.85f),
            style = MaterialTheme.typography.bodyMedium
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = formatUzs(totalPayroll),
            color = Color.White,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.height(14.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column {
              Text(text = "O'rtacha maosh", color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp)
              Text(text = formatUzs(averageSalary), color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            }
            Column {
              Text(text = "Jami bonuslar", color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp)
              Text(text = "+${formatUzs(totalBonus)}", color = Color(0xFF86EFAC), fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            }
            Column {
              Text(text = "Ushlab qolingan", color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp)
              Text(text = "-${formatUzs(totalDeduction)}", color = Color(0xFFFCA5A5), fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            }
          }
        }
      }
    }

    // Search bar and Export
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        OutlinedTextField(
          value = searchQuery,
          onValueChange = onSearchChange,
          placeholder = { Text("Qidirish...") },
          leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
          trailingIcon = {
            if (searchQuery.isNotEmpty()) {
              IconButton(onClick = { onSearchChange("") }) {
                Icon(Icons.Default.Clear, contentDescription = "Tozalash")
              }
            }
          },
          modifier = Modifier
            .weight(1f)
            .testTag("payroll_search_input"),
          singleLine = true,
          shape = RoundedCornerShape(12.dp)
        )

        Button(
          onClick = onExportReport,
          colors = ButtonDefaults.buttonColors(containerColor = CorporatePrimary),
          shape = RoundedCornerShape(12.dp),
          contentPadding = PaddingValues(horizontal = 12.dp, vertical = 14.dp),
          modifier = Modifier.testTag("export_payroll_button")
        ) {
          Icon(imageVector = Icons.Default.FileDownload, contentDescription = null, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Eksport", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
      }
    }

    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = if (isDirector) "Xodimlar oylik maoshlari (Ko'rish)" else "Xodimlar oylik maosh jadvali (Tahrirlash)",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold
        )
        Text(
          text = "${filteredEmployees.size} nafar",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    if (filteredEmployees.isEmpty()) {
      item {
        EmptyStateView(
          title = "Xodim topilmadi",
          description = "Qidiruv so'zini tekshiring."
        )
      }
    } else {
      items(filteredEmployees, key = { it.id }) { emp ->
        PayrollEmployeeCard(
          employee = emp,
          canEdit = !isDirector,
          onEdit = { onEditPayroll(emp) }
        )
      }
    }
  }
}

@Composable
fun PersonalPayslipView(
  employee: Employee,
  onRequestCertificate: () -> Unit,
  modifier: Modifier = Modifier
) {
  val gross = employee.baseSalary + employee.bonus
  val tax12 = (gross * 0.12).toLong()
  val inps1 = (gross * 0.01).toLong()
  val totalDeductions = tax12 + inps1 + employee.deduction

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
    contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)
  ) {
    // Header Banner
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = CorporatePrimary)
      ) {
        Column(modifier = Modifier.padding(20.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "Shaxsiy hisob-kitob varaqasi (Payslip)",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 12.sp
              )
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = "Sentabr 2026 oyi",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
              )
            }
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF22C55E).copy(alpha = 0.25f))
                .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
              Text("To'langan", color = Color(0xFF86EFAC), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
          }

          Spacer(modifier = Modifier.height(16.dp))
          HorizontalDivider(color = Color.White.copy(alpha = 0.2f))
          Spacer(modifier = Modifier.height(14.dp))

          Text(
            text = "Qo'lga tegadigan sof oylik (Net):",
            color = Color.White.copy(alpha = 0.85f),
            fontSize = 13.sp
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = formatUzs(employee.netSalary),
            color = Color.White,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "Xodim: ${employee.fullName} • ${employee.position}",
            color = Color.White.copy(alpha = 0.8f),
            fontSize = 12.sp
          )
        }
      }
    }

    // Detailed Breakdown Card
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Text(
            text = "Daromadlar va Hisoblangan oylik",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = CorporatePrimary
          )
          Spacer(modifier = Modifier.height(12.dp))

          PayslipRow(label = "Asosiy lavozim maoshi (Oklad)", amount = formatUzs(employee.baseSalary))
          Spacer(modifier = Modifier.height(8.dp))
          PayslipRow(
            label = "Samaradorlik va KPI bonusi",
            amount = if (employee.bonus > 0) "+${formatUzs(employee.bonus)}" else "0 UZS",
            amountColor = if (employee.bonus > 0) StatusSuccessText else MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(10.dp))
          HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
          Spacer(modifier = Modifier.height(10.dp))
          PayslipRow(
            label = "Jami hisoblangan daromad (Gross)",
            amount = formatUzs(gross),
            isBold = true
          )

          Spacer(modifier = Modifier.height(20.dp))

          Text(
            text = "Majburiy ushlanmalar va soliqlar",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFDC2626)
          )
          Spacer(modifier = Modifier.height(12.dp))

          PayslipRow(
            label = "JShShDS (Daromad solig'i 12%)",
            amount = "-${formatUzs(tax12)}",
            amountColor = Color(0xFFDC2626)
          )
          Spacer(modifier = Modifier.height(8.dp))
          PayslipRow(
            label = "INPS (Shaxsiy jamg'arib boriladigan pensiya 1%)",
            amount = "-${formatUzs(inps1)}",
            amountColor = Color(0xFFDC2626)
          )
          if (employee.deduction > 0) {
            Spacer(modifier = Modifier.height(8.dp))
            PayslipRow(
              label = "Boshqa ushlanmalar / jarimalar",
              amount = "-${formatUzs(employee.deduction)}",
              amountColor = Color(0xFFDC2626)
            )
          }

          Spacer(modifier = Modifier.height(12.dp))
          HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
          Spacer(modifier = Modifier.height(12.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Plastik kartaga to'langan (Net):",
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp
            )
            Text(
              text = formatUzs(employee.netSalary),
              fontWeight = FontWeight.Bold,
              fontSize = 16.sp,
              color = Color(0xFF15803D)
            )
          }
        }
      }
    }

    // Quick Certificate Request CTA
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
          ) {
            Icon(
              imageVector = Icons.Default.Description,
              contentDescription = null,
              tint = CorporatePrimary,
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text("Daromad to'g'risida spravka kerakmi?", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
              Text("Bank yoki elchixona uchun QR-kodli blank", fontSize = 11.sp, color = CorporateTextSecondary)
            }
          }
          OutlinedButton(
            onClick = onRequestCertificate,
            shape = RoundedCornerShape(8.dp)
          ) {
            Text("So'rov yuborish", fontSize = 12.sp)
          }
        }
      }
    }
  }
}

@Composable
fun PayslipRow(
  label: String,
  amount: String,
  amountColor: Color = Color.Unspecified,
  isBold: Boolean = false
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(
      text = label,
      fontSize = if (isBold) 13.sp else 12.sp,
      fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
      color = if (isBold) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
    )
    Text(
      text = amount,
      fontSize = if (isBold) 14.sp else 13.sp,
      fontWeight = if (isBold) FontWeight.Bold else FontWeight.SemiBold,
      color = if (amountColor != Color.Unspecified) amountColor else MaterialTheme.colorScheme.onSurface
    )
  }
}

@Composable
fun PayrollEmployeeCard(
  employee: Employee,
  canEdit: Boolean = true,
  onEdit: () -> Unit
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          EmployeeAvatar(
            firstName = employee.firstName,
            lastName = employee.lastName,
            colorHex = employee.avatarColorHex,
            size = 40
          )
          Spacer(modifier = Modifier.width(12.dp))
          Column {
            Text(
              text = employee.fullName,
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "${employee.position} • ${employee.department}",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }

        if (canEdit) {
          IconButton(onClick = onEdit) {
            Icon(
              imageVector = Icons.Default.Edit,
              contentDescription = "Tahrirlash",
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(20.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))
      HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
      Spacer(modifier = Modifier.height(10.dp))

      // Columns breakdown: Asosiy, Bonus, Chegirma, Jami
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Column {
          Text(text = "Asosiy maosh", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          Spacer(modifier = Modifier.height(2.dp))
          Text(text = formatUzs(employee.baseSalary), fontSize = 13.sp, fontWeight = FontWeight.Medium)
        }

        Column {
          Text(text = "Bonus", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = if (employee.bonus > 0) "+${formatUzs(employee.bonus)}" else "0",
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = if (employee.bonus > 0) StatusSuccessText else MaterialTheme.colorScheme.onSurface
          )
        }

        Column {
          Text(text = "Chegirma", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = if (employee.deduction > 0) "-${formatUzs(employee.deduction)}" else "0",
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = if (employee.deduction > 0) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
          )
        }

        Column(horizontalAlignment = Alignment.End) {
          Text(text = "Jami summa", fontSize = 11.sp, color = CorporatePrimary, fontWeight = FontWeight.Bold)
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = formatUzs(employee.netSalary),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = CorporatePrimary
          )
        }
      }
    }
  }
}
