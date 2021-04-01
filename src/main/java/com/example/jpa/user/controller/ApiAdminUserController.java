package com.example.jpa.user.controller;

import com.example.jpa.notice.repository.NoticeRepository;
import com.example.jpa.user.entity.User;
import com.example.jpa.user.entity.UserLoginHistory;
import com.example.jpa.user.exception.UserNotFoundException;
import com.example.jpa.user.model.*;
import com.example.jpa.user.repository.UserLoginHistoryRepository;
import com.example.jpa.user.repository.UserRepository;
import com.example.jpa.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ApiAdminUserController {

    private final UserRepository userRepository;

    private final NoticeRepository noticeRepository;

    private final UserLoginHistoryRepository userLoginHistoryRepository;

    private final UserService userService;

    @GetMapping("/admin/user")
    public List<User> userList() {

        List<User> userList = userRepository.findAll ();

        long totalUserCount = userRepository.count ();

        return userList;
    }

    @GetMapping("/admin/user/{id}")
    public ResponseEntity<?> user(@PathVariable Long id) {

        Optional<User> user = userRepository.findById (id);

        if (!user.isPresent ()) {
            return new ResponseEntity<> (ResponseMessage.fail ("user not found.."), HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok (ResponseMessage.success(user));

    }

    @GetMapping("/admin/user/search")
    public ResponseEntity<?> searchUser(@RequestBody UserSearch userSearch) {

        List<User> user = userRepository.findByEmailContainsOrPhoneContainsOrUserNameContains (userSearch.getEmail (), userSearch.getPhone (), userSearch.getUserName ());

        if (!(user.size () > 0)) {
            return new ResponseEntity<> (ResponseMessage.fail ("user not found.."), HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok (ResponseMessage.success(user));

    }


    // enum 사용
    @PatchMapping("/admin/user/{id}/status")
    public ResponseEntity<?> userStatus(@PathVariable Long id, @RequestBody UserStatusInput userStatusInput) {

        Optional<User> userFind = userRepository.findById (id);

        if (!userFind.isPresent ()) {
            return new ResponseEntity<> (ResponseMessage.fail ("user not found.."), HttpStatus.BAD_REQUEST);
        }

        User user = userFind.get ();

        user.setStatus (userStatusInput.getStatus ());

        return ResponseEntity.ok (ResponseMessage.success (user));

    }

    @DeleteMapping("/admin/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        Optional<User> userFind = userRepository.findById (id);

        if (!userFind.isPresent ()) {
            return new ResponseEntity<> (ResponseMessage.fail ("user not found.."), HttpStatus.BAD_REQUEST);
        }

        User user = userFind.get ();
        if (noticeRepository.countByUser (user) > 0) {
            return new ResponseEntity<> (ResponseMessage.fail ("this user do not delete..."), HttpStatus.BAD_REQUEST);
        }

        userRepository.delete (user);

        return ResponseEntity.ok ().build ();
    }

    @GetMapping("/admin/user/login/history")
    public ResponseEntity<?> userLoginHistory() {

        List<UserLoginHistory> userLoginHistoryList = userLoginHistoryRepository.findAll ();

        return ResponseEntity.ok ().body (userLoginHistoryList);
    }

    @PatchMapping("/admin/user/{id}/lock")
    public ResponseEntity<?> userLock(@PathVariable Long id) {

        Optional<User> userFind = userRepository.findById (id);

        if (!userFind.isPresent ()) {
            return new ResponseEntity<> (ResponseMessage.fail ("user not found.."), HttpStatus.BAD_REQUEST);
        }

        User user = userFind.get ();

        if(user.isLockYn ()) {
            return new ResponseEntity<> (ResponseMessage.fail ("user already locked..."), HttpStatus.BAD_REQUEST);
        }

        user.setLockYn (true);
        userRepository.save (user);

        return ResponseEntity.ok (ResponseMessage.success (user));

    }

    @PatchMapping("/admin/user/{id}/unlock")
    public ResponseEntity<?> userUnlock(@PathVariable Long id) {

        Optional<User> userFind = userRepository.findById (id);

        if (!userFind.isPresent ()) {
            return new ResponseEntity<> (ResponseMessage.fail ("user not found.."), HttpStatus.BAD_REQUEST);
        }

        User user = userFind.get ();

        if(!user.isLockYn ()) {
            return new ResponseEntity<> (ResponseMessage.fail ("user already unlocked..."), HttpStatus.BAD_REQUEST);
        }

        user.setLockYn (false);
        userRepository.save (user);

        return ResponseEntity.ok (ResponseMessage.success (user));

    }

    @GetMapping("/admin/user/status/count")
    public ResponseEntity<?> userStatusCount() {

        UserSummary userSummary = userService.getUserStatusCount ();

        return ResponseEntity.ok (ResponseMessage.success (userSummary));
    }

    @GetMapping("/admin/user/today")
    public ResponseEntity<?> todayUser() {

        List<User> userList = userService.getTodayUsers();

        return ResponseEntity.ok ().body (ResponseMessage.success (userList));
    }

    @GetMapping("/admin/user/notice/count")
    public ResponseEntity<?> userNoticeCount() {

        List<UserNoticeCount> userNoticeCounts = userService.getUserNoticeCount();

        return ResponseEntity.ok ().body (ResponseMessage.success (userNoticeCounts));
    }

    @GetMapping("/admin/user/log/count")
    public ResponseEntity<?> userLogCount() {

        List<UserLogCount> userLogCounts = userService.getUserLogCount();

        return ResponseEntity.ok ().body (ResponseMessage.success (userLogCounts));
    }

    @GetMapping("/admin/user/count/best")
    public ResponseEntity<?> userLikeBest() {

        List<UserLogCount> userLogCounts = userService.getUserLikeBest();

        return ResponseEntity.ok ().body (ResponseMessage.success (userLogCounts));
    }
}
