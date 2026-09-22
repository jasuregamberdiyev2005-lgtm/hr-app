package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*
import com.example.ui.theme.*

@Composable
fun LoginScreen(
  employees: List<Employee>,
  onLogin: (email: String, role: UserRole) -> Unit,
  onLoginAsEmployee: (Employee) -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedRole by remember { mutableStateOf(UserRole.HR_MANAGER) }
  var email by remember { mutableStateOf("hr@hrm.uz") }
  var password by remember { mutableStateOf("••••••••") }
  var isPasswordVisible by remember { mutableStateOf(false) }

  // When role changes, update suggested email
  LaunchedEffect(selectedRole) {
    email = when (selectedRole) {
      UserRole.DIRECTOR -> "director@hrm.uz"
      UserRole.HR_MANAGER -> "hr@hrm.uz"
      UserRole.EMPLOYEE -> "sardor.aliyev@hrm.uz"
    }
  }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(CorporateBg)
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 24.dp, vertical = 32.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Spacer(modifier = Modifier.height(16.dp))

      // App Logo & Header
      Box(
        modifier = Modifier
          .size(72.dp)
          .clip(RoundedCornerShape(20.dp))
          .background(CorporatePrimary),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.CorporateFare,
          contentDescription = "HRM Logo",
          tint = Color.White,
          modifier = Modifier.size(42.dp)
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      Text(
        text = "HRM Boshqaruv Tizimi",
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold,
        color = CorporateTextPrimary
      )

      Text(
        text = "Korporativ xodimlar va resurslar portali",
        style = MaterialTheme.typography.bodyMedium,
        color = CorporateTextSecondary,
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(28.dp))

      // Role Selection Label
      Text(
        text = "Kim sifatida kirmoqchisiz?",
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.SemiBold,
        color = CorporateTextPrimary,
        modifier = Modifier.align(Alignment.Start)
      )

      Spacer(modifier = Modifier.height(10.dp))

      // 3 Role Selection Cards
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        RoleSelectCard(
          role = UserRole.DIRECTOR,
          title = "Direktor",
          subtitle = "Rahbar",
          icon = Icons.Default.Shield,
          isSelected = selectedRole == UserRole.DIRECTOR,
          onClick = { selectedRole = UserRole.DIRECTOR },
          modifier = Modifier.weight(1f)
        )
        RoleSelectCard(
          role = UserRole.HR_MANAGER,
          title = "HR Menejer",
          subtitle = "Kadrlar",
          icon = Icons.Default.AdminPanelSettings,
          isSelected = selectedRole == UserRole.HR_MANAGER,
          onClick = { selectedRole = UserRole.HR_MANAGER },
          modifier = Modifier.weight(1f)
        )
        RoleSelectCard(
          role = UserRole.EMPLOYEE,
          title = "Xodim",
          subtitle = "Portal",
          icon = Icons.Default.Badge,
          isSelected = selectedRole == UserRole.EMPLOYEE,
          onClick = { selectedRole = UserRole.EMPLOYEE },
          modifier = Modifier.weight(1f)
        )
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Login Card Container
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
        ) {
          Text(
            text = "${selectedRole.label} sifatida kirish",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = CorporatePrimary
          )

          Spacer(modifier = Modifier.height(16.dp))

          // Email Input
          OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email yoki login") },
            leadingIcon = {
              Icon(Icons.Default.Email, contentDescription = null)
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
              keyboardType = KeyboardType.Email,
              imeAction = ImeAction.Next
            ),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("login_email_input"),
            shape = RoundedCornerShape(12.dp)
          )

          Spacer(modifier = Modifier.height(12.dp))

          // Password Input
          OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Parol") },
            leadingIcon = {
              Icon(Icons.Default.Lock, contentDescription = null)
            },
            trailingIcon = {
              IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                Icon(
                  imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                  contentDescription = if (isPasswordVisible) "Parolni yashirish" else "Parolni ko'rsatish"
                )
              }
            },
            singleLine = true,
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
              keyboardType = KeyboardType.Password,
              imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
              onDone = { onLogin(email, selectedRole) }
            ),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("login_password_input"),
            shape = RoundedCornerShape(12.dp)
          )

          Spacer(modifier = Modifier.height(20.dp))

          // Main Submit Button
          Button(
            onClick = { onLogin(email, selectedRole) },
            modifier = Modifier
              .fillMaxWidth()
              .height(48.dp)
              .testTag("login_submit_button"),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = CorporatePrimary)
          ) {
            Icon(Icons.Default.Login, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Tizimga kirish",
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(24.dp))

      // Quick 1-Tap Demo Logins Section
      Text(
        text = "Tezkor sinov (Demo 1-klik orqali kirish)",
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.SemiBold,
        color = CorporateTextPrimary,
        modifier = Modifier.align(Alignment.Start)
      )

      Spacer(modifier = Modifier.height(10.dp))

      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        QuickLoginButton(
          title = "Jasur Rahimov (Direktor / Rahbar)",
          subtitle = "Kompaniya ko'rsatkichlari, byudjet, tashkiliy tuzilma",
          icon = Icons.Default.BusinessCenter,
          onClick = { onLogin("director@hrm.uz", UserRole.DIRECTOR) }
        )

        QuickLoginButton(
          title = "Malika Karimova (HR Menejer)",
          subtitle = "Xodimlar boshqaruvi, davomat, arizalar, maosh",
          icon = Icons.Default.Groups,
          onClick = { onLogin("hr@hrm.uz", UserRole.HR_MANAGER) }
        )

        val demoEmployee = employees.find { it.id == "emp-003" } ?: employees.firstOrNull()
        demoEmployee?.let { emp ->
          QuickLoginButton(
            title = "${emp.fullName} (Xodim portali)",
            subtitle = "${emp.position} • Shaxsiy davomat va maosh",
            icon = Icons.Default.Person,
            onClick = { onLoginAsEmployee(emp) }
          )
        }
      }

      Spacer(modifier = Modifier.height(24.dp))

      // Footer note
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
      ) {
        Icon(
          imageVector = Icons.Default.CheckCircle,
          contentDescription = null,
          tint = StatusSuccessText,
          modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "Xavfsiz korporativ avtorizatsiya prototipi",
          style = MaterialTheme.typography.bodySmall,
          color = CorporateTextSecondary
        )
      }
    }
  }
}

@Composable
fun RoleSelectCard(
  role: UserRole,
  title: String,
  subtitle: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  isSelected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .clip(RoundedCornerShape(12.dp))
      .clickable(onClick = onClick)
      .border(
        width = if (isSelected) 2.dp else 1.dp,
        color = if (isSelected) CorporatePrimary else Color(0xFFE2E8F0),
        shape = RoundedCornerShape(12.dp)
      ),
    colors = CardDefaults.cardColors(
      containerColor = if (isSelected) CorporatePrimaryContainer else MaterialTheme.colorScheme.surface
    )
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 12.dp, horizontal = 8.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Icon(
        imageVector = icon,
        contentDescription = null,
        tint = if (isSelected) CorporatePrimary else CorporateTextSecondary,
        modifier = Modifier.size(24.dp)
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = title,
        fontSize = 12.sp,
        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
        color = if (isSelected) CorporatePrimary else CorporateTextPrimary,
        maxLines = 1
      )
      Text(
        text = subtitle,
        fontSize = 10.sp,
        color = if (isSelected) CorporatePrimary.copy(alpha = 0.8f) else CorporateTextSecondary,
        maxLines = 1
      )
    }
  }
}

@Composable
fun QuickLoginButton(
  title: String,
  subtitle: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  onClick: () -> Unit
) {
  OutlinedCard(
    onClick = onClick,
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(36.dp)
          .clip(CircleShape)
          .background(CorporatePrimaryContainer),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = CorporatePrimary,
          modifier = Modifier.size(20.dp)
        )
      }
      Spacer(modifier = Modifier.width(12.dp))
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = title,
          style = MaterialTheme.typography.bodyMedium,
          fontWeight = FontWeight.SemiBold,
          color = CorporateTextPrimary
        )
        Text(
          text = subtitle,
          style = MaterialTheme.typography.bodySmall,
          color = CorporateTextSecondary
        )
      }
      Icon(
        imageVector = Icons.Default.ArrowForward,
        contentDescription = "Kirish",
        tint = CorporatePrimary,
        modifier = Modifier.size(18.dp)
      )
    }
  }
}
