package com.example.jpa.notice.entity;

import com.example.jpa.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 1개의 글은 한명만, 1명의 사람은 여러개의 글을 사용가능한 관계
    @ManyToOne
    private User user;

    @Column
    private String title;

    @Column
    private String contents;

    @Column
    private LocalDateTime regDate;

    @Column
    private int hits;

    @Column
    private int likes;

    @Column
    private LocalDateTime updateDate;

    @Column
    private boolean deleted;

    @Column
    private LocalDateTime deletedDate;
}
