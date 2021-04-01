package com.example.jpa.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdate {

    @Size(max = 20, message = "20자 이하로 입력해 주세요.")
    @NotBlank(message = "전화번호는 필수")
    private String phone;
}
