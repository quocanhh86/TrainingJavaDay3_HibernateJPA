package com.example.springhibernatedatajpa.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class DepartmentRequest {
    private UUID id;

    @NotBlank(message = "Tên phòng ban không được để trống")
    private String departmentName;

    private String description;
}
