package com.example.ui.screens

import android.content.Intent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AuthUser
import com.example.model.CompanyAnnouncement
import com.example.model.Employee
import com.example.model.UserRole
import com.example.ui.theme.*

@Composable
fun AnnouncementsScreen(
  announcements: List<CompanyAnnouncement>,
  employees: List<Employee>,
  currentUser: AuthUser,
  onToggleLike: (String) -> Unit,
  onSendBirthdayGreeting: (String) -> Unit,
  onAddAnnouncementClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  var selectedFilter by remember { mutableStateOf("Barchasi") }
  val filterOptions = listOf("Barchasi", "Muhim", "Bayramlar", "Yutuqlar")

  val birthdayEmployees = remember(employees) {
    employees.filter { it.isBirthdayToday }
  }

  val filteredAnnouncements = remember(announcements, selectedFilter) {
    when (selectedFilter) {
      "Muhim" -> announcements.filter { it.isImportant }
      "Bayramlar" -> announcements.filter { it.celebrationType == "BIRTHDAY" }
      "Yutuqlar" -> announcements.filter { it.celebrationType == "ACHIEVEMENT" }
      else -> announcements
    }
  }

  Box(modifier = modifier.fillMaxSize().background(CorporateBg)) {
    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding = PaddingValues(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // 1. Header Celebration Banner (Birthdays / Festivities)
      if (birthdayEmployees.isNotEmpty()) {
        item {
          BirthdayCelebrationCard(
            employees = birthdayEmployees,
            onSendGreeting = onSendBirthdayGreeting
          )
        }
      }

      // 2. Filter Pills
      item {
        LazyRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          modifier = Modifier.padding(vertical = 4.dp)
        ) {
          items(filterOptions) { filter ->
            val isSelected = selectedFilter == filter
            FilterChip(
              selected = isSelected,
              onClick = { selectedFilter = filter },
              label = {
                Text(
                  text = filter,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                )
              },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = CorporatePrimary,
                selectedLabelColor = Color.White
              ),
              shape = RoundedCornerShape(20.dp)
            )
          }
        }
      }

      // 3. Announcements List
      if (filteredAnnouncements.isEmpty()) {
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
                imageVector = Icons.Default.Campaign,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = CorporateTextSecondary
              )
              Spacer(modifier = Modifier.height(12.dp))
              Text(
                text = "Hozircha e'lonlar mavjud emas",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
              )
              Text(
                text = "Yangi yangiliklar paydo bo'lganda shu yerda ko'rinadi.",
                style = MaterialTheme.typography.bodySmall,
                color = CorporateTextSecondary
              )
            }
          }
        }
      } else {
        items(filteredAnnouncements, key = { it.id }) { item ->
          AnnouncementCard(
            announcement = item,
            onToggleLike = { onToggleLike(item.id) },
            onShare = {
              val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "📢 ${item.title}\n\n${item.content}\n\n(HRM Korporativ Tizimi)")
                type = "text/plain"
              }
              val shareIntent = Intent.createChooser(sendIntent, "E'lonni ulashish")
              context.startActivity(shareIntent)
            }
          )
        }
      }

      item {
        Spacer(modifier = Modifier.height(72.dp))
      }
    }

    // FAB for Director / HR to create an announcement
    if (currentUser.role != UserRole.EMPLOYEE) {
      FloatingActionButton(
        onClick = onAddAnnouncementClick,
        containerColor = CorporatePrimary,
        contentColor = Color.White,
        modifier = Modifier
          .align(Alignment.BottomEnd)
          .padding(20.dp)
          .testTag("add_announcement_fab")
      ) {
        Icon(imageVector = Icons.Default.AddComment, contentDescription = "E'lon qo'shish")
      }
    }
  }
}

@Composable
fun BirthdayCelebrationCard(
  employees: List<Employee>,
  onSendGreeting: (String) -> Unit
) {
  var isGreeted by remember { mutableStateOf(false) }

  // Animated gradient shimmer
  val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
  val shimmerOffset by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 1000f,
    animationSpec = infiniteRepeatable(
      animation = tween(2500, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "shimmerOffset"
  )

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("birthday_celebration_card"),
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .background(
          Brush.horizontalGradient(
            colors = listOf(
              Color(0xFFE11D48),
              Color(0xFFBE123C),
              Color(0xFF881337)
            )
          )
        )
        .padding(18.dp)
    ) {
      Column {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween,
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.25f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Cake,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
              )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = "🎉 Bugun tavallud ayyomi!",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
              )
              Text(
                text = "Jamoamiz a'zosini birgalikda tabriklaymiz",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.9f)
              )
            }
          }

          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(Color.White.copy(alpha = 0.2f))
              .padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Text(
              text = "17-Sentabr",
              color = Color.White,
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        employees.forEach { emp ->
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(12.dp))
              .background(Color.White.copy(alpha = 0.15f))
              .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(40.dp)
                  .clip(CircleShape)
                  .background(Color(emp.avatarColorHex)),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = "${emp.firstName.first()}${emp.lastName.first()}",
                  color = Color.White,
                  fontWeight = FontWeight.Bold,
                  fontSize = 14.sp
                )
              }
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(
                  text = emp.fullName,
                  fontWeight = FontWeight.Bold,
                  color = Color.White,
                  fontSize = 15.sp
                )
                Text(
                  text = "${emp.position} • ${emp.department}",
                  color = Color.White.copy(alpha = 0.85f),
                  fontSize = 12.sp
                )
              }
            }

            Button(
              onClick = {
                isGreeted = true
                onSendGreeting(emp.fullName)
              },
              colors = ButtonDefaults.buttonColors(
                containerColor = if (isGreeted) Color.White.copy(alpha = 0.3f) else Color.White,
                contentColor = if (isGreeted) Color.White else Color(0xFFBE123C)
              ),
              shape = RoundedCornerShape(10.dp),
              contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
              Icon(
                imageVector = if (isGreeted) Icons.Default.Check else Icons.Default.VolunteerActivism,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = if (isGreeted) "Tabriklandi" else "Tabriklash",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
              )
            }
          }
        }
      }
    }
  }
}

@Composable
fun AnnouncementCard(
  announcement: CompanyAnnouncement,
  onToggleLike: () -> Unit,
  onShare: () -> Unit
) {
  val likeScale by animateFloatAsState(
    targetValue = if (announcement.isLikedByMe) 1.25f else 1.0f,
    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium),
    label = "like_scale"
  )

  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (announcement.isImportant) Color(0xFFFEF2F2) else MaterialTheme.colorScheme.surface
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      // Header: Author, Role, Date, and Badges
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(36.dp)
              .clip(CircleShape)
              .background(CorporatePrimaryContainer),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = when (announcement.celebrationType) {
                "BIRTHDAY" -> Icons.Default.Cake
                "ACHIEVEMENT" -> Icons.Default.EmojiEvents
                else -> Icons.Default.Notifications
              },
              contentDescription = null,
              tint = CorporatePrimary,
              modifier = Modifier.size(20.dp)
            )
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = announcement.authorName,
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "${announcement.authorRole} • ${announcement.date}",
              style = MaterialTheme.typography.labelSmall,
              color = CorporateTextSecondary
            )
          }
        }

        if (announcement.isImportant) {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(6.dp))
              .background(Color(0xFFDC2626))
              .padding(horizontal = 8.dp, vertical = 3.dp)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.PriorityHigh,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(12.dp)
              )
              Text(
                text = "MUHIM",
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Announcement Title
      Text(
        text = announcement.title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )

      Spacer(modifier = Modifier.height(6.dp))

      // Announcement Content
      Text(
        text = announcement.content,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        lineHeight = 20.sp
      )

      Spacer(modifier = Modifier.height(14.dp))
      HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
      Spacer(modifier = Modifier.height(8.dp))

      // Actions: Like, Like counter, Share
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onToggleLike() }
            .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Icon(
            imageVector = if (announcement.isLikedByMe) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
            contentDescription = "Like",
            tint = if (announcement.isLikedByMe) Color(0xFFE11D48) else CorporateTextSecondary,
            modifier = Modifier
              .size(20.dp)
              .scale(likeScale)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "${announcement.likesCount} ta yoqdi",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = if (announcement.isLikedByMe) FontWeight.Bold else FontWeight.Medium,
            color = if (announcement.isLikedByMe) Color(0xFFE11D48) else CorporateTextSecondary
          )
        }

        IconButton(onClick = onShare, modifier = Modifier.size(36.dp)) {
          Icon(
            imageVector = Icons.Default.Share,
            contentDescription = "Ulashish",
            tint = CorporateTextSecondary,
            modifier = Modifier.size(18.dp)
          )
        }
      }
    }
  }
}
