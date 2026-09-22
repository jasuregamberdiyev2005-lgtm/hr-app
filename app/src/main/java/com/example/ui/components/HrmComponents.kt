package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*
import com.example.ui.theme.*
import java.text.NumberFormat
import java.util.Locale

fun formatUzs(amount: Long): String {
  val formatter = NumberFormat.getNumberInstance(Locale.US)
  return "${formatter.format(amount)} so'm"
}

@Composable
fun EmployeeAvatar(
  firstName: String,
  lastName: String,
  colorHex: Long,
  size: Int = 42,
  modifier: Modifier = Modifier
) {
  val initials = "${firstName.take(1)}${lastName.take(1)}".uppercase()
  Box(
    modifier = modifier
      .size(size.dp)
      .clip(CircleShape)
      .background(Color(colorHex)),
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = initials,
      color = Color.White,
      fontSize = (size * 0.38).sp,
      fontWeight = FontWeight.Bold
    )
  }
}

@Composable
fun StatusBadge(
  text: String,
  backgroundColor: Color,
  textColor: Color,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(6.dp))
      .background(backgroundColor)
      .padding(horizontal = 8.dp, vertical = 4.dp)
  ) {
    Text(
      text = text,
      color = textColor,
      fontSize = 12.sp,
      fontWeight = FontWeight.SemiBold
    )
  }
}

@Composable
fun EmployeeStatusBadge(status: EmployeeStatus) {
  when (status) {
    EmployeeStatus.ACTIVE -> StatusBadge(
      text = status.label,
      backgroundColor = StatusSuccessBg,
      textColor = StatusSuccessText
    )
    EmployeeStatus.ON_LEAVE -> StatusBadge(
      text = status.label,
      backgroundColor = StatusInfoBg,
      textColor = StatusInfoText
    )
    EmployeeStatus.PROBATION -> StatusBadge(
      text = status.label,
      backgroundColor = StatusWarningBg,
      textColor = StatusWarningText
    )
    EmployeeStatus.TERMINATED -> StatusBadge(
      text = status.label,
      backgroundColor = StatusDangerBg,
      textColor = StatusDangerText
    )
  }
}

@Composable
fun LeaveStatusBadge(status: LeaveStatus) {
  when (status) {
    LeaveStatus.APPROVED -> StatusBadge(
      text = status.label,
      backgroundColor = StatusSuccessBg,
      textColor = StatusSuccessText
    )
    LeaveStatus.PENDING -> StatusBadge(
      text = status.label,
      backgroundColor = StatusWarningBg,
      textColor = StatusWarningText
    )
    LeaveStatus.REJECTED -> StatusBadge(
      text = status.label,
      backgroundColor = StatusDangerBg,
      textColor = StatusDangerText
    )
  }
}

@Composable
fun AttendanceStatusBadge(status: AttendanceStatus) {
  when (status) {
    AttendanceStatus.PRESENT -> StatusBadge(
      text = status.label,
      backgroundColor = StatusSuccessBg,
      textColor = StatusSuccessText
    )
    AttendanceStatus.LATE -> StatusBadge(
      text = status.label,
      backgroundColor = StatusWarningBg,
      textColor = StatusWarningText
    )
    AttendanceStatus.REMOTE -> StatusBadge(
      text = status.label,
      backgroundColor = StatusPurpleBg,
      textColor = StatusPurpleText
    )
    AttendanceStatus.ABSENT -> StatusBadge(
      text = status.label,
      backgroundColor = StatusDangerBg,
      textColor = StatusDangerText
    )
    AttendanceStatus.ON_LEAVE -> StatusBadge(
      text = status.label,
      backgroundColor = StatusInfoBg,
      textColor = StatusInfoText
    )
  }
}

@Composable
fun StatCard(
  title: String,
  value: String,
  subtitle: String? = null,
  icon: ImageVector,
  iconBgColor: Color = CorporatePrimaryContainer,
  iconColor: Color = CorporatePrimary,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier.fillMaxWidth(),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = title,
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = value,
          style = MaterialTheme.typography.headlineMedium,
          color = MaterialTheme.colorScheme.onSurface,
          fontWeight = FontWeight.Bold
        )
        if (subtitle != null) {
          Spacer(modifier = Modifier.height(4.dp))
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.TrendingUp,
              contentDescription = null,
              tint = StatusSuccessText,
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = subtitle,
              style = MaterialTheme.typography.labelSmall,
              color = StatusSuccessText,
              fontWeight = FontWeight.Medium
            )
          }
        }
      }
      Box(
        modifier = Modifier
          .size(48.dp)
          .clip(RoundedCornerShape(12.dp))
          .background(iconBgColor),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = iconColor,
          modifier = Modifier.size(24.dp)
        )
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HrmTopBar(
  currentTab: HrmTab,
  pendingLeavesCount: Int,
  currentUser: AuthUser?,
  onOpenMenu: () -> Unit,
  onQuickAction: () -> Unit,
  onLogout: () -> Unit
) {
  TopAppBar(
    title = {
      Column {
        Text(
          text = currentTab.uzbekTitle,
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
        if (currentUser != null) {
          Text(
            text = "${currentUser.name} • ${currentUser.role.label}",
            style = MaterialTheme.typography.labelSmall,
            color = CorporatePrimary,
            maxLines = 1
          )
        } else {
          Text(
            text = "HRM Boshqaruv Tizimi",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    },
    navigationIcon = {
      IconButton(onClick = onOpenMenu, modifier = Modifier.testTag("menu_drawer_button")) {
        Icon(
          imageVector = Icons.Default.Menu,
          contentDescription = "Menyu",
          tint = MaterialTheme.colorScheme.onSurface
        )
      }
    },
    actions = {
      // Pending notification badge
      BadgedBox(
        badge = {
          if (pendingLeavesCount > 0) {
            Badge(
              containerColor = Color(0xFFEF4444),
              contentColor = Color.White
            ) {
              Text("$pendingLeavesCount")
            }
          }
        }
      ) {
        IconButton(onClick = onQuickAction, modifier = Modifier.testTag("notification_button")) {
          Icon(
            imageVector = Icons.Default.Notifications,
            contentDescription = "Bildirishnomalar",
            tint = MaterialTheme.colorScheme.onSurface
          )
        }
      }

      // Logout button
      IconButton(onClick = onLogout, modifier = Modifier.testTag("logout_button")) {
        Icon(
          imageVector = Icons.Default.Logout,
          contentDescription = "Chiqish",
          tint = MaterialTheme.colorScheme.error
        )
      }
    },
    colors = TopAppBarDefaults.topAppBarColors(
      containerColor = MaterialTheme.colorScheme.surface
    )
  )
}

@Composable
fun HrmBottomNavBar(
  currentTab: HrmTab,
  onSelectTab: (HrmTab) -> Unit,
  pendingLeavesCount: Int,
  currentUser: AuthUser? = null,
  modifier: Modifier = Modifier
) {
  NavigationBar(
    modifier = modifier,
    containerColor = MaterialTheme.colorScheme.surface,
    tonalElevation = 6.dp
  ) {
    val tabs = when (currentUser?.role) {
      UserRole.DIRECTOR -> listOf(
        Triple(HrmTab.DASHBOARD, Icons.Default.Dashboard, "Boshqaruv"),
        Triple(HrmTab.PAYROLL, Icons.Default.Payments, "Ish haqi"),
        Triple(HrmTab.KPI, Icons.Default.Star, "KPI"),
        Triple(HrmTab.EMPLOYEES, Icons.Default.People, "Xodimlar"),
        Triple(HrmTab.ANNOUNCEMENTS, Icons.Default.Campaign, "Qarorlar")
      )
      UserRole.HR_MANAGER -> listOf(
        Triple(HrmTab.EMPLOYEES, Icons.Default.People, "Kadrlar"),
        Triple(HrmTab.ATTENDANCE, Icons.Default.FactCheck, "Davomat"),
        Triple(HrmTab.LEAVES, Icons.Default.DateRange, "Arizalar"),
        Triple(HrmTab.DOCUMENTS, Icons.Default.Description, "Spravka"),
        Triple(HrmTab.ANNOUNCEMENTS, Icons.Default.Campaign, "E'lonlar")
      )
      UserRole.EMPLOYEE -> listOf(
        Triple(HrmTab.DASHBOARD, Icons.Default.Dashboard, "Portal"),
        Triple(HrmTab.PAYROLL, Icons.Default.Payments, "Maoshim"),
        Triple(HrmTab.KPI, Icons.Default.Flag, "Mening KPI"),
        Triple(HrmTab.LEAVES, Icons.Default.DateRange, "Arizalar"),
        Triple(HrmTab.DOCUMENTS, Icons.Default.Description, "Spravka")
      )
      null -> listOf(
        Triple(HrmTab.DASHBOARD, Icons.Default.Dashboard, "Boshqaruv"),
        Triple(HrmTab.EMPLOYEES, Icons.Default.People, "Xodimlar"),
        Triple(HrmTab.ATTENDANCE, Icons.Default.FactCheck, "Davomat"),
        Triple(HrmTab.ANNOUNCEMENTS, Icons.Default.Campaign, "E'lonlar")
      )
    }

    tabs.forEach { (tab, icon, label) ->
      val selected = currentTab == tab
      NavigationBarItem(
        selected = selected,
        onClick = { onSelectTab(tab) },
        icon = {
          if (tab == HrmTab.LEAVES && pendingLeavesCount > 0) {
            BadgedBox(
              badge = {
                Badge(containerColor = Color(0xFFEF4444)) {
                  Text("$pendingLeavesCount")
                }
              }
            ) {
              Icon(imageVector = icon, contentDescription = label)
            }
          } else {
            Icon(imageVector = icon, contentDescription = label)
          }
        },
        label = {
          Text(
            text = label,
            fontSize = 11.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        },
        colors = NavigationBarItemDefaults.colors(
          selectedIconColor = CorporatePrimary,
          selectedTextColor = CorporatePrimary,
          indicatorColor = CorporatePrimaryContainer,
          unselectedIconColor = CorporateTextSecondary,
          unselectedTextColor = CorporateTextSecondary
        )
      )
    }
  }
}

@Composable
fun EmptyStateView(
  title: String,
  description: String,
  icon: ImageVector = Icons.Default.SearchOff,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .padding(32.dp),
    contentAlignment = Alignment.Center
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      Box(
        modifier = Modifier
          .size(64.dp)
          .clip(CircleShape)
          .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.size(32.dp)
        )
      }
      Spacer(modifier = Modifier.height(16.dp))
      Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )
      Spacer(modifier = Modifier.height(6.dp))
      Text(
        text = description,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center
      )
    }
  }
}
