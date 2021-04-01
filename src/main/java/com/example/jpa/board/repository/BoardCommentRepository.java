package com.example.jpa.board.repository;

import com.example.jpa.board.entity.BoardBookmark;
import com.example.jpa.board.entity.BoardComment;
import com.example.jpa.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BoardCommentRepository extends JpaRepository<BoardComment, Long> {

    List<BoardComment> findByUser(User user);

}
