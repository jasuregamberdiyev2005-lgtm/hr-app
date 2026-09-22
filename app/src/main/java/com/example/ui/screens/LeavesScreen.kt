package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun LeavesScreen(
  currentUser: AuthUser? = null,
  leaveRequests: List<LeaveRequest>,
  selectedFilter: String,
  onFilterChange: (String) -> Unit,
  onAddLeaveClick: () -> Unit,
  onApproveLeave: (String) -> Unit,
  onRejectLeave: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val isEmployee = currentUser?.role == UserRole.EMPLOYEE
  val baseRequests = remember(leaveRequests, currentUser) {
    if (isEmployee && currentUser != null) {
      leaveRequests.filter { 
        it.employeeId == currentUser.employeeId || 
        it.employeeName.contains(currentUser.name.split(" ").firstOrNull() ?: "", ignoreCase = true) 
      }
    } else {
      leaveRequests
    }
  }

  val filterOptions = listOf("Barchasi", "Kutilmoqda", "Tasdiqlandi", "Rad etildi")

  val filteredRequests = remember(baseRequests, selectedFilter) {
    when (selectedFilter) {
      "Kutilmoqda" -> baseRequests.filter { it.status == LeaveStatus.PENDING }
      "Tasdiqlandi" -> baseRequests.filter { it.status == LeaveStatus.APPROVED }
      "Rad etildi" -> baseRequests.filter { it.status == LeaveStatus.REJECTED }
      else -> baseRequests
    }
  }

  Scaffold(
    floatingActionButton = {
      ExtendedFloatingActionButton(
        onClick = onAddLeaveClick,
        icon = { Icon(Icons.Default.Add, contentDescription = null) },
        text = { Text(if (isEmployee) "Ta'til so'rash" else "Ariza yuborish") },
        modifier = Modifier.testTag("add_leave_fab")
      )
    },
    modifier = modifier
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
        .padding(paddingValues)
    ) {
      // Role-specific Header Banner
      Surface(
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = if (isEmployee) Icons.Default.Person else Icons.Default.SupervisorAccount,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = if (isEmployee) "Faqat shaxsiy ta'til va mehnat arizalaringiz ro'yxati"
                   else "Barcha xodimlarning ta'til arizalarini ko'rib chiqish va tasdiqlash",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      // Filter row
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .background(MaterialTheme.colorScheme.surface)
          .padding(horizontal = 16.dp, vertical = 12.dp)
          .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        filterOptions.forEach { opt ->
          val selected = opt == selectedFilter
          FilterChip(
            selected = selected,
            onClick = { onFilterChange(opt) },
            label = { Text(opt, fontSize = 13.sp) },
            shape = RoundedCornerShape(8.dp)
          )
        }
      }

      // Summary counter
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Jami arizalar: ${filteredRequests.size} ta",
          style = MaterialTheme.typography.bodySmall,
          fontWeight = FontWeight.SemiBold,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      if (filteredRequests.isEmpty()) {
        EmptyStateView(
          title = "Arizalar mavjud emas",
          description = "Ushbu toifada arizalar topilmadi yoki barcha arizalar ko'rib chiqilgan.",
          icon = Icons.Default.EventBusy
        )
      } else {
        LazyColumn(
          modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp),
          contentPadding = PaddingValues(bottom = 80.dp)
        ) {
          items(filteredRequests, key = { it.id }) { req ->
            LeaveRequestCard(
              request = req,
              canApproveOrReject = !isEmployee,
              onApprove = { onApproveLeave(req.id) },
              onReject = { onRejectLeave(req.id) }
            )
          }
        }
      }
    }
  }
}

@Composable
fun LeaveRequestCard(
  request: LeaveRequest,
  canApproveOrReject: Boolean = true,
  onApprove: () -> Unit,
  onReject: () -> Unit
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
        Column {
          Text(
            text = request.employeeName,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = request.department,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
        LeaveStatusBadge(status = request.status)
      }

      Spacer(modifier = Modifier.height(10.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .background(CorporatePrimaryContainer, shape = RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Text(
            text = request.leaveType.label,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = CorporateOnPrimaryContainer
          )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "${request.startDate} – ${request.endDate} (${request.daysCount} kun)",
          style = MaterialTheme.typography.bodyMedium,
          fontWeight = FontWeight.Medium
        )
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = "Sabab: \"${request.reason}\"",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Spacer(modifier = Modifier.height(4.dp))

      Text(
        text = "Ariza sanasi: ${request.requestedAt}",
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
      )

      // Decision action buttons if status is PENDING
      if (request.status == LeaveStatus.PENDING) {
        Spacer(modifier = Modifier.height(14.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(10.dp))
        if (canApproveOrReject) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
          ) {
            OutlinedButton(
              onClick = onReject,
              colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
            ) {
              Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Rad etish")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
              onClick = onApprove,
              colors = ButtonDefaults.buttonColors(containerColor = StatusSuccessText)
            ) {
              Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Tasdiqlash")
            }
          }
        } else {
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.HourglassTop,
              contentDescription = null,
              tint = Color(0xFFD97706),
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "Arizangiz HR va Rahbariyat tomonidan ko'rib chiqilmoqda",
              fontSize = 12.sp,
              color = Color(0xFFD97706),
              fontWeight = FontWeight.Medium
            )
          }
        }
      }
    }
  }
}
