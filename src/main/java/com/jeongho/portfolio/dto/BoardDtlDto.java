package com.jeongho.portfolio.dto;

import com.jeongho.portfolio.entity.Board;
import com.jeongho.portfolio.util.TimeReformer;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
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

    public static BoardDtlDto toDto(Board board) {
        return BoardDtlDto.builder()
                .writer(board.getMember().getName())
                .title(board.getTitle())
                .content(board.getContent())
                .regDate(TimeReformer.getBoardRegisteredTime(board.getRegTime()))
                .build();
    }
}
