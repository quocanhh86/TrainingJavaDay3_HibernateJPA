/**
 * Employee Management - Modal & Detail Handlers
 */

function populateModalData(button) {
    const id = button.getAttribute('data-id') || '';
    const firstName = button.getAttribute('data-firstname') || '';
    const lastName = button.getAttribute('data-lastname') || '';
    const age = button.getAttribute('data-age') || '';
    const email = button.getAttribute('data-email') || '';
    const phone = button.getAttribute('data-phone') || '';
    const address = button.getAttribute('data-address') || '';
    const avatar = button.getAttribute('data-avatar') || '';

    // View Section
    const viewAvatar = document.getElementById('viewAvatar');
    if (avatar) {
        viewAvatar.src = avatar;
        viewAvatar.style.display = 'block';
    } else {
        viewAvatar.src = 'https://i.pravatar.cc/150';
        viewAvatar.style.display = 'block';
    }

    document.getElementById('viewFullName').textContent = (firstName + ' ' + lastName).trim() || 'Chưa có tên';
    document.getElementById('viewId').textContent = id;
    document.getElementById('viewFirstName').textContent = firstName || '-';
    document.getElementById('viewLastName').textContent = lastName || '-';
    document.getElementById('viewAge').textContent = age ? (age + ' tuổi') : '-';
    document.getElementById('viewPhone').textContent = phone || '-';
    document.getElementById('viewEmail').textContent = email || '-';
    document.getElementById('viewAddress').textContent = address || '-';

    // Badge phân loại tuổi
    const badge = document.getElementById('viewBadge');
    const ageNum = parseInt(age);
    if (!isNaN(ageNum)) {
        if (ageNum >= 45) {
            badge.className = 'badge badge-old';
            badge.textContent = 'Lớn tuổi';
        } else if (ageNum >= 25) {
            badge.className = 'badge badge-medium';
            badge.textContent = 'Trung niên';
        } else {
            badge.className = 'badge badge-young';
            badge.textContent = 'Trẻ';
        }
        badge.style.display = 'inline-block';
    } else {
        badge.style.display = 'none';
    }

    // Edit Section Form
    document.getElementById('editId').value = id;
    document.getElementById('editFirstName').value = firstName;
    document.getElementById('editLastName').value = lastName;
    document.getElementById('editAge').value = age;
    document.getElementById('editPhone').value = phone;
    document.getElementById('editEmail').value = email;
    document.getElementById('editAddress').value = address;
    document.getElementById('editAvatarUrl').value = avatar;
}

function openDetailModal(button) {
    populateModalData(button);
    switchToViewMode();
    document.getElementById('employeeModal').classList.add('active');
}

function openEditModal(button) {
    populateModalData(button);
    switchToEditMode();
    document.getElementById('employeeModal').classList.add('active');
}

function switchToEditMode() {
    document.getElementById('employeeViewSection').style.display = 'none';
    document.getElementById('employeeEditSection').style.display = 'block';
    document.getElementById('modalTitle').textContent = 'Chỉnh sửa thông tin nhân viên';
}

function switchToViewMode() {
    document.getElementById('employeeViewSection').style.display = 'block';
    document.getElementById('employeeEditSection').style.display = 'none';
    document.getElementById('modalTitle').textContent = 'Chi tiết nhân viên';
}

function closeModal() {
    const modal = document.getElementById('employeeModal');
    if (modal) {
        modal.classList.remove('active');
    }
}

// Đóng khi click ngoài backdrop
window.addEventListener('click', function (event) {
    const modal = document.getElementById('employeeModal');
    if (event.target === modal) {
        closeModal();
    }
});

// Đóng khi nhấn phím ESC
window.addEventListener('keydown', function (event) {
    if (event.key === 'Escape') {
        closeModal();
    }
});
