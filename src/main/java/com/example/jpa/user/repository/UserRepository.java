package com.example.jpa.user.repository;


import com.example.jpa.user.entity.User;
import com.example.jpa.user.model.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
// 상속을 받을때 모델 타입과 모델의 PK 를 상속 받는다.
public interface UserRepository extends JpaRepository<User, Long> {

    int countByEmail(String email);

    // 없을 수 있으니 옵셔널로 싸줌
    Optional<User> findByIdAndPassword(long id, String password);

    Optional<User> findByUserNameAndPhone(String userName, String phone);

    Optional<User> findByEmail(String email);

    List<User> findByEmailContainsOrPhoneContainsOrUserNameContains(String email, String phone, String userName);

    long countByStatus(UserStatus status);

    // JPQL
    @Query(" select u from User u where u.regDate between :startDate and :endDate")
    List<User> findToday(LocalDateTime startDate, LocalDateTime endDate);
}
