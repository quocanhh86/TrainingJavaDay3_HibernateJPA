package com.example.springhibernatedatajpa.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Data
public class LoginRequest {

    @NotBlank(message = "Không được để trống email")
    @Email
    private String username;

    @NotBlank(message = "Không được để trống mật khẩu")
    @Size(min = 6, max = 32, message = "Mật khẩu ít nhất 6 ký tự và tối đa 32 ký tự")
    private String password;
}
