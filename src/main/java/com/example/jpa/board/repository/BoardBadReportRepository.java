package com.example.jpa.board.repository;

import com.example.jpa.board.entity.BoardBadReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BoardBadReportRepository extends JpaRepository<BoardBadReport, Long> {


}
