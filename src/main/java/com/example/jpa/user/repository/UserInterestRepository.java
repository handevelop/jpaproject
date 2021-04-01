package com.example.jpa.user.repository;


import com.example.jpa.user.entity.User;
import com.example.jpa.user.entity.UserInterest;
import com.example.jpa.user.entity.UserLoginHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
// 상속을 받을때 모델 타입과 모델의 PK 를 상속 받는다.
public interface UserInterestRepository extends JpaRepository<UserInterest, Long> {

    long countByUserAndAndInterestUser(User user, User interestUser);
}
