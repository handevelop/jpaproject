package com.example.jpa.board.repository;

import com.example.jpa.board.entity.BoardBadReport;
import com.example.jpa.board.entity.BoardScrap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BoardScarpRepository extends JpaRepository<BoardScrap, Long> {


}
