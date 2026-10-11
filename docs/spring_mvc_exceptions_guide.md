# GIÁO TRÌNH CHUẨN KIẾN TRÚC: QUẢN LÝ VÀ XỬ LÝ NGOẠI LỆ (EXCEPTION HANDLING) TRONG SPRING MVC

> **Mục tiêu bài học:**
> 1. Hiểu bản chất phân tầng: Khi nào ném Exception nào, tầng nào được phép ném gì.
> 2. Giải quyết triệt để bài toán **100+ màn hình**: View nhận thông báo lỗi thế nào, xử lý tại Controller hay tập trung tại `@ControllerAdvice`?
> 3. Nắm vững cơ chế cốt lõi của Web: Phân biệt **Forward vs Redirect**, hiểu cách hoạt động của **HTTP `Referer`** và **Flash Attributes**.
> 4. Biết cách thiết kế hệ thống báo lỗi chuyên nghiệp, tối ưu trải nghiệm người dùng (UX).

---

## MỤC LỤC
1. [Nguyên Tắc Cốt Lõi Về Phân Tầng (Layered Architecture)](#1-nguyên-tắc-cốt-lõi-về-phân-tầng)
2. [Phân Tích Sai Lầm Kinh Điển Trong Code Service](#2-phân-tích-sai-lầm-kinh-điển-trong-code-service)
3. [Bảng Ma Trận: Khi Nào Nên Ném Loại Exception Nào?](#3-bảng-ma-trận-khi-nào-nên-ném-loại-exception-nào)
4. [Code Mẫu Chuẩn Cho Từng Thao Tác Tầng Service](#4-code-mẫu-chuẩn-cho-từng-thao-tác-tầng-service)
5. [BÀI TOÁN THỰC TẾ: XỬ LÝ LỖI CHO 100+ MÀN HÌNH NHƯ THẾ NÀO?](#5-bài-toán-thực-tế-xử-lý-lỗi-cho-100-màn-hình-như-thế-nào)
   - [5.1. Tại sao code ví dụ trước đó lại viết `redirect:/employee`?](#51-tại-sao-code-ví-dụ-trước-đó-lại-viết-redirectemployee)
   - [5.2. Nền tảng lý thuyết Web cần biết: Forward vs Redirect & HTTP Referer](#52-nền-tảng-lý-thuyết-web-cần-biết-forward-vs-redirect--http-referer)
   - [5.3. Chiến lược 3 cấp độ: Xử lý ở Controller hay ở đâu?](#53-chiến-lược-3-cấp-độ-xử-lý-ở-controller-hay-ở-đâu)
6. [Hướng Dẫn Thực Hành Từng Bước (Step-by-Step Tutorial)](#6-hướng-dẫn-thực-hành-từng-bước)
   - [Bước 1: Tạo UI Alert dùng chung cho toàn dự án](#bước-1-tạo-ui-alert-dùng-chung-cho-toàn-dự-án)
   - [Bước 2: Cấu hình `GlobalException` động với Header `Referer`](#bước-2-cấu-hình-globalexception-động-với-header-referer)
   - [Bước 3: Xử lý tại Controller riêng cho các màn nhập Form (Giữ dữ liệu Form)](#bước-3-xử-lý-tại-controller-riêng-cho-các-màn-nhập-form)
   - [Bước 4: Thiết lập trang lỗi tập trung 404/500](#bước-4-thiết-lập-trang-lỗi-tập-trung-404500)
7. [Bảng Quyết Định (Decision Matrix) & Tổng Kết](#7-bảng-quyết-định-decision-matrix--tổng-kết)

---

## 1. Nguyên Tắc Cốt Lõi Về Phân Tầng

Trong một ứng dụng doanh nghiệp chuẩn, luồng xử lý và Exception được phân định ranh giới nghiêm ngặt:

```
[Trình duyệt] ──HTTP Request──> [Controller Layer] ──────> [Service Layer] ──────> [Database]
                                     │                           │
                             Spring Framework              Lập trình viên
                                 tự ném                        tự ném
                                     │                           │
                                     ▼                           ▼
                        (Framework/Web Exceptions)      (Domain/Business Exceptions)
                        - MissingPathVariableException  - IllegalArgumentException
                        - MethodArgumentNotValid        - NoSuchElementException
                        - HttpRequestMethodNotSupported - IllegalStateException
```

> [!CAUTION]
> **Quy tắc vàng 1:** **Tầng Service tuyệt đối KHÔNG BAO GIỜ ném các Exception của tầng Web.**
> Các Exception như `MissingPathVariableException`, `MissingServletRequestParameterException` thuộc về Spring Framework ở tầng Servlet. Chúng phụ thuộc vào `HttpServletRequest`, `MethodParameter`... Tầng Service là tầng thuần logic Java, không được phép "biết" Controller hay Servlet tồn tại.

> [!IMPORTANT]
> **Quy tắc vàng 2:** **Tầng Service chỉ làm việc với Java Core và Dữ liệu.**
> Khi vi phạm điều kiện logic, Service chỉ ném Exception thuần Java (`IllegalArgumentException`, `NoSuchElementException`, `IllegalStateException`) hoặc Custom Exception của dự án.

---

## 2. Phân Tích Sai Lầm Kinh Điển Trong Code Service

### ❌ Đoạn code sai phạm thường gặp:
```java
@Override
public EmployeeResponse findById(UUID id) throws NoSuchMethodException {
    MethodParameter methodParameter = new MethodParameter(
        EmployeeController.class.getMethod("getEmployeeById"), UUID.class.getModifiers()), 0; // ❌ SAI NẶNG
    if (id.equals("") || id == null) {
        throw new MissingPathVariableException("id", id); // ❌ SAI NẶNG
    }
    ...
}
```

### 🔍 Tại sao sai?
1. **Vi phạm Dependency Inversion (Phụ thuộc ngược):** 
   - Tầng `Service` (nghiệp vụ bên trong) lại đi `import EmployeeController` (tầng Web bên ngoài) và dùng Reflection (`getMethod`) để soi Controller. Nguyên lý thiết kế quy định: Controller phụ thuộc Service, chiều ngược lại bị cấm tuyệt đối.
2. **`MissingPathVariableException` là việc của Spring Framework:**
   - Khi người dùng gõ thiếu ID trên URL, **Spring Framework tự động ném Exception này tại tầng Filter/DispatcherServlet**, code Service thậm chí chưa từng được gọi đến! Lập trình viên không bao giờ cần tự `new` class này.
3. **`id.equals("")` gây `NullPointerException`:**
   - Nếu `id == null`, gọi `.equals("")` trước sẽ làm sập ứng dụng (Crash) ngay lập tức trước khi kiểm tra `id == null`.

---

## 3. Bảng Ma Trận: Khi Nào Nên Ném Loại Exception Nào?

| Tình huống thực tế trong code | Exception nên ném (Chuẩn Java) | Custom Exception tương ứng | Ý nghĩa HTTP Status |
| :--- | :--- | :--- | :--- |
| **1. Tham số truyền vào hàm bị `null`, rỗng, hoặc âm** | `IllegalArgumentException` | `BadRequestException` | 400 Bad Request |
| **2. Tìm kiếm trong Database nhưng không có bản ghi** | `NoSuchElementException` | `ResourceNotFoundException` | 404 Not Found |
| **3. Dữ liệu bị trùng lặp (Email, Mã phòng ban đã tồn tại)** | `IllegalStateException` | `DuplicateResourceException` | 409 Conflict |
| **4. Vi phạm điều kiện nghiệp vụ logic (vd: tuổi < 18)** | `IllegalStateException` | `BusinessRuleViolationException`| 422 Unprocessable |
| **5. Vi phạm ràng buộc xóa dữ liệu (vẫn còn dữ liệu con)** | `IllegalStateException` | `DataConstraintViolationException`| 409 Conflict |
| **6. Lỗi đọc ghi file ảnh / đĩa cứng** | `RuntimeException` bọc `IOException` | `FileStorageException` | 500 Internal Error |

---

## 4. Code Mẫu Chuẩn Cho Từng Thao Tác Tầng Service

### 4.1. Hàm tìm kiếm (`findById`)
```java
@Override
public EmployeeResponse findById(UUID id) {
    // 1. Kiểm tra tham số đầu vào
    if (id == null) {
        throw new IllegalArgumentException("Mã nhân viên (ID) không được để trống!");
    }

    // 2. Query cơ sở dữ liệu
    Employee employee = employeeDao.getEmployee(id);

    // 3. Kiểm tra kết quả
    if (employee == null) {
        throw new NoSuchElementException("Không tìm thấy nhân viên với ID: " + id);
    }

    return mapToResponse(employee);
}
```

### 4.2. Hàm xóa (`deleteById`)
```java
@Override
public void deleteById(UUID id) {
    if (id == null) {
        throw new IllegalArgumentException("Mã nhân viên (ID) không được để trống!");
    }

    Employee employee = employeeDao.getEmployee(id);
    if (employee == null) {
        throw new NoSuchElementException("Nhân viên cần xóa không tồn tại trong hệ thống!");
    }

    employeeDao.deleteEmployee(id);
}
```

---

## 5. BÀI TOÁN THỰC TẾ: XỬ LÝ LỖI CHO 100+ MÀN HÌNH NHƯ THẾ NÀO?

Đây là câu hỏi cốt lõi mà bất kỳ lập trình viên nào khi chuyển từ làm bài tập nhỏ sang dự án thực tế đều gặp phải:
> *"Trong bài giảng trước tôi thấy viết `return "redirect:/employee";`. Thế nếu tôi có 100 màn hình (Phòng ban, Dự án, Đơn hàng, Kho, Khách hàng...) thì xử lý thế nào để View nhận được thông báo lỗi? Xử lý trong Controller hay ở đâu?"*

### 5.1. Tại sao code ví dụ trước đó lại viết `redirect:/employee`?
- Đoạn code `return "redirect:/employee";` trong ví dụ ban đầu **chỉ là code demo (hardcode)** cho một màn hình Nhân viên duy nhất để bạn dễ hình dung luồng dữ liệu cơ bản.
- **Trong dự án thực tế 100 màn hình, TUYỆT ĐỐI KHÔNG ĐƯỢC hardcode như vậy!** Nếu hardcode, khi bạn đang ở màn "Xóa phòng ban" mà gặp lỗi, người dùng lại bị đá văng sang màn "Danh sách nhân viên" - đây là một lỗi ngớ ngẩn về mặt logic.

---

### 5.2. Nền tảng lý thuyết Web cần biết: Forward vs Redirect & HTTP Referer

Để hiểu giải pháp kiến trúc cho 100 màn hình, bạn cần nắm vững 3 khái niệm sau:

#### A. Khái niệm HTTP `Referer` (Người chỉ đường) — Trình duyệt TỰ ĐỘNG CÓ!

> **Câu hỏi hay gặp của học viên:** *"Phần `Referer` này chúng ta có phải tự add vào header không hay nó đang được set ở đâu? Tự động có à?"*

**Câu trả lời: TỰ ĐỘNG 100%! Lập trình viên KHÔNG CẦN và KHÔNG PHẢI tự add bằng tay.**

1. **Ai là người tạo ra header này?**
   - Chính **Trình duyệt Web** (Google Chrome, Edge, Firefox, Safari...) là người tự động sinh ra header này theo chuẩn giao thức HTTP thế giới (RFC 7231).
   - Khi bạn đang đứng ở trang A (`http://localhost:8080/department`), và bạn thực hiện bất kỳ hành động nào:
     - Bấm một thẻ link: `<a href="...">`
     - Bấm nút Submit một form: `<form action="/department/remove/123" method="POST">`
     - Hoặc gọi JavaScript `fetch()` / `axios`
   - Trình duyệt sẽ tự động chèn thêm một dòng vào gói tin Request gửi lên Server:
     ```http
     POST /department/remove/123 HTTP/1.1
     Host: localhost:8080
     Referer: http://localhost:8080/department
     User-Agent: Mozilla/5.0 ...
     ```
   - Khi request đến Spring Boot, Tomcat sẽ bọc toàn bộ header này vào đối tượng `HttpServletRequest`. Bạn chỉ việc gọi hàm `request.getHeader("Referer")` là lấy ra được ngay URL trang trước đó!

2. **Cách tự kiểm chứng bằng mắt trên máy tính (F12 DevTools):**
   - Mở trình duyệt lên trang bất kỳ trong dự án của bạn.
   - Bấm phím `F12` (hoặc chuột phải -> `Inspect`), chọn tab **Network**.
   - Bấm nút "Xóa" hoặc "Thêm mới" trên giao diện.
   - Click vào dòng request vừa xuất hiện bên tab Network -> nhìn sang cột **Headers** -> cuộn xuống mục **Request Headers** -> bạn sẽ thấy tận mắt dòng `Referer: http://localhost:8080/...`.

3. **Khi nào thì `Referer` bị `null`? (Tại sao code Java luôn phải kiểm tra `null`?):**
   Trình duyệt sẽ **KHÔNG** gửi header `Referer` trong các tình huống sau:
   - Người dùng mở một Tab mới toanh và gõ trực tiếp URL vào thanh địa chỉ rồi gõ Enter (vì họ không đi từ trang nào đến cả).
   - Người dùng click vào một đường link được lưu trong Bookmark (Dấu trang).
   - Chuyển hướng từ trang bảo mật `HTTPS` sang trang thường `HTTP` (trình duyệt bảo vệ quyền riêng tư người dùng).
   - Website có gắn thẻ cấu hình `<meta name="referrer" content="no-referrer">`.
   
   👉 **Chính vì vậy, trong code Java của chúng ta BẮT BUỘC phải viết dòng dự phòng (Fallback):**
   ```java
   String referer = request.getHeader("Referer");
   if (referer != null && !referer.isBlank()) {
       return "redirect:" + referer; // Có trang trước đó -> quay lại đúng trang đó
   }
   return "redirect:/"; // Không có referer -> an toàn nhất là về Trang chủ!
   ```

*(Thú vị: Chữ `Referer` trong chuẩn HTTP thực chất là một lỗi chính tả từ năm 1996 của tác giả giao thức HTTP — viết đúng tiếng Anh phải là `Referrer` với 2 chữ 'r', nhưng cả thế giới vẫn giữ nguyên chữ `Referer` đến tận ngày nay).*

#### B. Phân biệt FORWARD và REDIRECT trong xử lý lỗi

| Tiêu chí | FORWARD (`return "view-name";`) | REDIRECT (`return "redirect:/url";`) |
| :--- | :--- | :--- |
| **Bản chất** | Chuyển tiếp nội bộ trong Server | Server trả về mã 302, bảo Trình duyệt gửi GET mới |
| **URL trên trình duyệt** | Giữ nguyên URL cũ | Đổi sang URL mới |
| **Dữ liệu trong Model** | **Vẫn còn nguyên vẹn** | **Bị xóa sạch** (vì là request mới) |
| **Trải nghiệm nhập Form** | **Giữ được dữ liệu người dùng vừa gõ** | Mất sạch dữ liệu người dùng vừa gõ! |
| **Flash Attribute** | Không cần dùng | Phải dùng `RedirectAttributes.addFlashAttribute()` |

---

### 5.3. Chiến lược 3 cấp độ: Xử lý ở Controller hay ở đâu?

Đối với 100 màn hình, chúng ta chia các lỗi ra làm **3 kịch bản thực tế** và đặt vị trí xử lý ở đúng nơi:

```
                                  CÓ LỖI XẢY RA
                                        │
        ┌───────────────────────────────┼───────────────────────────────┐
        ▼                               ▼                               ▼
[Kịch bản 1: Thao tác nhanh]   [Kịch bản 2: Form nhập liệu]   [Kịch bản 3: Lỗi chết trang]
(Bấm nút Xóa, Đổi trạng thái)   (Form Thêm mới/Sửa 15 ô input)  (Gõ sai link URL, ID 404, 500)
        │                               │                               │
  Xử lý tại:                      Xử lý tại:                      Xử lý tại:
@ControllerAdvice               Controller của màn đó           @ControllerAdvice
        │                               │                               │
Dùng HTTP "Referer"             Dùng try-catch + FORWARD        Trả về view lỗi tập trung
Redirect về đúng màn cũ         Giữ nguyên input người gõ       (templates/error/404.html)
```

---

## 6. HƯỚNG DẪN THỰC HÀNH TỪNG BƯỚC

### Bước 1: Tạo UI Alert dùng chung cho toàn dự án
Tạo một file fragment alert tại `src/main/resources/templates/fragments/alert.html`:

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<body>

<!-- Fragment này sẽ hiển thị ở bất kỳ màn nào nếu có biến businessError -->
<div th:fragment="error-alert"
     th:if="${businessError != null}"
     class="alert alert-danger"
     style="padding: 12px; margin: 10px 0; background-color: #f8d7da; color: #721c24; border-radius: 4px;">
    <strong>⚠️ Thông báo: </strong>
    <span th:text="${businessError}"></span>
</div>

</body>
</html>
```

Nhúng fragment này vào file layout hoặc header dùng chung (`fragments/header.html`):
```html
<header th:fragment="header">
    <!-- Menu điều hướng của bạn... -->
</header>
<!-- Đặt alert ngay dưới header: 100 màn hình tự động có khung thông báo lỗi! -->
<div th:replace="~{fragments/alert :: error-alert}"></div>
```

---

### Bước 2: Cấu hình `GlobalException` động với Header `Referer`
*(Xử lý Kịch bản 1: Bấm nút Xóa, Cập nhật trạng thái... trên 100 màn hình)*

Đặt tại `src/main/java/.../common/GlobalException.java`:

```java
package com.example.springhibernatedatajpa.common;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.NoSuchElementException;

@ControllerAdvice
@Slf4j
public class GlobalException {

    /**
     * Bắt các lỗi nghiệp vụ chung cho 100+ màn hình:
     * - Tham số sai (IllegalArgumentException)
     * - Trạng thái sai / trùng lặp (IllegalStateException)
     * - Không tìm thấy dữ liệu (NoSuchElementException)
     */
    @ExceptionHandler({
        IllegalArgumentException.class,
        IllegalStateException.class,
        NoSuchElementException.class
    })
    public String handleBusinessException(RuntimeException ex,
                                          HttpServletRequest request,
                                          RedirectAttributes redirectAttributes) {
        log.warn("Ngoại lệ nghiệp vụ: {}", ex.getMessage());

        // 1. Gán message lỗi vào FlashAttribute (sống qua 1 lần redirect)
        redirectAttributes.addFlashAttribute("businessError", ex.getMessage());

        // 2. Lấy URL của màn hình mà người dùng vừa đứng thao tác
        String referer = request.getHeader("Referer");

        // 3. Tự động quay về đúng màn hình đó!
        if (referer != null && !referer.isBlank()) {
            return "redirect:" + referer;
        }

        // Dự phòng: Nếu người dùng truy cập trực tiếp không có referer thì về trang chủ
        return "redirect:/";
    }

    /**
     * Bắt các lỗi hệ thống không mong muốn (500 Server Error)
     */
    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex,
                                         HttpServletRequest request,
                                         RedirectAttributes redirectAttributes) {
        log.error("Lỗi hệ thống chưa phân loại: ", ex);
        redirectAttributes.addFlashAttribute("businessError", "Lỗi hệ thống: " + ex.getMessage());

        String referer = request.getHeader("Referer");
        if (referer != null && !referer.isBlank()) {
            return "redirect:" + referer;
        }
        return "redirect:/";
    }
}
```

> **Giải thích cơ chế hoạt động:**
> - Nếu người dùng đang ở `/department` và bấm xóa một phòng ban còn nhân viên -> Service ném `IllegalStateException("Không thể xóa phòng ban đang có nhân viên!")`.
> - `GlobalException` bắt được, đọc header `Referer` = `http://localhost:8080/department`.
> - Trả về `redirect:http://localhost:8080/department` kèm flash message.
> - Trình duyệt load lại chính trang `/department`, thẻ `alert` bắt được biến `${businessError}` và hiện lên thông báo đỏ. **Bạn không cần viết một dòng code nào trong DepartmentController!**

---

### Bước 3: Xử lý tại Controller riêng cho các màn nhập Form
*(Xử lý Kịch bản 2: Màn Thêm mới / Chỉnh sửa form dài)*

> ⚠️ **Quy tắc vàng về Trải nghiệm người dùng (UX):**
> Nếu người dùng vừa bỏ công gõ 20 trường thông tin vào form, nhưng khi bấm "Lưu" bị lỗi trùng Email:
> - Nếu bạn để `GlobalException` redirect lại: **Toàn bộ 20 trường thông tin người dùng vừa gõ sẽ biến mất trắng!** Người dùng sẽ cực kỳ bực bội.
> - Vì vậy, tại các hàm xử lý Form trong Controller, hãy dùng `try-catch` để **Forward** (render lại chính template đó), giữ nguyên object request:

```java
@PostMapping("/add")
public String createEmployee(@Validated @ModelAttribute("employeeRequest") EmployeeRequest request,
                             BindingResult bindingResult, 
                             Model model) throws IOException {
    // 1. Lỗi định dạng do Bean Validation (@NotBlank, @Email...)
    if (bindingResult.hasErrors()) {
        model.addAttribute("departmentList", departmentService.findAll());
        return "employee/add-employee"; // Forward lại, giữ nguyên dữ liệu đã gõ
    }

    // 2. Lỗi nghiệp vụ từ tầng Service (trùng email, dữ liệu logic...)
    try {
        employeeService.addEmployee(request);
        return "redirect:/employee"; // Thành công thì mới redirect về danh sách
    } catch (IllegalStateException | IllegalArgumentException ex) {
        // Bắt lỗi nghiệp vụ tại đây để giữ dữ liệu cho người dùng
        model.addAttribute("businessError", ex.getMessage());
        model.addAttribute("departmentList", departmentService.findAll());
        return "employee/add-employee"; // Forward lại view form, KHÔNG redirect!
    }
}
```

---

### Bước 4: Thiết lập trang lỗi tập trung 404/500
*(Xử lý Kịch bản 3: Người dùng gõ link bậy trên thanh địa chỉ)*

Nếu người dùng cố tình gõ vào thanh địa chỉ: `http://localhost:8080/employee/00000000-0000-0000-0000-000000000000` (ID không tồn tại):
- Khi này `Referer` chính là URL hỏng đó, nếu redirect lại sẽ bị lặp vô tận (Loop).
- Giải pháp: Trong `GlobalException`, ta trả về một trang hiển thị lỗi tập trung `error/404`:

```java
@ExceptionHandler(NoSuchElementException.class)
public ModelAndView handleNotFound(NoSuchElementException ex) {
    ModelAndView mav = new ModelAndView("error/404");
    mav.addObject("errorMessage", ex.getMessage());
    return mav;
}
```

Tạo file `src/main/resources/templates/error/404.html`:
```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head><title>404 - Không tìm thấy dữ liệu</title></head>
<body style="text-align: center; padding: 50px;">
    <h1>🔍 404 - Không Tìm Thấy</h1>
    <p th:text="${errorMessage}">Nội dung yêu cầu không tồn tại.</p>
    <a th:href="@{/}" class="btn btn-primary">Quay lại Trang chủ</a>
</body>
</html>
```

---

## 7. BẢNG QUYẾT ĐỊNH (DECISION MATRIX)

Khi xây dựng một chức năng trong Spring MVC, bạn chỉ cần nhìn vào bảng quyết định này:

| Loại màn hình / Thao tác | Xử lý ở đâu? | Cơ chế chuyển trang | View nhận lỗi thế nào? |
| :--- | :--- | :--- | :--- |
| **Nút Xóa / Khóa / Duyệt (Thao tác nhanh trên bảng)** | `@ControllerAdvice` (`GlobalException`) | `redirect: + referer` (Quay về trang cũ) | `redirectAttributes.addFlashAttribute("businessError")` |
| **Gõ sai URL / ID không tồn tại trên thanh địa chỉ** | `@ControllerAdvice` (`GlobalException`) | Render trang lỗi chung (`error/404`) | `ModelAndView("error/404")` |
| **Lỗi validate form định dạng (Spring `@Valid`)** | Request DTO + **Controller** | **Forward** (`BindingResult.hasErrors()`) | Thymeleaf `th:errors="*{field}"` |
| **Lỗi nghiệp vụ Form (Trùng email, sai khóa ngoại)** | Custom Validator Annotation | **Forward** (`BindingResult.hasErrors()`) | Thymeleaf `th:errors="*{field}"` (Không cần try-catch!) |

---

## 8. CHUẨN KIẾN TRÚC SOLID & MVC: BỘ TỨ LỖI KINH ĐIỂN
*(NULL - TRÙNG - KHÔNG TỒN TẠI - LOGIC NGHIỆP VỤ)*

> **Vấn đề đặt ra:**
> *"Ở RESTful API, tôi xử lý rất nhẹ nhàng: Request validate định dạng, Service kiểm tra nghiệp vụ và ném Exception, `@RestControllerAdvice` quẳng ra JSON là xong vì Client (React/Vue/Mobile) tự giữ dữ liệu form. Nhưng sang Spring MVC Monolith (Thymeleaf), nếu Controller hàm nào cũng `try-catch` thì code bị 'bẩn' và vi phạm SOLID. Vậy cách làm nào là CHUẨN NHẤT ĐẲNG CẤP SENIOR?"*

### 8.1. Bảng So Sánh Chiến Lược: RESTful API vs Spring MVC Monolith

| Loại lỗi | Bản chất lỗi | Trong RESTful API | Trong Spring MVC Monolith (Chuẩn SOLID - KHÔNG try-catch) |
| :--- | :--- | :--- | :--- |
| **1. NULL / Rỗng / Sai format** | Vi phạm cú pháp dữ liệu | DTO Annotation (`@NotNull`, `@NotBlank`) ➔ `@RestControllerAdvice` trả JSON 400. | **DTO Request Annotation** (`@NotNull`, `@NotBlank`) ➔ `bindingResult.hasErrors()` ➔ Forward view, `th:errors` đỏ chân từng ô input. |
| **2. Trùng lặp (Duplicate)**<br>*(Email, SĐT, Mã)* | Vi phạm tính duy nhất trong DB | Service check `existsBy...` ➔ throw `DuplicateException` ➔ Trả JSON 409 Conflict. | **Custom Constraint Validator** (`@UniqueEmail`) ngay tại Request DTO ➔ Tự động vào `BindingResult` báo đỏ ngay ô input. **Controller sạch 100%, không try-catch!** |
| **3. Không tồn tại (Not Found)**<br>*(ID cha, Khóa ngoại)* | Khóa ngoại không có thật trong DB | Service check `findById == null` ➔ throw `NotFoundException` ➔ Trả JSON 404. | **Trường hợp Form (chọn phòng ban):** Dùng Custom Validator `@DepartmentExists` ➔ Báo đỏ dropdown.<br>**Trường hợp URL (`/employee/{id}`):** Service ném `NoSuchElementException` ➔ `GlobalException` render `error/404.html`. |
| **4. Vi phạm Logic nghiệp vụ**<br>*(Tuổi < 18, Xóa phòng ban có người...)* | Quy tắc kinh doanh phức tạp | Service ném `BusinessException` ➔ Trả JSON 422 Unprocessable Entity. | **Service** ném `BusinessException` ➔ `GlobalException` đọc HTTP Header **`Referer`** ➔ `redirect:` về trang cũ kèm Alert đỏ. **Controller không cần try-catch!** |

---

### 8.2. Chi Tiết Thực Thi Bộ Tứ Lỗi (Clean Architecture & SOLID)

```
[HTTP Request gửi đến]
          │
          ▼
┌────────────────────────────────────────────────────────────────────────┐
│ BƯỚC 1: XỬ LÝ LỖI NULL & LỖI TRÙNG (Tại Request DTO qua Bean Validation)│
│  - @NotBlank, @Min, @Email... (Lỗi cú pháp)                            │
│  - @UniqueEmail, @DepartmentExists (Lỗi nghiệp vụ truy vấn DB ngầm)   │
└────────────────────────────────────────────────────────────────────────┘
          │ (Nếu có lỗi ➔ Controller dừng ngay, Forward render lại form)
          ▼
┌────────────────────────────────────────────────────────────────────────┐
│ BƯỚC 2: CONTROLLER SIÊU TINH GỌN (Single Responsibility)               │
│  if (bindingResult.hasErrors()) return "form-view";                   │
│  service.doSomething(request);                                         │
│  return "redirect:/success";                                           │
│  👉 TUYỆT ĐỐI KHÔNG CÓ KHỐI TRY-CATCH NÀO!                             │
└────────────────────────────────────────────────────────────────────────┘
          │ (Dữ liệu hợp lệ ➔ Chuyển giao xuống Service)
          ▼
┌────────────────────────────────────────────────────────────────────────┐
│ BƯỚC 3: SERVICE XỬ LÝ LỖI KHÔNG TỒN TẠI & LOGIC NGHIỆP VỤ LÕI          │
│  - Kiểm tra điều kiện: if (age < 18) throw new BusinessRuleException   │
│  - Tìm kiếm bản ghi: if (emp == null) throw new NoSuchElementException │
└────────────────────────────────────────────────────────────────────────┘
          │ (Nếu vi phạm ➔ Ném Exception văng ra ngoài)
          ▼
┌────────────────────────────────────────────────────────────────────────┐
│ BƯỚC 4: GLOBALEXCEPTION (@ControllerAdvice) HỨNG VÀ ĐIỀU HƯỚNG        │
│  - Gặp NoSuchElementException ➔ Render templates/error/404.html        │
│  - Gặp BusinessRuleException  ➔ Đọc Header 'Referer', redirect về cũ   │
│    kèm FlashAttribute("businessError") ➔ Hiện Alert đỏ rực rỡ          │
└────────────────────────────────────────────────────────────────────────┘
```

---

### 8.3. Hướng Dẫn Code Mẫu Chi Tiết Từng Phần

#### Phần 1: Xử lý lỗi TRÙNG mà KHÔNG cần try-catch trong Controller
Tạo Custom Annotation `@UniqueEmail`:

```java
// 1. Khai báo Annotation
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueEmailValidator.class)
public @interface UniqueEmail {
    String message() default "Email này đã được sử dụng trong hệ thống!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

// 2. Validator truy vấn DB (Spring tự động inject DAO vào đây)
@Component
@RequiredArgsConstructor
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {
    private final IEmployeeDao employeeDao;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.isBlank()) return true; // Để @NotBlank lo phần null/rỗng
        return !employeeDao.existsByEmail(email); // Trả về false nếu email đã tồn tại
    }
}
```

Gắn trực tiếp vào `EmployeeRequest`:
```java
public class EmployeeRequest {
    @NotBlank(message = "Email không được để trống")
    @Email(message = "Định dạng email không hợp lệ")
    @UniqueEmail // 👈 Tự động kiểm tra trùng trong DB!
    private String email;
    ...
}
```

#### Phần 2: Controller chuẩn MVC Monolith (100% Không có try-catch)
```java
@PostMapping("/add")
public String createEmployee(@Validated @ModelAttribute("employeeRequest") EmployeeRequest request,
                             BindingResult bindingResult, 
                             Model model) throws IOException {
    // 👈 Bắt cả lỗi định dạng LẪN lỗi trùng email DB tại đây!
    if (bindingResult.hasErrors()) {
        model.addAttribute("departmentList", departmentService.findAll());
        return "employee/add-employee"; // Forward giữ nguyên form và hiện chữ đỏ dưới ô email
    }

    // 👈 Controller chỉ làm đúng 1 việc: gọi Service và redirect
    employeeService.addEmployee(request);
    return "redirect:/employee";
}
```

#### Phần 3: Service xử lý Logic nghiệp vụ lõi (Clean & Purity)
```java
@Override
public void deleteById(UUID id) {
    if (id == null) {
        throw new IllegalArgumentException("Mã nhân viên không được để trống!");
    }

    Employee employee = employeeDao.getEmployee(id);
    if (employee == null) {
        // Lỗi không tồn tại
        throw new NoSuchElementException("Không tìm thấy nhân viên cần xóa!");
    }

    // Lỗi vi phạm logic nghiệp vụ
    if (employee.getProject() != null) {
        throw new IllegalStateException("Không thể xóa nhân viên đang phụ trách dự án: " 
                + employee.getProject().getName());
    }

    employeeDao.deleteEmployee(id);
}
```

#### Phần 4: `GlobalException` làm trạm điều phối tự động
```java
@ControllerAdvice
@Slf4j
public class GlobalException {

    // 1. Xử lý lỗi Không tồn tại khi truy cập URL trực tiếp ➔ Trang 404 chuyên nghiệp
    @ExceptionHandler(NoSuchElementException.class)
    public ModelAndView handleNotFound(NoSuchElementException ex) {
        ModelAndView mav = new ModelAndView("error/404");
        mav.addObject("errorMessage", ex.getMessage());
        return mav;
    }

    // 2. Xử lý lỗi Vi phạm nghiệp vụ ➔ Tự động quay về đúng trang cũ bằng Referer + Alert đỏ
    @ExceptionHandler({IllegalStateException.class, IllegalArgumentException.class})
    public String handleBusinessViolation(RuntimeException ex, 
                                          HttpServletRequest request, 
                                          RedirectAttributes redirectAttributes) {
        log.warn("Vi phạm quy tắc nghiệp vụ: {}", ex.getMessage());
        redirectAttributes.addFlashAttribute("businessError", ex.getMessage());

        String referer = request.getHeader("Referer");
        return (referer != null && !referer.isBlank()) ? "redirect:" + referer : "redirect:/";
    }
}
```

---

### 8.4. Đối Chiếu Chuẩn 5 Nguyên Lý SOLID

1. **S - Single Responsibility Principle (Đơn trách nhiệm):**
   * **Request DTO:** Chịu trách nhiệm kiểm tra tính hợp lệ dữ liệu đầu vào của Form (bao gồm cả cú pháp và tính duy nhất).
   * **Controller:** Chỉ làm người điều phối giao thông (Traffic Controller): nhận request, kiểm tra `BindingResult`, chuyển xuống Service, trả view. Không có một dòng `try-catch` nghiệp vụ nào.
   * **Service:** Chịu trách nhiệm xử lý logic kinh doanh thuần túy, không biết Controller hay HTML là gì.
   * **GlobalException:** Chịu trách nhiệm tập trung toàn bộ việc dịch ngoại lệ thành điều hướng View.
2. **O - Open/Closed Principle (Mở rộng / Đóng sửa đổi):**
   * Bạn thêm màn hình thứ 50, 100 hay 1000: Bạn chỉ việc tạo thêm DTO, Controller và Service.
   * Toàn bộ cơ chế báo lỗi, điều hướng bằng `Referer`, trang 404 trong `GlobalException` **đóng hoàn toàn với việc sửa đổi** nhưng **mở rộng tự động cho mọi màn hình mới**.
3. **L - Liskov Substitution Principle (Thay thế Liskov):**
   * Các exception nghiệp vụ kế thừa từ `RuntimeException`, `BaseEntity` kế thừa an toàn trên mọi module.
4. **I - Interface Segregation Principle (Phân tách Interface):**
   * Tách biệt rõ `EmployeeRequest` (cho Form nhập) và `EmployeeResponse` (cho hiển thị bảng). Không để trường thừa thãi.
5. **D - Dependency Inversion Principle (Đảo ngược phụ thuộc):**
   * Controller phụ thuộc vào `IEmployeeService` (Interface).
   * Service phụ thuộc vào `IEmployeeDao` (Interface).
   * Custom Validator phụ thuộc vào DAO Interface, không phụ thuộc class cụ thể.

