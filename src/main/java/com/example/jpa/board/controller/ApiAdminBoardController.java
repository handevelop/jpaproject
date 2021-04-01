package com.example.jpa.board.controller;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.example.jpa.board.entity.BoardBadReport;
import com.example.jpa.board.entity.BoardType;
import com.example.jpa.board.model.*;
import com.example.jpa.board.repository.BoardScarpRepository;
import com.example.jpa.board.repository.BoardTypeRepository;
import com.example.jpa.board.service.BoardService;
import com.example.jpa.common.ResponseResult;
import com.example.jpa.notice.model.ResponseError;
import com.example.jpa.user.model.ResponseMessage;
import com.example.jpa.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin")
public class ApiAdminBoardController {

    private final BoardService boardService;

    @GetMapping("/board/badreport")
    public ResponseEntity<?> badReport() {

        List<BoardBadReport> boardBadReports = boardService.badReportList();

        return ResponseResult.success (boardBadReports);
    }

    @PutMapping("/board/{id}/scrap")
    public ResponseEntity<?> boardScrap(@PathVariable Long id, @RequestHeader("F-TOKEN") String token) {
        String email = "";
        try {
            email = JWTUtil.getIssuer (token);
        } catch (SignatureVerificationException e) {
            return new ResponseEntity<>("토큰 정보가 정확하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        ServiceResult result = boardService.scrapBoard (id, email);

        return ResponseResult.result (result);
    }

    @DeleteMapping("/board/{id}/scrap")
    public ResponseEntity<?> deleteBoardScrap(@PathVariable Long id, @RequestHeader("F-TOKEN") String token) {
        String email = "";
        try {
            email = JWTUtil.getIssuer (token);
        } catch (SignatureVerificationException e) {
            return new ResponseEntity<>("토큰 정보가 정확하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        ServiceResult result = boardService.deleteScrapBoard (id, email);

        return ResponseResult.result (result);
    }



}
