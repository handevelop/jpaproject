package com.example.jpa.pay.model;

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
public class UserInput {

    private long totalInvestingAmount;

    @Email(message = "이메일 형식에 맞게 입력해 주세요.")
    @NotBlank(message = "이메일은 필수")
    private String email;

    @NotBlank(message = "이름은 필수")
    private String userNm;

    @Size(min = 8, message = "8자 이상으로 입력해 주세요.")
    @NotBlank(message = "비번은 필수")
    private String password;

    @Size(max = 20, message = "20자 이하로 입력해 주세요.")
    @NotBlank(message = "전화번호는 필수")
    private String phone;
}
