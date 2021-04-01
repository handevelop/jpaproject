package com.example.jpa.board.service;

import com.example.jpa.board.entity.Board;
import com.example.jpa.board.entity.BoardBadReport;
import com.example.jpa.board.entity.BoardComment;
import com.example.jpa.board.entity.BoardType;
import com.example.jpa.board.model.*;

import java.util.List;

public interface BoardService {

    ServiceResult addBoard(BoardTypeInput boardTypeInput);

    ServiceResult updateBoard(long id, BoardTypeInput boardTypeInput);

    ServiceResult deleteBoardType(Long id);

    List<BoardType> getBoardTypeAll();

    boolean getUsingBoardType(Long id);

    ServiceResult setBoardTypeUsingYn(Long id, BoardTypeUsing boardTypeUsing);

    List<BoardTypeCount> getBoardTypeCount();

    ServiceResult setBoardHits(Long id, String email);

    ServiceResult setBoardLike(Long id, String email);

    ServiceResult setBoardUnlike(Long id, String email);

    ServiceResult setBoardBadReport(Long id, String email, BoardBadReportInput boardBadRequestInput);

    List<BoardBadReport> badReportList();

    ServiceResult scrapBoard(Long id, String email);

    ServiceResult deleteScrapBoard(Long id, String email);

    ServiceResult addBookmark(Long id, String email);

    ServiceResult deleteBookmark(Long id, String email);

    List<Board> postList(String email);

    List<BoardComment> commentList(String email);

    Board detail(Long id);
}
