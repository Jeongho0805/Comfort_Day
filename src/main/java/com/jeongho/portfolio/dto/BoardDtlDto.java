package com.jeongho.portfolio.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardDtlDto {

    private String writer;

    private String title;

    private String content;

    private String regDate;

    public BoardDtlDto(String writer, String title, String content, String regDate) {
        this.writer = writer;
        this.title = title;
        this.content = content;
        this.regDate = regDate;
    }
}
