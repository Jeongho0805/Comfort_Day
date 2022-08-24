package com.jeongho.portfolio.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardListDto {

    private Long id;

    private String title;

    private String writer;

    private String regDate;

    private int view;

    public BoardListDto(Long id, String title, String writer, String regDate, int view) {
        this.id = id;
        this.title = title;
        this.writer = writer;
        this.regDate = regDate;
        this.view = view;
    }
}
