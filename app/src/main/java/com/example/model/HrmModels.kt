package com.example.model

enum class HrmTab(val title: String, val uzbekTitle: String) {
  DASHBOARD("Dashboard", "Boshqaruv paneli"),
  EMPLOYEES("Employees", "Xodimlar"),
  ATTENDANCE("Attendance", "Davomat"),
  LEAVES("Leave Requests", "Arizalar"),
  PAYROLL("Payroll", "Ish haqi"),
  DEPARTMENTS("Departments", "Bo'limlar"),
  ANNOUNCEMENTS("Announcements", "E'lonlar & Yangiliklar"),
  DOCUMENTS("Documents", "Hujjatlar & Spravka"),
  KPI("KPI", "KPI & Baholash")
}

enum class EmployeeStatus(val label: String) {
  ACTIVE("Faol"),
  ON_LEAVE("Ta'tilda"),
  PROBATION("Sinov muddati"),
  TERMINATED("Nofaol")
}

data class Employee(
  val id: String,
  val firstName: String,
  val lastName: String,
  val position: String,
  val department: String,
  val phone: String,
  val email: String,
  val baseSalary: Long, // in UZS
  val bonus: Long = 0L,
  val deduction: Long = 0L,
  val status: EmployeeStatus = EmployeeStatus.ACTIVE,
  val joinDate: String,
  val avatarColorHex: Long = 0xFF1E40AF,
  val birthday: String = "15-Avgust",
  val isBirthdayToday: Boolean = false
) {
  val fullName: String get() = "$firstName $lastName"
  val netSalary: Long get() = baseSalary + bonus - deduction
}

enum class AttendanceStatus(val label: String, val shortLabel: String) {
  PRESENT("Keldi", "K"),
  LATE("Kechikdi", "Kch"),
  ABSENT("Kelmadi", "Y"),
  REMOTE("Masofaviy", "M"),
  ON_LEAVE("Ta'tilda", "T")
}

data class DailyAttendance(
  val employeeId: String,
  val employeeName: String,
  val department: String,
  val status: AttendanceStatus,
  val checkInTime: String? = null,
  val checkOutTime: String? = null,
  val date: String = "2026-09-17"
)

enum class LeaveType(val label: String) {
  ANNUAL("Yillik mehnat ta'tili"),
  SICK("Kasallik ta'tili"),
  UNPAID("O'z hisobidan ta'til"),
  MATERNITY("Dekret / Bola parvarishi"),
  STUDY("O'qish ta'tili")
}

enum class LeaveStatus(val label: String) {
  PENDING("Kutilmoqda"),
  APPROVED("Tasdiqlandi"),
  REJECTED("Rad etildi")
}

data class LeaveRequest(
  val id: String,
  val employeeId: String,
  val employeeName: String,
  val department: String,
  val leaveType: LeaveType,
  val startDate: String,
  val endDate: String,
  val daysCount: Int,
  val reason: String,
  val status: LeaveStatus = LeaveStatus.PENDING,
  val requestedAt: String
)

data class Department(
  val id: String,
  val name: String,
  val headName: String,
  val headPosition: String,
  val employeeCount: Int,
  val monthlyBudget: Long,
  val description: String
)

data class OrgNode(
  val id: String,
  val name: String,
  val role: String,
  val department: String,
  val reportsToId: String? = null,
  val subRolesCount: Int = 0
)

enum class UserRole(val label: String) {
  DIRECTOR("Rahbar / Direktor"),
  HR_MANAGER("HR Menejer"),
  EMPLOYEE("Xodim")
}

data class AuthUser(
  val id: String,
  val name: String,
  val email: String,
  val role: UserRole,
  val position: String,
  val employeeId: String? = null
)

// Announcements & News
data class CompanyAnnouncement(
  val id: String,
  val title: String,
  val content: String,
  val authorName: String,
  val authorRole: String,
  val date: String,
  val isImportant: Boolean = false,
  val likesCount: Int = 0,
  val isLikedByMe: Boolean = false,
  val celebrationType: String? = null // "BIRTHDAY", "NEW_HIRE", "ACHIEVEMENT", "GENERAL"
)

// Document Requests (Spravka)
enum class DocumentType(val label: String, val description: String) {
  WORK_CERTIFICATE("Ish joyidan ma'lumotnoma", "Ishlayotganligi va egallab turgan lavozimi tasdig'i"),
  SALARY_CERTIFICATE("Daromad ma'lumotnomasi", "So'nggi 6 oylik ish haqi hisoboti (Bank/Kredit uchun)"),
  EMBASSY_LETTER("Viza / Elchixona ma'lumotnomasi", "Chet el safarlari va viza markazlari uchun rasmiy tasdiqnoma"),
  EXPERIENCE_CERTIFICATE("Ish staji ma'lumotnomasi", "Tashkilotdagi umumiy mehnat staji ko'chirmasi")
}

enum class DocumentStatus(val label: String) {
  PENDING("Kutilmoqda"),
  APPROVED("Tayyorlandi (QR-kodli)"),
  REJECTED("Rad etildi")
}

data class DocumentRequest(
  val id: String,
  val employeeId: String,
  val employeeName: String,
  val department: String,
  val docType: DocumentType,
  val purpose: String,
  val status: DocumentStatus,
  val requestedAt: String,
  val certificateNumber: String? = null
)

// KPI & Performance
data class KpiTask(
  val id: String,
  val employeeId: String,
  val employeeName: String,
  val department: String,
  val title: String,
  val targetDescription: String,
  val progressPercent: Int, // 0..100
  val score: Float = 0f, // 1.0 to 5.0
  val status: String = "Jarayonda" // "Jarayonda", "Bajarildi", "Tasdiqlandi"
)
