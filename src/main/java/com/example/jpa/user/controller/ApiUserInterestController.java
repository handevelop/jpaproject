package com.example.jpa.user.controller;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.example.jpa.board.model.ServiceResult;
import com.example.jpa.common.ResponseResult;
import com.example.jpa.user.service.UserService;
import com.example.jpa.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class ApiUserInterestController {

    private final UserService userService;

    @PutMapping("/{id}/interest")
    public ResponseEntity<?> interestUser(@PathVariable Long id, @RequestHeader("F-TOKEN") String token) {
        String email = "";
        try {
            email = JWTUtil.getIssuer (token);
        } catch (SignatureVerificationException e) {
            return new ResponseEntity<> ("토큰 정보가 정확하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        ServiceResult result = userService.addInterestUser(id, email);

        return ResponseResult.result (result);
    }

    @DeleteMapping("/interest/{id}")
    private ResponseEntity<?> deleteInterestUser(@PathVariable Long id, @RequestHeader("F-TOKEN") String token) {
        String email = "";
        try {
            email = JWTUtil.getIssuer (token);
        } catch (SignatureVerificationException e) {
            return new ResponseEntity<> ("토큰 정보가 정확하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        ServiceResult result = userService.deleteInterestUser(id, email);

        return ResponseResult.result (result);
    }
}
