package com.example.jpa.board.controller;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.example.jpa.board.entity.BoardType;
import com.example.jpa.board.model.*;
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
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ApiBoardController {

    private final BoardTypeRepository boardRepository;

    private final BoardService boardService;

    @PostMapping("/board/type")
    public ResponseEntity<?> addBoardType(@RequestBody @Valid BoardTypeInput boardTypeInput, Errors errors) {

        if (errors.hasErrors ()) {
            List<ResponseError> responseErrorList = ResponseError.of(errors.getAllErrors ());

            return new ResponseEntity<> (ResponseMessage.fail ("입력 값이 정확하지 않습니다.", responseErrorList), HttpStatus.BAD_REQUEST);
        }

        ServiceResult result = boardService.addBoard (boardTypeInput);

        if (!result.isResult ()) {
            return ResponseEntity.ok ().body (ResponseMessage.fail (result.getMessage ()));
        }

        return ResponseEntity.ok ().build ();

    }

    @PatchMapping("/board/type/{id}")
    public ResponseEntity<?> updateBoardType(@PathVariable Long id, @RequestBody @Valid BoardTypeInput boardTypeInput, Errors errors) {

        if (errors.hasErrors ()) {
            List<ResponseError> responseErrorList = ResponseError.of (errors.getAllErrors ());

            return new ResponseEntity<> (ResponseMessage.fail ("입력 값이 정확하지 않습니다.", responseErrorList), HttpStatus.BAD_REQUEST);
        }

        ServiceResult result = boardService.updateBoard (id, boardTypeInput);

        if (!result.isResult ()) {
            return ResponseEntity.ok ().body (ResponseMessage.fail (result.getMessage ()));
        }

        return ResponseEntity.ok ().build ();
    }

    @DeleteMapping("/board/type/{id}")
    public ResponseEntity<?> deleteBoardType(@PathVariable Long id) {

        ServiceResult result = boardService.deleteBoardType (id);

        if (!result.isResult ()) {
            return ResponseEntity.ok ().body (ResponseMessage.fail (result.getMessage ()));
        }

        return ResponseEntity.ok ().build ();
    }

    @GetMapping("/board/type")
    public ResponseEntity<?> getBoardTypeList() {

        List<BoardType> result = boardService.getBoardTypeAll ();

        Collections.sort (result, new Comparator<BoardType> () {
            @Override
            public int compare(BoardType o1, BoardType o2) {
                if (o1.getId () < o2.getId ()) {
                    return 1;
                } else if (o1.getId () > o2.getId ()) {
                    return -1;
                }
                return 0;
            }
        });
        result = result.stream ().filter (x -> x.getId () > 1).collect(Collectors.toList());

        Predicate<BoardType> idGreaterThan1 = x -> x.getId () > 1;
        result.sort (Comparator.comparing (BoardType::getId).reversed ());
        result = result.stream ().filter (idGreaterThan1).collect(Collectors.toList());

        return ResponseEntity.ok ().body (result);
    }

    @PatchMapping("/board/type/{id}/using")
    public ResponseEntity<?> usingBoardType(@PathVariable Long id, @RequestBody BoardTypeUsing boardTypeUsing) {

        ServiceResult serviceResult = boardService.setBoardTypeUsingYn(id, boardTypeUsing);

        if (!serviceResult.isResult ()) {
            return ResponseEntity.ok ().body (ResponseMessage.fail (serviceResult.getMessage ()));
        }

        return ResponseEntity.ok ().build ();

    }

    @GetMapping("/board/type/count")
    public ResponseEntity<?> getBoardTypeCount() {

        List<BoardTypeCount> boardTypeCountList = boardService.getBoardTypeCount();

        return ResponseEntity.ok ().body (boardTypeCountList);
    }

    /*
    @param; id: 업데이트 할 board_id, F-TOKEN: 암호화 된 토큰 (이메일)
     */
    @PutMapping("/board/{id}/hits")
    public ResponseEntity<?> boardHits(@PathVariable Long id, @RequestHeader("F-TOKEN") String token) {

        String email = "";
        try {
            email = JWTUtil.getIssuer (token);
        } catch (SignatureVerificationException e) {
            return new ResponseEntity<>("토큰 정보가 정확하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        ServiceResult result = boardService.setBoardHits(id, email);

        if (result.isFail ()) {
            return ResponseEntity.ok ().body (ResponseMessage.fail (result.getMessage ()));
        }

        return ResponseEntity.ok ().build ();
    }

    @PutMapping("/board/{id}/like")
    public ResponseEntity<?> boardLike(@PathVariable Long id, @RequestHeader("F-TOKEN") String token) {
        String email = "";
        try {
            email = JWTUtil.getIssuer (token);
        } catch (SignatureVerificationException e) {
            return new ResponseEntity<>("토큰 정보가 정확하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        ServiceResult result = boardService.setBoardLike(id, email);

        return ResponseResult.result (result);
    }

    @PutMapping("/board/{id}/unlike")
    public ResponseEntity<?> boardUnlike(@PathVariable Long id, @RequestHeader("F-TOKEN") String token) {
        String email = "";
        try {
            email = JWTUtil.getIssuer (token);
        } catch (SignatureVerificationException e) {
            return new ResponseEntity<>("토큰 정보가 정확하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        ServiceResult result = boardService.setBoardUnlike(id, email);

        return ResponseResult.result (result);
    }

    @PutMapping("/board/{id}/badReport")
    public ResponseEntity<?> boardBadReport(@PathVariable Long id
            , @RequestHeader("F-TOKEN") String token, @RequestBody BoardBadReportInput boardBadReportInput) {
        String email = "";
        try {
            email = JWTUtil.getIssuer (token);
        } catch (SignatureVerificationException e) {
            return new ResponseEntity<>("토큰 정보가 정확하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        ServiceResult result = boardService.setBoardBadReport(id, email, boardBadReportInput);

        return ResponseResult.result (result);
    }



}
