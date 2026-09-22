package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*
import com.example.ui.components.*
import com.example.viewmodel.EmployeeSortOption

@Composable
fun EmployeesScreen(
  currentUser: AuthUser? = null,
  employees: List<Employee>,
  departments: List<Department>,
  searchQuery: String,
  departmentFilter: String,
  sortOption: EmployeeSortOption,
  onSearchChange: (String) -> Unit,
  onDepartmentFilterChange: (String) -> Unit,
  onSortOptionChange: (EmployeeSortOption) -> Unit,
  onSelectEmployee: (Employee) -> Unit,
  onAddEmployeeClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val isEmployee = currentUser?.role == UserRole.EMPLOYEE
  val isHR = currentUser?.role == UserRole.HR_MANAGER
  var isSortMenuOpen by remember { mutableStateOf(false) }

  // Filter & Sort logic
  val filteredEmployees = remember(employees, searchQuery, departmentFilter, sortOption) {
    val query = searchQuery.trim().lowercase()
    val filtered = employees.filter { emp ->
      val matchesQuery = query.isEmpty() ||
        emp.fullName.lowercase().contains(query) ||
        emp.position.lowercase().contains(query) ||
        emp.phone.contains(query) ||
        emp.email.lowercase().contains(query)
      val matchesDept = departmentFilter == "Barchasi" || emp.department == departmentFilter
      matchesQuery && matchesDept
    }

    when (sortOption) {
      EmployeeSortOption.NAME_ASC -> filtered.sortedBy { it.fullName }
      EmployeeSortOption.NAME_DESC -> filtered.sortedByDescending { it.fullName }
      EmployeeSortOption.SALARY_HIGH -> filtered.sortedByDescending { it.baseSalary }
      EmployeeSortOption.SALARY_LOW -> filtered.sortedBy { it.baseSalary }
    }
  }

  Scaffold(
    floatingActionButton = {
      if (isHR) {
        ExtendedFloatingActionButton(
          onClick = onAddEmployeeClick,
          icon = { Icon(Icons.Default.PersonAdd, contentDescription = null) },
          text = { Text("Yangi xodim") },
          modifier = Modifier.testTag("add_employee_fab")
        )
      }
    },
    modifier = modifier
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
        .padding(paddingValues)
    ) {
      // Role header banner
      Surface(
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = if (isEmployee) Icons.Default.Contacts else Icons.Default.Badge,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = when (currentUser?.role) {
              UserRole.EMPLOYEE -> "Hamkasblar ma'lumotnomasi (Aloqa va kontaktlar)"
              UserRole.HR_MANAGER -> "Kadrlar boshqaruvi: Yangi xodim qabul qilish va tahrirlash"
              UserRole.DIRECTOR -> "Kompaniya xodimlari va tashkiliy shtat jadvali"
              null -> "Xodimlar ro'yxati"
            },
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      // Search bar and Sort header
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .background(MaterialTheme.colorScheme.surface)
          .padding(horizontal = 16.dp, vertical = 12.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            placeholder = { Text("Ism, lavozim yoki telefon qidirish...") },
            leadingIcon = {
              Icon(Icons.Default.Search, contentDescription = null)
            },
            trailingIcon = {
              if (searchQuery.isNotEmpty()) {
                IconButton(onClick = { onSearchChange("") }) {
                  Icon(Icons.Default.Clear, contentDescription = "Tozalash")
                }
              }
            },
            modifier = Modifier
              .weight(1f)
              .testTag("employee_search_input"),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
          )

          // Sort button
          Box {
            IconButton(
              onClick = { isSortMenuOpen = !isSortMenuOpen },
              modifier = Modifier.testTag("sort_button")
            ) {
              Icon(Icons.Default.Sort, contentDescription = "Saralash")
            }
            DropdownMenu(
              expanded = isSortMenuOpen,
              onDismissRequest = { isSortMenuOpen = false }
            ) {
              val availableSortOptions = if (isEmployee) {
                listOf(EmployeeSortOption.NAME_ASC, EmployeeSortOption.NAME_DESC)
              } else {
                EmployeeSortOption.values().toList()
              }

              availableSortOptions.forEach { option ->
                DropdownMenuItem(
                  text = {
                    Text(
                      text = option.title,
                      fontWeight = if (option == sortOption) FontWeight.Bold else FontWeight.Normal
                    )
                  },
                  onClick = {
                    onSortOptionChange(option)
                    isSortMenuOpen = false
                  },
                  trailingIcon = {
                    if (option == sortOption) {
                      Icon(Icons.Default.Check, contentDescription = null)
                    }
                  }
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Department Filter chips
        val allDeptOptions = listOf("Barchasi") + departments.map { it.name }
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          allDeptOptions.forEach { dept ->
            val selected = dept == departmentFilter
            FilterChip(
              selected = selected,
              onClick = { onDepartmentFilterChange(dept) },
              label = { Text(dept, fontSize = 12.sp) },
              shape = RoundedCornerShape(8.dp)
            )
          }
        }
      }

      // Count Header
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Topildi: ${filteredEmployees.size} ta xodim",
          style = MaterialTheme.typography.bodySmall,
          fontWeight = FontWeight.SemiBold,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
          text = sortOption.title,
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.primary
        )
      }

      // Employee list / cards
      if (filteredEmployees.isEmpty()) {
        EmptyStateView(
          title = "Xodimlar topilmadi",
          description = "Qidiruv parametrlarini o'zgartiring yoki yangi xodim qo'shing."
        )
      } else {
        LazyColumn(
          modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp),
          contentPadding = PaddingValues(bottom = 80.dp)
        ) {
          items(filteredEmployees, key = { it.id }) { emp ->
            EmployeeCard(
              employee = emp,
              onClick = { onSelectEmployee(emp) }
            )
          }
        }
      }
    }
  }
}

@Composable
fun EmployeeCard(
  employee: Employee,
  onClick: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clickable(onClick = onClick)
      .testTag("employee_card_${employee.id}"),
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      EmployeeAvatar(
        firstName = employee.firstName,
        lastName = employee.lastName,
        colorHex = employee.avatarColorHex,
        size = 48
      )

      Spacer(modifier = Modifier.width(14.dp))

      Column(modifier = Modifier.weight(1f)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = employee.fullName,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )
          EmployeeStatusBadge(status = employee.status)
        }

        Spacer(modifier = Modifier.height(2.dp))

        Text(
          text = employee.position,
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.primary,
          fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.Business,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(14.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = employee.department,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          Spacer(modifier = Modifier.width(12.dp))

          Icon(
            imageVector = Icons.Default.Phone,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(14.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = employee.phone,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      Spacer(modifier = Modifier.width(6.dp))
      Icon(
        imageVector = Icons.Default.ChevronRight,
        contentDescription = "Profil",
        tint = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }
  }
}
