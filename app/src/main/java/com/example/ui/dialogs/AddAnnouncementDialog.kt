package com.example.ui.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.CorporatePrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAnnouncementDialog(
  onDismiss: () -> Unit,
  onConfirm: (title: String, content: String, isImportant: Boolean, celebrationType: String?) -> Unit
) {
  var title by remember { mutableStateOf("") }
  var content by remember { mutableStateOf("") }
  var isImportant by remember { mutableStateOf(false) }
  var celebrationType by remember { mutableStateOf<String?>("GENERAL") }

  val celebrationOptions = listOf(
    Pair("GENERAL", "📢 Oddiy e'lon"),
    Pair("BIRTHDAY", "🎂 Tavallud ayyom"),
    Pair("ACHIEVEMENT", "🏆 Yutuq / G'alaba")
  )

  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .testTag("add_announcement_dialog")
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Campaign,
            contentDescription = null,
            tint = CorporatePrimary,
            modifier = Modifier.size(24.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Yangi E'lon Chop Etish",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          label = { Text("E'lon sarlavhasi") },
          placeholder = { Text("Masalan: Korporativ yangi loyiha...") },
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(10.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
          value = content,
          onValueChange = { content = it },
          label = { Text("Batafsil matn") },
          placeholder = { Text("Barcha xodimlar diqqatiga...") },
          minLines = 3,
          maxLines = 5,
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(10.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
          text = "E'lon turi:",
          style = MaterialTheme.typography.labelMedium,
          fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          celebrationOptions.forEach { (type, label) ->
            val isSelected = celebrationType == type
            FilterChip(
              selected = isSelected,
              onClick = { celebrationType = type },
              label = { Text(label, fontSize = 11.sp) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = CorporatePrimary,
                selectedLabelColor = Color.White
              ),
              modifier = Modifier.weight(1f)
            )
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.fillMaxWidth()
        ) {
          Checkbox(
            checked = isImportant,
            onCheckedChange = { isImportant = it },
            colors = CheckboxDefaults.colors(checkedColor = Color(0xFFDC2626))
          )
          Text(
            text = "Muhim e'lon (qizil belgi bilan ajratilsin)",
            style = MaterialTheme.typography.bodySmall
          )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.End,
          verticalAlignment = Alignment.CenterVertically
        ) {
          TextButton(onClick = onDismiss) {
            Text("Bekor qilish")
          }
          Spacer(modifier = Modifier.width(8.dp))
          Button(
            onClick = {
              if (title.isNotBlank() && content.isNotBlank()) {
                onConfirm(title, content, isImportant, celebrationType)
              }
            },
            enabled = title.isNotBlank() && content.isNotBlank(),
            colors = ButtonDefaults.buttonColors(containerColor = CorporatePrimary),
            shape = RoundedCornerShape(10.dp)
          ) {
            Text("Chop etish", fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}
