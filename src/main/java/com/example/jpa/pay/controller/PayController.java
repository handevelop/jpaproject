package com.example.jpa.pay.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.example.jpa.notice.model.ResponseError;
import com.example.jpa.pay.model.ProductResponse;
import com.example.jpa.pay.model.UserInput;
import com.example.jpa.pay.model.UserInvestInput;
import com.example.jpa.pay.model.UserLogin;
import com.example.jpa.pay.entity.PayUser;
import com.example.jpa.pay.entity.Product;
import com.example.jpa.pay.exception.ProductNotFoundException;
import com.example.jpa.pay.repository.PayUserRepository;
import com.example.jpa.pay.repository.ProductrRepository;
import com.example.jpa.pay.exception.DuplicateUserException;
import com.example.jpa.pay.exception.PasswordMissMatchException;
import com.example.jpa.pay.exception.UserNotFoundException;
import com.example.jpa.user.model.*;
import com.example.jpa.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
@Slf4j
public class PayController {

    private final PayUserRepository userRepository;

    private final ProductrRepository productrRepository;

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handlerUserNotFoundException(UserNotFoundException exception) {

        return new ResponseEntity<> (exception.getMessage (), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<String> handlerDuplicateUserException(DuplicateUserException exception) {

        return new ResponseEntity<> (exception.getMessage (), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PasswordMissMatchException.class)
    public ResponseEntity<String> handlerPasswordMissMatchException(PasswordMissMatchException exception) {

        return new ResponseEntity<> (exception.getMessage (), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<String> handlerProductNotFoundException(ProductNotFoundException exception) {

        return new ResponseEntity<> (exception.getMessage (), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/pay/user")
    public ResponseEntity<Object> addPayUser(@RequestBody @Valid UserInput userInput, Errors errors) {

        List<ResponseError> responseErrorList = new ArrayList<> ();

        if (errors.hasErrors ()) {
            errors.getAllErrors ().forEach (e -> {
                responseErrorList.add (ResponseError.of ((FieldError) e));
            });
            return new ResponseEntity<> (responseErrorList, HttpStatus.BAD_REQUEST);
        }

        String encryptPassword = PasswordUtil.getEncryptPassword (userInput.getPassword ());

        PayUser user = PayUser.builder ()
                .userName (userInput.getUserNm ())
                .totalInvestingAmount (userInput.getTotalInvestingAmount ())
                .email (userInput.getEmail ())
                .password (encryptPassword)
                .phone (userInput.getPhone ())
                .regDate (LocalDateTime.now ())
                .build ();

        userRepository.save (user);

        return ResponseEntity.ok ().build ();
    }

    @PostMapping("/pay/login")
    public ResponseEntity<?> createToken(@RequestBody @Valid UserLogin userLogin, Errors errors) {

        List<ResponseError> responseErrorList = new ArrayList<> ();

        if (errors.hasErrors ()) {
            errors.getAllErrors ().forEach (e -> {
                responseErrorList.add (ResponseError.of ((FieldError) e));
            });
            return new ResponseEntity<> (responseErrorList, HttpStatus.BAD_REQUEST);
        }

        PayUser user = userRepository.findByEmail (userLogin.getEmail ())
                .orElseThrow (() -> new UserNotFoundException ("사용자 정보가 없습니다."));

        if (!PasswordUtil.equalPassword (userLogin.getPassword (), user.getPassword ())) {
            throw new PasswordMissMatchException ("비밀번호가 일치하지 않습니다.");
        }

        // 5분 동안 유효한 Json Web Token 발행
        LocalDateTime expiredDateTime = LocalDateTime.now ().plusMinutes (5);
        Date expiredDate = Timestamp.valueOf (expiredDateTime);

        String token = JWT.create ()
                .withExpiresAt (expiredDate)
                .withClaim ("user_id", user.getId ())
                .withSubject (user.getUserName ())
                .withIssuer (user.getEmail ())
                .sign (Algorithm.HMAC512 ("test".getBytes ()));

        return ResponseEntity.ok ().body (UserLoginToken.builder ().token (token).build ());
    }

    @PostMapping("/pay/invest")
    public ResponseEntity<?> invest(HttpServletRequest request, @RequestBody UserInvestInput userInvestInput, Errors errors) {

        List<ResponseError> responseErrorList = new ArrayList<> ();

        if (errors.hasErrors ()) {
            errors.getAllErrors ().forEach (e -> {
                responseErrorList.add (ResponseError.of ((FieldError) e));
            });
            return new ResponseEntity<> (responseErrorList, HttpStatus.BAD_REQUEST);
        }

        String xUserId = request.getHeader ("X-USER-ID");

        String email = "";

        try {
            email = JWT.require (Algorithm.HMAC512 ("test".getBytes ()))
                    .build ()
                    .verify (xUserId)
                    .getIssuer ();
        } catch (SignatureVerificationException e) {
            log.error (e.getMessage ());
            throw new UserNotFoundException ("토큰 유효 기간 끝.");
        } catch (Exception e) {
            log.error (e.getMessage ());
            throw new UserNotFoundException ("알 수 없는 이유.");
        }

        Product product = productrRepository.findById (userInvestInput.getId ())
                .orElseThrow (() -> new ProductNotFoundException ("상품이 없습니다."));

        ProductResponse productResponse = ProductResponse.of(product);

        return ResponseEntity.ok (productResponse);

    }


}
