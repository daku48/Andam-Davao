let centers = [
    { id: "ec_001", name: "Rizal Memorial Coliseum", barangay: "Poblacion 4-A", district: "Poblacion District", capacity: 1000, occupancy: 400, status: "Open", verification: "Verified", verifiedAt: "Today 08:30 AM" },
    { id: "ec_002", name: "Davao City National High School Gym", barangay: "Poblacion 8-A", district: "Poblacion District", capacity: 800, occupancy: 780, status: "Full", verification: "Verified", verifiedAt: "Today 09:15 AM" },
    { id: "ec_003", name: "Calinan Gymnasium", barangay: "Calinan Poblacion", district: "Calinan District", capacity: 600, occupancy: 120, status: "Open", verification: "Verified", verifiedAt: "Yesterday 04:00 PM" },
    { id: "ec_004", name: "Toril Cultural Center", barangay: "Toril Poblacion", district: "Toril District", capacity: 500, occupancy: 0, status: "Closed", verification: "Verified", verifiedAt: "Today 07:00 AM" },
    { id: "ec_005", name: "Bunawan Barangay Hall", barangay: "Bunawan", district: "Bunawan District", capacity: 300, occupancy: 0, status: "Status unknown", verification: "Awaiting Verification", verifiedAt: "Pending Verification" }
];

let announcements = [
    { id: "ann_001", title: "Flood Warning — Matina & Davao River Basin", severity: "Urgent", agency: "Davao City DRRMO / PAGASA", affected: "Matina, Maa, Bankerohan", status: "published" },
    { id: "ann_002", title: "Typhoon Advisory — Pre-Emptive Evacuation Prep", severity: "Advisory", agency: "PAGASA / OCD XI", affected: "Coastal Barangays", status: "published" },
    { id: "ann_003", title: "Scheduled Power Maintenance Advisory", severity: "Info", agency: "Davao Light & Power", affected: "Calinan, Baguio", status: "published" },
    { id: "ann_004", title: "Draft Emergency Warning for District 3", severity: "Urgent", agency: "Davao City DRRMO", affected: "District 3", status: "draft" }
];

let auditLogs = [
    { timestamp: "2026-11-19 08:30:00", admin: "adm_001", collection: "evacuation_centers", action: "UPDATE_STATUS", details: "Rizal Coliseum set to OPEN" },
    { timestamp: "2026-11-19 09:15:00", admin: "adm_002", collection: "evacuation_centers", action: "UPDATE_STATUS", details: "City High Gym set to FULL" }
];

document.addEventListener("DOMContentLoaded", () => {
    renderCentersTable();
    renderAnnouncementsTable();
    renderAuditLogsTable();
});

function switchTab(tabName) {
    document.querySelectorAll("section[id^='tab-']").forEach(sec => sec.style.display = "none");
    document.getElementById(`tab-${tabName}`).style.display = "block";

    document.querySelectorAll(".nav-item").forEach(item => item.classList.remove("active"));
    event.currentTarget.classList.add("active");
}

function renderCentersTable() {
    const tbody = document.getElementById("centersTableBody");
    tbody.innerHTML = "";

    centers.forEach(c => {
        const badgeClass = c.status === "Open" ? "badge-open" :
                           c.status === "Full" ? "badge-full" :
                           c.status === "Closed" ? "badge-closed" : "badge-unknown";

        const tr = document.createElement("tr");
        tr.innerHTML = `
            <td><strong>${c.name}</strong></td>
            <td>${c.barangay} (${c.district})</td>
            <td><span class="badge ${badgeClass}">${c.status}</span></td>
            <td><span class="badge badge-verified">${c.verification}</span></td>
            <td>${c.occupancy} / ${c.capacity}</td>
            <td>${c.verifiedAt}</td>
            <td>
                <select class="form-control" style="width: auto; padding: 4px; display: inline-block;" onchange="updateCenterStatus('${c.id}', this.value)">
                    <option value="" disabled selected>Change Status</option>
                    <option value="Open">Set Open</option>
                    <option value="Full">Set Full</option>
                    <option value="Closed">Set Closed</option>
                    <option value="Status unknown">Set Unknown</option>
                </select>
            </td>
        `;
        tbody.appendChild(tr);
    });
}

function updateCenterStatus(centerId, newStatus) {
    const center = centers.find(c => c.id === centerId);
    if (center) {
        center.status = newStatus;
        center.verifiedAt = "Just Now";
        center.verification = "Verified";

        auditLogs.unshift({
            timestamp: new Date().toISOString().replace('T', ' ').substring(0, 19),
            admin: "admin_drrmo_davao",
            collection: "evacuation_centers",
            action: "UPDATE_STATUS",
            details: `${center.name} updated to ${newStatus}`
        });

        renderCentersTable();
        renderAuditLogsTable();
        alert(`Status for "${center.name}" updated to ${newStatus}. Audit log entry generated.`);
    }
}

function renderAnnouncementsTable() {
    const tbody = document.getElementById("announcementsTableBody");
    tbody.innerHTML = "";

    announcements.forEach(a => {
        const tr = document.createElement("tr");
        tr.innerHTML = `
            <td><strong>${a.title}</strong></td>
            <td>${a.severity}</td>
            <td>${a.agency}</td>
            <td>${a.affected}</td>
            <td><span class="badge ${a.status === 'published' ? 'badge-open' : 'badge-full'}">${a.status.toUpperCase()}</span></td>
            <td>
                ${a.status === 'draft' ? `<button class="btn-primary" style="padding: 4px 10px; font-size: 12px;" onclick="publishAnnouncement('${a.id}')">Publish Advisory</button>` : `<span style="font-size: 12px; color: var(--status-open); font-weight: bold;">Published</span>`}
            </td>
        `;
        tbody.appendChild(tr);
    });
}

function publishAnnouncement(announcementId) {
    const ann = announcements.find(a => a.id === announcementId);
    if (ann) {
        ann.status = "published";

        auditLogs.unshift({
            timestamp: new Date().toISOString().replace('T', ' ').substring(0, 19),
            admin: "admin_drrmo_davao",
            collection: "announcements",
            action: "PUBLISH_ANNOUNCEMENT",
            details: `Published advisory: ${ann.title}`
        });

        renderAnnouncementsTable();
        renderAuditLogsTable();
        alert(`Advisory "${ann.title}" is now published and available to public users.`);
    }
}

function renderAuditLogsTable() {
    const tbody = document.getElementById("auditLogsBody");
    tbody.innerHTML = "";

    auditLogs.forEach(log => {
        const tr = document.createElement("tr");
        tr.innerHTML = `
            <td>${log.timestamp}</td>
            <td><code>${log.admin}</code></td>
            <td>${log.collection}</td>
            <td><strong>${log.action}</strong></td>
            <td>${log.details}</td>
        `;
        tbody.appendChild(tr);
    });
}

function openCenterModal() {
    document.getElementById("centerModal").style.display = "flex";
}

function closeCenterModal() {
    document.getElementById("centerModal").style.display = "none";
}

function handleSaveCenter(e) {
    e.preventDefault();
    const name = document.getElementById("centerName").value;
    const address = document.getElementById("centerAddress").value;
    const barangay = document.getElementById("centerBarangay").value;
    const district = document.getElementById("centerDistrict").value;
    const capacity = parseInt(document.getElementById("centerCapacity").value) || 500;
    const status = document.getElementById("centerStatus").value;

    const newCenter = {
        id: `ec_${Date.now()}`,
        name,
        barangay,
        district,
        capacity,
        occupancy: 0,
        status,
        verification: "Verified",
        verifiedAt: "Just Now"
    };

    centers.unshift(newCenter);
    auditLogs.unshift({
        timestamp: new Date().toISOString().replace('T', ' ').substring(0, 19),
        admin: "admin_drrmo_davao",
        collection: "evacuation_centers",
        action: "CREATE_RECORD",
        details: `Created new center record: ${name}`
    });

    renderCentersTable();
    renderAuditLogsTable();
    closeCenterModal();
    alert(`Evacuation Center "${name}" created successfully!`);
}

function handleSignOut() {
    if (confirm("Are you sure you want to sign out of the Admin Dashboard?")) {
        alert("Signed out of administrator session.");
    }
}
