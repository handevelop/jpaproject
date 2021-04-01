package com.example.jpa.notice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


// 모든 인자를 넣은 생성자, 아무 인자 없이 넣은 생성자, 빌더패턴을 위한 어노테이션, 게터/세터/투스트링 등을 위한 데이터 어노테이션 추가
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class NoticeInput {

    @Size(min = 10, max = 100, message = "10-100자 사이로 입력해 주세요.")
    @NotBlank(message = "제목은 필수")
    private String title;

    @Size(min = 50, max = 1000, message = "50-1000자 사이로 입력해 주세요.")
    @NotBlank(message = "내용은 필수")
    private String contents;

}
