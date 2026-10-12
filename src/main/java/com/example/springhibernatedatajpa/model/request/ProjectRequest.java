package com.example.springhibernatedatajpa.model.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.UUID;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProjectRequest {
    private UUID id;

    @NotBlank(message = "Tên dự án không được để trống")
    private String name;

    @NotNull(message = "Budget dự án không được null")
    @Min(value = 1, message = "Budget của dự án quá ít")
    @Max(value = 99999999, message = "Budget của dự án quá lớn")
    private Double budget;

    @Past(message = "Thời điểm bắt đầu dự án phải ở tương lai hoặc hiện tại")
    @NotNull(message = "Không được để trống thời điểm bắt đầu dự án")
    private LocalDate startDate;

    @Past(message = "Thời điểm kết thúc dự án phải ở tương lai")
    private LocalDate endDate;

    @NotBlank(message = "Vui lòng chọn dự án thuộc phòng ban nào")
    private UUID departmentId;

    private MultipartFile contractFile;
    private String contractUrl;

    private UUID teamLeaderId;

}
