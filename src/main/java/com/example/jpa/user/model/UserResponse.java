package com.example.jpa.user.model;

import com.example.jpa.user.entity.User;
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
public class UserResponse {

    private long id;

    private String email;

    private String userNm;

    protected String phone;

    public UserResponse(User user) {
        this.id = user.getId ();
        this.email = user.getEmail ();
        this.userNm = user.getUserName ();
        this.phone = user.getPhone ();

    }

    public static UserResponse of(User user) {
        return UserResponse.builder ()
                .id (user.getId ())
                .email(user.getEmail ())
                .userNm(user.getUserName ())
                .phone (user.getPhone ())
                .build ();
    }
}
