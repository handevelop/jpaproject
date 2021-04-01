package com.example.jpa.board.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardTypeCount {

    private long id;
    private String boardName;
    private LocalDateTime regDate;
    private boolean usingYn;
    private long boardCount;

    public BoardTypeCount(Object[] e) {
        this.id = ((BigInteger) e[0]).longValue ();
        this.boardName = ((String) e[1]);
        this.regDate = ((Timestamp) e[2]).toLocalDateTime ();
        this.usingYn = ((boolean) e[3]);
        this.boardCount = ((BigInteger) e[4]).longValue ();
    }

    public BoardTypeCount(BigInteger id, String boardName, Timestamp regDate, boolean usingYn, BigInteger boardCount) {
        this.id = id.longValue ();
        this.boardName = boardName;
        this.regDate = regDate.toLocalDateTime ();
        this.usingYn = usingYn;
        this.boardCount = boardCount.longValue ();
    }
}
