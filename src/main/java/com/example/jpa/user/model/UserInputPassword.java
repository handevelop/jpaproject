package com.example.jpa.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserInputPassword {

    @NotBlank(message = "현재 비번은 필수")
    private String password;

    @Size(min = 8, max = 20, message = "8자 이상, 20자 이하로 입력해 주세요.")
    @NotBlank(message = "신규 비번은 필수")
    private String newPassword;
}
