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
            return ServiceResult.fail ("???????????? ????????????.");
        }
        User user = optionalUser.get ();

        Optional<User> optionalInterestUser = userRepository.findById (userId);

        if (!optionalInterestUser.isPresent ()) {
            return ServiceResult.fail ("?????????????????? ????????? ?????? ????????? ????????????.");
        }

        User interestUser = optionalInterestUser.get ();

        if (user.getId () == interestUser.getId ()) {
            return ServiceResult.fail ("?????? ????????? ????????? ??? ????????????.");
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

        return ServiceResult.success ("??????????????? ?????? ???????????????.");
    }

    @Override
    public ServiceResult deleteInterestUser(Long interestId, String email) {
        Optional<User> optionalUser = userRepository.findByEmail (email);

        if (!optionalUser.isPresent ()) {
            return ServiceResult.fail ("???????????? ????????????.");
        }
        User user = optionalUser.get ();

        Optional<UserInterest> optionalUserInterest = userInterestRepository.findById (interestId);

        if (!optionalUserInterest.isPresent ()) {
            return ServiceResult.fail ("?????? ??? ???????????? ????????????.");
        }

        UserInterest userInterest = optionalUserInterest.get ();

        if (user.getId () != userInterest.getUser ().getId ()) {
            return ServiceResult.fail ("?????? ????????? ????????? ????????? ????????? ??? ????????????.");
        }

        userInterestRepository.delete (userInterest);

        return ServiceResult.success ("??????????????? ?????? ???????????????.");
    }

    @Override
    public User login(UserLogin userLogin) {

        Optional<User> optionalUser = userRepository.findByEmail (userLogin.getEmail ());

        if (!optionalUser.isPresent ()) {
            throw new BizException ("?????? ????????? ???????????? ????????????.");
        }

        User user = optionalUser.get ();

        if (!PasswordUtil.equalPassword (userLogin.getPassword (), user.getPassword ())) {
            throw new BizException ("???????????? ????????? ????????????.");
        }

        logService.add(user.getUserName() + "????????? ??????");

        return user;
    }
}
