package com.example.jpa.user.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.example.jpa.board.entity.Board;
import com.example.jpa.board.entity.BoardComment;
import com.example.jpa.board.model.ServiceResult;
import com.example.jpa.board.service.BoardService;
import com.example.jpa.common.ResponseResult;
import com.example.jpa.notice.entity.Notice;
import com.example.jpa.notice.entity.NoticeLike;
import com.example.jpa.notice.model.NoticeResponse;
import com.example.jpa.notice.model.ResponseError;
import com.example.jpa.notice.repository.NoticeLikeRepository;
import com.example.jpa.notice.repository.NoticeRepository;
import com.example.jpa.user.entity.User;
import com.example.jpa.user.exception.DuplicateUserException;
import com.example.jpa.user.exception.PasswordMissMatchException;
import com.example.jpa.user.exception.UserNotFoundException;
import com.example.jpa.user.model.*;
import com.example.jpa.user.repository.UserRepository;
import com.example.jpa.user.service.PointService;
import com.example.jpa.util.JWTUtil;
import com.example.jpa.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
import java.util.UUID;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ApiUserController {

    private final UserRepository userRepository;

    private final NoticeRepository noticeRepository;

    private final NoticeLikeRepository noticeLikeRepository;

    private final BoardService boardService;

    private final PointService pointService;

//    @PostMapping("/user")
//    public ResponseEntity<?> addUser(@RequestBody UserInput userInput, Errors errors) {
//
//        List<ResponseError> responseErrorList = new ArrayList<> ();
//
//        if (errors.hasErrors ()) {
//            errors.getAllErrors ().forEach (e -> {
//                responseErrorList.add (ResponseError.of ((FieldError) e));
//            });
//            return new ResponseEntity<> (responseErrorList, HttpStatus.BAD_REQUEST);
//        }
//
//        return ResponseEntity.ok ().build ();
//    }

//    @PostMapping("/user")
//    public ResponseEntity<Object> addUser(@RequestBody @Valid UserInput userInput, Errors errors) {
//
//        List<ResponseError> responseErrorList = new ArrayList<> ();
//
//        if (errors.hasErrors ()) {
//            errors.getAllErrors ().forEach (e -> {
//                responseErrorList.add (ResponseError.of ((FieldError) e));
//            });
//            return new ResponseEntity<> (responseErrorList, HttpStatus.BAD_REQUEST);
//        }
//
//        // 중복 체크
//
//        int duplicatedEmail = userRepository.countByEmail (userInput.getEmail ());
//
//        if (duplicatedEmail > 0) {
//            throw new DuplicateUserException ("동일한 이메일 정보가 존재합니다.");
//        }
//
//        User user = User.builder ()
//                .userName (userInput.getUserNm ())
//                .email (userInput.getEmail ())
//                .password (userInput.getPassword ())
//                .phone (userInput.getPhone ())
//                .regDate (LocalDateTime.now ())
//                .build ();
//
//        userRepository.save (user);
//
//        return ResponseEntity.ok ().build ();
//    }

    @PutMapping("/user/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody @Valid UserUpdate userUpdate, Errors errors) {

        List<ResponseError> responseErrorList = new ArrayList<> ();

        if (errors.hasErrors ()) {
            errors.getAllErrors ().forEach (e -> {
                responseErrorList.add (ResponseError.of ((FieldError) e));
            });
            return new ResponseEntity<> (responseErrorList, HttpStatus.BAD_REQUEST);
        }

        // 유저 정보 없을 때
        User user = userRepository.findById (id)
                .orElseThrow (() -> new UserNotFoundException ("사용자 정보가 없습니다."));

        user.setPhone (userUpdate.getPhone ());
        user.setUpdateDate (LocalDateTime.now ());

        userRepository.save (user);

        return ResponseEntity.ok ().build ();
    }

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

    @GetMapping("/user/{id}")
    public UserResponse getUserInfo(@PathVariable Long id) {

        // 유저 정보 없을 때
        User user = userRepository.findById (id)
                .orElseThrow (() -> new UserNotFoundException ("사용자 정보가 없습니다."));

        //UserResponse userResponse = new UserResponse (user);
        UserResponse userResponse = UserResponse.of(user);

        return userResponse;

    }

    @GetMapping("/user/{id}/notice")
    public List<NoticeResponse> userNotice(@PathVariable Long id) {

        User user = userRepository.findById (id)
                .orElseThrow (() -> new UserNotFoundException ("사용자 정보가 없습니다."));

        List<NoticeResponse> result = new ArrayList<> ();

        List<Notice> noticeList = noticeRepository.findByUser (user);

        noticeList.stream ().forEach (e -> {
            result.add (NoticeResponse.of (e));
        });

        return result;

    }

    @PostMapping("/user2")
    public ResponseEntity<?> addUser2(@RequestBody @Valid UserInput userInput, Errors errors) {

        List<ResponseError> responseErrorList = new ArrayList<> ();

        if (errors.hasErrors ()) {
            errors.getAllErrors ().forEach (e -> {
                responseErrorList.add (ResponseError.of ((FieldError) e));
            });
            return new ResponseEntity<> (responseErrorList, HttpStatus.BAD_REQUEST);
        }

        int duplicatedEmail = userRepository.countByEmail (userInput.getEmail ());

        if (duplicatedEmail > 0) {
            throw new DuplicateUserException ("동일한 이메일로 등록된 사용자 존재.");
        }

        userRepository.save (User.builder ()
                .email (userInput.getEmail ())
                .phone (userInput.getPhone ())
                .password (userInput.getPassword ())
                .regDate (LocalDateTime.now ())
                .userName (userInput.getUserNm ())
                .build ());

        return ResponseEntity.ok ().build ();
    }

    @PatchMapping("/user/{id}/password")
    public ResponseEntity<?> updatePassword(@PathVariable Long id, @RequestBody @Valid UserInputPassword userInputPassword, Errors errors) {

        List<ResponseError> responseErrorList = new ArrayList<> ();

        if (errors.hasErrors ()) {
            errors.getAllErrors ().forEach (e -> {
                responseErrorList.add (ResponseError.of ((FieldError) e));
            });
            return new ResponseEntity<> (responseErrorList, HttpStatus.BAD_REQUEST);
        }

        User user = userRepository.findByIdAndPassword (id, userInputPassword.getPassword ())
                .orElseThrow (() -> new PasswordMissMatchException ("비밀번호가 일치하지 않습니다."));

        user.setPassword (userInputPassword.getNewPassword ());
        user.setUpdateDate (LocalDateTime.now ());

        userRepository.save (user);

        return ResponseEntity.ok ().build ();
    }

    @PostMapping("/user")
    public ResponseEntity<Object> addUser(@RequestBody @Valid UserInput userInput, Errors errors) {

        List<ResponseError> responseErrorList = new ArrayList<> ();

        if (errors.hasErrors ()) {
            errors.getAllErrors ().forEach (e -> {
                responseErrorList.add (ResponseError.of ((FieldError) e));
            });
            return new ResponseEntity<> (responseErrorList, HttpStatus.BAD_REQUEST);
        }

        // 중복 체크

        int duplicatedEmail = userRepository.countByEmail (userInput.getEmail ());

        if (duplicatedEmail > 0) {
            throw new DuplicateUserException ("동일한 이메일 정보가 존재합니다.");
        }

        String encryptPassword = getEncryptPassword(userInput.getPassword ());

        User user = User.builder ()
                .userName (userInput.getUserNm ())
                .email (userInput.getEmail ())
                .password (encryptPassword)
                .phone (userInput.getPhone ())
                .regDate (LocalDateTime.now ())
                .build ();

        userRepository.save (user);

        return ResponseEntity.ok ().build ();
    }

    @DeleteMapping("/user/{id}")
    public void deleteUser(@PathVariable Long id) {
        List<ResponseError> responseErrorList = new ArrayList<> ();

        User user = userRepository.findById (id)
                .orElseThrow (() -> new UserNotFoundException ("사용자 정보가 없습니다."));

        userRepository.delete (user);
    }

    @GetMapping("/user")
    public UserResponse findUser(@RequestBody UserInputFind userInputFind){

        User user = userRepository.findByUserNameAndPhone (userInputFind.getUserNm (), userInputFind.getPhone ())
                .orElseThrow (() -> new UserNotFoundException ("사용자 정보가 없습니다."));

        UserResponse userResponse = UserResponse.of(user);

        return userResponse;
    }

    @GetMapping("/user/{id}/password/reset")
    public ResponseEntity<?> resetUserPassword(@PathVariable Long id) {
        User user = userRepository.findById (id)
                .orElseThrow (() -> new UserNotFoundException ("사용자 정보가 없습니다."));


        String resetPassword = getResetPassword ();
        String encryptPassword = getEncryptPassword (resetPassword);
        user.setPassword (encryptPassword);
        userRepository.save (user);

        System.out.println ("수정완료 메시지 로직.");

        return ResponseEntity.ok ().build ();

    }

    private String getResetPassword() {
        return UUID.randomUUID ().toString ().replace ("-", "").substring (0, 10);
    }

    private String getEncryptPassword(String password) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder ();
        String encryptPassword = bCryptPasswordEncoder.encode (password);

        return encryptPassword;
    }

    @GetMapping("/user/{id}/notice/like")
    public List<NoticeLike> likeNotice(@PathVariable Long id) {

        User user = userRepository.findById (id)
                .orElseThrow (() -> new UserNotFoundException ("사용자 정보가 없습니다."));

        List<NoticeLike> noticeLikeList = noticeLikeRepository.findByUser (user);

        return noticeLikeList;
    }

//    @PostMapping("/user/login")
//    public ResponseEntity<?> createToken(@RequestBody @Valid UserLogin userLogin, Errors errors) {
//
//        List<ResponseError> responseErrorList = new ArrayList<> ();
//
//        if (errors.hasErrors ()) {
//            errors.getAllErrors ().forEach (e -> {
//                responseErrorList.add (ResponseError.of ((FieldError) e));
//            });
//            return new ResponseEntity<> (responseErrorList, HttpStatus.BAD_REQUEST);
//        }
//
//        User user = userRepository.findByEmail (userLogin.getEmail ())
//                .orElseThrow (() -> new UserNotFoundException ("사용자 정보가 없습니다."));
//
//        if (!PasswordUtil.equalPassword (userLogin.getPassword (), user.getPassword ())) {
//            throw new PasswordMissMatchException ("비밀번호가 일치하지 않습니다.");
//        }
//
//        String token = JWT.create ()
//                .withExpiresAt (new Date ())
//                .withClaim ("user_id", user.getId ())
//                .withSubject (user.getUserName ())
//                .withIssuer (user.getEmail ())
//                .sign (Algorithm.HMAC512 ("lotteon".getBytes ()));
//
//
//
//        return ResponseEntity.ok ().body (UserLoginToken.builder ().token (token).build ());
//    }

    @PostMapping("/user/login")
    public ResponseEntity<?> createToken(@RequestBody @Valid UserLogin userLogin, Errors errors) {

        List<ResponseError> responseErrorList = new ArrayList<> ();

        if (errors.hasErrors ()) {
            errors.getAllErrors ().forEach (e -> {
                responseErrorList.add (ResponseError.of ((FieldError) e));
            });
            return new ResponseEntity<> (responseErrorList, HttpStatus.BAD_REQUEST);
        }

        User user = userRepository.findByEmail (userLogin.getEmail ())
                .orElseThrow (() -> new UserNotFoundException ("사용자 정보가 없습니다."));

        if (!PasswordUtil.equalPassword (userLogin.getPassword (), user.getPassword ())) {
            throw new PasswordMissMatchException ("비밀번호가 일치하지 않습니다.");
        }

        // 현재보다 1개월 더해진 날짜
        LocalDateTime expiredDateTime = LocalDateTime.now ().plusMonths (1);
        Date expiredDate = Timestamp.valueOf (expiredDateTime);

        // 1개월 동안 유효한 Json Web Token 발행
        String token = JWT.create ()
                .withExpiresAt (expiredDate)
                .withClaim ("user_id", user.getId ())
                .withSubject (user.getUserName ())
                .withIssuer (user.getEmail ())
                .sign (Algorithm.HMAC512 ("lotteon".getBytes ()));

        return ResponseEntity.ok ().body (UserLoginToken.builder ().token (token).build ());
    }

    @PatchMapping("/user/login")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {

        String token = request.getHeader ("TOKEN");

        String email = "";

        try {
            email = JWT.require (Algorithm.HMAC512 ("lotteon".getBytes ()))
                    .build ()
                    .verify (token)
                    .getIssuer ();
        } catch (SignatureVerificationException e) {
            return new ResponseEntity<>("토큰 정보가 정확하지 않습니다.", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("알 수 없는 이유", HttpStatus.BAD_REQUEST);
        }

        User user = userRepository.findByEmail (email)
                .orElseThrow (() -> new UserNotFoundException ("사용자 정보가 없습니다."));

        // 현재보다 1개월 더해진 날짜
        LocalDateTime expiredDateTime = LocalDateTime.now ().plusMonths (1);
        Date expiredDate = Timestamp.valueOf (expiredDateTime);

        // 1개월 동안 유효한 Json Web Token 발행
        String newToken = JWT.create ()
                .withExpiresAt (expiredDate)
                .withClaim ("user_id", user.getId ())
                .withSubject (user.getUserName ())
                .withIssuer (user.getEmail ())
                .sign (Algorithm.HMAC512 ("lotteon".getBytes ()));

        return ResponseEntity.ok ().body (UserLoginToken.builder ().token (newToken).build ());
    }

    @DeleteMapping("/user/token")
    public ResponseEntity<?> removeToken(@RequestHeader("F-TOKEN") String token) {

        String email = "";
        try {
            email = JWTUtil.getIssuer (token);
        } catch (SignatureVerificationException e) {
            return new ResponseEntity<>("토큰 정보가 정확하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok ().build ();
    }

    @GetMapping("/user/board/post")
    public ResponseEntity<?> myPost(@RequestHeader("F-TOKEN") String token) {
        String email = "";
        try {
            email = JWTUtil.getIssuer (token);
        } catch (SignatureVerificationException e) {
            return ResponseResult.fail ("토큰 정보가 정확하지 않습니다.");
        }

        List<Board> boards = boardService.postList(email);

        return ResponseResult.success (boards);
    }

    @GetMapping("/user/board/comment")
    public ResponseEntity<?> myComments(@RequestHeader("F-TOKEN") String token) {
        String email = "";
        try {
            email = JWTUtil.getIssuer (token);
        } catch (SignatureVerificationException e) {
            return ResponseResult.fail ("토큰 정보가 정확하지 않습니다.");
        }

        List<BoardComment> boardComments = boardService.commentList (email);

        return ResponseResult.success (boardComments);
    }

    @PostMapping("/user/point")
    public ResponseEntity<?> userPoint(@RequestHeader("F-TOKEN") String token,
                                       @RequestBody UserPointInput userPointInput) {
        String email = "";
        try {
            email = JWTUtil.getIssuer (token);
        } catch (SignatureVerificationException e) {
            return ResponseResult.fail ("토큰 정보가 정확하지 않습니다.");
        }

        ServiceResult result = pointService.addPoint (email, userPointInput);

        return ResponseResult.success (result);
    }

}
