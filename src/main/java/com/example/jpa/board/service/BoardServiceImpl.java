package com.example.jpa.board.service;

import com.example.jpa.board.entity.*;
import com.example.jpa.board.model.*;
import com.example.jpa.board.repository.*;
import com.example.jpa.common.exception.BizException;
import com.example.jpa.user.entity.User;
import com.example.jpa.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BoardServiceImpl implements BoardService {

    private final BoardTypeRepository boardTypeRepository;

    private final BoardRepository boardRepository;

    private final BoardTypeCustomRepository boardTypeCustomRepository;

    private final BoardHitsRepository boardHitsRepository;

    private final UserRepository userRepository;

    private final BoardLikeRepository boardLikeRepository;

    private final BoardBadReportRepository boardBadRequestRepository;

    private final BoardScarpRepository boardScarpRepository;

    private final BoardBookmarkRepository boardBookmarkRepository;

    private final BoardCommentRepository boardCommentRepository;

    @Override
    public ServiceResult addBoard(BoardTypeInput boardTypeInput) {

        BoardType boardType = boardTypeRepository.findByBoardName (boardTypeInput.getName ());

        if (boardType != null) {
            return ServiceResult.fail("동일한 이름의 게시글이 존재합니다.");
        }
        BoardType addBoardType = BoardType.builder ()
                .boardName (boardTypeInput.getName ())
                .regDate (LocalDateTime.now ())
                .build ();

        boardTypeRepository.save (addBoardType);

        return ServiceResult.success("정상 등록 되었습니다.");
    }

    @Override
    public ServiceResult updateBoard(long id, BoardTypeInput boardTypeInput) {

        Optional<BoardType> boardType = boardTypeRepository.findById (id);

        if (!boardType.isPresent ()) {
            return ServiceResult.fail ("게시글이 없습니다.");
        }
        BoardType updateBoardType = BoardType.builder ()
                .boardName (boardTypeInput.getName ())
                .updateDate (LocalDateTime.now ())
                .build ();

        boardTypeRepository.save (updateBoardType);

        return ServiceResult.success("정상 수정 되었습니다.");
    }

    @Override
    public ServiceResult deleteBoardType(Long id) {
        Optional<BoardType> boardType = boardTypeRepository.findById (id);

        if (!boardType.isPresent ()) {
            return ServiceResult.fail ("게시글이 없습니다.");
        }

        int board = boardRepository.countByBoardType (boardType.get ());

        if (board > 0) {
            return ServiceResult.fail ("등록된 게시글이 존재합니다.");
        }

        boardTypeRepository.delete (boardType.get ());

        return ServiceResult.success("정상 삭제 되었습니다.");
    }

    @Override
    public List<BoardType> getBoardTypeAll() {

        return boardTypeRepository.findAll ();
    }

    @Override
    public boolean getUsingBoardType(Long id) {

        return boardTypeRepository.findById (id).get().isUsingYn ();
    }

    @Override
    public ServiceResult setBoardTypeUsingYn(Long id, BoardTypeUsing boardTypeUsing) {
        Optional<BoardType> boardType = boardTypeRepository.findById (id);

        if (!boardType.isPresent ()) {
            return ServiceResult.fail ("게시글이 없습니다.");
        }
        boardType.get ().setUsingYn (boardTypeUsing.isUsingYn ());
        boardTypeRepository.save(boardType.get ());

        return ServiceResult.success ("정상 수정 되었습니다.");
    }

    @Override
    public List<BoardTypeCount> getBoardTypeCount() {

        List<BoardTypeCount> boardTypeCountList = boardTypeCustomRepository.getBoardTypeCount();
        return boardTypeCountList;
    }

    @Override
    public ServiceResult setBoardHits(Long id, String email) {
        Optional<Board> board = boardRepository.findById (id);

        if (!board.isPresent ()) {
            return ServiceResult.fail ("게시글이 없습니다.");
        }
        Optional<User> user = userRepository.findByEmail (email);

        if (!user.isPresent ()) {
            return ServiceResult.fail ("사용자가 없습니다.");
        }

        long count = boardHitsRepository.countByBoardAndUser (board.get (), user.get ());

        if (count > 0) {
            return ServiceResult.fail ("이미 조회수가 추가된 게시글입니다.");
        }

        // 조회수 올릴 게시글이 있고, 유효한 사용자이고, 그 사용자가 조회수를 이미 조회한 게시글이 아니라면 boardHits 추가
        boardHitsRepository.save (BoardHits.builder ()
                .board (board.get ())
                .user (user.get ())
                .regDate (LocalDateTime.now ())
                .build ());

        return ServiceResult.success ("조회수가 정상적으로 증가 되었습니다.");
    }

    @Override
    public ServiceResult setBoardLike(Long id, String email) {
        Optional<Board> board = boardRepository.findById (id);

        if (!board.isPresent ()) {
            return ServiceResult.fail ("게시글이 없습니다.");
        }
        Optional<User> user = userRepository.findByEmail (email);

        if (!user.isPresent ()) {
            return ServiceResult.fail ("사용자가 없습니다.");
        }

        long count = boardLikeRepository.countByBoardAndUser (board.get (), user.get ());

        if (count > 0) {
            return ServiceResult.fail ("이미 좋아요 한 게시글입니다.");
        }

        boardLikeRepository.save (BoardLike.builder ()
                .board (board.get ())
                .user (user.get ())
                .regDate (LocalDateTime.now ())
                .build ());

        return ServiceResult.success ("좋아요가 정상적으로 증가 되었습니다.");
    }

    @Override
    public ServiceResult setBoardUnlike(Long id, String email) {
        Optional<Board> board = boardRepository.findById (id);

        if (!board.isPresent ()) {
            return ServiceResult.fail ("게시글이 없습니다.");
        }
        Optional<User> user = userRepository.findByEmail (email);

        if (!user.isPresent ()) {
            return ServiceResult.fail ("사용자가 없습니다.");
        }

        Optional<BoardLike> boardLike = boardLikeRepository.findByBoardAndUser (board.get (), user.get ());

        if (!boardLike.isPresent ()) {
            return ServiceResult.fail ("좋아요 한 게시글이 없습니다.");
        }

        boardLikeRepository.delete (boardLike.get ());

        return ServiceResult.success ("좋아요가 정상적으로 감소 되었습니다.");
    }

    @Override
    public ServiceResult setBoardBadReport(Long id, String email, BoardBadReportInput boardBadReportInput) {
        Optional<Board> optionalBoard = boardRepository.findById (id);

        if (!optionalBoard.isPresent ()) {
            return ServiceResult.fail ("게시글이 없습니다.");
        }

        Board board = optionalBoard.get ();

        Optional<User> optionalUser = userRepository.findByEmail (email);

        if (!optionalUser.isPresent ()) {
            return ServiceResult.fail ("사용자가 없습니다.");
        }

        User user = optionalUser.get ();

        BoardBadReport boardBadReport = BoardBadReport.builder ()
                .userId (user.getId ())
                .userEmail (user.getEmail ())
                .userName (user.getUserName ())
                .comments (boardBadReportInput.getComments ())
                .boardId (board.getId ())
                .boardUserId (board.getUser ().getId ())
                .boardContents (board.getContents ())
                .boardTitle (board.getTitle ())
                .boardRegDate (board.getRegDate ())
                .regDate (LocalDateTime.now ())
                .build ();

        boardBadRequestRepository.save (boardBadReport);

        return ServiceResult.success ("정상적으로 신고가 등록 되었습니다.");
    }

    @Override
    public List<BoardBadReport> badReportList() {

        return boardBadRequestRepository.findAll ();
    }

    @Override
    public ServiceResult scrapBoard(Long id, String email) {
        Optional<Board> optionalBoardboard = boardRepository.findById (id);
        Board board = optionalBoardboard.get ();

        if (!optionalBoardboard.isPresent ()) {
            return ServiceResult.fail ("게시글이 없습니다.");
        }
        Optional<User> optionalUseruser = userRepository.findByEmail (email);
        User user = optionalUseruser.get ();

        if (!optionalUseruser.isPresent ()) {
            return ServiceResult.fail ("사용자가 없습니다.");
        }

        BoardScrap boardScrap = BoardScrap.builder ()
                .user (optionalUseruser.get ())
                .boardId (board.getId ())
                .boardContents (board.getContents ())
                .boardRegDate (board.getRegDate ())
                .boardTitle (board.getTitle ())
                .boardContents (board.getContents ())
                .boardTypeId (board.getBoardType ().getId ())
                .regDate (LocalDateTime.now ())
                .build ();

        boardScarpRepository.save (boardScrap);

        return ServiceResult.success ("정상적으로 스크랩 되었습니다.");
    }

    @Override
    public ServiceResult deleteScrapBoard(Long id, String email) {

        Optional<User> optionalUseruser = userRepository.findByEmail (email);
        User user = optionalUseruser.get ();

        if (!optionalUseruser.isPresent ()) {
            return ServiceResult.fail ("사용자가 없습니다.");
        }

        Optional<BoardScrap> boardScrap = boardScarpRepository.findById (id);

        if (!boardScrap.isPresent ()) {
            return ServiceResult.fail ("삭제할 스크랩이 없습니다.");
        }

        if (user.getId () != boardScrap.get ().getUser ().getId ()) {
            return ServiceResult.fail ("사용자가 작성한 스크랩이 아닙니다.");
        }

        boardScarpRepository.delete (boardScrap.get ());

        return ServiceResult.success ("정상적으로 삭제 되었습니다.");
    }

    private String getBoardUrl(long boardId) {

        return String.format ("/board/%d", boardId);
    }

    @Override
    public ServiceResult addBookmark(Long id, String email) {

        Optional<Board> optionalBoardboard = boardRepository.findById (id);

        if (!optionalBoardboard.isPresent ()) {
            return ServiceResult.fail ("게시글이 없습니다.");
        }
        Board board = optionalBoardboard.get ();

        Optional<User> optionalUseruser = userRepository.findByEmail (email);

        if (!optionalUseruser.isPresent ()) {
            return ServiceResult.fail ("사용자가 없습니다.");
        }

        User user = optionalUseruser.get ();

        BoardBookmark boardBookmark = BoardBookmark.builder ()
                .user (user)
                .boardId (board.getId ())
                .boardTitle (board.getTitle ())
                .boardTypeId (board.getBoardType ().getId ())
                .boardUrl (getBoardUrl (board.getId ()))
                .regDate (LocalDateTime.now ())
                .build ();

        boardBookmarkRepository.save (boardBookmark);

        return ServiceResult.success ("정상적으로 북마크가 등록되었습니다.");
    }

    @Override
    public ServiceResult deleteBookmark(Long id, String email) {

        Optional<User> optionalUseruser = userRepository.findByEmail (email);
        User user = optionalUseruser.get ();

        if (!optionalUseruser.isPresent ()) {
            return ServiceResult.fail ("사용자가 없습니다.");
        }

        Optional<BoardBookmark> optionalBoardBookmark = boardBookmarkRepository.findById (id);

        if (!optionalBoardBookmark.isPresent ()) {
            return ServiceResult.fail ("북마크한 정보가 없습니다.");
        }

        BoardBookmark boardBookmark = optionalBoardBookmark.get ();

        if (boardBookmark.getUser ().getId () != user.getId ()) {
            return ServiceResult.fail ("본인의 북마크만 삭제할 수 있습니다.");
        }

        boardBookmarkRepository.delete (boardBookmark);

        return ServiceResult.success ("정상적으로 삭제 되었습니다.");
    }

    @Override
    public List<Board> postList(String email) {

        Optional<User> optionalUseruser = userRepository.findByEmail (email);

        if (!optionalUseruser.isPresent ()) {
            throw new BizException ("회원 정보가 존재하지 않습니다.");
        }

        User user = optionalUseruser.get ();

        return boardRepository.findByUser (user);
    }

    @Override
    public List<BoardComment> commentList(String email) {

        Optional<User> optionalUseruser = userRepository.findByEmail (email);

        if (!optionalUseruser.isPresent ()) {
            throw new BizException ("회원 정보가 존재하지 않습니다.");
        }
        User user = optionalUseruser.get ();

        return boardCommentRepository.findByUser (user);
    }
}
