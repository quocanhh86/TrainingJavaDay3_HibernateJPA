package com.example.springhibernatedatajpa.model;

import com.example.springhibernatedatajpa.entity.Department;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class EmployeeDto {
    private UUID id;

    @NotBlank(message = "Họ không được để trống")
    private String firstName;

    @NotBlank(message = "Tên không được để trống")
    private String lastName;

    @NotNull(message = "Tuổi không được để trống")
    @Min(value = 18, message = "Tuổi tối thiểu là 18")
    @Max(value = 65, message = "Tuổi tối đa là 65")
    private Integer age;

    @NotBlank(message = "Email không được để trống")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "Định dạng email không chính xác")
    private String email;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^(84|0[3|5|7|8|9])([0-9]{8})$", message = "Định dạng số điện thoại không chính xác")
    private String phoneNumber;

    @NotBlank(message = "Địa chỉ của nhân viên không được để trống")
    private String address;

    private String avatarUrl;
    private MultipartFile avatar;

    @NotNull(message = "Vui lòng chọn phòng ban cho nhân viên")
    private UUID departmentId;

    private String departmentName;
}
