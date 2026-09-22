package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.*
import com.example.ui.components.*

@Composable
fun AddEmployeeDialog(
  departments: List<Department>,
  onDismiss: () -> Unit,
  onConfirm: (
    firstName: String,
    lastName: String,
    position: String,
    department: String,
    phone: String,
    email: String,
    baseSalary: Long
  ) -> Unit
) {
  var firstName by remember { mutableStateOf("") }
  var lastName by remember { mutableStateOf("") }
  var position by remember { mutableStateOf("") }
  var selectedDept by remember { mutableStateOf(departments.firstOrNull()?.name ?: "IT & Dasturlash") }
  var phone by remember { mutableStateOf("+998 ") }
  var email by remember { mutableStateOf("") }
  var salaryText by remember { mutableStateOf("12000000") }
  var isDeptExpanded by remember { mutableStateOf(false) }
  var errorMessage by remember { mutableStateOf<String?>(null) }

  Dialog(onDismissRequest = onDismiss) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .testTag("add_employee_dialog"),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
      Column(
        modifier = Modifier
          .padding(20.dp)
          .verticalScroll(rememberScrollState())
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Yangi xodim qo'shish",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
          )
          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "Yopish")
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
          value = firstName,
          onValueChange = { firstName = it },
          label = { Text("Ism *") },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("input_first_name"),
          singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = lastName,
          onValueChange = { lastName = it },
          label = { Text("Familiya *") },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("input_last_name"),
          singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = position,
          onValueChange = { position = it },
          label = { Text("Lavozim *") },
          placeholder = { Text("Masalan: Kotlin Dasturchi") },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("input_position"),
          singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Department dropdown
        Box(modifier = Modifier.fillMaxWidth()) {
          OutlinedTextField(
            value = selectedDept,
            onValueChange = {},
            readOnly = true,
            label = { Text("Bo'lim") },
            trailingIcon = {
              IconButton(onClick = { isDeptExpanded = !isDeptExpanded }) {
                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
              }
            },
            modifier = Modifier
              .fillMaxWidth()
              .clickable { isDeptExpanded = true }
          )
          DropdownMenu(
            expanded = isDeptExpanded,
            onDismissRequest = { isDeptExpanded = false }
          ) {
            departments.forEach { dept ->
              DropdownMenuItem(
                text = { Text(dept.name) },
                onClick = {
                  selectedDept = dept.name
                  isDeptExpanded = false
                }
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = phone,
          onValueChange = { phone = it },
          label = { Text("Telefon raqami") },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = email,
          onValueChange = { email = it },
          label = { Text("Email manzili") },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = salaryText,
          onValueChange = { salaryText = it.filter { char -> char.isDigit() } },
          label = { Text("Asosiy oylik maosh (so'm)") },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )

        if (errorMessage != null) {
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = errorMessage!!,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall
          )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.End
        ) {
          TextButton(onClick = onDismiss) {
            Text("Bekor qilish")
          }
          Spacer(modifier = Modifier.width(8.dp))
          Button(
            onClick = {
              if (firstName.isBlank() || lastName.isBlank() || position.isBlank()) {
                errorMessage = "Iltimos, ism, familiya va lavozimni to'ldiring!"
                return@Button
              }
              val salary = salaryText.toLongOrNull() ?: 10_000_000L
              val finalEmail = if (email.isBlank()) {
                "${firstName.lowercase()}.${lastName.lowercase()}@hrm.uz"
              } else email
              onConfirm(firstName, lastName, position, selectedDept, phone, finalEmail, salary)
            },
            modifier = Modifier.testTag("confirm_add_employee_button")
          ) {
            Text("Qo'shish")
          }
        }
      }
    }
  }
}

@Composable
fun EmployeeDetailModal(
  employee: Employee,
  departments: List<Department>,
  currentUser: AuthUser? = null,
  onDismiss: () -> Unit,
  onSave: (Employee) -> Unit,
  onDelete: (String) -> Unit
) {
  val isEmployee = currentUser?.role == UserRole.EMPLOYEE
  val isHR = currentUser?.role == UserRole.HR_MANAGER
  val isDirector = currentUser?.role == UserRole.DIRECTOR

  var isEditing by remember { mutableStateOf(false) }
  var position by remember { mutableStateOf(employee.position) }
  var department by remember { mutableStateOf(employee.department) }
  var phone by remember { mutableStateOf(employee.phone) }
  var email by remember { mutableStateOf(employee.email) }
  var baseSalaryText by remember { mutableStateOf(employee.baseSalary.toString()) }
  var status by remember { mutableStateOf(employee.status) }
  var isDeptExpanded by remember { mutableStateOf(false) }
  var isStatusExpanded by remember { mutableStateOf(false) }

  Dialog(onDismissRequest = onDismiss) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .testTag("employee_detail_dialog"),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
      Column(
        modifier = Modifier
          .padding(20.dp)
          .verticalScroll(rememberScrollState())
      ) {
        // Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = if (isEditing) "Xodimni tahrirlash" else if (isEmployee) "Hamkasb profili" else "Xodim profili",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
          )
          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "Yopish")
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Avatar + Name
        Row(verticalAlignment = Alignment.CenterVertically) {
          EmployeeAvatar(
            firstName = employee.firstName,
            lastName = employee.lastName,
            colorHex = employee.avatarColorHex,
            size = 56
          )
          Spacer(modifier = Modifier.width(16.dp))
          Column {
            Text(
              text = employee.fullName,
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "ID: ${employee.id} • ${employee.joinDate} dan beri",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            EmployeeStatusBadge(status = employee.status)
          }
        }

        Spacer(modifier = Modifier.height(20.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(16.dp))

        if (!isEditing) {
          // Read-only info
          DetailRow(label = "Lavozim", value = employee.position, icon = Icons.Default.Work)
          DetailRow(label = "Bo'lim", value = employee.department, icon = Icons.Default.Business)
          DetailRow(label = "Telefon", value = employee.phone, icon = Icons.Default.Phone)
          DetailRow(label = "Email", value = employee.email, icon = Icons.Default.Email)

          // Salary details only for Director and HR Manager
          if (!isEmployee) {
            DetailRow(label = "Asosiy maosh", value = formatUzs(employee.baseSalary), icon = Icons.Default.Payments)
            DetailRow(label = "Mukofot / Bonus", value = formatUzs(employee.bonus), icon = Icons.Default.AddCircleOutline)
            DetailRow(label = "Chegirma / Jarima", value = formatUzs(employee.deduction), icon = Icons.Default.RemoveCircleOutline)
            DetailRow(label = "Sof maosh (Net)", value = formatUzs(employee.netSalary), icon = Icons.Default.AccountBalanceWallet)
          }

          Spacer(modifier = Modifier.height(24.dp))

          if (isEmployee) {
            // Employee contact actions
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier.weight(1f)
              ) {
                Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Qo'ng'iroq")
              }
              Button(
                onClick = onDismiss,
                modifier = Modifier.weight(1f)
              ) {
                Icon(Icons.Default.Email, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Xabar yozish")
              }
            }
          } else if (isHR) {
            // HR can delete and edit
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              OutlinedButton(
                onClick = { onDelete(employee.id) },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
              ) {
                Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("O'chirish")
              }

              Button(
                onClick = { isEditing = true },
                modifier = Modifier.testTag("edit_employee_button")
              ) {
                Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Tahrirlash")
              }
            }
          } else {
            // Director: Close / View only
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.End
            ) {
              Button(onClick = onDismiss) {
                Text("Yopish")
              }
            }
          }
        } else {
          // Edit form
          OutlinedTextField(
            value = position,
            onValueChange = { position = it },
            label = { Text("Lavozim") },
            modifier = Modifier.fillMaxWidth()
          )

          Spacer(modifier = Modifier.height(10.dp))

          // Dept selector
          Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
              value = department,
              onValueChange = {},
              readOnly = true,
              label = { Text("Bo'lim") },
              trailingIcon = {
                IconButton(onClick = { isDeptExpanded = !isDeptExpanded }) {
                  Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }
              },
              modifier = Modifier
                .fillMaxWidth()
                .clickable { isDeptExpanded = true }
            )
            DropdownMenu(
              expanded = isDeptExpanded,
              onDismissRequest = { isDeptExpanded = false }
            ) {
              departments.forEach { dept ->
                DropdownMenuItem(
                  text = { Text(dept.name) },
                  onClick = {
                    department = dept.name
                    isDeptExpanded = false
                  }
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Telefon") },
            modifier = Modifier.fillMaxWidth()
          )

          Spacer(modifier = Modifier.height(10.dp))

          OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
          )

          Spacer(modifier = Modifier.height(10.dp))

          OutlinedTextField(
            value = baseSalaryText,
            onValueChange = { baseSalaryText = it.filter { char -> char.isDigit() } },
            label = { Text("Asosiy maosh (so'm)") },
            modifier = Modifier.fillMaxWidth()
          )

          Spacer(modifier = Modifier.height(10.dp))

          // Status selector
          Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
              value = status.label,
              onValueChange = {},
              readOnly = true,
              label = { Text("Holat") },
              trailingIcon = {
                IconButton(onClick = { isStatusExpanded = !isStatusExpanded }) {
                  Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }
              },
              modifier = Modifier
                .fillMaxWidth()
                .clickable { isStatusExpanded = true }
            )
            DropdownMenu(
              expanded = isStatusExpanded,
              onDismissRequest = { isStatusExpanded = false }
            ) {
              EmployeeStatus.values().forEach { st ->
                DropdownMenuItem(
                  text = { Text(st.label) },
                  onClick = {
                    status = st
                    isStatusExpanded = false
                  }
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(20.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
          ) {
            TextButton(onClick = { isEditing = false }) {
              Text("Bekor qilish")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
              onClick = {
                val updated = employee.copy(
                  position = position,
                  department = department,
                  phone = phone,
                  email = email,
                  baseSalary = baseSalaryText.toLongOrNull() ?: employee.baseSalary,
                  status = status
                )
                onSave(updated)
                isEditing = false
              }
            ) {
              Text("Saqlash")
            }
          }
        }
      }
    }
  }
}

@Composable
private fun DetailRow(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 6.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(
      imageVector = icon,
      contentDescription = null,
      tint = MaterialTheme.colorScheme.primary,
      modifier = Modifier.size(18.dp)
    )
    Spacer(modifier = Modifier.width(10.dp))
    Text(
      text = label,
      style = MaterialTheme.typography.bodyMedium,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      modifier = Modifier.width(120.dp)
    )
    Text(
      text = value,
      style = MaterialTheme.typography.bodyMedium,
      fontWeight = FontWeight.SemiBold,
      color = MaterialTheme.colorScheme.onSurface
    )
  }
}

@Composable
fun AddLeaveDialog(
  employees: List<Employee>,
  onDismiss: () -> Unit,
  onConfirm: (
    employeeId: String,
    leaveType: LeaveType,
    startDate: String,
    endDate: String,
    daysCount: Int,
    reason: String
  ) -> Unit
) {
  var selectedEmpId by remember { mutableStateOf(employees.firstOrNull()?.id ?: "") }
  var selectedType by remember { mutableStateOf(LeaveType.ANNUAL) }
  var startDate by remember { mutableStateOf("20.09.2026") }
  var endDate by remember { mutableStateOf("25.09.2026") }
  var daysCountText by remember { mutableStateOf("5") }
  var reason by remember { mutableStateOf("") }
  var isEmpExpanded by remember { mutableStateOf(false) }
  var isTypeExpanded by remember { mutableStateOf(false) }

  val selectedEmp = employees.find { it.id == selectedEmpId }

  Dialog(onDismissRequest = onDismiss) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .testTag("add_leave_dialog"),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
      Column(
        modifier = Modifier
          .padding(20.dp)
          .verticalScroll(rememberScrollState())
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Yangi ta'til arizasi",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
          )
          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "Yopish")
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Employee picker
        Box(modifier = Modifier.fillMaxWidth()) {
          OutlinedTextField(
            value = selectedEmp?.fullName ?: "Xodimni tanlang",
            onValueChange = {},
            readOnly = true,
            label = { Text("Xodim") },
            trailingIcon = {
              IconButton(onClick = { isEmpExpanded = !isEmpExpanded }) {
                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
              }
            },
            modifier = Modifier
              .fillMaxWidth()
              .clickable { isEmpExpanded = true }
          )
          DropdownMenu(
            expanded = isEmpExpanded,
            onDismissRequest = { isEmpExpanded = false }
          ) {
            employees.forEach { emp ->
              DropdownMenuItem(
                text = { Text("${emp.fullName} (${emp.position})") },
                onClick = {
                  selectedEmpId = emp.id
                  isEmpExpanded = false
                }
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Leave type picker
        Box(modifier = Modifier.fillMaxWidth()) {
          OutlinedTextField(
            value = selectedType.label,
            onValueChange = {},
            readOnly = true,
            label = { Text("Ta'til turi") },
            trailingIcon = {
              IconButton(onClick = { isTypeExpanded = !isTypeExpanded }) {
                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
              }
            },
            modifier = Modifier
              .fillMaxWidth()
              .clickable { isTypeExpanded = true }
          )
          DropdownMenu(
            expanded = isTypeExpanded,
            onDismissRequest = { isTypeExpanded = false }
          ) {
            LeaveType.values().forEach { type ->
              DropdownMenuItem(
                text = { Text(type.label) },
                onClick = {
                  selectedType = type
                  isTypeExpanded = false
                }
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedTextField(
            value = startDate,
            onValueChange = { startDate = it },
            label = { Text("Boshlanish") },
            modifier = Modifier.weight(1f),
            singleLine = true
          )
          OutlinedTextField(
            value = endDate,
            onValueChange = { endDate = it },
            label = { Text("Tugash") },
            modifier = Modifier.weight(1f),
            singleLine = true
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = daysCountText,
          onValueChange = { daysCountText = it.filter { char -> char.isDigit() } },
          label = { Text("Kunlar soni") },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = reason,
          onValueChange = { reason = it },
          label = { Text("Sababi") },
          placeholder = { Text("Ta'til sababini yozing...") },
          modifier = Modifier.fillMaxWidth(),
          minLines = 2
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.End
        ) {
          TextButton(onClick = onDismiss) {
            Text("Bekor qilish")
          }
          Spacer(modifier = Modifier.width(8.dp))
          Button(
            onClick = {
              val days = daysCountText.toIntOrNull() ?: 1
              val finalReason = if (reason.isBlank()) "Rejalashtirilgan ${selectedType.label}" else reason
              onConfirm(selectedEmpId, selectedType, startDate, endDate, days, finalReason)
            },
            modifier = Modifier.testTag("confirm_add_leave_button")
          ) {
            Text("Yuborish")
          }
        }
      }
    }
  }
}

@Composable
fun EditPayrollDialog(
  employee: Employee,
  onDismiss: () -> Unit,
  onSave: (employeeId: String, bonus: Long, deduction: Long) -> Unit
) {
  var bonusText by remember { mutableStateOf(employee.bonus.toString()) }
  var deductionText by remember { mutableStateOf(employee.deduction.toString()) }

  val bonus = bonusText.toLongOrNull() ?: 0L
  val deduction = deductionText.toLongOrNull() ?: 0L
  val calculatedNet = employee.baseSalary + bonus - deduction

  Dialog(onDismissRequest = onDismiss) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .testTag("edit_payroll_dialog"),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
      Column(
        modifier = Modifier
          .padding(20.dp)
          .verticalScroll(rememberScrollState())
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Ish haqini tahrirlash",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
          )
          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "Yopish")
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
          text = employee.fullName,
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.SemiBold
        )
        Text(
          text = "${employee.position} • ${employee.department}",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
          shape = RoundedCornerShape(10.dp)
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Text(
              text = "Asosiy oylik maosh:",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
              text = formatUzs(employee.baseSalary),
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
          value = bonusText,
          onValueChange = { bonusText = it.filter { char -> char.isDigit() } },
          label = { Text("Bonus / Mukofot puli (so'm)") },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = deductionText,
          onValueChange = { deductionText = it.filter { char -> char.isDigit() } },
          label = { Text("Chegirma / Jarima (so'm)") },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
          shape = RoundedCornerShape(10.dp)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Jami hisoblangan:",
              style = MaterialTheme.typography.bodyMedium,
              fontWeight = FontWeight.Medium,
              color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
              text = formatUzs(calculatedNet),
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onPrimaryContainer
            )
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.End
        ) {
          TextButton(onClick = onDismiss) {
            Text("Bekor qilish")
          }
          Spacer(modifier = Modifier.width(8.dp))
          Button(
            onClick = {
              onSave(employee.id, bonus, deduction)
            }
          ) {
            Text("Saqlash")
          }
        }
      }
    }
  }
}

@Composable
fun AddDepartmentDialog(
  onDismiss: () -> Unit,
  onConfirm: (name: String, headName: String, headPosition: String, budget: Long, description: String) -> Unit
) {
  var name by remember { mutableStateOf("") }
  var headName by remember { mutableStateOf("") }
  var headPosition by remember { mutableStateOf("") }
  var budgetText by remember { mutableStateOf("20000000") }
  var description by remember { mutableStateOf("") }

  Dialog(onDismissRequest = onDismiss) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .testTag("add_dept_dialog"),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
      Column(
        modifier = Modifier
          .padding(20.dp)
          .verticalScroll(rememberScrollState())
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Yangi bo'lim qo'shish",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
          )
          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "Yopish")
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
          value = name,
          onValueChange = { name = it },
          label = { Text("Bo'lim nomi *") },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = headName,
          onValueChange = { headName = it },
          label = { Text("Bo'lim boshlig'i (F.I.SH)") },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = headPosition,
          onValueChange = { headPosition = it },
          label = { Text("Boshliq lavozimi") },
          placeholder = { Text("Masalan: Bo'lim boshlig'i") },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = budgetText,
          onValueChange = { budgetText = it.filter { char -> char.isDigit() } },
          label = { Text("Oylik byudjet (so'm)") },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = description,
          onValueChange = { description = it },
          label = { Text("Tavsif") },
          modifier = Modifier.fillMaxWidth(),
          minLines = 2
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.End
        ) {
          TextButton(onClick = onDismiss) {
            Text("Bekor qilish")
          }
          Spacer(modifier = Modifier.width(8.dp))
          Button(
            onClick = {
              if (name.isNotBlank()) {
                val budget = budgetText.toLongOrNull() ?: 15_000_000L
                onConfirm(
                  name,
                  if (headName.isBlank()) "Tayinlanmagan" else headName,
                  if (headPosition.isBlank()) "Rahbar" else headPosition,
                  budget,
                  description
                )
              }
            }
          ) {
            Text("Yaratish")
          }
        }
      }
    }
  }
}
