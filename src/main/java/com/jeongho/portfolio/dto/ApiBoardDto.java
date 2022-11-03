package com.jeongho.portfolio.dto;

import com.jeongho.portfolio.entity.Board;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiBoardDto {

    private String title;

    private String content;

    private int view;

    public ApiBoardDto(Board board) {
        this.title = board.getTitle();
        this.content = board.getContent();
        this.view = board.getView();
    }
}
