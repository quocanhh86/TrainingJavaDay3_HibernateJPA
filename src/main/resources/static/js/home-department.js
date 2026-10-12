document.addEventListener('DOMContentLoaded', function () {
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
});
