package com.example.jpa.notice.repository;


import com.example.jpa.notice.entity.NoticeLike;
import com.example.jpa.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
// 상속을 받을때 모델 타입과 모델의 PK 를 상속 받는다.
public interface NoticeLikeRepository extends JpaRepository<NoticeLike, Long> {

    List<NoticeLike> findByUser(User user);
}
