package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun DepartmentsScreen(
  currentUser: AuthUser? = null,
  departments: List<Department>,
  orgHierarchy: List<OrgNode>,
  onAddDepartmentClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val canAddDept = currentUser?.role == UserRole.HR_MANAGER || currentUser?.role == UserRole.DIRECTOR
  var selectedTab by remember { mutableIntStateOf(0) } // 0: Bo'limlar, 1: Tashkiliy tuzilma (Daraxt)

  Scaffold(
    floatingActionButton = {
      if (selectedTab == 0 && canAddDept) {
        ExtendedFloatingActionButton(
          onClick = onAddDepartmentClick,
          icon = { Icon(Icons.Default.AddBusiness, contentDescription = null) },
          text = { Text("Bo'lim qo'shish") },
          modifier = Modifier.testTag("add_dept_fab")
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
      TabRow(
        selectedTabIndex = selectedTab,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = CorporatePrimary
      ) {
        Tab(
          selected = selectedTab == 0,
          onClick = { selectedTab = 0 },
          text = { Text("Bo'limlar ro'yxati", fontWeight = FontWeight.SemiBold) },
          icon = { Icon(Icons.Default.Business, contentDescription = null) }
        )
        Tab(
          selected = selectedTab == 1,
          onClick = { selectedTab = 1 },
          text = { Text("Tashkiliy tuzilma", fontWeight = FontWeight.SemiBold) },
          icon = { Icon(Icons.Default.AccountTree, contentDescription = null) }
        )
      }

      if (selectedTab == 0) {
        // Departments List
        LazyColumn(
          modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp),
          contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp)
        ) {
          item {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Tashkilot bo'limlari (${departments.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
              )
            }
          }

          items(departments, key = { it.id }) { dept ->
            DepartmentCard(department = dept)
          }
        }
      } else {
        // Organizational Tree View
        LazyColumn(
          modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp),
          contentPadding = PaddingValues(vertical = 16.dp)
        ) {
          item {
            Card(
              modifier = Modifier.fillMaxWidth(),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
              shape = RoundedCornerShape(12.dp)
            ) {
              Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                  imageVector = Icons.Default.Info,
                  contentDescription = null,
                  tint = CorporatePrimary
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                  text = "Iyerarxik tashkiliy tuzilma: Rahbariyatdan bo'lim boshliqlari va mutaxassislargacha",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          }

          items(orgHierarchy, key = { it.id }) { node ->
            OrgTreeNodeItem(node = node)
          }
        }
      }
    }
  }
}

@Composable
fun DepartmentCard(department: Department) {
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
          Box(
            modifier = Modifier
              .size(44.dp)
              .clip(RoundedCornerShape(10.dp))
              .background(CorporatePrimaryContainer),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Business,
              contentDescription = null,
              tint = CorporatePrimary,
              modifier = Modifier.size(24.dp)
            )
          }
          Spacer(modifier = Modifier.width(12.dp))
          Column {
            Text(
              text = department.name,
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "${department.employeeCount} nafar xodim",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.primary,
              fontWeight = FontWeight.Medium
            )
          }
        }

        StatusBadge(
          text = "Faol",
          backgroundColor = StatusSuccessBg,
          textColor = StatusSuccessText
        )
      }

      Spacer(modifier = Modifier.height(12.dp))

      Text(
        text = department.description,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Spacer(modifier = Modifier.height(14.dp))
      HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
      Spacer(modifier = Modifier.height(10.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(text = "Bo'lim boshlig'i", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          Text(text = department.headName, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
          Text(text = department.headPosition, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        Column(horizontalAlignment = Alignment.End) {
          Text(text = "Oylik byudjet", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          Text(text = formatUzs(department.monthlyBudget), fontSize = 13.sp, fontWeight = FontWeight.Bold, color = CorporatePrimary)
        }
      }
    }
  }
}

@Composable
fun OrgTreeNodeItem(node: OrgNode) {
  val level = when {
    node.reportsToId == null -> 0
    node.subRolesCount > 0 -> 1
    else -> 2
  }

  val indent = when (level) {
    0 -> 0.dp
    1 -> 20.dp
    else -> 40.dp
  }

  val cardBg = when (level) {
    0 -> CorporatePrimary
    1 -> MaterialTheme.colorScheme.surface
    else -> MaterialTheme.colorScheme.surface
  }

  val textColor = when (level) {
    0 -> Color.White
    else -> MaterialTheme.colorScheme.onSurface
  }

  val subtitleColor = when (level) {
    0 -> Color.White.copy(alpha = 0.85f)
    else -> MaterialTheme.colorScheme.onSurfaceVariant
  }

  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(start = indent),
    verticalAlignment = Alignment.CenterVertically
  ) {
    if (level > 0) {
      Icon(
        imageVector = Icons.Default.SubdirectoryArrowRight,
        contentDescription = null,
        tint = CorporatePrimary,
        modifier = Modifier
          .size(20.dp)
          .padding(end = 4.dp)
      )
    }

    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(12.dp),
      colors = CardDefaults.cardColors(containerColor = cardBg),
      elevation = CardDefaults.cardElevation(defaultElevation = if (level == 0) 3.dp else 1.dp)
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(36.dp)
              .clip(CircleShape)
              .background(
                if (level == 0) Color.White.copy(alpha = 0.2f)
                else CorporatePrimaryContainer
              ),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = if (level == 0) Icons.Default.Stars else Icons.Default.Person,
              contentDescription = null,
              tint = if (level == 0) Color.White else CorporatePrimary,
              modifier = Modifier.size(20.dp)
            )
          }

          Spacer(modifier = Modifier.width(12.dp))

          Column {
            Text(
              text = node.name,
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold,
              color = textColor
            )
            Text(
              text = node.role,
              style = MaterialTheme.typography.bodySmall,
              color = subtitleColor
            )
          }
        }

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(
              if (level == 0) Color.White.copy(alpha = 0.25f)
              else MaterialTheme.colorScheme.surfaceVariant
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Text(
            text = node.department,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = textColor
          )
        }
      }
    }
  }
}
