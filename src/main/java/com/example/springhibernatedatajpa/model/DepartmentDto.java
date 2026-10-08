package com.example.springhibernatedatajpa.model;

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
public class DepartmentDto {
    private UUID id;

    @NotBlank(message = "Tên phòng ban không được để trống")
    private String departmentName;
    private String description;
}
