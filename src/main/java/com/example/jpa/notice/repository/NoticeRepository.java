package com.example.jpa.notice.repository;

import com.example.jpa.notice.entity.Notice;
import com.example.jpa.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
// 상속을 받을때 모델 타입과 모델의 PK 를 상속 받는다.
public interface NoticeRepository extends JpaRepository<Notice, Long> {

    Optional<List<Notice>> findByTitleAndContentsAndRegDateIsGreaterThanEqual(String title, String contents, LocalDateTime regDate);

    int countByTitleAndContentsAndRegDateIsGreaterThanEqual(String title, String contents, LocalDateTime regDate);

    List<Notice> findByUser(User user);

    int countByUser(User user);
}
