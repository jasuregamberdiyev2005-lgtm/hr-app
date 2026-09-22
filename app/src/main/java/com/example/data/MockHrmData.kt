package com.example.data

import com.example.model.*

object MockHrmData {

  val departments = listOf(
    Department(
      id = "dept-1",
      name = "IT & Dasturlash",
      headName = "Jasur Rahimov",
      headPosition = "Bosh Texnologik Rahbar (CTO)",
      employeeCount = 4,
      monthlyBudget = 72_000_000L,
      description = "Mobil ilovalar, veb platformalar va server infratuzilmasini ishlab chiqish"
    ),
    Department(
      id = "dept-2",
      name = "HR & Kadrlar",
      headName = "Malika Karimova",
      headPosition = "HR Direktor",
      employeeCount = 2,
      monthlyBudget = 26_000_000L,
      description = "Iste'dodlarni jalb qilish, korporativ madaniyat va xodimlar rivojlanishi"
    ),
    Department(
      id = "dept-3",
      name = "Marketing & PR",
      headName = "Bekzod Umarov",
      headPosition = "Marketing Bo'limi Boshlig'i",
      employeeCount = 1,
      monthlyBudget = 18_000_000L,
      description = "Raqamli marketing, brending va mijozlar bilan aloqalar"
    ),
    Department(
      id = "dept-4",
      name = "Moliya & Buxgalteriya",
      headName = "Shahnoza Qodirova",
      headPosition = "Bosh Hisobchi (CFO)",
      employeeCount = 1,
      monthlyBudget = 16_000_000L,
      description = "Moliyaviy hisobotlar, ish haqi hisob-kitoblari va soliq auditlari"
    ),
    Department(
      id = "dept-5",
      name = "Sotuv & Mijozlar",
      headName = "Bobur Mirzayev",
      headPosition = "Sotuv Bo'limi Menejeri",
      employeeCount = 2,
      monthlyBudget = 22_000_000L,
      description = "Korporativ savdo, B2B kelishuvlar va mijozlarni qo'llab-quvvatlash"
    )
  )

  val initialEmployees = listOf(
    Employee(
      id = "EMP-001",
      firstName = "Jasur",
      lastName = "Rahimov",
      position = "Bosh direktor & CTO",
      department = "IT & Dasturlash",
      phone = "+998 90 123 45 67",
      email = "jasur.rahimov@hrm.uz",
      baseSalary = 24_000_000L,
      bonus = 2_000_000L,
      deduction = 0L,
      status = EmployeeStatus.ACTIVE,
      joinDate = "15.01.2022",
      avatarColorHex = 0xFF1E40AF
    ),
    Employee(
      id = "EMP-002",
      firstName = "Malika",
      lastName = "Karimova",
      position = "HR Direktor",
      department = "HR & Kadrlar",
      phone = "+998 93 234 56 78",
      email = "malika.k@hrm.uz",
      baseSalary = 16_000_000L,
      bonus = 1_500_000L,
      deduction = 0L,
      status = EmployeeStatus.ACTIVE,
      joinDate = "01.03.2022",
      avatarColorHex = 0xFF7E22CE
    ),
    Employee(
      id = "EMP-003",
      firstName = "Sardor",
      lastName = "Aliyev",
      position = "Senior Kotlin Developer",
      department = "IT & Dasturlash",
      phone = "+998 97 345 67 89",
      email = "sardor.a@hrm.uz",
      baseSalary = 19_000_000L,
      bonus = 1_000_000L,
      deduction = 200_000L,
      status = EmployeeStatus.ACTIVE,
      joinDate = "10.06.2022",
      avatarColorHex = 0xFF0284C7
    ),
    Employee(
      id = "EMP-004",
      firstName = "Nilufar",
      lastName = "Usmonova",
      position = "UI/UX Dizayner",
      department = "IT & Dasturlash",
      phone = "+998 94 456 78 90",
      email = "nilufar.u@hrm.uz",
      baseSalary = 14_000_000L,
      bonus = 800_000L,
      deduction = 0L,
      status = EmployeeStatus.ACTIVE,
      joinDate = "18.09.2022",
      avatarColorHex = 0xFFDB2777,
      birthday = "17-Sentabr",
      isBirthdayToday = true
    ),
    Employee(
      id = "EMP-005",
      firstName = "Bekzod",
      lastName = "Umarov",
      position = "Marketing Boshlig'i",
      department = "Marketing & PR",
      phone = "+998 99 567 89 01",
      email = "bekzod.u@hrm.uz",
      baseSalary = 15_000_000L,
      bonus = 1_200_000L,
      deduction = 150_000L,
      status = EmployeeStatus.ACTIVE,
      joinDate = "05.11.2022",
      avatarColorHex = 0xFFEA580C
    ),
    Employee(
      id = "EMP-006",
      firstName = "Shahnoza",
      lastName = "Qodirova",
      position = "Bosh Hisobchi",
      department = "Moliya & Buxgalteriya",
      phone = "+998 91 678 90 12",
      email = "shahnoza.q@hrm.uz",
      baseSalary = 15_500_000L,
      bonus = 500_000L,
      deduction = 0L,
      status = EmployeeStatus.ACTIVE,
      joinDate = "12.01.2023",
      avatarColorHex = 0xFF0D9488
    ),
    Employee(
      id = "EMP-007",
      firstName = "Bobur",
      lastName = "Mirzayev",
      position = "Sotuv Menejeri",
      department = "Sotuv & Savdo",
      phone = "+998 95 789 01 23",
      email = "bobur.m@hrm.uz",
      baseSalary = 11_000_000L,
      bonus = 2_500_000L,
      deduction = 0L,
      status = EmployeeStatus.ACTIVE,
      joinDate = "20.02.2023",
      avatarColorHex = 0xFF16A34A
    ),
    Employee(
      id = "EMP-008",
      firstName = "Dildora",
      lastName = "Saidova",
      position = "Frontend Dasturchi",
      department = "IT & Dasturlash",
      phone = "+998 93 890 12 34",
      email = "dildora.s@hrm.uz",
      baseSalary = 13_000_000L,
      bonus = 600_000L,
      deduction = 0L,
      status = EmployeeStatus.ON_LEAVE,
      joinDate = "15.04.2023",
      avatarColorHex = 0xFF9333EA
    ),
    Employee(
      id = "EMP-009",
      firstName = "Azizbek",
      lastName = "Toirov",
      position = "DevOps Muhandis",
      department = "IT & Dasturlash",
      phone = "+998 90 901 23 45",
      email = "azizbek.t@hrm.uz",
      baseSalary = 17_500_000L,
      bonus = 900_000L,
      deduction = 0L,
      status = EmployeeStatus.ACTIVE,
      joinDate = "01.07.2023",
      avatarColorHex = 0xFF4F46E5
    ),
    Employee(
      id = "EMP-010",
      firstName = "Madina",
      lastName = "Ahmedova",
      position = "Kadrlar bo'yicha mutaxassis",
      department = "HR & Kadrlar",
      phone = "+998 97 012 34 56",
      email = "madina.a@hrm.uz",
      baseSalary = 9_500_000L,
      bonus = 400_000L,
      deduction = 0L,
      status = EmployeeStatus.PROBATION,
      joinDate = "01.08.2024",
      avatarColorHex = 0xFF0891B2
    )
  )

  val initialAttendance = listOf(
    DailyAttendance("EMP-001", "Jasur Rahimov", "IT & Dasturlash", AttendanceStatus.PRESENT, "08:45"),
    DailyAttendance("EMP-002", "Malika Karimova", "HR & Kadrlar", AttendanceStatus.PRESENT, "08:55"),
    DailyAttendance("EMP-003", "Sardor Aliyev", "IT & Dasturlash", AttendanceStatus.LATE, "09:22"),
    DailyAttendance("EMP-004", "Nilufar Usmonova", "IT & Dasturlash", AttendanceStatus.PRESENT, "08:50"),
    DailyAttendance("EMP-005", "Bekzod Umarov", "Marketing & PR", AttendanceStatus.REMOTE, "09:00"),
    DailyAttendance("EMP-006", "Shahnoza Qodirova", "Moliya & Buxgalteriya", AttendanceStatus.PRESENT, "08:35"),
    DailyAttendance("EMP-007", "Bobur Mirzayev", "Sotuv & Savdo", AttendanceStatus.PRESENT, "09:05"),
    DailyAttendance("EMP-008", "Dildora Saidova", "IT & Dasturlash", AttendanceStatus.ON_LEAVE, null),
    DailyAttendance("EMP-009", "Azizbek Toirov", "IT & Dasturlash", AttendanceStatus.PRESENT, "08:40"),
    DailyAttendance("EMP-010", "Madina Ahmedova", "HR & Kadrlar", AttendanceStatus.ABSENT, null)
  )

  val initialLeaveRequests = listOf(
    LeaveRequest(
      id = "LR-101",
      employeeId = "EMP-008",
      employeeName = "Dildora Saidova",
      department = "IT & Dasturlash",
      leaveType = LeaveType.ANNUAL,
      startDate = "15.09.2026",
      endDate = "25.09.2026",
      daysCount = 10,
      reason = "Yillik rejalashtirilgan mehnat ta'tili",
      status = LeaveStatus.APPROVED,
      requestedAt = "10.09.2026"
    ),
    LeaveRequest(
      id = "LR-102",
      employeeId = "EMP-003",
      employeeName = "Sardor Aliyev",
      department = "IT & Dasturlash",
      leaveType = LeaveType.SICK,
      startDate = "18.09.2026",
      endDate = "20.09.2026",
      daysCount = 3,
      reason = "Shifokor ko'rigi va sog'lomlashtirish",
      status = LeaveStatus.PENDING,
      requestedAt = "16.09.2026"
    ),
    LeaveRequest(
      id = "LR-103",
      employeeId = "EMP-005",
      employeeName = "Bekzod Umarov",
      department = "Marketing & PR",
      leaveType = LeaveType.STUDY,
      startDate = "22.09.2026",
      endDate = "26.09.2026",
      daysCount = 5,
      reason = "Xalqaro Marketing Konferensiyasida qatnashish",
      status = LeaveStatus.PENDING,
      requestedAt = "16.09.2026"
    ),
    LeaveRequest(
      id = "LR-104",
      employeeId = "EMP-010",
      employeeName = "Madina Ahmedova",
      department = "HR & Kadrlar",
      leaveType = LeaveType.UNPAID,
      startDate = "05.09.2026",
      endDate = "06.09.2026",
      daysCount = 2,
      reason = "Oilaviy marosim sababli",
      status = LeaveStatus.APPROVED,
      requestedAt = "01.09.2026"
    ),
    LeaveRequest(
      id = "LR-105",
      employeeId = "EMP-007",
      employeeName = "Bobur Mirzayev",
      department = "Sotuv & Savdo",
      leaveType = LeaveType.ANNUAL,
      startDate = "01.08.2026",
      endDate = "05.08.2026",
      daysCount = 5,
      reason = "Shoshilinch dam olish",
      status = LeaveStatus.REJECTED,
      requestedAt = "28.07.2026"
    )
  )

  val orgHierarchy = listOf(
    OrgNode("org-1", "Jasur Rahimov", "Bosh direktor & Texnik rahbar (CEO/CTO)", "Rahbariyat", null, 4),
    OrgNode("org-2", "Malika Karimova", "HR Direktor", "HR & Kadrlar", "org-1", 1),
    OrgNode("org-3", "Shahnoza Qodirova", "Bosh Hisobchi (CFO)", "Moliya", "org-1", 0),
    OrgNode("org-4", "Bekzod Umarov", "Marketing Rahbari", "Marketing", "org-1", 0),
    OrgNode("org-5", "Bobur Mirzayev", "Sotuv Rahbari", "Sotuv", "org-1", 0),
    OrgNode("org-6", "Sardor Aliyev", "Lead Kotlin Engineer", "IT & Dasturlash", "org-1", 3),
    OrgNode("org-7", "Nilufar Usmonova", "Lead UI/UX Dizayner", "IT & Dasturlash", "org-6", 0),
    OrgNode("org-8", "Dildora Saidova", "Frontend Engineer", "IT & Dasturlash", "org-6", 0),
    OrgNode("org-9", "Azizbek Toirov", "DevOps Engineer", "IT & Dasturlash", "org-6", 0),
    OrgNode("org-10", "Madina Ahmedova", "Recruiter / Kadrlar mutaxassisi", "HR & Kadrlar", "org-2", 0)
  )

  val announcements = listOf(
    CompanyAnnouncement(
      id = "ann-1",
      title = "🎂 Bugun Nilufar Usmonovaning tavallud kuni!",
      content = "Jamoamizning mohir yetakchi UI/UX dizayneri Nilufar Usmonovani bugungi tavallud ayyomi bilan qizg'in tabriklaymiz! Mustahkam sog'liq, ulkan ijodiy parvozlar tilaymiz!",
      authorName = "Malika Karimova",
      authorRole = "HR Direktor",
      date = "17-Sentabr, 2026",
      isImportant = true,
      likesCount = 9,
      isLikedByMe = true,
      celebrationType = "BIRTHDAY"
    ),
    CompanyAnnouncement(
      id = "ann-2",
      title = "🚀 Korporativ yangi avlod mobil ilovasi ishga tushirildi",
      content = "Kompaniyamizning barcha ichki jarayonlarini avtomatlashtiruvchi HRM tizimi muvaffaqiyatli sinovdan o'tdi. Barcha xodimlardan mobil portal orqali davomat va arizalarni yuborish so'raladi.",
      authorName = "Jasur Rahimov",
      authorRole = "Bosh direktor & CTO",
      date = "16-Sentabr, 2026",
      isImportant = true,
      likesCount = 14,
      isLikedByMe = false,
      celebrationType = "ACHIEVEMENT"
    ),
    CompanyAnnouncement(
      id = "ann-3",
      title = "🌴 1-Oktabr 'Ustoz va Murabbiylar kuni' dam olish tartibi",
      content = "O'zbekiston Respublikasi Mehnat kodeksiga muvofiq, 1-Oktabr barcha xodimlar uchun to'liq dam olish kuni hisoblanadi. Ish vaqti 2-Oktabrdan odatiy tartibda davom etadi.",
      authorName = "Malika Karimova",
      authorRole = "HR Direktor",
      date = "15-Sentabr, 2026",
      isImportant = false,
      likesCount = 8,
      isLikedByMe = false,
      celebrationType = "GENERAL"
    )
  )

  val documentRequests = listOf(
    DocumentRequest(
      id = "DOC-2026-001",
      employeeId = "EMP-003",
      employeeName = "Sardor Aliyev",
      department = "IT & Dasturlash",
      docType = DocumentType.WORK_CERTIFICATE,
      purpose = "Ipoteka krediti rasmiylashtirish uchun bankka",
      status = DocumentStatus.APPROVED,
      requestedAt = "15.09.2026",
      certificateNumber = "HRM-2026-0891"
    ),
    DocumentRequest(
      id = "DOC-2026-002",
      employeeId = "EMP-008",
      employeeName = "Dildora Saidova",
      department = "IT & Dasturlash",
      docType = DocumentType.SALARY_CERTIFICATE,
      purpose = "Avtokredit olish maqsadida daromad tasdig'i",
      status = DocumentStatus.PENDING,
      requestedAt = "17.09.2026"
    ),
    DocumentRequest(
      id = "DOC-2026-003",
      employeeId = "EMP-001",
      employeeName = "Jasur Rahimov",
      department = "IT & Dasturlash",
      docType = DocumentType.EMBASSY_LETTER,
      purpose = "Germaniya elchixonasi vizasi uchun xizmat safari xati",
      status = DocumentStatus.APPROVED,
      requestedAt = "10.09.2026",
      certificateNumber = "HRM-2026-0840"
    )
  )

  val kpiTasks = listOf(
    KpiTask(
      id = "kpi-1",
      employeeId = "EMP-003",
      employeeName = "Sardor Aliyev",
      department = "IT & Dasturlash",
      title = "Jetpack Compose migratsiyasi va modullashtirish",
      targetDescription = "Ilovaning 80% dan ortiq ekranlarini to'liq zamonaviy Compose'ga o'tkazish",
      progressPercent = 85,
      score = 4.8f,
      status = "Jarayonda"
    ),
    KpiTask(
      id = "kpi-2",
      employeeId = "EMP-004",
      employeeName = "Nilufar Usmonova",
      department = "IT & Dasturlash",
      title = "Korporativ UI Design System v2.0 ishlab chiqish",
      targetDescription = "Figma komponentlar kutubxonasi va ranglar palitrasini yakunlash",
      progressPercent = 95,
      score = 4.9f,
      status = "Bajarildi"
    ),
    KpiTask(
      id = "kpi-3",
      employeeId = "EMP-005",
      employeeName = "Bekzod Umarov",
      department = "Marketing & PR",
      title = "Q3 Raqamli marketing va lead generatsiya",
      targetDescription = "B2B mijozlar oqimini 25% ga oshirish va reklama ROI'ni yaxshilash",
      progressPercent = 70,
      score = 4.5f,
      status = "Jarayonda"
    ),
    KpiTask(
      id = "kpi-4",
      employeeId = "EMP-008",
      employeeName = "Dildora Saidova",
      department = "IT & Dasturlash",
      title = "Unit va UI testlar qamrovini 75% ga yetkazish",
      targetDescription = "Avtomatlashgan testlar orqali barqarorlikni ta'minlash",
      progressPercent = 60,
      score = 4.2f,
      status = "Jarayonda"
    ),
    KpiTask(
      id = "kpi-5",
      employeeId = "EMP-010",
      employeeName = "Madina Ahmedova",
      department = "HR & Kadrlar",
      title = "Yangi 3 nafar Senior dasturchini yollash",
      targetDescription = "Kadrlar rekrutingini 1 oy ichida to'liq yakunlash",
      progressPercent = 100,
      score = 5.0f,
      status = "Tasdiqlandi"
    )
  )
}
