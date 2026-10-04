/**
 * JAVAC HEALTH HMS - APPLICATION LOGIC & STATE ENGINE
 * Provides interactive CRUD, appointment booking with transaction simulation,
 * live search, role switching, EHR dossier inspection, pharmacy restocking,
 * and JDBC SQL query runner.
 */

// ==========================================
// 1. DATA STATE (Seeded from database.sql)
// ==========================================

const HMS_STATE = {
  currentUserRole: 'admin', // 'admin' | 'doctor' | 'patient'
  
  patients: [
    {
      id: 101,
      name: "Eleanor Vance",
      age: 42,
      gender: "Female",
      blood_group: "O+",
      phone: "+1 (555) 234-8901",
      email: "eleanor.vance@example.com",
      address: "742 Evergreen Terrace, Springfield",
      emergency_contact: "Robert Vance (Spouse) - +1 (555) 987-6543",
      status: "Outpatient",
      created_at: "2026-08-14"
    },
    {
      id: 102,
      name: "Arthur Pendelton",
      age: 67,
      gender: "Male",
      blood_group: "A-",
      phone: "+1 (555) 345-6789",
      email: "arthur.p@example.com",
      address: "12 Baker Street, Apt 4B",
      emergency_contact: "Grace Pendelton (Daughter) - +1 (555) 876-5432",
      status: "Inpatient",
      created_at: "2026-09-02"
    },
    {
      id: 103,
      name: "Rajesh Kumar",
      age: 35,
      gender: "Male",
      blood_group: "B+",
      phone: "+1 (555) 456-7890",
      email: "rajesh.kumar@example.com",
      address: "88 Orchid Highway, Metro City",
      emergency_contact: "Priya Kumar (Wife) - +1 (555) 765-4321",
      status: "Outpatient",
      created_at: "2026-09-18"
    },
    {
      id: 104,
      name: "Sophia Martinez",
      age: 29,
      gender: "Female",
      blood_group: "O-",
      phone: "+1 (555) 567-8901",
      email: "sophia.m@example.com",
      address: "450 Sunset Blvd, Los Angeles",
      emergency_contact: "Carlos Martinez (Brother) - +1 (555) 654-3210",
      status: "Discharged",
      created_at: "2026-09-25"
    },
    {
      id: 105,
      name: "David Chen",
      age: 51,
      gender: "Male",
      blood_group: "AB+",
      phone: "+1 (555) 678-9012",
      email: "david.chen@example.com",
      address: "900 Silicon Way, San Jose",
      emergency_contact: "Helen Chen (Wife) - +1 (555) 543-2109",
      status: "Outpatient",
      created_at: "2026-10-01"
    },
    {
      id: 106,
      name: "Amara Okafor",
      age: 24,
      gender: "Female",
      blood_group: "A+",
      phone: "+1 (555) 789-0123",
      email: "amara.o@example.com",
      address: "310 University Avenue, Boston",
      emergency_contact: "Chidi Okafor (Father) - +1 (555) 432-1098",
      status: "Inpatient",
      created_at: "2026-10-02"
    }
  ],

  doctors: [
    {
      id: 1,
      name: "Dr. Sarah Smith",
      specialization: "Cardiology",
      phone: "+1 (555) 901-2345",
      email: "sarah.smith@javachealth.org",
      fee: 180.00,
      available: true,
      photo: "assets/doctor_sarah.jpg",
      department: "Cardiovascular Institute",
      experience: "14 Years Exp.",
      rating: 4.9,
      badge: "Chief Physician",
      room: "Suite 302"
    },
    {
      id: 2,
      name: "Dr. James Wilson",
      specialization: "Neurosurgery",
      phone: "+1 (555) 902-3456",
      email: "james.wilson@javachealth.org",
      fee: 250.00,
      available: true,
      photo: "assets/doctor_james.jpg",
      department: "Brain & Spine Surgery",
      experience: "22 Years Exp.",
      rating: 5.0,
      badge: "Senior Surgeon",
      room: "Surgical Suite 4B"
    },
    {
      id: 3,
      name: "Dr. Elena Rostova",
      specialization: "Pediatrics",
      phone: "+1 (555) 903-4567",
      email: "elena.rostova@javachealth.org",
      fee: 120.00,
      available: true,
      photo: "https://images.unsplash.com/photo-1594824813576-96b6b7d72242?w=500&auto=format&fit=crop&q=80",
      department: "Child Health Center",
      experience: "9 Years Exp.",
      rating: 4.8,
      badge: "Pediatric Lead",
      room: "Clinic 108"
    },
    {
      id: 4,
      name: "Dr. Marcus Brody",
      specialization: "Orthopedics",
      phone: "+1 (555) 904-5678",
      email: "marcus.brody@javachealth.org",
      fee: 160.00,
      available: false,
      photo: "https://images.unsplash.com/photo-1622253692010-333f2da6031d?w=500&auto=format&fit=crop&q=80",
      department: "Bone & Joint Center",
      experience: "11 Years Exp.",
      rating: 4.7,
      badge: "In Surgery",
      room: "OR Room 3"
    }
  ],

  appointments: [
    {
      id: 501,
      patient_id: 101,
      doctor_id: 1,
      patient_name: "Eleanor Vance",
      doctor_name: "Dr. Sarah Smith",
      specialty: "Cardiology",
      appointment_date: "2026-10-04",
      appointment_time: "09:00:00",
      fee: "$180.00",
      status: "Scheduled",
      notes: "Follow-up ECG and blood pressure review"
    },
    {
      id: 502,
      patient_id: 102,
      doctor_id: 2,
      patient_name: "Arthur Pendelton",
      doctor_name: "Dr. James Wilson",
      specialty: "Neurosurgery",
      appointment_date: "2026-10-04",
      appointment_time: "10:30:00",
      fee: "$250.00",
      status: "Scheduled",
      notes: "Post-op cervical spine evaluation"
    },
    {
      id: 503,
      patient_id: 103,
      doctor_id: 3,
      patient_name: "Rajesh Kumar",
      doctor_name: "Dr. Elena Rostova",
      specialty: "Pediatrics",
      appointment_date: "2026-10-04",
      appointment_time: "11:45:00",
      fee: "$120.00",
      status: "Scheduled",
      notes: "Annual pediatric allergy screening for infant"
    },
    {
      id: 504,
      patient_id: 105,
      doctor_id: 1,
      patient_name: "David Chen",
      doctor_name: "Dr. Sarah Smith",
      specialty: "Cardiology",
      appointment_date: "2026-10-03",
      appointment_time: "14:00:00",
      fee: "$180.00",
      status: "Completed",
      notes: "Routine stress test clearance"
    },
    {
      id: 505,
      patient_id: 106,
      doctor_id: 4,
      patient_name: "Amara Okafor",
      doctor_name: "Dr. Marcus Brody",
      specialty: "Orthopedics",
      appointment_date: "2026-10-02",
      appointment_time: "16:15:00",
      fee: "$160.00",
      status: "Completed",
      notes: "Anterior cruciate ligament MRI review"
    },
    {
      id: 506,
      patient_id: 104,
      doctor_id: 2,
      patient_name: "Sophia Martinez",
      doctor_name: "Dr. James Wilson",
      specialty: "Neurosurgery",
      appointment_date: "2026-10-01",
      appointment_time: "15:00:00",
      fee: "$250.00",
      status: "Cancelled",
      notes: "Patient rescheduled due to travel conflict"
    }
  ],

  medical_records: [
    {
      id: 801,
      patient_id: 101,
      patient_name: "Eleanor Vance",
      doctor_name: "Dr. Sarah Smith",
      visit_date: "2026-09-28",
      symptoms: "Occasional chest tightness, palpitations during mild exertion",
      diagnosis: "Mild Sinus Tachycardia, Stage 1 Essential Hypertension",
      treatment: "Prescribed Metoprolol 25mg daily. Low-sodium diet.",
      notes: "Holter monitor study recommended if palpitations persist."
    },
    {
      id: 802,
      patient_id: 102,
      patient_name: "Arthur Pendelton",
      doctor_name: "Dr. James Wilson",
      visit_date: "2026-09-15",
      symptoms: "Cervical radiculopathy, numbness radiating to right forearm",
      diagnosis: "C5-C6 Disc Herniation with moderate foraminal stenosis",
      treatment: "Anterior Cervical Discectomy and Fusion (ACDF) performed. Excellent recovery.",
      notes: "Physical therapy scheduled 3x weekly."
    },
    {
      id: 803,
      patient_id: 105,
      patient_name: "David Chen",
      doctor_name: "Dr. Sarah Smith",
      visit_date: "2026-10-01",
      symptoms: "Executive health checkup, mild shortness of breath upon stair climbing",
      diagnosis: "Borderline hyperlipidemia, normal left ventricular ejection fraction (62%)",
      treatment: "Atorvastatin 10mg nightly. Cardio aerobic program 4x weekly.",
      notes: "Re-check lipid panel in 90 days."
    }
  ],

  medicines: [
    { id: 201, name: "Amoxicillin 500mg", category: "Antibiotic", quantity: 15, price: 12.50, expiry: "2027-04-15" },
    { id: 202, name: "Metoprolol Succinate 25mg", category: "Cardiovascular", quantity: 180, price: 18.00, expiry: "2027-11-20" },
    { id: 203, name: "Atorvastatin 20mg", category: "Lipid Lowering", quantity: 240, price: 24.50, expiry: "2028-02-10" },
    { id: 204, name: "Paracetamol IV Infusion 1000mg", category: "Analgesic", quantity: 8, price: 15.00, expiry: "2026-12-30" },
    { id: 205, name: "Propofol 1% Emulsion 20ml", category: "Anesthetic", quantity: 45, price: 38.00, expiry: "2027-06-18" },
    { id: 206, name: "Insulin Glargine 100U/ml", category: "Antidiabetic", quantity: 60, price: 72.00, expiry: "2027-08-05" }
  ],

  bills: [
    { id: 9001, patient_id: 101, patient_name: "Eleanor Vance", description: "Cardiology Consultation & Resting 12-Lead ECG", amount: 280.00, payment_status: "Paid", bill_date: "2026-09-28" },
    { id: 9002, patient_id: 102, patient_name: "Arthur Pendelton", description: "ACDF Surgery Hospital Stay & OR Anesthesia", amount: 8450.00, payment_status: "Pending", bill_date: "2026-09-16" },
    { id: 9003, patient_id: 103, patient_name: "Rajesh Kumar", description: "Pediatric Consultation & Allergy Panel", amount: 195.00, payment_status: "Paid", bill_date: "2026-09-20" },
    { id: 9004, patient_id: 105, patient_name: "David Chen", description: "Executive Cardiac Stress Test & Blood Chemistry", amount: 420.00, payment_status: "Pending", bill_date: "2026-10-01" },
    { id: 9005, patient_id: 106, patient_name: "Amara Okafor", description: "Orthopedic Knee MRI & Knee Joint Immobilizer", amount: 650.00, payment_status: "Paid", bill_date: "2026-10-02" }
  ]
};

// ==========================================
// 2. DOM CONTENT LOADED & INITIALIZATION
// ==========================================

document.addEventListener('DOMContentLoaded', () => {
  initClock();
  initNavigation();
  initRoleSelector();
  initThemeToggle();
  initGlobalSearch();
  initModals();
  
  // Render modules
  renderDashboard();
  renderPatientsTable();
  renderDoctorsGrid();
  renderAppointmentsTable('ALL');
  renderMedicalRecords();
  renderPharmacyTable();
  renderBillingTable();
  initSqlConsole();

  // Populate Select Boxes
  populatePatientSelects();
  populateDoctorSelects();

  // Set default appointment date to tomorrow
  const dateInput = document.getElementById('appt-date');
  if (dateInput) {
    const tomorrow = new Date();
    tomorrow.setDate(tomorrow.getDate() + 1);
    dateInput.value = tomorrow.toISOString().split('T')[0];
  }
});

// ==========================================
// 3. CLOCK & TICKER
// ==========================================

function initClock() {
  const clockEl = document.getElementById('ticker-time');
  function updateTime() {
    if (clockEl) {
      const now = new Date();
      clockEl.textContent = now.toLocaleTimeString('en-US', { hour12: false });
    }
  }
  updateTime();
  setInterval(updateTime, 1000);
}

// ==========================================
// 4. NAVIGATION & TABS
// ==========================================

function initNavigation() {
  const navItems = document.querySelectorAll('.nav-item');
  const panes = document.querySelectorAll('.tab-pane');
  const mobileNavBtns = document.querySelectorAll('.mobile-nav-btn[data-tab]');
  const mobileMoreBtn = document.getElementById('btn-mobile-more');
  const mobileDrawer = document.getElementById('mobile-more-drawer');
  const closeDrawerBtn = document.getElementById('btn-close-mobile-drawer');
  const searchToggleBtn = document.getElementById('btn-mobile-search-toggle');
  const searchContainer = document.getElementById('header-search-container');
  const mobileFab = document.getElementById('mobile-fab');

  // Desktop & Tablet Navigation
  navItems.forEach(item => {
    item.addEventListener('click', (e) => {
      e.preventDefault();
      const tabTarget = item.getAttribute('data-tab');
      switchTab(tabTarget);
    });
  });

  // Mobile Bottom Navigation
  mobileNavBtns.forEach(btn => {
    btn.addEventListener('click', (e) => {
      e.preventDefault();
      const tabTarget = btn.getAttribute('data-tab');
      switchTab(tabTarget);
    });
  });

  // Mobile "More" Drawer Open/Close
  mobileMoreBtn?.addEventListener('click', () => {
    mobileDrawer?.classList.remove('hidden');
  });

  closeDrawerBtn?.addEventListener('click', () => {
    mobileDrawer?.classList.add('hidden');
  });

  mobileDrawer?.addEventListener('click', (e) => {
    if (e.target === mobileDrawer) {
      mobileDrawer.classList.add('hidden');
    }
  });

  // Drawer Items navigation
  document.querySelectorAll('.drawer-item').forEach(item => {
    item.addEventListener('click', () => {
      const tabTarget = item.getAttribute('data-tab');
      if (tabTarget) {
        mobileDrawer?.classList.add('hidden');
        switchTab(tabTarget);
      }
    });
  });

  document.getElementById('btn-mobile-drawer-add-patient')?.addEventListener('click', () => {
    mobileDrawer?.classList.add('hidden');
    openModal('modal-add-patient');
  });

  // Mobile Search Toggle
  searchToggleBtn?.addEventListener('click', () => {
    searchContainer?.classList.toggle('mobile-search-active');
    if (searchContainer?.classList.contains('mobile-search-active')) {
      document.getElementById('global-search-input')?.focus();
    }
  });

  // Mobile FAB action
  mobileFab?.addEventListener('click', () => {
    openModal('modal-book-appt');
  });

  // Hero Quick Buttons
  document.getElementById('btn-hero-book-appointment')?.addEventListener('click', () => {
    openModal('modal-book-appt');
  });

  document.getElementById('btn-hero-open-sql')?.addEventListener('click', () => {
    switchTab('tab-database');
  });

  document.getElementById('btn-hero-emergency-triage')?.addEventListener('click', () => {
    showToast("Emergency Triage Protocol Initiated", "Trauma bay alert dispatched to Chief On-Duty Physician", "toast-error");
  });

  document.getElementById('btn-quick-new-patient')?.addEventListener('click', () => {
    openModal('modal-add-patient');
  });

  document.getElementById('btn-open-add-patient-modal')?.addEventListener('click', () => {
    openModal('modal-add-patient');
  });

  document.getElementById('btn-open-book-modal')?.addEventListener('click', () => {
    openModal('modal-book-appt');
  });

  document.getElementById('btn-quick-book-today')?.addEventListener('click', () => {
    openModal('modal-book-appt');
  });

  document.getElementById('btn-refresh-queue')?.addEventListener('click', () => {
    renderDashboard();
    showToast("Schedule Synchronized", "Refreshed live queue with MySQL appointments table.", "toast-info");
  });
}

function switchTab(tabId) {
  const navItems = document.querySelectorAll('.nav-item');
  const panes = document.querySelectorAll('.tab-pane');
  const mobileNavBtns = document.querySelectorAll('.mobile-nav-btn[data-tab]');

  // Update Desktop Sidebar Nav active
  navItems.forEach(n => {
    if (n.getAttribute('data-tab') === tabId) {
      n.classList.add('active');
    } else {
      n.classList.remove('active');
    }
  });

  // Update Mobile Bottom Nav active
  mobileNavBtns.forEach(btn => {
    if (btn.getAttribute('data-tab') === tabId) {
      btn.classList.add('active');
    } else {
      btn.classList.remove('active');
    }
  });

  // Update Tab Pane
  panes.forEach(pane => {
    if (pane.id === tabId) {
      pane.classList.add('active');
    } else {
      pane.classList.remove('active');
    }
  });

  window.scrollTo({ top: 0, behavior: 'smooth' });
}

// ==========================================
// 5. ROLE SELECTOR / PERSONA SWITCHER
// ==========================================

function initRoleSelector() {
  const roleSelect = document.getElementById('role-select');
  const userNameEl = document.getElementById('current-user-name');
  const userRoleEl = document.getElementById('current-user-role');
  const userAvatarEl = document.getElementById('current-user-avatar');
  const greetingEl = document.getElementById('dashboard-greeting');

  roleSelect.addEventListener('change', (e) => {
    const role = e.target.value;
    HMS_STATE.currentUserRole = role;

    if (role === 'admin') {
      userNameEl.textContent = 'Dr. Marcus Vance';
      userRoleEl.textContent = 'System Administrator';
      userAvatarEl.textContent = 'AD';
      userAvatarEl.style.background = 'linear-gradient(135deg, #3b82f6, #8b5cf6)';
      greetingEl.textContent = 'City General Medical Center - Administrator Hub';
      showToast('Switched to Admin Console', 'Full privileges enabled: DB schema access, patient editing, billing.', 'toast-info');
    } else if (role === 'doctor') {
      userNameEl.textContent = 'Dr. Sarah Smith, MD';
      userRoleEl.textContent = 'Chief Cardiologist';
      userAvatarEl.textContent = 'DR';
      userAvatarEl.style.background = 'linear-gradient(135deg, #00e5be, #0284c7)';
      greetingEl.textContent = 'Welcome back, Dr. Sarah Smith';
      showToast('Switched to Doctor Portal', 'Accessing clinical consultations, patient dossiers, and surgical schedules.', 'toast-info');
    } else if (role === 'patient') {
      userNameEl.textContent = 'Eleanor Vance';
      userRoleEl.textContent = 'Patient Account #101';
      userAvatarEl.textContent = 'EV';
      userAvatarEl.style.background = 'linear-gradient(135deg, #f59e0b, #ec4899)';
      greetingEl.textContent = 'Welcome, Eleanor Vance';
      showToast('Switched to Patient Portal', 'Self-service appointment booking, EHR timeline, and payment ledger.', 'toast-info');
    }
  });
}

// ==========================================
// 6. THEME TOGGLER
// ==========================================

function initThemeToggle() {
  const toggleBtn = document.getElementById('btn-theme-toggle');
  const themeIcon = document.getElementById('theme-icon');

  toggleBtn.addEventListener('click', () => {
    const isDark = document.body.classList.contains('theme-dark');
    if (isDark) {
      document.body.classList.remove('theme-dark');
      document.body.classList.add('theme-light');
      themeIcon.classList.remove('fa-moon');
      themeIcon.classList.add('fa-sun');
    } else {
      document.body.classList.remove('theme-light');
      document.body.classList.add('theme-dark');
      themeIcon.classList.remove('fa-sun');
      themeIcon.classList.add('fa-moon');
    }
  });
}

// ==========================================
// 7. GLOBAL SEARCH
// ==========================================

function initGlobalSearch() {
  const searchInput = document.getElementById('global-search-input');
  const dropdown = document.getElementById('search-dropdown');

  // Keybinding: '/' to focus search
  document.addEventListener('keydown', (e) => {
    if (e.key === '/' && document.activeElement !== searchInput && !document.querySelector('.modal-backdrop:not(.hidden)')) {
      e.preventDefault();
      searchInput.focus();
    }
  });

  searchInput.addEventListener('input', (e) => {
    const query = e.target.value.trim().toLowerCase();
    if (!query) {
      dropdown.classList.add('hidden');
      dropdown.innerHTML = '';
      return;
    }

    const patientMatches = HMS_STATE.patients.filter(p => p.name.toLowerCase().includes(query) || p.blood_group.toLowerCase().includes(query));
    const doctorMatches = HMS_STATE.doctors.filter(d => d.name.toLowerCase().includes(query) || d.specialization.toLowerCase().includes(query));
    const medicineMatches = HMS_STATE.medicines.filter(m => m.name.toLowerCase().includes(query) || m.category.toLowerCase().includes(query));

    let html = '';

    patientMatches.forEach(p => {
      html += `
        <div class="search-item" onclick="openPatientDossier(${p.id})">
          <span><i class="fa-solid fa-hospital-user" style="color: #60a5fa;"></i> <strong>${p.name}</strong> (Patient #${p.id})</span>
          <span class="badge badge-info">${p.blood_group}</span>
        </div>`;
    });

    doctorMatches.forEach(d => {
      html += `
        <div class="search-item" onclick="switchTab('tab-doctors')">
          <span><i class="fa-solid fa-user-doctor" style="color: var(--accent-teal);"></i> <strong>${d.name}</strong> - ${d.specialization}</span>
          <span class="badge badge-success">$${d.fee.toFixed(2)}</span>
        </div>`;
    });

    medicineMatches.forEach(m => {
      html += `
        <div class="search-item" onclick="switchTab('tab-pharmacy')">
          <span><i class="fa-solid fa-pills" style="color: #c084fc;"></i> <strong>${m.name}</strong> (${m.category})</span>
          <span class="badge badge-warning">${m.quantity} in stock</span>
        </div>`;
    });

    if (!html) {
      html = `<div style="padding: 12px; color: var(--text-muted); font-size: 0.8rem; text-align: center;">No matching clinical records found.</div>`;
    }

    dropdown.innerHTML = html;
    dropdown.classList.remove('hidden');
  });

  // Close search dropdown on click outside
  document.addEventListener('click', (e) => {
    if (!searchInput.contains(e.target) && !dropdown.contains(e.target)) {
      dropdown.classList.add('hidden');
    }
  });
}

// ==========================================
// 8. DASHBOARD RENDERING
// ==========================================

function renderDashboard() {
  // Update Stats
  document.getElementById('stat-patients-count').textContent = HMS_STATE.patients.length + 1422; // aggregate demonstration
  document.getElementById('badge-total-patients').textContent = HMS_STATE.patients.length + 1422;
  document.getElementById('stat-doctors-count').textContent = HMS_STATE.doctors.length + 32;

  // Render Queue (Today's scheduled appointments)
  const queueContainer = document.getElementById('dashboard-queue-container');
  const todayAppts = HMS_STATE.appointments.filter(a => a.appointment_date === "2026-10-04");

  if (!todayAppts.length) {
    queueContainer.innerHTML = `<div style="padding: 20px; text-align: center; color: var(--text-muted);">No more appointments scheduled for today.</div>`;
    return;
  }

  queueContainer.innerHTML = todayAppts.map(apt => `
    <div class="queue-item">
      <div class="queue-time"><i class="fa-regular fa-clock"></i> ${formatTime(apt.appointment_time)}</div>
      <div class="queue-patient">
        <div class="queue-patient-name">${apt.patient_name}</div>
        <div class="queue-details">${apt.doctor_name} • ${apt.specialty} • ${apt.notes}</div>
      </div>
      <div>
        <span class="badge ${apt.status === 'Completed' ? 'badge-success' : 'badge-info'}">${apt.status}</span>
      </div>
    </div>
  `).join('');
}

// ==========================================
// 9. PATIENTS DIRECTORY (CRUD + FILTER)
// ==========================================

function renderPatientsTable() {
  const tbody = document.getElementById('patients-table-body');
  const searchVal = document.getElementById('patient-search-filter')?.value.toLowerCase() || '';
  const bloodVal = document.getElementById('patient-blood-filter')?.value || 'ALL';
  const statusVal = document.getElementById('patient-status-filter')?.value || 'ALL';

  const filtered = HMS_STATE.patients.filter(p => {
    const matchesSearch = p.name.toLowerCase().includes(searchVal) ||
                          p.phone.toLowerCase().includes(searchVal) ||
                          p.id.toString().includes(searchVal);
    const matchesBlood = bloodVal === 'ALL' || p.blood_group === bloodVal;
    const matchesStatus = statusVal === 'ALL' || p.status === statusVal;
    return matchesSearch && matchesBlood && matchesStatus;
  });

  document.getElementById('patient-count-display').textContent = `Showing ${filtered.length} of ${HMS_STATE.patients.length} patients`;

  if (!filtered.length) {
    tbody.innerHTML = `<tr><td colspan="8" style="text-align: center; padding: 24px; color: var(--text-muted);">No matching patients found.</td></tr>`;
    return;
  }

  tbody.innerHTML = filtered.map(p => `
    <tr>
      <td><span class="system-pill">#${p.id}</span></td>
      <td>
        <strong style="cursor: pointer; color: var(--text-highlight);" onclick="openPatientDossier(${p.id})">
          ${p.name}
        </strong>
      </td>
      <td>${p.age} yrs / ${p.gender}</td>
      <td><span class="badge badge-purple">${p.blood_group}</span></td>
      <td>
        <div style="font-size: 0.8rem;">${p.phone}</div>
        <div style="font-size: 0.72rem; color: var(--text-muted);">${p.email || 'N/A'}</div>
      </td>
      <td><span style="font-size: 0.78rem;">${p.emergency_contact}</span></td>
      <td>
        <span class="badge ${p.status === 'Inpatient' ? 'badge-warning' : p.status === 'Outpatient' ? 'badge-success' : 'badge-info'}">
          ${p.status}
        </span>
      </td>
      <td class="text-right">
        <button class="btn btn-xs btn-outline" onclick="openPatientDossier(${p.id})" title="View Clinical Dossier">
          <i class="fa-solid fa-notes-medical"></i> Dossier
        </button>
        <button class="btn btn-xs btn-primary" onclick="quickBookForPatient(${p.id})" title="Book Appointment">
          <i class="fa-solid fa-calendar-plus"></i>
        </button>
      </td>
    </tr>
  `).join('');
}

// Hook Patient Filters
document.getElementById('patient-search-filter')?.addEventListener('input', renderPatientsTable);
document.getElementById('patient-blood-filter')?.addEventListener('change', renderPatientsTable);
document.getElementById('patient-status-filter')?.addEventListener('change', renderPatientsTable);

// Form: Add Patient (ACID Transaction Simulation)
document.getElementById('form-add-patient')?.addEventListener('submit', (e) => {
  e.preventDefault();

  const name = document.getElementById('p-name').value.trim();
  const age = parseInt(document.getElementById('p-age').value);
  const gender = document.getElementById('p-gender').value;
  const blood = document.getElementById('p-blood').value;
  const phone = document.getElementById('p-phone').value.trim();
  const email = document.getElementById('p-email').value.trim();
  const emergency = document.getElementById('p-emergency').value.trim();
  const status = document.getElementById('p-status').value;
  const address = document.getElementById('p-address').value.trim();

  // Create new patient record
  const newId = 100 + HMS_STATE.patients.length + 1;
  const newPatient = {
    id: newId,
    name,
    age,
    gender,
    blood_group: blood,
    phone,
    email,
    address,
    emergency_contact: emergency,
    status,
    created_at: new Date().toISOString().split('T')[0]
  };

  HMS_STATE.patients.unshift(newPatient);

  // Re-render UI
  renderPatientsTable();
  populatePatientSelects();
  closeModal('modal-add-patient');
  document.getElementById('form-add-patient').reset();

  showToast(
    `Patient #${newId} Registered!`,
    `Database transaction committed: conn.commit() with PreparedStatement`,
    "toast-success"
  );
});

// ==========================================
// 10. DOCTORS DIRECTORY
// ==========================================

function renderDoctorsGrid() {
  const grid = document.getElementById('doctors-cards-grid');
  grid.innerHTML = HMS_STATE.doctors.map(doc => `
    <div class="doctor-card">
      <div class="doctor-photo-wrapper">
        <img src="${doc.photo}" alt="${doc.name}" class="doctor-photo" onerror="this.src='https://images.unsplash.com/photo-1537368910025-700350fe46c7?w=500&auto=format&fit=crop&q=80'">
        <span class="doctor-status-badge ${doc.available ? 'available' : 'busy'}">
          <i class="fa-solid ${doc.available ? 'fa-circle-check' : 'fa-clock'}"></i>
          ${doc.available ? 'Available Today' : doc.badge}
        </span>
      </div>
      <div class="doctor-body">
        <h3 class="doctor-name">${doc.name}</h3>
        <div class="doctor-specialty">${doc.specialization} • ${doc.badge}</div>
        <ul class="doctor-meta-list">
          <li><i class="fa-solid fa-hospital"></i> ${doc.department}</li>
          <li><i class="fa-solid fa-award"></i> ${doc.experience} (Rating: ★ ${doc.rating})</li>
          <li><i class="fa-solid fa-door-open"></i> ${doc.room}</li>
          <li><i class="fa-solid fa-phone"></i> ${doc.phone}</li>
        </ul>
        <div class="doctor-footer">
          <div class="doctor-fee">$${doc.fee.toFixed(2)} <span>/ consult</span></div>
          <button class="btn btn-xs btn-primary" onclick="quickBookWithDoctor(${doc.id})">
            <i class="fa-solid fa-calendar-plus"></i> Book Slot
          </button>
        </div>
      </div>
    </div>
  `).join('');
}

// ==========================================
// 11. APPOINTMENTS SCHEDULER
// ==========================================

function renderAppointmentsTable(filterStatus = 'ALL') {
  const tbody = document.getElementById('appointments-table-body');
  
  // Update badge counts
  document.getElementById('count-all-appts').textContent = HMS_STATE.appointments.length;
  document.getElementById('count-scheduled-appts').textContent = HMS_STATE.appointments.filter(a => a.status === 'Scheduled').length;
  document.getElementById('count-completed-appts').textContent = HMS_STATE.appointments.filter(a => a.status === 'Completed').length;
  document.getElementById('count-cancelled-appts').textContent = HMS_STATE.appointments.filter(a => a.status === 'Cancelled').length;

  const list = filterStatus === 'ALL' 
    ? HMS_STATE.appointments 
    : HMS_STATE.appointments.filter(a => a.status === filterStatus);

  if (!list.length) {
    tbody.innerHTML = `<tr><td colspan="8" style="text-align: center; padding: 24px; color: var(--text-muted);">No consultations found with status "${filterStatus}".</td></tr>`;
    return;
  }

  tbody.innerHTML = list.map(apt => `
    <tr>
      <td><span class="system-pill">#${apt.id}</span></td>
      <td><strong>${apt.patient_name}</strong></td>
      <td>${apt.doctor_name}</td>
      <td><span class="badge badge-purple">${apt.specialty}</span></td>
      <td>
        <div><i class="fa-regular fa-calendar"></i> ${apt.appointment_date}</div>
        <small style="color: var(--accent-teal); font-family: 'JetBrains Mono', monospace;">${formatTime(apt.appointment_time)}</small>
      </td>
      <td><strong>${apt.fee}</strong></td>
      <td>
        <span class="badge ${apt.status === 'Completed' ? 'badge-success' : apt.status === 'Scheduled' ? 'badge-info' : 'badge-danger'}">
          ${apt.status}
        </span>
      </td>
      <td class="text-right">
        ${apt.status === 'Scheduled' ? `
          <button class="btn btn-xs btn-outline" onclick="completeAppointment(${apt.id})" title="Mark as Completed">
            <i class="fa-solid fa-check"></i>
          </button>
          <button class="btn btn-xs btn-outline" onclick="cancelAppointment(${apt.id})" title="Cancel Consultation" style="color: var(--accent-red); border-color: rgba(239, 68, 68, 0.3);">
            <i class="fa-solid fa-xmark"></i>
          </button>
        ` : `
          <span style="font-size: 0.75rem; color: var(--text-muted);"><i class="fa-solid fa-lock"></i> Locked</span>
        `}
      </td>
    </tr>
  `).join('');
}

// Subtabs
document.querySelectorAll('.subtab-btn').forEach(btn => {
  btn.addEventListener('click', () => {
    document.querySelectorAll('.subtab-btn').forEach(b => b.classList.remove('active'));
    btn.classList.add('active');
    const filter = btn.getAttribute('data-filter');
    renderAppointmentsTable(filter);
  });
});

// Book Appointment Form Handler
document.getElementById('form-book-appt')?.addEventListener('submit', (e) => {
  e.preventDefault();

  const patientId = parseInt(document.getElementById('appt-patient-select').value);
  const doctorId = parseInt(document.getElementById('appt-doctor-select').value);
  const date = document.getElementById('appt-date').value;
  const time = document.getElementById('appt-time').value;
  const notes = document.getElementById('appt-notes').value.trim() || 'Routine Consultation';

  const patient = HMS_STATE.patients.find(p => p.id === patientId);
  const doctor = HMS_STATE.doctors.find(d => d.id === doctorId);

  if (!patient || !doctor) {
    showToast("Error", "Could not resolve patient or physician database IDs.", "toast-error");
    return;
  }

  const newApptId = 500 + HMS_STATE.appointments.length + 1;
  const newAppt = {
    id: newApptId,
    patient_id: patient.id,
    doctor_id: doctor.id,
    patient_name: patient.name,
    doctor_name: doctor.name,
    specialty: doctor.specialization,
    appointment_date: date,
    appointment_time: time,
    fee: `$${doctor.fee.toFixed(2)}`,
    status: "Scheduled",
    notes: notes
  };

  HMS_STATE.appointments.unshift(newAppt);

  // Also auto-generate a pending bill in bills table!
  const newBillId = 9000 + HMS_STATE.bills.length + 1;
  HMS_STATE.bills.unshift({
    id: newBillId,
    patient_id: patient.id,
    patient_name: patient.name,
    description: `Consultation Fee - ${doctor.name} (${doctor.specialization})`,
    amount: doctor.fee,
    payment_status: "Pending",
    bill_date: date
  });

  renderAppointmentsTable('ALL');
  renderDashboard();
  renderBillingTable();
  closeModal('modal-book-appt');
  document.getElementById('form-book-appt').reset();

  showToast(
    `Consultation Booked for ${patient.name}!`,
    `Atomic SQL Transaction committed (Patient #${patient.id} + Doctor #${doctor.id})`,
    "toast-success"
  );
});

function completeAppointment(id) {
  const apt = HMS_STATE.appointments.find(a => a.id === id);
  if (apt) {
    apt.status = 'Completed';
    renderAppointmentsTable();
    renderDashboard();
    showToast("Appointment Completed", `Consultation for ${apt.patient_name} marked as completed.`, "toast-success");
  }
}

function cancelAppointment(id) {
  const apt = HMS_STATE.appointments.find(a => a.id === id);
  if (apt) {
    apt.status = 'Cancelled';
    renderAppointmentsTable();
    renderDashboard();
    showToast("Appointment Cancelled", `Consultation #${apt.id} marked as cancelled.`, "toast-info");
  }
}

// ==========================================
// 12. MEDICAL RECORDS (EHR)
// ==========================================

function renderMedicalRecords() {
  const container = document.getElementById('records-grid-container');
  container.innerHTML = HMS_STATE.medical_records.map(rec => `
    <div class="record-card">
      <div class="record-header">
        <div>
          <h4 class="record-patient">${rec.patient_name}</h4>
          <span class="system-pill">Record #${rec.id}</span>
        </div>
        <div class="record-date"><i class="fa-regular fa-calendar"></i> ${rec.visit_date}</div>
      </div>
      <div class="record-section">
        <span class="record-label">Attending Physician</span>
        <div class="record-val" style="color: var(--accent-teal); font-weight: 600;">${rec.doctor_name}</div>
      </div>
      <div class="record-section">
        <span class="record-label">Symptoms Reported</span>
        <div class="record-val">${rec.symptoms}</div>
      </div>
      <div class="record-section">
        <span class="record-label">Clinical Diagnosis</span>
        <div class="record-val" style="font-weight: 700; color: #f87171;">${rec.diagnosis}</div>
      </div>
      <div class="record-section">
        <span class="record-label">Prescribed Treatment</span>
        <div class="record-val">${rec.treatment}</div>
      </div>
      <div class="record-section" style="margin-bottom: 0;">
        <span class="record-label">Physician Notes</span>
        <div class="record-val" style="font-style: italic; color: var(--text-muted); font-size: 0.78rem;">${rec.notes}</div>
      </div>
    </div>
  `).join('');
}

// ==========================================
// 13. PHARMACY & INVENTORY
// ==========================================

function renderPharmacyTable() {
  const tbody = document.getElementById('pharmacy-table-body');
  tbody.innerHTML = HMS_STATE.medicines.map(m => {
    const isLow = m.quantity < 20;
    return `
      <tr>
        <td><span class="system-pill">#MED-${m.id}</span></td>
        <td><strong>${m.name}</strong></td>
        <td><span class="badge badge-purple">${m.category}</span></td>
        <td>
          <strong style="color: ${isLow ? '#f87171' : 'var(--text-primary)'};">${m.quantity} units</strong>
        </td>
        <td>$${m.price.toFixed(2)}</td>
        <td>${m.expiry}</td>
        <td>
          <span class="badge ${isLow ? 'badge-danger' : 'badge-success'}">
            <i class="fa-solid ${isLow ? 'fa-triangle-exclamation' : 'fa-check'}"></i>
            ${isLow ? 'Low Stock Warning' : 'In Stock'}
          </span>
        </td>
        <td class="text-right">
          <button class="btn btn-xs btn-outline" onclick="restockMedicine(${m.id})" title="Restock +50 Units">
            <i class="fa-solid fa-boxes-packing"></i> Restock +50
          </button>
        </td>
      </tr>
    `;
  }).join('');
}

function restockMedicine(id) {
  const med = HMS_STATE.medicines.find(m => m.id === id);
  if (med) {
    med.quantity += 50;
    renderPharmacyTable();
    showToast("Stock Reordered", `Added 50 units to inventory of ${med.name}. New total: ${med.quantity}`, "toast-success");
  }
}

// ==========================================
// 14. BILLING & INVOICES
// ==========================================

function renderBillingTable() {
  const tbody = document.getElementById('billing-table-body');
  tbody.innerHTML = HMS_STATE.bills.map(b => `
    <tr>
      <td><span class="system-pill">INV-${b.id}</span></td>
      <td><strong>${b.patient_name}</strong></td>
      <td>${b.description}</td>
      <td><strong style="font-size: 0.95rem;">$${b.amount.toFixed(2)}</strong></td>
      <td>${b.bill_date}</td>
      <td>
        <span class="badge ${b.payment_status === 'Paid' ? 'badge-success' : 'badge-warning'}">
          ${b.payment_status}
        </span>
      </td>
      <td class="text-right">
        ${b.payment_status === 'Pending' ? `
          <button class="btn btn-xs btn-primary" onclick="payBill(${b.id})">
            <i class="fa-solid fa-credit-card"></i> Settle Bill
          </button>
        ` : `
          <span style="font-size: 0.75rem; color: #34d399;"><i class="fa-solid fa-receipt"></i> Paid in Full</span>
        `}
      </td>
    </tr>
  `).join('');
}

function payBill(id) {
  const bill = HMS_STATE.bills.find(b => b.id === id);
  if (bill) {
    bill.payment_status = 'Paid';
    renderBillingTable();
    showToast(
      "Payment Successful!",
      `Processed payment of $${bill.amount.toFixed(2)} for ${bill.patient_name}`,
      "toast-success"
    );
  }
}

// ==========================================
// 15. PATIENT DOSSIER MODAL
// ==========================================

window.openPatientDossier = function(patientId) {
  const patient = HMS_STATE.patients.find(p => p.id === patientId);
  if (!patient) return;

  const records = HMS_STATE.medical_records.filter(r => r.patient_id === patientId);
  const appts = HMS_STATE.appointments.filter(a => a.patient_id === patientId);
  const bills = HMS_STATE.bills.filter(b => b.patient_id === patientId);

  const container = document.getElementById('patient-dossier-content');
  container.innerHTML = `
    <div style="display: flex; gap: 20px; align-items: center; margin-bottom: 20px; padding-bottom: 16px; border-bottom: 1px solid var(--border-subtle);">
      <div style="width: 60px; height: 60px; border-radius: 50%; background: linear-gradient(135deg, #00e5be, #3b82f6); display: flex; align-items: center; justify-content: center; font-size: 1.5rem; color: #081121; font-weight: 800;">
        ${patient.name.split(' ').map(n=>n[0]).join('')}
      </div>
      <div>
        <h3 style="font-size: 1.4rem; margin-bottom: 2px;">${patient.name}</h3>
        <div style="font-size: 0.8rem; color: var(--text-muted);">
          Patient ID: #${patient.id} • ${patient.age} yrs • ${patient.gender} • Blood Group: <span class="badge badge-purple">${patient.blood_group}</span>
        </div>
      </div>
      <div style="margin-left: auto;">
        <span class="badge ${patient.status === 'Inpatient' ? 'badge-warning' : 'badge-success'}">${patient.status}</span>
      </div>
    </div>

    <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px; margin-bottom: 20px;">
      <div class="panel" style="padding: 14px;">
        <span class="record-label">Contact Details</span>
        <div style="font-size: 0.84rem;"><strong>Phone:</strong> ${patient.phone}</div>
        <div style="font-size: 0.84rem;"><strong>Email:</strong> ${patient.email || 'N/A'}</div>
        <div style="font-size: 0.84rem;"><strong>Address:</strong> ${patient.address || 'N/A'}</div>
      </div>
      <div class="panel" style="padding: 14px;">
        <span class="record-label">Emergency Contact</span>
        <div style="font-size: 0.84rem; color: #fbbf24;"><strong>${patient.emergency_contact}</strong></div>
        <div style="font-size: 0.76rem; color: var(--text-muted); margin-top: 4px;">Registered on: ${patient.created_at}</div>
      </div>
    </div>

    <h4 style="font-size: 1rem; margin-bottom: 10px;"><i class="fa-solid fa-file-waveform"></i> Clinical Visit History</h4>
    ${records.length ? records.map(r => `
      <div style="background: var(--bg-card); padding: 12px; border-radius: 8px; border: 1px solid var(--border-subtle); margin-bottom: 10px;">
        <div style="display: flex; justify-content: space-between; font-size: 0.78rem; color: var(--text-muted); margin-bottom: 4px;">
          <span>Attending: <strong>${r.doctor_name}</strong></span>
          <span>${r.visit_date}</span>
        </div>
        <div style="font-size: 0.85rem; font-weight: 700; color: #f87171;">Diagnosis: ${r.diagnosis}</div>
        <div style="font-size: 0.8rem; margin-top: 2px;">Rx: ${r.treatment}</div>
      </div>
    `).join('') : '<div style="font-size: 0.8rem; color: var(--text-muted); margin-bottom: 16px;">No recorded clinical visits on file.</div>'}

    <h4 style="font-size: 1rem; margin: 16px 0 10px;"><i class="fa-solid fa-receipt"></i> Invoicing Ledger</h4>
    <div style="display: flex; flex-direction: column; gap: 6px;">
      ${bills.map(b => `
        <div style="display: flex; justify-content: space-between; align-items: center; font-size: 0.82rem; background: var(--bg-card); padding: 8px 12px; border-radius: 6px;">
          <span>${b.description} (${b.bill_date})</span>
          <span><strong>$${b.amount.toFixed(2)}</strong> <span class="badge ${b.payment_status === 'Paid' ? 'badge-success' : 'badge-warning'}">${b.payment_status}</span></span>
        </div>
      `).join('')}
    </div>
  `;

  document.getElementById('btn-dossier-book-appt').onclick = () => {
    closeModal('modal-patient-dossier');
    quickBookForPatient(patientId);
  };

  openModal('modal-patient-dossier');
};

window.quickBookForPatient = function(patientId) {
  const select = document.getElementById('appt-patient-select');
  if (select) select.value = patientId;
  openModal('modal-book-appt');
};

window.quickBookWithDoctor = function(doctorId) {
  const select = document.getElementById('appt-doctor-select');
  if (select) select.value = doctorId;
  openModal('modal-book-appt');
};

// ==========================================
// 16. SQL CONSOLE & QUERY SIMULATOR
// ==========================================

function initSqlConsole() {
  const sqlArea = document.getElementById('sql-input-area');
  const execBtn = document.getElementById('btn-execute-sql');
  const metaEl = document.getElementById('sql-meta-text');
  const resultTable = document.getElementById('sql-result-table');

  const presetSelect = document.getElementById('btn-sql-preset-select');
  const presetTx = document.getElementById('btn-sql-preset-tx');
  const presetFk = document.getElementById('btn-sql-preset-fk');

  presetSelect?.addEventListener('click', () => {
    sqlArea.value = "SELECT id, name, age, gender, blood_group FROM patients LIMIT 5;";
    execBtn.click();
  });

  presetTx?.addEventListener('click', () => {
    sqlArea.value = `-- Transaction Demo in PatientDAO\nconn.setAutoCommit(false);\nINSERT INTO appointments (patient_id, doctor_id, appointment_date, appointment_time, status)\nVALUES (101, 1, '2026-10-15', '10:00:00', 'SCHEDULED');\nconn.commit();`;
    execBtn.click();
  });

  presetFk?.addEventListener('click', () => {
    sqlArea.value = `SELECT a.id AS apt_id, p.name AS patient, d.name AS doctor, a.status\nFROM appointments a\nJOIN patients p ON a.patient_id = p.id\nJOIN doctors d ON a.doctor_id = d.id;`;
    execBtn.click();
  });

  execBtn?.addEventListener('click', () => {
    const rawSql = sqlArea.value.trim();
    const startTime = performance.now();

    // Check query type
    if (rawSql.includes("INSERT") || rawSql.includes("setAutoCommit")) {
      const elapsed = (performance.now() - startTime + 1.2).toFixed(1);
      metaEl.textContent = `Transaction SUCCESS! 1 row affected in ${elapsed}ms. (JDBC setAutoCommit(false) -> commit())`;
      resultTable.innerHTML = `
        <thead><tr><th>Status</th><th>Transaction State</th><th>Isolation Level</th></tr></thead>
        <tbody><tr><td style="color:#34d399;">COMMITTED</td><td>ACID compliant</td><td>TRANSACTION_READ_COMMITTED</td></tr></tbody>
      `;
      return;
    }

    if (rawSql.includes("JOIN") || rawSql.includes("appointments")) {
      const elapsed = (performance.now() - startTime + 0.9).toFixed(1);
      metaEl.textContent = `Query executed in ${elapsed}ms (${HMS_STATE.appointments.length} rows returned). MySQL Connection #48`;
      resultTable.innerHTML = `
        <thead>
          <tr><th>apt_id</th><th>patient</th><th>doctor</th><th>status</th></tr>
        </thead>
        <tbody>
          ${HMS_STATE.appointments.map(a => `
            <tr>
              <td>${a.id}</td>
              <td>${a.patient_name}</td>
              <td>${a.doctor_name}</td>
              <td>${a.status}</td>
            </tr>
          `).join('')}
        </tbody>
      `;
      return;
    }

    // Default: patients query
    const elapsed = (performance.now() - startTime + 0.8).toFixed(1);
    metaEl.textContent = `Query executed in ${elapsed}ms (${HMS_STATE.patients.length} rows returned). MySQL Connection #48`;
    resultTable.innerHTML = `
      <thead>
        <tr><th>id</th><th>name</th><th>age</th><th>gender</th><th>blood_group</th></tr>
      </thead>
      <tbody>
        ${HMS_STATE.patients.map(p => `
          <tr>
            <td>${p.id}</td>
            <td>${p.name}</td>
            <td>${p.age}</td>
            <td>${p.gender}</td>
            <td>${p.blood_group}</td>
          </tr>
        `).join('')}
      </tbody>
    `;
  });

  // Run default query once on load
  execBtn?.click();
}

// ==========================================
// 17. UTILITIES & MODAL HELPERS
// ==========================================

function populatePatientSelects() {
  const select = document.getElementById('appt-patient-select');
  if (select) {
    select.innerHTML = HMS_STATE.patients.map(p => `
      <option value="${p.id}">${p.name} (ID: #${p.id} - Blood: ${p.blood_group})</option>
    `).join('');
  }
}

function populateDoctorSelects() {
  const select = document.getElementById('appt-doctor-select');
  if (select) {
    select.innerHTML = HMS_STATE.doctors.map(d => `
      <option value="${d.id}">${d.name} (${d.specialization} - Fee: $${d.fee.toFixed(2)})</option>
    `).join('');
  }
}

function initModals() {
  document.querySelectorAll('[data-close-modal]').forEach(btn => {
    btn.addEventListener('click', () => {
      document.querySelectorAll('.modal-backdrop').forEach(m => m.classList.add('hidden'));
    });
  });

  // Close on backdrop click
  document.querySelectorAll('.modal-backdrop').forEach(modal => {
    modal.addEventListener('click', (e) => {
      if (e.target === modal) modal.classList.add('hidden');
    });
  });

  // Close on ESC
  document.addEventListener('keydown', (e) => {
    if (e.key === 'Escape') {
      document.querySelectorAll('.modal-backdrop').forEach(m => m.classList.add('hidden'));
    }
  });
}

function openModal(modalId) {
  const modal = document.getElementById(modalId);
  if (modal) modal.classList.remove('hidden');
}

function closeModal(modalId) {
  const modal = document.getElementById(modalId);
  if (modal) modal.classList.add('hidden');
}

function showToast(message, subtitle = '', type = 'toast-info') {
  const container = document.getElementById('toast-container');
  if (!container) return;

  const toast = document.createElement('div');
  toast.className = `toast ${type}`;
  toast.innerHTML = `
    <div>
      <div class="toast-msg">${message}</div>
      ${subtitle ? `<div class="toast-sub">${subtitle}</div>` : ''}
    </div>
  `;

  container.appendChild(toast);

  setTimeout(() => {
    toast.style.transition = 'all 0.3s ease';
    toast.style.opacity = '0';
    toast.style.transform = 'translateX(100%)';
    setTimeout(() => toast.remove(), 300);
  }, 3800);
}

function formatTime(timeStr) {
  if (!timeStr) return '';
  const parts = timeStr.split(':');
  let h = parseInt(parts[0]);
  const m = parts[1] || '00';
  const ampm = h >= 12 ? 'PM' : 'AM';
  h = h % 12;
  if (h === 0) h = 12;
  return `${h}:${m} ${ampm}`;
}
