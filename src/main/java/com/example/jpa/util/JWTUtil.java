package com.example.jpa.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.jpa.user.entity.User;
import com.example.jpa.user.model.UserLoginToken;
import lombok.experimental.UtilityClass;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@UtilityClass
public class JWTUtil {

    private static final String JWTKey = "lotteon";
    private static final String CLAIM_USER_ID = "user_id";

    public static UserLoginToken createToken(User user) {

        // null 처리
        if (user == null) {
            return null;
        }

        // 현재보다 1개월 더해진 날짜
        LocalDateTime expiredDateTime = LocalDateTime.now ().plusMonths (1);
        Date expiredDate = Timestamp.valueOf (expiredDateTime);

        // 1개월 동안 유효한 Json Web Token 발행
        String token = JWT.create ()
                .withExpiresAt (expiredDate)
                .withClaim (CLAIM_USER_ID, user.getId ())
                .withSubject (user.getUserName ())
                .withIssuer (user.getEmail ())
                .sign (Algorithm.HMAC512 (JWTKey.getBytes ()));

        return UserLoginToken.builder ()
                .token (token)
                .build ();
    }


    public String getIssuer(String token) {
        return JWT.require (Algorithm.HMAC512 (JWTKey.getBytes ()))
                .build ()
                .verify (token)
                .getIssuer ();
    }
}
