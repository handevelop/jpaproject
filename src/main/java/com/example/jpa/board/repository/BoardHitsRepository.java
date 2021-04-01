package com.example.jpa.board.repository;

import com.example.jpa.board.entity.Board;
import com.example.jpa.board.entity.BoardHits;
import com.example.jpa.board.model.BoardTypeCount;
import com.example.jpa.user.entity.User;
import lombok.AllArgsConstructor;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;


@Repository
public interface BoardHitsRepository extends JpaRepository<BoardHits, Long> {

    long countByBoardAndUser(Board board, User user);
}
