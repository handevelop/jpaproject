package com.example.jpa.board.service;

import com.example.jpa.board.entity.BoardType;
import com.example.jpa.board.model.BoardTypeCount;
import com.example.jpa.board.model.BoardTypeInput;
import com.example.jpa.board.model.BoardTypeUsing;
import com.example.jpa.board.model.ServiceResult;

import java.util.List;

public interface BoardCustomService {

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
}
