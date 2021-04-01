package com.example.jpa.board.controller;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.example.jpa.board.entity.BoardBadReport;
import com.example.jpa.board.model.ServiceResult;
import com.example.jpa.board.service.BoardService;
import com.example.jpa.common.ResponseResult;
import com.example.jpa.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin")
public class ApiBoardBookmarkController {

    private final BoardService boardService;

    @PutMapping("/board/{id}/bookmark")
    public ResponseEntity<?> boardBookmark(@PathVariable Long id, @RequestHeader("F-TOKEN") String token) {
        String email = "";
        try {
            email = JWTUtil.getIssuer (token);
        } catch (SignatureVerificationException e) {
            return new ResponseEntity<>("토큰 정보가 정확하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        ServiceResult result = boardService.addBookmark (id, email);

        return ResponseResult.result (result);
    }

    @DeleteMapping("/board/{id}/bookmark")
    public ResponseEntity<?> deleteBoardBookmark(@PathVariable Long id, @RequestHeader("F-TOKEN") String token) {
        String email = "";
        try {
            email = JWTUtil.getIssuer (token);
        } catch (SignatureVerificationException e) {
            return new ResponseEntity<>("토큰 정보가 정확하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        ServiceResult result = boardService.deleteBookmark (id, email);

        return ResponseResult.result (result);
    }



}
