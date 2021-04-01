package com.example.jpa.user.repository;


import com.example.jpa.user.entity.UserLoginHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
// 상속을 받을때 모델 타입과 모델의 PK 를 상속 받는다.
public interface UserLoginHistoryRepository extends JpaRepository<UserLoginHistory, Long> {

}
