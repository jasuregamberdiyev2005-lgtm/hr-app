package com.example.ui.dialogs

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.DailyAttendance
import com.example.model.Employee
import com.example.ui.theme.CorporatePrimary
import com.example.ui.theme.CorporateTextSecondary
import com.example.ui.theme.StatusSuccessText

@Composable
fun ExportReportDialog(
  reportType: String, // "PAYROLL" or "ATTENDANCE"
  employees: List<Employee>,
  attendanceList: List<DailyAttendance>,
  onDismiss: () -> Unit
) {
  val context = LocalContext.current
  var selectedType by remember { mutableStateOf(reportType) }
  var selectedFormat by remember { mutableStateOf("TEXT") } // "TEXT" or "CSV"

  val totalBaseSalary = remember(employees) { employees.sumOf { it.baseSalary } }
  val totalBonus = remember(employees) { employees.sumOf { it.bonus } }
  val totalDeduction = remember(employees) { employees.sumOf { it.deduction } }
  val totalNetPayroll = remember(employees) { employees.sumOf { it.netSalary } }

  val presentCount = remember(attendanceList) { attendanceList.count { it.status == com.example.model.AttendanceStatus.PRESENT } }
  val lateCount = remember(attendanceList) { attendanceList.count { it.status == com.example.model.AttendanceStatus.LATE } }
  val absentCount = remember(attendanceList) { attendanceList.count { it.status == com.example.model.AttendanceStatus.ABSENT } }

  // Generate Report Content
  val reportText = remember(selectedType, selectedFormat, employees, attendanceList) {
    if (selectedType == "PAYROLL") {
      if (selectedFormat == "CSV") {
        buildString {
          appendLine("ID,F.I.O,Bo'lim,Lavozim,Asosiy maosh,Bonus,Ushlanma,Sof to'lov (UZS)")
          employees.forEach { emp ->
            appendLine("${emp.id},\"${emp.fullName}\",\"${emp.department}\",\"${emp.position}\",${emp.baseSalary},${emp.bonus},${emp.deduction},${emp.netSalary}")
          }
          appendLine(",,,,JAMI,$totalBonus,$totalDeduction,$totalNetPayroll")
        }
      } else {
        buildString {
          appendLine("📊 HRM KORPORATIV ISH HAQI HISOBOTI (PAYROLL)")
          appendLine("Sana: 17-Sentabr, 2026")
          appendLine("Jami xodimlar: ${employees.size} nafar")
          appendLine("--------------------------------------------")
          appendLine("💰 Jami Asosiy Maosh: %,d UZS".format(totalBaseSalary))
          appendLine("➕ Jami Mukofot (Bonus): %,d UZS".format(totalBonus))
          appendLine("➖ Jami Ushlanmalar: %,d UZS".format(totalDeduction))
          appendLine("✅ Jami To'lanadigan Sof Ish Haqi: %,d UZS".format(totalNetPayroll))
          appendLine("--------------------------------------------")
          appendLine("XODIMLAR RO'YXATI:")
          employees.forEachIndexed { i, emp ->
            appendLine("${i + 1}. ${emp.fullName} (${emp.position}) - %,d UZS".format(emp.netSalary))
          }
        }
      }
    } else {
      if (selectedFormat == "CSV") {
        buildString {
          appendLine("Xodim ID,F.I.O,Bo'lim,Holat,Kelgan vaqti,Ketgan vaqti,Sana")
          attendanceList.forEach { att ->
            appendLine("${att.employeeId},\"${att.employeeName}\",\"${att.department}\",${att.status.label},${att.checkInTime ?: "-"},${att.checkOutTime ?: "-"},${att.date}")
          }
        }
      } else {
        buildString {
          appendLine("📋 KUNLIK DAVOMAT TABELI (ATTENDANCE)")
          appendLine("Sana: 17-Sentabr, 2026")
          appendLine("--------------------------------------------")
          appendLine("✅ Kelganlar: $presentCount nafar")
          appendLine("⏰ Kechikkanlar: $lateCount nafar")
          appendLine("❌ Kelmaganlar: $absentCount nafar")
          appendLine("--------------------------------------------")
          attendanceList.forEachIndexed { i, att ->
            val timeStr = att.checkInTime?.let { "($it)" } ?: ""
            appendLine("${i + 1}. ${att.employeeName} - ${att.status.label} $timeStr")
          }
        }
      }
    }
  }

  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(18.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.85f)
        .padding(8.dp)
        .testTag("export_report_dialog")
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(20.dp)
      ) {
        // Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.FileDownload,
              contentDescription = null,
              tint = CorporatePrimary,
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Hisobotni Eksport Qilish",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold
            )
          }
          IconButton(onClick = onDismiss) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Yopish")
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Type Switcher: Payroll vs Attendance
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          FilterChip(
            selected = selectedType == "PAYROLL",
            onClick = { selectedType = "PAYROLL" },
            label = { Text("💰 Ish haqi (Payroll)") },
            modifier = Modifier.weight(1f),
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = CorporatePrimary,
              selectedLabelColor = Color.White
            )
          )
          FilterChip(
            selected = selectedType == "ATTENDANCE",
            onClick = { selectedType = "ATTENDANCE" },
            label = { Text("📋 Davomat tabeli") },
            modifier = Modifier.weight(1f),
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = CorporatePrimary,
              selectedLabelColor = Color.White
            )
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Format Switcher: TEXT vs CSV
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          FilterChip(
            selected = selectedFormat == "TEXT",
            onClick = { selectedFormat = "TEXT" },
            label = { Text("📄 Telegram / Matn formati") },
            modifier = Modifier.weight(1f)
          )
          FilterChip(
            selected = selectedFormat == "CSV",
            onClick = { selectedFormat = "CSV" },
            label = { Text("📊 Excel / CSV jadval") },
            modifier = Modifier.weight(1f)
          )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Preview Box
        Text(
          text = "Hisobot ko'rinishi (Oldindan ko'rish):",
          style = MaterialTheme.typography.labelMedium,
          fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Box(
          modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFF8FAFC))
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(10.dp))
            .verticalScroll(rememberScrollState())
            .padding(12.dp)
        ) {
          Text(
            text = reportText,
            fontFamily = FontFamily.Monospace,
            fontSize = 11.sp,
            color = Color(0xFF1E293B),
            lineHeight = 16.sp
          )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Action Buttons: Copy & Share
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          OutlinedButton(
            onClick = {
              val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
              val clip = ClipData.newPlainText("HRM Hisoboti", reportText)
              clipboard.setPrimaryClip(clip)
              Toast.makeText(context, "Hisobot nusxa olindi!", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(10.dp)
          ) {
            Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Nusxalash")
          }

          Button(
            onClick = {
              val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, reportText)
                type = "text/plain"
              }
              context.startActivity(Intent.createChooser(shareIntent, "Hisobotni jo'natish"))
            },
            colors = ButtonDefaults.buttonColors(containerColor = CorporatePrimary),
            modifier = Modifier.weight(1.3f),
            shape = RoundedCornerShape(10.dp)
          ) {
            Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Ulashish (Telegram)", fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}
