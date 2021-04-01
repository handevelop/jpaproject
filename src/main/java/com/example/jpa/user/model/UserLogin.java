package com.example.jpa.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserLogin {

    @NotBlank(message = "이메일은 필수")
    private String email;

    @NotBlank(message = "비번은 필수")
    private String password;
}
