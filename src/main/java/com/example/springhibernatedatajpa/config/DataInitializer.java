package com.example.springhibernatedatajpa.config;

import com.example.springhibernatedatajpa.common.enums.Status;
import com.example.springhibernatedatajpa.dao.IDepartmentDao;
import com.example.springhibernatedatajpa.dao.IEmployeeDao;
import com.example.springhibernatedatajpa.dao.IProjectDao;
import com.example.springhibernatedatajpa.entity.Department;
import com.example.springhibernatedatajpa.entity.Employee;
import com.example.springhibernatedatajpa.entity.Project;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final IDepartmentDao departmentDao;
    private final IEmployeeDao employeeDao;
    private final IProjectDao projectDao;

    @Override
    @Transactional
    public void run(String... args) {
        // Kiểm tra nếu dữ liệu đã tồn tại đầy đủ thì bỏ qua
        List<Department> existingDepts = departmentDao.findAll();
        List<Project> existingProjects = projectDao.findAll();
        List<Employee> existingEmployees = employeeDao.getEmployees();

        if (!existingDepts.isEmpty() && !existingProjects.isEmpty() && !existingEmployees.isEmpty()) {
            log.info("Dữ liệu mẫu đã tồn tại đầy đủ (Departments, Projects, Employees), bỏ qua khởi tạo.");
            return;
        }

        log.info("Bắt đầu khởi tạo dữ liệu mẫu với các trường mới (BaseEntity status, liên kết Employee - Project)...");

        // 1. Tạo 5 Departments (Phòng ban)
        Department deptIT = Department.builder()
                .name("Phòng Công Nghệ Thông Tin")
                .description("Nghiên cứu, phát triển phần mềm và quản trị hạ tầng hệ thống")
                .build();
        deptIT.setStatus(Status.ACTIVE);

        Department deptFinance = Department.builder()
                .name("Phòng Kế Toán - Tài Chính")
                .description("Quản lý dòng tiền, kế toán tài chính và quyết toán thuế")
                .build();
        deptFinance.setStatus(Status.ACTIVE);

        Department deptHR = Department.builder()
                .name("Phòng Nhân Sự")
                .description("Tuyển dụng, đào tạo nhân sự và quản lý văn hóa doanh nghiệp")
                .build();
        deptHR.setStatus(Status.ACTIVE);

        Department deptMarketing = Department.builder()
                .name("Phòng Marketing")
                .description("Quảng bá thương hiệu, truyền thông và tiếp thị kỹ thuật số")
                .build();
        deptMarketing.setStatus(Status.ACTIVE);

        Department deptSales = Department.builder()
                .name("Phòng Kinh Doanh")
                .description("Phát triển thị trường, tìm kiếm đối tác và bán hàng")
                .build();
        deptSales.setStatus(Status.ACTIVE);

        List<Department> departments = Arrays.asList(deptIT, deptFinance, deptHR, deptMarketing, deptSales);
        for (Department dept : departments) {
            departmentDao.save(dept);
        }

        // 2. Tạo 10 Employees (Nhân viên)
        Employee empAn = Employee.builder()
                .firstName("Nguyễn Văn")
                .lastName("An")
                .age(28)
                .email("an.nguyen@company.com")
                .phoneNumber("0912345671")
                .address("Hà Nội")
                .avatar("https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150")
                .department(deptIT)
                .build();

        Employee empBich = Employee.builder()
                .firstName("Trần Thị")
                .lastName("Bích")
                .age(25)
                .email("bich.tran@company.com")
                .phoneNumber("0912345672")
                .address("Đà Nẵng")
                .avatar("https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=150")
                .department(deptIT)
                .build();

        Employee empCuong = Employee.builder()
                .firstName("Lê Hoàng")
                .lastName("Cường")
                .age(32)
                .email("cuong.le@company.com")
                .phoneNumber("0912345673")
                .address("TP. Hồ Chí Minh")
                .avatar("https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?w=150")
                .department(deptFinance)
                .build();

        Employee empDung = Employee.builder()
                .firstName("Phạm Thị")
                .lastName("Dung")
                .age(29)
                .email("dung.pham@company.com")
                .phoneNumber("0912345674")
                .address("Hải Phòng")
                .avatar("https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=150")
                .department(deptFinance)
                .build();

        Employee empDuc = Employee.builder()
                .firstName("Hoàng Minh")
                .lastName("Đức")
                .age(27)
                .email("duc.hoang@company.com")
                .phoneNumber("0912345675")
                .address("Cần Thơ")
                .avatar("https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150")
                .department(deptHR)
                .build();

        Employee empGiang = Employee.builder()
                .firstName("Vũ Thị")
                .lastName("Giang")
                .age(24)
                .email("giang.vu@company.com")
                .phoneNumber("0912345676")
                .address("Bắc Ninh")
                .avatar("https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150")
                .department(deptHR)
                .build();

        Employee empHuy = Employee.builder()
                .firstName("Đặng Quốc")
                .lastName("Huy")
                .age(35)
                .email("huy.dang@company.com")
                .phoneNumber("0912345677")
                .address("Quảng Ninh")
                .avatar("https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=150")
                .department(deptMarketing)
                .build();

        Employee empHang = Employee.builder()
                .firstName("Bùi Thu")
                .lastName("Hằng")
                .age(26)
                .email("hang.bui@company.com")
                .phoneNumber("0912345678")
                .address("Thừa Thiên Huế")
                .avatar("https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=150")
                .department(deptMarketing)
                .build();

        Employee empKhai = Employee.builder()
                .firstName("Ngô Quang")
                .lastName("Khải")
                .age(31)
                .email("khai.ngo@company.com")
                .phoneNumber("0912345679")
                .address("Nghệ An")
                .avatar("https://images.unsplash.com/photo-1522075469751-3a6694fb2f61?w=150")
                .department(deptSales)
                .build();

        Employee empLinh = Employee.builder()
                .firstName("Đỗ Mai")
                .lastName("Linh")
                .age(23)
                .email("linh.do@company.com")
                .phoneNumber("0912345680")
                .address("Thanh Hóa")
                .avatar("https://images.unsplash.com/photo-1517841905240-472988babdf9?w=150")
                .department(deptSales)
                .build();

        List<Employee> employees = Arrays.asList(
                empAn, empBich, empCuong, empDung, empDuc,
                empGiang, empHuy, empHang, empKhai, empLinh
        );

        for (Employee emp : employees) {
            emp.setStatus(Status.ACTIVE);
            employeeDao.addEmployee(emp);
        }

        // 3. Tạo 10 Projects (Dự án) - Gán Department, TeamLead và danh sách Employees
        Project prjERP = Project.builder()
                .name("Hệ thống ERP Doanh Nghiệp")
                .budget(500_000_000.0)
                .startDate(LocalDate.of(2026, 1, 15))
                .endDate(LocalDate.of(2026, 12, 31))
                .department(deptIT)
                .teamLead(empAn)
                .employees(new HashSet<>(Arrays.asList(empAn, empBich)))
                .contractUrl("hop_dong_erp.pdf")
                .build();
        prjERP.setStatus(Status.ACTIVE);

        Project prjCloud = Project.builder()
                .name("Nâng cấp Hạ tầng Cloud & DevOps")
                .budget(250_000_000.0)
                .startDate(LocalDate.of(2026, 3, 1))
                .endDate(LocalDate.of(2026, 9, 30))
                .department(deptIT)
                .teamLead(empBich)
                .employees(new HashSet<>(Arrays.asList(empBich)))
                .contractUrl("hop_dong_cloud.docx")
                .build();
        prjCloud.setStatus(Status.ACTIVE);

        Project prjFinanceAuto = Project.builder()
                .name("Phần mềm Quản lý Tài chính Tự động")
                .budget(180_000_000.0)
                .startDate(LocalDate.of(2026, 2, 10))
                .endDate(LocalDate.of(2026, 8, 15))
                .department(deptFinance)
                .teamLead(empCuong)
                .employees(new HashSet<>(Arrays.asList(empCuong, empDung)))
                .contractUrl("hop_dong_tai_chinh.pdf")
                .build();
        prjFinanceAuto.setStatus(Status.ACTIVE);

        Project prjTaxReport = Project.builder()
                .name("Tối ưu hóa Báo cáo Quyết toán Thuế")
                .budget(90_000_000.0)
                .startDate(LocalDate.of(2026, 4, 1))
                .endDate(LocalDate.of(2026, 7, 31))
                .department(deptFinance)
                .teamLead(empDung)
                .employees(new HashSet<>(Arrays.asList(empDung)))
                .contractUrl("hop_dong_thue.docx")
                .build();
        prjTaxReport.setStatus(Status.INACTIVE);

        Project prjKPI = Project.builder()
                .name("Hệ thống Quản lý Hiệu suất Nhân viên (KPIs)")
                .budget(120_000_000.0)
                .startDate(LocalDate.of(2026, 1, 5))
                .endDate(LocalDate.of(2026, 6, 30))
                .department(deptHR)
                .teamLead(empDuc)
                .employees(new HashSet<>(Arrays.asList(empDuc, empGiang)))
                .contractUrl("hop_dong_kpi.pdf")
                .build();
        prjKPI.setStatus(Status.ACTIVE);

        Project prjTraining = Project.builder()
                .name("Chương trình Đào tạo Hội nhập & Kỹ năng số")
                .budget(80_000_000.0)
                .startDate(LocalDate.of(2026, 5, 1))
                .endDate(LocalDate.of(2026, 11, 30))
                .department(deptHR)
                .teamLead(empGiang)
                .employees(new HashSet<>(Arrays.asList(empGiang)))
                .contractUrl("hop_dong_dao_tao.pdf")
                .build();
        prjTraining.setStatus(Status.ACTIVE);

        Project prjSummerCampaign = Project.builder()
                .name("Chiến dịch Thương hiệu Mùa Hè 2026")
                .budget(350_000_000.0)
                .startDate(LocalDate.of(2026, 4, 15))
                .endDate(LocalDate.of(2026, 8, 30))
                .department(deptMarketing)
                .teamLead(empHuy)
                .employees(new HashSet<>(Arrays.asList(empHuy, empHang)))
                .contractUrl("hop_dong_mua_he.pdf")
                .build();
        prjSummerCampaign.setStatus(Status.ACTIVE);

        Project prjSEO = Project.builder()
                .name("Tối ưu hóa SEO & Kênh Tiếp thị Đa nền tảng")
                .budget(150_000_000.0)
                .startDate(LocalDate.of(2026, 2, 1))
                .endDate(LocalDate.of(2026, 12, 15))
                .department(deptMarketing)
                .teamLead(empHang)
                .employees(new HashSet<>(Arrays.asList(empHang)))
                .contractUrl("hop_dong_seo.docx")
                .build();
        prjSEO.setStatus(Status.ACTIVE);

        Project prjCentralMarket = Project.builder()
                .name("Mở rộng Thị trường Khu vực Miền Trung")
                .budget(400_000_000.0)
                .startDate(LocalDate.of(2026, 3, 15))
                .endDate(LocalDate.of(2026, 10, 31))
                .department(deptSales)
                .teamLead(empKhai)
                .employees(new HashSet<>(Arrays.asList(empKhai, empLinh)))
                .contractUrl("hop_dong_mien_trung.pdf")
                .build();
        prjCentralMarket.setStatus(Status.ACTIVE);

        Project prjVIP = Project.builder()
                .name("Chương trình Tri ân Khách hàng VIP B2B")
                .budget(200_000_000.0)
                .startDate(LocalDate.of(2026, 6, 1))
                .endDate(LocalDate.of(2026, 12, 25))
                .department(deptSales)
                .teamLead(empLinh)
                .employees(new HashSet<>(Arrays.asList(empLinh)))
                .contractUrl("hop_dong_vip.pdf")
                .build();
        prjVIP.setStatus(Status.ACTIVE);

        List<Project> projects = Arrays.asList(
                prjERP, prjCloud, prjFinanceAuto, prjTaxReport,
                prjKPI, prjTraining, prjSummerCampaign, prjSEO,
                prjCentralMarket, prjVIP
        );

        for (Project prj : projects) {
            projectDao.add(prj);
        }

        log.info("Khởi tạo dữ liệu mẫu thành công: 5 Departments, 10 Employees, 10 Projects (đầy đủ quan hệ liên kết TeamLead, Employees, Department, Contract và Status).");
    }
}
