package com.example.viewmodel

import androidx.lifecycle.ViewModel
import com.example.data.MockHrmData
import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class EmployeeSortOption(val title: String) {
  NAME_ASC("Ism (A-Z)"),
  NAME_DESC("Ism (Z-A)"),
  SALARY_HIGH("Maosh (Yuqori)"),
  SALARY_LOW("Maosh (Quyi)")
}

class HrmViewModel : ViewModel() {

  // Current authenticated user (null means Login screen is shown)
  private val _currentUser = MutableStateFlow<AuthUser?>(null)
  val currentUser: StateFlow<AuthUser?> = _currentUser.asStateFlow()

  private val _currentTab = MutableStateFlow(HrmTab.DASHBOARD)
  val currentTab: StateFlow<HrmTab> = _currentTab.asStateFlow()

  private val _employees = MutableStateFlow(MockHrmData.initialEmployees)
  val employees: StateFlow<List<Employee>> = _employees.asStateFlow()

  private val _attendanceList = MutableStateFlow(MockHrmData.initialAttendance)
  val attendanceList: StateFlow<List<DailyAttendance>> = _attendanceList.asStateFlow()

  private val _leaveRequests = MutableStateFlow(MockHrmData.initialLeaveRequests)
  val leaveRequests: StateFlow<List<LeaveRequest>> = _leaveRequests.asStateFlow()

  private val _departments = MutableStateFlow(MockHrmData.departments)
  val departments: StateFlow<List<Department>> = _departments.asStateFlow()

  private val _orgHierarchy = MutableStateFlow(MockHrmData.orgHierarchy)
  val orgHierarchy: StateFlow<List<OrgNode>> = _orgHierarchy.asStateFlow()

  // Announcements & Feed
  private val _announcements = MutableStateFlow(MockHrmData.announcements)
  val announcements: StateFlow<List<CompanyAnnouncement>> = _announcements.asStateFlow()

  // Document Requests (Spravkalar)
  private val _documentRequests = MutableStateFlow(MockHrmData.documentRequests)
  val documentRequests: StateFlow<List<DocumentRequest>> = _documentRequests.asStateFlow()

  // KPI & Performance
  private val _kpiTasks = MutableStateFlow(MockHrmData.kpiTasks)
  val kpiTasks: StateFlow<List<KpiTask>> = _kpiTasks.asStateFlow()

  // Filters & Search
  private val _employeeSearchQuery = MutableStateFlow("")
  val employeeSearchQuery: StateFlow<String> = _employeeSearchQuery.asStateFlow()

  private val _employeeDepartmentFilter = MutableStateFlow("Barchasi")
  val employeeDepartmentFilter: StateFlow<String> = _employeeDepartmentFilter.asStateFlow()

  private val _employeeSortOption = MutableStateFlow(EmployeeSortOption.NAME_ASC)
  val employeeSortOption: StateFlow<EmployeeSortOption> = _employeeSortOption.asStateFlow()

  private val _leaveStatusFilter = MutableStateFlow("Barchasi")
  val leaveStatusFilter: StateFlow<String> = _leaveStatusFilter.asStateFlow()

  private val _payrollSearchQuery = MutableStateFlow("")
  val payrollSearchQuery: StateFlow<String> = _payrollSearchQuery.asStateFlow()

  // Selected for inspection / dialogs
  private val _selectedEmployee = MutableStateFlow<Employee?>(null)
  val selectedEmployee: StateFlow<Employee?> = _selectedEmployee.asStateFlow()

  private val _editingPayrollEmployee = MutableStateFlow<Employee?>(null)
  val editingPayrollEmployee: StateFlow<Employee?> = _editingPayrollEmployee.asStateFlow()

  private val _showAddEmployeeDialog = MutableStateFlow(false)
  val showAddEmployeeDialog: StateFlow<Boolean> = _showAddEmployeeDialog.asStateFlow()

  private val _showAddLeaveDialog = MutableStateFlow(false)
  val showAddLeaveDialog: StateFlow<Boolean> = _showAddLeaveDialog.asStateFlow()

  private val _showAddDepartmentDialog = MutableStateFlow(false)
  val showAddDepartmentDialog: StateFlow<Boolean> = _showAddDepartmentDialog.asStateFlow()

  private val _showAddAnnouncementDialog = MutableStateFlow(false)
  val showAddAnnouncementDialog: StateFlow<Boolean> = _showAddAnnouncementDialog.asStateFlow()

  private val _showRequestDocumentDialog = MutableStateFlow(false)
  val showRequestDocumentDialog: StateFlow<Boolean> = _showRequestDocumentDialog.asStateFlow()

  private val _showExportReportDialog = MutableStateFlow(false)
  val showExportReportDialog: StateFlow<Boolean> = _showExportReportDialog.asStateFlow()

  private val _exportReportType = MutableStateFlow("PAYROLL") // "PAYROLL" or "ATTENDANCE"
  val exportReportType: StateFlow<String> = _exportReportType.asStateFlow()

  private val _snackbarMessage = MutableStateFlow<String?>(null)
  val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

  fun setTab(tab: HrmTab) {
    _currentTab.value = tab
  }

  fun setEmployeeSearchQuery(query: String) {
    _employeeSearchQuery.value = query
  }

  fun setEmployeeDepartmentFilter(dept: String) {
    _employeeDepartmentFilter.value = dept
  }

  fun setEmployeeSortOption(option: EmployeeSortOption) {
    _employeeSortOption.value = option
  }

  fun setLeaveStatusFilter(status: String) {
    _leaveStatusFilter.value = status
  }

  fun setPayrollSearchQuery(query: String) {
    _payrollSearchQuery.value = query
  }

  fun selectEmployee(employee: Employee?) {
    _selectedEmployee.value = employee
  }

  fun setEditingPayrollEmployee(employee: Employee?) {
    _editingPayrollEmployee.value = employee
  }

  fun setShowAddEmployeeDialog(show: Boolean) {
    _showAddEmployeeDialog.value = show
  }

  fun setShowAddLeaveDialog(show: Boolean) {
    _showAddLeaveDialog.value = show
  }

  fun setShowAddDepartmentDialog(show: Boolean) {
    _showAddDepartmentDialog.value = show
  }

  fun openExportReport(type: String) {
    _exportReportType.value = type
    _showExportReportDialog.value = true
  }

  fun closeExportReport() {
    _showExportReportDialog.value = false
  }

  fun clearSnackbar() {
    _snackbarMessage.value = null
  }

  // Employee Actions
  fun addEmployee(
    firstName: String,
    lastName: String,
    position: String,
    department: String,
    phone: String,
    email: String,
    baseSalary: Long
  ) {
    val newId = "EMP-${String.format("%03d", _employees.value.size + 1)}"
    val avatarColors = listOf(0xFF1E40AF, 0xFF0284C7, 0xFF0D9488, 0xFF7E22CE, 0xFFEA580C, 0xFFDB2777)
    val chosenColor = avatarColors[(_employees.value.size) % avatarColors.size]

    val newEmp = Employee(
      id = newId,
      firstName = firstName.trim(),
      lastName = lastName.trim(),
      position = position.trim(),
      department = department,
      phone = phone.trim(),
      email = email.trim(),
      baseSalary = baseSalary,
      bonus = 0L,
      deduction = 0L,
      status = EmployeeStatus.ACTIVE,
      joinDate = "17.09.2026",
      avatarColorHex = chosenColor
    )

    _employees.update { listOf(newEmp) + it }
    // Add to daily attendance
    _attendanceList.update {
      it + DailyAttendance(newId, newEmp.fullName, department, AttendanceStatus.PRESENT, "09:00")
    }
    // Update department count
    _departments.update { depts ->
      depts.map { if (it.name == department) it.copy(employeeCount = it.employeeCount + 1) else it }
    }
    _snackbarMessage.value = "Yangi xodim ${newEmp.fullName} muvaffaqiyatli qo'shildi!"
    _showAddEmployeeDialog.value = false
  }

  fun updateEmployee(updated: Employee) {
    _employees.update { list ->
      list.map { if (it.id == updated.id) updated else it }
    }
    _selectedEmployee.value = updated
    _snackbarMessage.value = "${updated.fullName} ma'lumotlari yangilandi."
  }

  fun deleteEmployee(id: String) {
    val emp = _employees.value.find { it.id == id }
    _employees.update { list -> list.filterNot { it.id == id } }
    _attendanceList.update { list -> list.filterNot { it.employeeId == id } }
    _selectedEmployee.value = null
    _snackbarMessage.value = "${emp?.fullName ?: "Xodim"} ro'yxatdan o'chirildi."
  }

  // Attendance Actions
  fun setAttendanceStatus(employeeId: String, newStatus: AttendanceStatus) {
    _attendanceList.update { list ->
      list.map { item ->
        if (item.employeeId == employeeId) {
          val time = if (newStatus == AttendanceStatus.PRESENT || newStatus == AttendanceStatus.LATE || newStatus == AttendanceStatus.REMOTE) {
            item.checkInTime ?: "09:00"
          } else null
          item.copy(status = newStatus, checkInTime = time)
        } else item
      }
    }
  }

  fun markAllPresent() {
    _attendanceList.update { list ->
      list.map { it.copy(status = AttendanceStatus.PRESENT, checkInTime = it.checkInTime ?: "09:00") }
    }
    _snackbarMessage.value = "Barcha xodimlarga 'Keldi' holati belgilandi."
  }

  // Leave Actions
  fun submitLeaveRequest(
    employeeId: String,
    leaveType: LeaveType,
    startDate: String,
    endDate: String,
    daysCount: Int,
    reason: String
  ) {
    val emp = _employees.value.find { it.id == employeeId } ?: return
    val newId = "LR-${_leaveRequests.value.size + 101}"
    val newRequest = LeaveRequest(
      id = newId,
      employeeId = emp.id,
      employeeName = emp.fullName,
      department = emp.department,
      leaveType = leaveType,
      startDate = startDate,
      endDate = endDate,
      daysCount = daysCount,
      reason = reason,
      status = LeaveStatus.PENDING,
      requestedAt = "17.09.2026"
    )
    _leaveRequests.update { listOf(newRequest) + it }
    _showAddLeaveDialog.value = false
    _snackbarMessage.value = "Yangi ta'til arizasi yuborildi."
  }

  fun approveLeaveRequest(requestId: String) {
    _leaveRequests.update { list ->
      list.map { req ->
        if (req.id == requestId) req.copy(status = LeaveStatus.APPROVED) else req
      }
    }
    _snackbarMessage.value = "Ariza tasdiqlandi!"
  }

  fun rejectLeaveRequest(requestId: String) {
    _leaveRequests.update { list ->
      list.map { req ->
        if (req.id == requestId) req.copy(status = LeaveStatus.REJECTED) else req
      }
    }
    _snackbarMessage.value = "Ariza rad etildi."
  }

  // Payroll Actions
  fun updatePayroll(employeeId: String, bonus: Long, deduction: Long) {
    _employees.update { list ->
      list.map {
        if (it.id == employeeId) it.copy(bonus = bonus, deduction = deduction) else it
      }
    }
    _editingPayrollEmployee.value = null
    _snackbarMessage.value = "Ish haqi parametrlari saqlandi."
  }

  // Department Actions
  fun addDepartment(name: String, headName: String, headPosition: String, budget: Long, description: String) {
    val newDept = Department(
      id = "dept-${_departments.value.size + 1}",
      name = name.trim(),
      headName = headName.trim(),
      headPosition = headPosition.trim(),
      employeeCount = 0,
      monthlyBudget = budget,
      description = description.trim()
    )
    _departments.update { it + newDept }
    _showAddDepartmentDialog.value = false
    _snackbarMessage.value = "Yangi bo'lim '${newDept.name}' yaratildi."
  }

  // Authentication & Role Management
  fun login(email: String, role: UserRole) {
    val user = when (role) {
      UserRole.DIRECTOR -> AuthUser(
        id = "user-director",
        name = "Jasur Rahimov",
        email = if (email.isNotBlank()) email else "director@hrm.uz",
        role = UserRole.DIRECTOR,
        position = "Bosh direktor / Rahbar"
      )
      UserRole.HR_MANAGER -> AuthUser(
        id = "user-hr",
        name = "Malika Karimova",
        email = if (email.isNotBlank()) email else "hr@hrm.uz",
        role = UserRole.HR_MANAGER,
        position = "HR Menejer / Kadrlar bo'limi boshlig'i"
      )
      UserRole.EMPLOYEE -> {
        val emp = _employees.value.find { it.id == "emp-003" } ?: _employees.value.first()
        AuthUser(
          id = "user-emp-003",
          name = emp.fullName,
          email = if (email.isNotBlank()) email else emp.email,
          role = UserRole.EMPLOYEE,
          position = emp.position,
          employeeId = emp.id
        )
      }
    }
    _currentUser.value = user
    _currentTab.value = HrmTab.DASHBOARD
    _snackbarMessage.value = "Xush kelibsiz, ${user.name} (${user.role.label})!"
  }

  fun loginAsEmployee(employee: Employee) {
    val user = AuthUser(
      id = "user-${employee.id}",
      name = employee.fullName,
      email = employee.email,
      role = UserRole.EMPLOYEE,
      position = employee.position,
      employeeId = employee.id
    )
    _currentUser.value = user
    _currentTab.value = HrmTab.DASHBOARD
    _snackbarMessage.value = "Xush kelibsiz, ${user.name}!"
  }

  fun logout() {
    _currentUser.value = null
    _currentTab.value = HrmTab.DASHBOARD
    _snackbarMessage.value = "Tizimdan muvaffaqiyatli chiqildi."
  }

  // Self attendance check-in for employee
  fun selfCheckIn(status: AttendanceStatus) {
    val empId = _currentUser.value?.employeeId ?: return
    setAttendanceStatus(empId, status)
    _snackbarMessage.value = "Bugungi davomatingiz belgilandi: ${status.label}"
  }

  // Dialog controls
  fun setShowAddAnnouncementDialog(show: Boolean) {
    _showAddAnnouncementDialog.value = show
  }

  fun setShowRequestDocumentDialog(show: Boolean) {
    _showRequestDocumentDialog.value = show
  }

  fun setShowExportReportDialog(show: Boolean, type: String = "PAYROLL") {
    _exportReportType.value = type
    _showExportReportDialog.value = show
  }

  // Announcements actions
  fun toggleLikeAnnouncement(id: String) {
    _announcements.value = _announcements.value.map { ann ->
      if (ann.id == id) {
        val newLiked = !ann.isLikedByMe
        val newCount = if (newLiked) ann.likesCount + 1 else (ann.likesCount - 1).coerceAtLeast(0)
        ann.copy(isLikedByMe = newLiked, likesCount = newCount)
      } else {
        ann
      }
    }
  }

  fun addAnnouncement(
    title: String,
    content: String,
    isImportant: Boolean,
    celebrationType: String?
  ) {
    val author = _currentUser.value ?: return
    val newAnn = CompanyAnnouncement(
      id = "ann-${System.currentTimeMillis()}",
      title = title,
      content = content,
      authorName = author.name,
      authorRole = author.role.label,
      date = "17-Sentabr, 2026",
      isImportant = isImportant,
      likesCount = 0,
      isLikedByMe = false,
      celebrationType = celebrationType
    )
    _announcements.value = listOf(newAnn) + _announcements.value
    _showAddAnnouncementDialog.value = false
    _snackbarMessage.value = "Yangi e'lon muvaffaqiyatli e'lon qilindi!"
  }

  fun sendBirthdayGreeting(employeeName: String) {
    _snackbarMessage.value = "🎉 $employeeName ga jamoaviy tabrik va iliq tilaklar yuborildi!"
  }

  // Documents actions (Spravka)
  fun requestDocument(docType: DocumentType, purpose: String) {
    val user = _currentUser.value ?: return
    val emp = _employees.value.find { it.id == user.employeeId } ?: _employees.value.first()
    val newDoc = DocumentRequest(
      id = "DOC-2026-${(100..999).random()}",
      employeeId = emp.id,
      employeeName = emp.fullName,
      department = emp.department,
      docType = docType,
      purpose = purpose,
      status = DocumentStatus.PENDING,
      requestedAt = "17.09.2026"
    )
    _documentRequests.value = listOf(newDoc) + _documentRequests.value
    _showRequestDocumentDialog.value = false
    _snackbarMessage.value = "${docType.label} uchun so'rov muvaffaqiyatli yuborildi!"
  }

  fun approveDocument(id: String) {
    val certNumber = "HRM-2026-0${(800..999).random()}"
    _documentRequests.value = _documentRequests.value.map { doc ->
      if (doc.id == id) {
        doc.copy(status = DocumentStatus.APPROVED, certificateNumber = certNumber)
      } else {
        doc
      }
    }
    _snackbarMessage.value = "Hujjat tasdiqlandi va QR-kodli elektron blank ($certNumber) berildi!"
  }

  fun rejectDocument(id: String) {
    _documentRequests.value = _documentRequests.value.map { doc ->
      if (doc.id == id) {
        doc.copy(status = DocumentStatus.REJECTED)
      } else {
        doc
      }
    }
    _snackbarMessage.value = "Hujjat so'rovi rad etildi."
  }

  // KPI actions
  fun updateKpiProgress(id: String, progress: Int) {
    val clamped = progress.coerceIn(0, 100)
    _kpiTasks.value = _kpiTasks.value.map { kpi ->
      if (kpi.id == id) {
        val newStatus = if (clamped == 100) "Bajarildi" else "Jarayonda"
        kpi.copy(progressPercent = clamped, status = newStatus)
      } else {
        kpi
      }
    }
    _snackbarMessage.value = "KPI progress yangilandi: $clamped%"
  }

  fun rateKpiTask(id: String, score: Float) {
    _kpiTasks.value = _kpiTasks.value.map { kpi ->
      if (kpi.id == id) {
        kpi.copy(score = score, status = "Tasdiqlandi")
      } else {
        kpi
      }
    }
    _snackbarMessage.value = "KPI vazifasi baholandi: $score / 5.0"
  }
}
