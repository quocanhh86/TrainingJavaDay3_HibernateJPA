document.addEventListener('DOMContentLoaded', function () {
    // 1. Modal backdrop click & escape key
    const modal = document.getElementById('departmentModal');
    if (modal) {
        modal.addEventListener('click', function (e) {
            if (e.target === this) {
                window.location.href = '/department';
            }
        });

        document.addEventListener('keydown', function (e) {
            if (e.key === 'Escape') {
                window.location.href = '/department';
            }
        });
    }

    // 2. Real-time search filter for department table
    const searchInput = document.getElementById('deptSearchInput');
    const tableBody = document.getElementById('deptTableBody');
    if (searchInput && tableBody) {
        searchInput.addEventListener('input', function () {
            const query = this.value.trim().toLowerCase();
            const rows = tableBody.querySelectorAll('tr.dept-row');
            let visibleCount = 0;

            rows.forEach(row => {
                const text = row.innerText.toLowerCase();
                if (text.includes(query)) {
                    row.style.display = '';
                    visibleCount++;
                } else {
                    row.style.display = 'none';
                }
            });

            // Handle no match row
            let noMatchRow = document.getElementById('deptNoMatchRow');
            if (visibleCount === 0 && rows.length > 0) {
                if (!noMatchRow) {
                    noMatchRow = document.createElement('tr');
                    noMatchRow.id = 'deptNoMatchRow';
                    noMatchRow.innerHTML = `<td colspan="5" style="text-align: center; color: #94a3b8; padding: 2rem;">Không tìm thấy phòng ban nào khớp với từ khóa "<strong>${query}</strong>"</td>`;
                    tableBody.appendChild(noMatchRow);
                } else {
                    noMatchRow.style.display = '';
                    noMatchRow.querySelector('strong').textContent = query;
                }
            } else if (noMatchRow) {
                noMatchRow.style.display = 'none';
            }
        });
    }

    // 3. Click to copy ID badge
    const idBadges = document.querySelectorAll('.dept-id-badge');
    idBadges.forEach(badge => {
        badge.style.cursor = 'pointer';
        badge.title = 'Nhấn để sao chép ID';
        badge.addEventListener('click', function () {
            const text = this.innerText.trim();
            navigator.clipboard.writeText(text).then(() => {
                const originalText = this.innerText;
                this.innerText = 'Đã sao chép! ✓';
                this.style.color = '#059669';
                this.style.borderColor = '#10b981';
                setTimeout(() => {
                    this.innerText = originalText;
                    this.style.color = '';
                    this.style.borderColor = '';
                }, 1500);
            }).catch(() => {});
        });
    });
});
