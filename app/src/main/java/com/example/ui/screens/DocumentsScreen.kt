package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AuthUser
import com.example.model.DocumentRequest
import com.example.model.DocumentStatus
import com.example.model.UserRole
import com.example.ui.theme.*

@Composable
fun DocumentsScreen(
  documentRequests: List<DocumentRequest>,
  currentUser: AuthUser,
  onRequestDocumentClick: () -> Unit,
  onApproveDocument: (String) -> Unit,
  onRejectDocument: (String) -> Unit,
  onViewDocument: (DocumentRequest) -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedTab by remember { mutableIntStateOf(0) }
  val tabTitles = listOf("Barchasi", "Kutilmoqda", "Tayyorlangan")

  val visibleRequests = remember(documentRequests, selectedTab, currentUser) {
    val roleFiltered = if (currentUser.role == UserRole.EMPLOYEE) {
      documentRequests.filter { it.employeeId == currentUser.employeeId }
    } else {
      documentRequests
    }

    when (selectedTab) {
      1 -> roleFiltered.filter { it.status == DocumentStatus.PENDING }
      2 -> roleFiltered.filter { it.status == DocumentStatus.APPROVED }
      else -> roleFiltered
    }
  }

  val pendingCount = remember(documentRequests) {
    documentRequests.count { it.status == DocumentStatus.PENDING }
  }

  Box(modifier = modifier.fillMaxSize().background(CorporateBg)) {
    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding = PaddingValues(16.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // 1. Overview header card
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Column {
                Text(
                  text = "Elektron Ma'lumotnomalar (Spravka)",
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.Bold
                )
                Text(
                  text = "Bank, elchixona va davlat xizmatlari uchun tasdiqlangan hujjatlar",
                  style = MaterialTheme.typography.bodySmall,
                  color = CorporateTextSecondary
                )
              }

              if (currentUser.role == UserRole.EMPLOYEE) {
                Button(
                  onClick = onRequestDocumentClick,
                  colors = ButtonDefaults.buttonColors(containerColor = CorporatePrimary),
                  shape = RoundedCornerShape(10.dp),
                  modifier = Modifier.testTag("request_doc_header_button")
                ) {
                  Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                  Spacer(modifier = Modifier.width(4.dp))
                  Text("So'rov yuborish", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Quick Stats Cards
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              StatPill(
                title = "Jami",
                count = documentRequests.size.toString(),
                color = CorporatePrimary,
                modifier = Modifier.weight(1f)
              )
              StatPill(
                title = "Kutilmoqda",
                count = pendingCount.toString(),
                color = Color(0xFFD97706),
                modifier = Modifier.weight(1f)
              )
              StatPill(
                title = "Berilgan",
                count = documentRequests.count { it.status == DocumentStatus.APPROVED }.toString(),
                color = StatusSuccessText,
                modifier = Modifier.weight(1f)
              )
            }
          }
        }
      }

      // 2. Tabs
      item {
        TabRow(
          selectedTabIndex = selectedTab,
          containerColor = MaterialTheme.colorScheme.surface,
          contentColor = CorporatePrimary
        ) {
          tabTitles.forEachIndexed { index, title ->
            Tab(
              selected = selectedTab == index,
              onClick = { selectedTab = index },
              text = {
                Text(
                  text = if (index == 1 && pendingCount > 0) "$title ($pendingCount)" else title,
                  fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                )
              }
            )
          }
        }
      }

      // 3. Document Requests List
      if (visibleRequests.isEmpty()) {
        item {
          Card(
            modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
          ) {
            Column(
              modifier = Modifier.fillMaxWidth().padding(32.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Icon(
                imageVector = Icons.Default.Description,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = CorporateTextSecondary
              )
              Spacer(modifier = Modifier.height(10.dp))
              Text(
                text = "Hujjat so'rovlari topilmadi",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
              )
              Text(
                text = "Kerakli ma'lumotnoma uchun so'rov qoldirishingiz mumkin.",
                style = MaterialTheme.typography.bodySmall,
                color = CorporateTextSecondary
              )
            }
          }
        }
      } else {
        items(visibleRequests, key = { it.id }) { doc ->
          DocumentRequestCard(
            request = doc,
            canModerate = currentUser.role != UserRole.EMPLOYEE,
            onApprove = { onApproveDocument(doc.id) },
            onReject = { onRejectDocument(doc.id) },
            onClick = { onViewDocument(doc) }
          )
        }
      }

      item {
        Spacer(modifier = Modifier.height(72.dp))
      }
    }

    // FAB for Employee to request document
    if (currentUser.role == UserRole.EMPLOYEE) {
      FloatingActionButton(
        onClick = onRequestDocumentClick,
        containerColor = CorporatePrimary,
        contentColor = Color.White,
        modifier = Modifier
          .align(Alignment.BottomEnd)
          .padding(20.dp)
          .testTag("request_doc_fab")
      ) {
        Icon(imageVector = Icons.Default.NoteAdd, contentDescription = "Spravka so'rash")
      }
    }
  }
}

@Composable
private fun StatPill(
  title: String,
  count: String,
  color: Color,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(10.dp))
      .background(color.copy(alpha = 0.08f))
      .padding(vertical = 8.dp, horizontal = 12.dp),
    contentAlignment = Alignment.Center
  ) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      Text(text = count, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = color)
      Text(text = title, fontSize = 11.sp, color = CorporateTextSecondary)
    }
  }
}

@Composable
fun DocumentRequestCard(
  request: DocumentRequest,
  canModerate: Boolean,
  onApprove: () -> Unit,
  onReject: () -> Unit,
  onClick: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() }
      .testTag("doc_item_${request.id}"),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(38.dp)
              .clip(RoundedCornerShape(8.dp))
              .background(CorporatePrimaryContainer),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Article,
              contentDescription = null,
              tint = CorporatePrimary,
              modifier = Modifier.size(20.dp)
            )
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = request.docType.label,
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "${request.employeeName} • ${request.department}",
              style = MaterialTheme.typography.labelSmall,
              color = CorporateTextSecondary
            )
          }
        }

        // Status badge
        val (bgColor, textColor) = when (request.status) {
          DocumentStatus.PENDING -> Pair(Color(0xFFFEF3C7), Color(0xFFB45309))
          DocumentStatus.APPROVED -> Pair(Color(0xFFD1FAE5), Color(0xFF065F46))
          DocumentStatus.REJECTED -> Pair(Color(0xFFFEE2E2), Color(0xFF991B1B))
        }

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bgColor)
            .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Text(
            text = request.status.label,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = textColor
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = "Maqsad: ${request.purpose}",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      if (request.certificateNumber != null) {
        Spacer(modifier = Modifier.height(6.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.QrCode2,
            contentDescription = null,
            tint = CorporatePrimary,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = "Elektron Blank Raqami: ${request.certificateNumber}",
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = CorporatePrimary
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))
      Text(
        text = "So'ralgan sana: ${request.requestedAt}",
        fontSize = 10.sp,
        color = CorporateTextSecondary
      )

      // Actions for Director / HR if PENDING
      if (canModerate && request.status == DocumentStatus.PENDING) {
        Spacer(modifier = Modifier.height(12.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.End,
          verticalAlignment = Alignment.CenterVertically
        ) {
          OutlinedButton(
            onClick = onReject,
            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
          ) {
            Icon(imageVector = Icons.Default.Close, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Rad etish", fontSize = 12.sp)
          }

          Spacer(modifier = Modifier.width(8.dp))

          Button(
            onClick = onApprove,
            colors = ButtonDefaults.buttonColors(containerColor = StatusSuccessText),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
          ) {
            Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Tasdiqlash & Berish", fontSize = 12.sp, fontWeight = FontWeight.Bold)
          }
        }
      }

      // If approved, show button to view official document
      if (request.status == DocumentStatus.APPROVED) {
        Spacer(modifier = Modifier.height(10.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.End
        ) {
          TextButton(
            onClick = onClick,
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Icon(imageVector = Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Elektron Blankni Ko'rish", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CorporatePrimary)
          }
        }
      }
    }
  }
}
