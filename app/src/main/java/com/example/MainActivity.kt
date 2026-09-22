package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.HrmTab
import com.example.model.UserRole
import com.example.ui.components.*
import com.example.ui.dialogs.*
import com.example.ui.screens.*
import com.example.ui.theme.CorporatePrimary
import com.example.ui.theme.CorporatePrimaryContainer
import com.example.ui.theme.CorporateTextSecondary
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.HrmViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        HrmAppRoot()
      }
    }
  }
}

@Composable
fun HrmAppRoot(
  viewModel: HrmViewModel = viewModel()
) {
  val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
  val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
  val employees by viewModel.employees.collectAsStateWithLifecycle()
  val attendanceList by viewModel.attendanceList.collectAsStateWithLifecycle()
  val leaveRequests by viewModel.leaveRequests.collectAsStateWithLifecycle()
  val departments by viewModel.departments.collectAsStateWithLifecycle()
  val orgHierarchy by viewModel.orgHierarchy.collectAsStateWithLifecycle()

  val employeeSearchQuery by viewModel.employeeSearchQuery.collectAsStateWithLifecycle()
  val employeeDepartmentFilter by viewModel.employeeDepartmentFilter.collectAsStateWithLifecycle()
  val employeeSortOption by viewModel.employeeSortOption.collectAsStateWithLifecycle()
  val leaveStatusFilter by viewModel.leaveStatusFilter.collectAsStateWithLifecycle()
  val payrollSearchQuery by viewModel.payrollSearchQuery.collectAsStateWithLifecycle()

  val selectedEmployee by viewModel.selectedEmployee.collectAsStateWithLifecycle()
  val editingPayrollEmployee by viewModel.editingPayrollEmployee.collectAsStateWithLifecycle()
  val showAddEmployeeDialog by viewModel.showAddEmployeeDialog.collectAsStateWithLifecycle()
  val showAddLeaveDialog by viewModel.showAddLeaveDialog.collectAsStateWithLifecycle()
  val showAddDepartmentDialog by viewModel.showAddDepartmentDialog.collectAsStateWithLifecycle()
  val snackbarMessage by viewModel.snackbarMessage.collectAsStateWithLifecycle()

  // New modules state
  val announcements by viewModel.announcements.collectAsStateWithLifecycle()
  val documentRequests by viewModel.documentRequests.collectAsStateWithLifecycle()
  val kpiTasks by viewModel.kpiTasks.collectAsStateWithLifecycle()
  val showAddAnnouncementDialog by viewModel.showAddAnnouncementDialog.collectAsStateWithLifecycle()
  val showRequestDocumentDialog by viewModel.showRequestDocumentDialog.collectAsStateWithLifecycle()
  val showExportReportDialog by viewModel.showExportReportDialog.collectAsStateWithLifecycle()
  val exportReportType by viewModel.exportReportType.collectAsStateWithLifecycle()
  var selectedPreviewDoc by remember { mutableStateOf<com.example.model.DocumentRequest?>(null) }

  val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
  val scope = rememberCoroutineScope()
  val snackbarHostState = remember { SnackbarHostState() }

  val pendingLeavesCount = remember(leaveRequests) {
    leaveRequests.count { it.status == com.example.model.LeaveStatus.PENDING }
  }

  val pendingDocsCount = remember(documentRequests) {
    documentRequests.count { it.status == com.example.model.DocumentStatus.PENDING }
  }

  // Show snackbar whenever message changes
  LaunchedEffect(snackbarMessage) {
    snackbarMessage?.let { msg ->
      snackbarHostState.showSnackbar(msg)
      viewModel.clearSnackbar()
    }
  }

  // If user is not logged in, show Login Screen
  if (currentUser == null) {
    Scaffold(
      snackbarHost = { SnackbarHost(snackbarHostState) },
      modifier = Modifier.fillMaxSize()
    ) { padding ->
      LoginScreen(
        employees = employees,
        onLogin = { email, role -> viewModel.login(email, role) },
        onLoginAsEmployee = { emp -> viewModel.loginAsEmployee(emp) },
        modifier = Modifier.padding(padding)
      )
    }
    return
  }

  val user = currentUser!!

  // Sidebar Drawer for full navigation
  ModalNavigationDrawer(
    drawerState = drawerState,
    drawerContent = {
      ModalDrawerSheet(
        modifier = Modifier.width(300.dp),
        drawerContainerColor = MaterialTheme.colorScheme.surface
      ) {
        // User Profile Header in Drawer
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .background(CorporatePrimary)
            .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.2f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = when (user.role) {
                  UserRole.DIRECTOR -> Icons.Default.Shield
                  UserRole.HR_MANAGER -> Icons.Default.AdminPanelSettings
                  UserRole.EMPLOYEE -> Icons.Default.Badge
                },
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(26.dp)
              )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = user.name,
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = user.position,
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 12.sp
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(6.dp))
              .background(Color.White.copy(alpha = 0.2f))
              .padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Text(
              text = user.role.label,
              color = Color.White,
              fontSize = 11.sp,
              fontWeight = FontWeight.SemiBold
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        val navItems = when (user.role) {
          UserRole.DIRECTOR -> listOf(
            Triple(HrmTab.DASHBOARD, Icons.Default.Dashboard, "Strategik Boshqaruv (Executive)"),
            Triple(HrmTab.PAYROLL, Icons.Default.Payments, "Moliya & Ish haqi (Payroll)"),
            Triple(HrmTab.KPI, Icons.Default.Star, "KPI & Baholash"),
            Triple(HrmTab.EMPLOYEES, Icons.Default.People, "Xodimlar ma'lumotnomasi"),
            Triple(HrmTab.DEPARTMENTS, Icons.Default.Business, "Bo'limlar va Byudjet"),
            Triple(HrmTab.ATTENDANCE, Icons.Default.FactCheck, "Davomat monitoringi"),
            Triple(HrmTab.ANNOUNCEMENTS, Icons.Default.Campaign, "Rasmiy qarorlar & E'lonlar"),
            Triple(HrmTab.LEAVES, Icons.Default.DateRange, "Ta'tillar monitoringi")
          )
          UserRole.HR_MANAGER -> listOf(
            Triple(HrmTab.EMPLOYEES, Icons.Default.People, "Kadrlar boshqaruvi"),
            Triple(HrmTab.ATTENDANCE, Icons.Default.FactCheck, "Kunlik Davomat (Tabel)"),
            Triple(HrmTab.LEAVES, Icons.Default.DateRange, "Ta'til arizalari (Tasdiqlash)"),
            Triple(HrmTab.DOCUMENTS, Icons.Default.Description, "Elektron Spravkalar berish"),
            Triple(HrmTab.ANNOUNCEMENTS, Icons.Default.Campaign, "E'lonlar & Bayram tabriklari"),
            Triple(HrmTab.PAYROLL, Icons.Default.Payments, "Ish haqi hisob-kitobi"),
            Triple(HrmTab.KPI, Icons.Default.Flag, "KPI monitoringi"),
            Triple(HrmTab.DEPARTMENTS, Icons.Default.Business, "Bo'limlar ro'yxati"),
            Triple(HrmTab.DASHBOARD, Icons.Default.Dashboard, "Kadrlar hisoboti (Dashboard)")
          )
          UserRole.EMPLOYEE -> listOf(
            Triple(HrmTab.DASHBOARD, Icons.Default.Dashboard, "Mening portalim (Check-in)"),
            Triple(HrmTab.PAYROLL, Icons.Default.Payments, "Mening maoshim (Payslip)"),
            Triple(HrmTab.KPI, Icons.Default.Flag, "Mening KPI topshiriqlarim"),
            Triple(HrmTab.LEAVES, Icons.Default.DateRange, "Ta'til arizalarim"),
            Triple(HrmTab.DOCUMENTS, Icons.Default.Description, "Spravka olish (Hujjatlarim)"),
            Triple(HrmTab.EMPLOYEES, Icons.Default.People, "Hamkasblar kontaktlari"),
            Triple(HrmTab.ANNOUNCEMENTS, Icons.Default.Campaign, "Kompaniya yangiliklari"),
            Triple(HrmTab.DEPARTMENTS, Icons.Default.Business, "Tashkiliy tuzilma")
          )
        }

        navItems.forEach { (tab, icon, label) ->
          val selected = currentTab == tab
          NavigationDrawerItem(
            icon = {
              if (tab == HrmTab.LEAVES && pendingLeavesCount > 0) {
                BadgedBox(badge = { Badge(containerColor = Color(0xFFEF4444)) { Text("$pendingLeavesCount") } }) {
                  Icon(imageVector = icon, contentDescription = label)
                }
              } else if (tab == HrmTab.DOCUMENTS && pendingDocsCount > 0) {
                BadgedBox(badge = { Badge(containerColor = Color(0xFFD97706)) { Text("$pendingDocsCount") } }) {
                  Icon(imageVector = icon, contentDescription = label)
                }
              } else {
                Icon(imageVector = icon, contentDescription = label)
              }
            },
            label = {
              Text(
                text = label,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
              )
            },
            selected = selected,
            onClick = {
              viewModel.setTab(tab)
              scope.launch { drawerState.close() }
            },
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
          )
        }

        Spacer(modifier = Modifier.weight(1f))
        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

        // Logout item in drawer
        NavigationDrawerItem(
          icon = {
            Icon(
              imageVector = Icons.Default.Logout,
              contentDescription = "Chiqish",
              tint = MaterialTheme.colorScheme.error
            )
          },
          label = {
            Text(
              text = "Tizimdan chiqish",
              color = MaterialTheme.colorScheme.error,
              fontWeight = FontWeight.SemiBold
            )
          },
          selected = false,
          onClick = {
            scope.launch { drawerState.close() }
            viewModel.logout()
          },
          modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "Prototip v1.0 • O'zbekiston HRM",
          style = MaterialTheme.typography.bodySmall,
          color = CorporateTextSecondary,
          modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        )
      }
    }
  ) {
    Scaffold(
      topBar = {
        HrmTopBar(
          currentTab = currentTab,
          pendingLeavesCount = pendingLeavesCount,
          currentUser = user,
          onOpenMenu = { scope.launch { drawerState.open() } },
          onQuickAction = { viewModel.setTab(HrmTab.LEAVES) },
          onLogout = { viewModel.logout() }
        )
      },
      bottomBar = {
        HrmBottomNavBar(
          currentTab = currentTab,
          onSelectTab = { viewModel.setTab(it) },
          pendingLeavesCount = pendingLeavesCount,
          currentUser = user,
          modifier = Modifier.testTag("hrm_bottom_navigation")
        )
      },
      snackbarHost = { SnackbarHost(snackbarHostState) },
      modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
      Box(modifier = Modifier.padding(innerPadding)) {
        AnimatedContent(
          targetState = Pair(currentTab, user.role),
          transitionSpec = {
            fadeIn(animationSpec = tween(220)) togetherWith fadeOut(animationSpec = tween(180))
          },
          label = "screen_tab_transition"
        ) { (targetTab, targetRole) ->
          when {
            targetRole == UserRole.EMPLOYEE && targetTab == HrmTab.DASHBOARD -> {
              EmployeePortalScreen(
                currentUser = user,
                employees = employees,
                attendanceList = attendanceList,
                leaveRequests = leaveRequests,
                onSelfCheckIn = { viewModel.selfCheckIn(it) },
                onRequestLeave = { viewModel.setShowAddLeaveDialog(true) },
                onRequestDocument = { viewModel.setShowRequestDocumentDialog(true) },
                onNavigateToAnnouncements = { viewModel.setTab(HrmTab.ANNOUNCEMENTS) },
                onNavigateToKpi = { viewModel.setTab(HrmTab.KPI) }
              )
            }

            targetTab == HrmTab.DASHBOARD -> DashboardScreen(
              currentUser = user,
              employees = employees,
              departments = departments,
              attendanceList = attendanceList,
              leaveRequests = leaveRequests,
              onNavigateToTab = { viewModel.setTab(it) },
              onAddEmployeeClick = { viewModel.setShowAddEmployeeDialog(true) },
              onAddLeaveClick = { viewModel.setShowAddLeaveDialog(true) },
              onApproveLeave = { viewModel.approveLeaveRequest(it) },
              onRejectLeave = { viewModel.rejectLeaveRequest(it) }
            )

            targetTab == HrmTab.EMPLOYEES -> EmployeesScreen(
              currentUser = user,
              employees = employees,
              departments = departments,
              searchQuery = employeeSearchQuery,
              departmentFilter = employeeDepartmentFilter,
              sortOption = employeeSortOption,
              onSearchChange = { viewModel.setEmployeeSearchQuery(it) },
              onDepartmentFilterChange = { viewModel.setEmployeeDepartmentFilter(it) },
              onSortOptionChange = { viewModel.setEmployeeSortOption(it) },
              onSelectEmployee = { viewModel.selectEmployee(it) },
              onAddEmployeeClick = { viewModel.setShowAddEmployeeDialog(true) }
            )

            targetTab == HrmTab.ATTENDANCE -> AttendanceScreen(
              currentUser = user,
              attendanceList = attendanceList,
              onUpdateStatus = { empId, status -> viewModel.setAttendanceStatus(empId, status) },
              onMarkAllPresent = { viewModel.markAllPresent() },
              onExportReport = { viewModel.openExportReport("ATTENDANCE") }
            )

            targetTab == HrmTab.LEAVES -> LeavesScreen(
              currentUser = user,
              leaveRequests = leaveRequests,
              selectedFilter = leaveStatusFilter,
              onFilterChange = { viewModel.setLeaveStatusFilter(it) },
              onAddLeaveClick = { viewModel.setShowAddLeaveDialog(true) },
              onApproveLeave = { viewModel.approveLeaveRequest(it) },
              onRejectLeave = { viewModel.rejectLeaveRequest(it) }
            )

            targetTab == HrmTab.PAYROLL -> PayrollScreen(
              currentUser = user,
              employees = employees,
              searchQuery = payrollSearchQuery,
              onSearchChange = { viewModel.setPayrollSearchQuery(it) },
              onEditPayroll = { viewModel.setEditingPayrollEmployee(it) },
              onExportReport = { viewModel.openExportReport("PAYROLL") },
              onNavigateToDocuments = { viewModel.setTab(HrmTab.DOCUMENTS) }
            )

            targetTab == HrmTab.DEPARTMENTS -> DepartmentsScreen(
              currentUser = user,
              departments = departments,
              orgHierarchy = orgHierarchy,
              onAddDepartmentClick = { viewModel.setShowAddDepartmentDialog(true) }
            )

            targetTab == HrmTab.ANNOUNCEMENTS -> AnnouncementsScreen(
              announcements = announcements,
              employees = employees,
              currentUser = user,
              onToggleLike = { viewModel.toggleLikeAnnouncement(it) },
              onSendBirthdayGreeting = { viewModel.sendBirthdayGreeting(it) },
              onAddAnnouncementClick = { viewModel.setShowAddAnnouncementDialog(true) }
            )

            targetTab == HrmTab.DOCUMENTS -> DocumentsScreen(
              documentRequests = documentRequests,
              currentUser = user,
              onRequestDocumentClick = { viewModel.setShowRequestDocumentDialog(true) },
              onApproveDocument = { viewModel.approveDocument(it) },
              onRejectDocument = { viewModel.rejectDocument(it) },
              onViewDocument = { selectedPreviewDoc = it }
            )

            targetTab == HrmTab.KPI -> KpiScreen(
              kpiTasks = kpiTasks,
              currentUser = user,
              onUpdateProgress = { id, prog -> viewModel.updateKpiProgress(id, prog) },
              onRateTask = { id, score -> viewModel.rateKpiTask(id, score) }
            )
          }
        }
      }
    }
  }

  // Dialogs
  if (showAddEmployeeDialog) {
    AddEmployeeDialog(
      departments = departments,
      onDismiss = { viewModel.setShowAddEmployeeDialog(false) },
      onConfirm = { firstName, lastName, position, dept, phone, email, salary ->
        viewModel.addEmployee(firstName, lastName, position, dept, phone, email, salary)
      }
    )
  }

  selectedEmployee?.let { emp ->
    EmployeeDetailModal(
      employee = emp,
      departments = departments,
      currentUser = user,
      onDismiss = { viewModel.selectEmployee(null) },
      onSave = { updated -> viewModel.updateEmployee(updated) },
      onDelete = { id -> viewModel.deleteEmployee(id) }
    )
  }

  if (showAddLeaveDialog) {
    AddLeaveDialog(
      employees = employees,
      onDismiss = { viewModel.setShowAddLeaveDialog(false) },
      onConfirm = { empId, type, start, end, days, reason ->
        viewModel.submitLeaveRequest(empId, type, start, end, days, reason)
      }
    )
  }

  editingPayrollEmployee?.let { emp ->
    EditPayrollDialog(
      employee = emp,
      onDismiss = { viewModel.setEditingPayrollEmployee(null) },
      onSave = { empId, bonus, deduction ->
        viewModel.updatePayroll(empId, bonus, deduction)
      }
    )
  }

  if (showAddDepartmentDialog) {
    AddDepartmentDialog(
      onDismiss = { viewModel.setShowAddDepartmentDialog(false) },
      onConfirm = { name, headName, headPos, budget, desc ->
        viewModel.addDepartment(name, headName, headPos, budget, desc)
      }
    )
  }

  if (showAddAnnouncementDialog) {
    AddAnnouncementDialog(
      onDismiss = { viewModel.setShowAddAnnouncementDialog(false) },
      onConfirm = { title, content, isImportant, celebrationType ->
        viewModel.addAnnouncement(title, content, isImportant, celebrationType)
      }
    )
  }

  if (showRequestDocumentDialog) {
    RequestDocumentDialog(
      onDismiss = { viewModel.setShowRequestDocumentDialog(false) },
      onConfirm = { docType, purpose ->
        viewModel.requestDocument(docType, purpose)
      }
    )
  }

  if (showExportReportDialog) {
    ExportReportDialog(
      reportType = exportReportType,
      employees = employees,
      attendanceList = attendanceList,
      onDismiss = { viewModel.closeExportReport() }
    )
  }

  selectedPreviewDoc?.let { doc ->
    DocumentPreviewDialog(
      request = doc,
      onDismiss = { selectedPreviewDoc = null }
    )
  }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
  Text(text = "HRM Boshqaruv: $name", modifier = modifier)
}
