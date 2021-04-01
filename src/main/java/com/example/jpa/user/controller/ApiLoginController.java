package com.example.jpa.user.controller;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.example.jpa.board.model.ServiceResult;
import com.example.jpa.common.ResponseResult;
import com.example.jpa.common.exception.BizException;
import com.example.jpa.notice.model.ResponseError;
import com.example.jpa.user.entity.User;
import com.example.jpa.user.exception.PasswordMissMatchException;
import com.example.jpa.user.exception.UserNotFoundException;
import com.example.jpa.user.model.UserLogin;
import com.example.jpa.user.model.UserLoginToken;
import com.example.jpa.user.repository.UserRepository;
import com.example.jpa.user.service.UserService;
import com.example.jpa.util.JWTUtil;
import com.example.jpa.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ApiLoginController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserLogin userLogin, Errors errors) {

        List<ResponseError> responseErrorList = new ArrayList<> ();

        if (errors.hasErrors ()) {
            errors.getAllErrors ().forEach (e -> {
                responseErrorList.add (ResponseError.of ((FieldError) e));
            });
            return ResponseResult.fail ("입력값이 정확하지 않습니다.", responseErrorList);
        }
        User user = null;
        try {
            user = userService.login(userLogin);
        } catch (BizException e) {
            log.info("로그인 에러: " + e.getMessage());
            return ResponseResult.fail (e.getMessage ());
        }

        UserLoginToken userLoginToken = JWTUtil.createToken (user);

        if (userLoginToken == null) {
            log.info("JWT 생성 에러");
            return ResponseResult.fail ("JWT 생성에 실패하였습니다.");
        }
        return ResponseResult.success (userLoginToken);
    }


}
