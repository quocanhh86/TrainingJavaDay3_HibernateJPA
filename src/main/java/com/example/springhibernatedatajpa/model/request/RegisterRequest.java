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
public class RegisterRequest {
    @NotBlank(message = "Không được để trống họ đệm")
    @Size(min = 1, max = 100, message = "Họ đệm không được vượt quá 100 ký tự")
    private String firstName;

    @NotBlank(message = "Không được để trống tên")
    @Size(min = 1, max = 100, message = "Tên không được vượt quá 100 ký tự")
    private String lastName;

    @NotBlank(message = "Không được để trống email")
    @Email(message = "Email không đúng định dạng")
    private String email;

    @NotBlank(message = "Không được để trống mật khẩu")
    @Size(min = 6, max = 32, message = "Mật khẩu ít nhất 6 ký tự và tối đa 32 ký tự")
    private String password;
}
