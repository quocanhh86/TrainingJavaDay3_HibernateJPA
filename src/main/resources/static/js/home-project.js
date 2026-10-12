document.addEventListener('DOMContentLoaded', function () {
    // 1. Modal backdrop click & escape key to close
    const modal = document.getElementById('projectModal');
    if (modal) {
        modal.addEventListener('click', function (e) {
            if (e.target === this) {
                window.location.href = '/project';
            }
        });

        document.addEventListener('keydown', function (e) {
            if (e.key === 'Escape') {
                window.location.href = '/project';
            }
        });
    }

    // 2. Real-time search filter for project table
    const searchInput = document.getElementById('prjSearchInput');
    const tableBody = document.getElementById('prjTableBody');
    if (searchInput && tableBody) {
        searchInput.addEventListener('input', function () {
            const query = this.value.trim().toLowerCase();
            const rows = tableBody.querySelectorAll('tr.prj-row');
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
            let noMatchRow = document.getElementById('prjNoMatchRow');
            if (visibleCount === 0 && rows.length > 0) {
                if (!noMatchRow) {
                    noMatchRow = document.createElement('tr');
                    noMatchRow.id = 'prjNoMatchRow';
                    noMatchRow.innerHTML = `<td colspan="8" style="text-align: center; color: #94a3b8; padding: 2rem;">Không tìm thấy dự án nào khớp với từ khóa "<strong>${query}</strong>"</td>`;
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
    const idBadges = document.querySelectorAll('.prj-info-value.mono');
    idBadges.forEach(badge => {
        badge.style.cursor = 'pointer';
        badge.title = 'Nhấn để sao chép ID';
        badge.addEventListener('click', function () {
            const text = this.innerText.trim();
            navigator.clipboard.writeText(text).then(() => {
                const originalText = this.innerText;
                this.innerText = 'Đã sao chép! ✓';
                this.style.color = '#059669';
                setTimeout(() => {
                    this.innerText = originalText;
                    this.style.color = '';
                }, 1500);
            }).catch(() => {});
        });
    });
});
