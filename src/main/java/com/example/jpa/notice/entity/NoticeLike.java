package com.example.jpa.notice.entity;

import com.example.jpa.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn
    private Notice notice;

    // 1개의 글은 한명만, 1명의 사람은 여러개의 글을 사용가능한 관계
    @ManyToOne
    @JoinColumn
    private User user;
}
