package com.example.ui.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.DocumentType
import com.example.ui.theme.CorporatePrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestDocumentDialog(
  onDismiss: () -> Unit,
  onConfirm: (docType: DocumentType, purpose: String) -> Unit
) {
  var selectedDocType by remember { mutableStateOf(DocumentType.WORK_CERTIFICATE) }
  var purpose by remember { mutableStateOf("") }
  var expanded by remember { mutableStateOf(false) }

  val commonPurposes = listOf(
    "Ipoteka / Bank krediti rasmiylashtirish uchun",
    "Avtokredit olish maqsadida",
    "Chet el vizasi (elchixona) uchun",
    "Davlat xizmatlari / Subsidiyaga taqdim etish uchun",
    "Ta'lim krediti yoki oliy o'quv yurti uchun"
  )

  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .testTag("request_document_dialog")
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Description,
            contentDescription = null,
            tint = CorporatePrimary,
            modifier = Modifier.size(24.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Elektron Ma'lumotnoma So'rash",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Doc type selector
        ExposedDropdownMenuBox(
          expanded = expanded,
          onExpandedChange = { expanded = it }
        ) {
          OutlinedTextField(
            value = selectedDocType.label,
            onValueChange = {},
            readOnly = true,
            label = { Text("Hujjat turi") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
              .fillMaxWidth()
              .menuAnchor(),
            shape = RoundedCornerShape(10.dp)
          )

          ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
          ) {
            DocumentType.entries.forEach { docType ->
              DropdownMenuItem(
                text = {
                  Column {
                    Text(text = docType.label, fontWeight = FontWeight.SemiBold)
                    Text(
                      text = docType.description,
                      style = MaterialTheme.typography.bodySmall,
                      color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                  }
                },
                onClick = {
                  selectedDocType = docType
                  expanded = false
                }
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
          value = purpose,
          onValueChange = { purpose = it },
          label = { Text("Taqdim etiladigan joy / Maqsad") },
          placeholder = { Text("Masalan: Ipoteka krediti uchun bankka") },
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(10.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
          text = "Tezkor sabablar:",
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(4.dp))

        commonPurposes.take(3).forEach { p ->
          TextButton(
            onClick = { purpose = p },
            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp)
          ) {
            Text(
              text = "• $p",
              fontSize = 11.sp,
              color = CorporatePrimary
            )
          }
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
              if (purpose.isNotBlank()) {
                onConfirm(selectedDocType, purpose)
              }
            },
            enabled = purpose.isNotBlank(),
            colors = ButtonDefaults.buttonColors(containerColor = CorporatePrimary),
            shape = RoundedCornerShape(10.dp)
          ) {
            Text("So'rov yuborish", fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}
