package com.example.ui.dialogs

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.DocumentRequest
import com.example.model.DocumentType
import com.example.ui.theme.CorporatePrimary
import com.example.ui.theme.CorporateTextSecondary

@Composable
fun DocumentPreviewDialog(
  request: DocumentRequest,
  onDismiss: () -> Unit
) {
  val context = LocalContext.current
  val certNum = request.certificateNumber ?: "HRM-2026-0891"

  val documentText = """
    O'ZBEKISTON RESPUBLIKASI
    "HRM INNOVATION GROUP" MCHJ
    Elektron Hujjat Aylanish Tizimi
    
    MA'LUMOTNOMA (Blank №: $certNum)
    Berilgan sana: ${request.requestedAt}
    
    Mazkur ma'lumotnoma berildiki haqiqatdan ham fuqaro ${request.employeeName} 
    "HRM INNOVATION GROUP" MCHJ ning "${request.department}" bo'limida 
    rasmiy mehnat shartnomasi asosida faoliyat ko'rsatib kelmoqda.
    
    Hujjat turi: ${request.docType.label}
    Taqdim etish maqsadi: ${request.purpose}
    
    Elektron raqamli imzo orqali tasdiqlangan:
    Direktor: J. Rahimov
    HR Bo'limi Boshlig'i: M. Karimova
    
    QR-kod orqali tekshirish: https://hrm.uz/verify/$certNum
  """.trimIndent()

  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.9f)
        .padding(8.dp)
        .testTag("document_preview_modal")
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(20.dp)
      ) {
        // Dialog Top Bar
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Verified,
              contentDescription = null,
              tint = CorporatePrimary,
              modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Elektron Ma'lumotnoma",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF0F172A)
            )
          }

          IconButton(onClick = onDismiss) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Yopish")
          }
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

        // Official Blank Sheet (Paper Look)
        Box(
          modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFFCFDFE))
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(8.dp))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Emblem & Header
            Text(
              text = "O'ZBEKISTON RESPUBLIKASI",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF334155),
              letterSpacing = 1.sp
            )
            Text(
              text = "\"HRM INNOVATION GROUP\" MCHJ",
              fontSize = 13.sp,
              fontWeight = FontWeight.ExtraBold,
              color = CorporatePrimary,
              textAlign = TextAlign.Center
            )
            Text(
              text = "Toshkent sh., Amir Temur shox ko'chasi 107-B • Tel: +998 71 200 00 00",
              fontSize = 9.sp,
              color = CorporateTextSecondary,
              textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(thickness = 1.5.dp, color = Color(0xFF0F172A))
            Spacer(modifier = Modifier.height(14.dp))

            Text(
              text = "MA'LUMOTNOMA",
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF0F172A),
              letterSpacing = 2.sp
            )
            Text(
              text = "№ $certNum",
              fontSize = 11.sp,
              color = CorporateTextSecondary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Body
            Text(
              text = "Mazkur ma'lumotnoma fuqaro ${request.employeeName}ga berildiki, u haqiqatdan ham \"HRM INNOVATION GROUP\" MCHJ korxonasining \"${request.department}\" tarkibida mehnat shartnomasi asosida doimiy ishlab kelmoqda.",
              fontSize = 12.sp,
              lineHeight = 20.sp,
              color = Color(0xFF1E293B),
              textAlign = TextAlign.Justify
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
              text = "Hujjat talab qilingan joy va maqsad: ${request.purpose}.",
              fontSize = 12.sp,
              fontWeight = FontWeight.SemiBold,
              lineHeight = 18.sp,
              color = Color(0xFF1E293B),
              textAlign = TextAlign.Start,
              modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Official Stamp & Verification Box
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              // Digital Stamp Seal
              Box(
                modifier = Modifier
                  .size(80.dp)
                  .clip(CircleShape)
                  .border(2.dp, Color(0xFF1D4ED8), CircleShape)
                  .padding(4.dp),
                contentAlignment = Alignment.Center
              ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                  Text(
                    text = "TASDIQLANDI",
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1D4ED8)
                  )
                  Text(
                    text = "HRM GROUP",
                    fontSize = 7.sp,
                    color = Color(0xFF1D4ED8)
                  )
                  Text(
                    text = "ERI № $certNum",
                    fontSize = 6.sp,
                    color = Color(0xFF1D4ED8)
                  )
                }
              }

              // QR Code Box
              Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                  modifier = Modifier
                    .size(64.dp)
                    .background(Color(0xFFF1F5F9))
                    .border(1.dp, Color(0xFFCBD5E1)),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.Default.QrCode2,
                    contentDescription = "QR Kod",
                    modifier = Modifier.size(54.dp),
                    tint = Color(0xFF0F172A)
                  )
                }
                Text(
                  text = "Haqiqiylikni tekshirish",
                  fontSize = 8.sp,
                  color = CorporateTextSecondary,
                  modifier = Modifier.padding(top = 2.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(14.dp))
            Text(
              text = "Elektron hujjat qonuniy kuchga ega va davlat organlari tomonidan qabul qilinadi.",
              fontSize = 9.sp,
              color = Color(0xFF059669),
              fontWeight = FontWeight.Medium,
              textAlign = TextAlign.Center
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Action Buttons: Share & Close
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.End
        ) {
          OutlinedButton(
            onClick = onDismiss,
            shape = RoundedCornerShape(10.dp)
          ) {
            Text("Yopish")
          }

          Spacer(modifier = Modifier.width(8.dp))

          Button(
            onClick = {
              val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, documentText)
                type = "text/plain"
              }
              context.startActivity(Intent.createChooser(shareIntent, "Ma'lumotnomani ulashish"))
            },
            colors = ButtonDefaults.buttonColors(containerColor = CorporatePrimary),
            shape = RoundedCornerShape(10.dp)
          ) {
            Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Ulashish / PDF", fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}
