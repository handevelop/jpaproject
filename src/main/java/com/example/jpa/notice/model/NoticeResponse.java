package com.example.jpa.notice.model;

import com.example.jpa.notice.entity.Notice;
import com.example.jpa.user.entity.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class NoticeResponse {

    private Long id;

    private String title;

    private String regUserName;

    private Long regUserId;

    private String contents;

    private LocalDateTime regDate;

    private int hits;

    private int likes;

    private LocalDateTime updateDate;

    public static NoticeResponse of(Notice notice) {
        return NoticeResponse.builder ()
                .id(notice.getId ())
                .regUserName(notice.getUser ().getUserName ())
                .regUserId(notice.getUser ().getId ())
                .contents(notice.getContents ())
                .hits(notice.getHits ())
                .likes(notice.getLikes ())
                .regDate(notice.getRegDate ())
                .title(notice.getTitle ())
                .build ();
    }

}
