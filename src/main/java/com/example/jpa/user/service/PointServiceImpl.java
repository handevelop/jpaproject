package com.example.jpa.user.service;

import com.example.jpa.board.model.ServiceResult;
import com.example.jpa.user.entity.User;
import com.example.jpa.user.entity.UserPoint;
import com.example.jpa.user.model.UserPointInput;
import com.example.jpa.user.model.UserPointType;
import com.example.jpa.user.repository.UserPointRepository;
import com.example.jpa.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PointServiceImpl implements PointService {

    private final UserPointRepository userPointRepository;

    private final UserRepository userRepository;

    @Override
    public ServiceResult addPoint(String email, UserPointInput userPointInput) {
        Optional<User> optionalUser = userRepository.findByEmail (email);

        if (!optionalUser.isPresent ()) {
            return ServiceResult.fail ("사용자가 없습니다.");
        }
        User user = optionalUser.get ();

        UserPoint userPoint = UserPoint.builder ()
                .user (user)
                .userPointType (userPointInput.getUserPointType ())
                .point (userPointInput.getUserPointType ().getValue ())
                .regDate (LocalDateTime.now ())
                .build ();

        userPointRepository.save (userPoint);

        return ServiceResult.success ("정상적으로 등록되었습니다.");
    }
}
