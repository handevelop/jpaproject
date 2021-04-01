package com.example.jpa.user.service;

import com.example.jpa.board.model.ServiceResult;
import com.example.jpa.common.exception.BizException;
import com.example.jpa.logs.service.LogService;
import com.example.jpa.user.entity.User;
import com.example.jpa.user.entity.UserInterest;
import com.example.jpa.user.model.*;
import com.example.jpa.user.repository.UserCustomRepository;
import com.example.jpa.user.repository.UserInterestRepository;
import com.example.jpa.user.repository.UserRepository;
import com.example.jpa.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserCustomRepository userCustomRepository;

    private final UserInterestRepository userInterestRepository;

    private final LogService logService;

    @Override
    public UserSummary getUserStatusCount() {

        long usingUserCount = userRepository.countByStatus(UserStatus.Using);
        long stopUserCount = userRepository.countByStatus(UserStatus.Stop);

        return UserSummary.builder ()
                .usingUserCount (usingUserCount)
                .stopUserCount (stopUserCount)
                .totalUserCount (usingUserCount + stopUserCount)
                .build ();

    }

    @Override
    public List<User> getTodayUsers() {

        LocalDateTime now = LocalDateTime.now ();

        LocalDateTime startDate = LocalDateTime.of (now.getYear (), now.getMonth (), now.getDayOfMonth (), 0, 0);
        LocalDateTime endDate = startDate.plusDays (1);

        return userRepository.findToday (startDate, endDate);


    }

    @Override
    public List<UserNoticeCount> getUserNoticeCount() {

        return userCustomRepository.findUserNoticeCount();

    }

    @Override
    public List<UserLogCount> getUserLogCount() {

        return userCustomRepository.findUserLogCount();
    }

    @Override
    public List<UserLogCount> getUserLikeBest() {

        return userCustomRepository.findUserLikeBest();
    }

    @Override
    public ServiceResult addInterestUser(Long userId, String email) {
        Optional<User> optionalUser = userRepository.findByEmail (email);

        if (!optionalUser.isPresent ()) {
            return ServiceResult.fail ("사용자가 없습니다.");
        }
        User user = optionalUser.get ();

        Optional<User> optionalInterestUser = userRepository.findById (userId);

        if (!optionalInterestUser.isPresent ()) {
            return ServiceResult.fail ("관심사용자에 추가할 회원 정보가 없습니다.");
        }

        User interestUser = optionalInterestUser.get ();

        if (user.getId () == interestUser.getId ()) {
            return ServiceResult.fail ("자기 자신을 추가할 수 없습니다.");
        }
        long count = userInterestRepository.countByUserAndAndInterestUser (user, interestUser);

        if (count > 0) {
            return ServiceResult.fail ("already...");
        }

        UserInterest userInterest = UserInterest.builder ()
                .user (user)
                .interestUser (interestUser)
                .regDate (LocalDateTime.now ())
                .build ();

        userInterestRepository.save (userInterest);

        return ServiceResult.success ("정상적으로 등록 되었습니다.");
    }

    @Override
    public ServiceResult deleteInterestUser(Long interestId, String email) {
        Optional<User> optionalUser = userRepository.findByEmail (email);

        if (!optionalUser.isPresent ()) {
            return ServiceResult.fail ("사용자가 없습니다.");
        }
        User user = optionalUser.get ();

        Optional<UserInterest> optionalUserInterest = userInterestRepository.findById (interestId);

        if (!optionalUserInterest.isPresent ()) {
            return ServiceResult.fail ("삭제 할 관심자가 없습니다.");
        }

        UserInterest userInterest = optionalUserInterest.get ();

        if (user.getId () != userInterest.getUser ().getId ()) {
            return ServiceResult.fail ("본인 이외의 관심자 정보는 삭제할 수 없습니다.");
        }

        userInterestRepository.delete (userInterest);

        return ServiceResult.success ("정상적으로 삭제 되었습니다.");
    }

    @Override
    public User login(UserLogin userLogin) {

        Optional<User> optionalUser = userRepository.findByEmail (userLogin.getEmail ());

        if (!optionalUser.isPresent ()) {
            throw new BizException ("회원 정보가 존재하지 않습니다.");
        }

        User user = optionalUser.get ();

        if (!PasswordUtil.equalPassword (userLogin.getPassword (), user.getPassword ())) {
            throw new BizException ("일치하는 정보가 없습니다.");
        }

        logService.add(user.getUserName() + "로그인 시도");

        return user;
    }
}
