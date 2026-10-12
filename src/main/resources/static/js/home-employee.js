document.addEventListener('DOMContentLoaded', function () {
    const modal = document.getElementById('employeeModal');
    if (modal) {
        modal.addEventListener('click', function (e) {
            if (e.target === this) {
                window.location.href = '/employee';
            }
        });

        document.addEventListener('keydown', function (e) {
            if (e.key === 'Escape') {
                window.location.href = '/employee';
            }
        });
    }
});
