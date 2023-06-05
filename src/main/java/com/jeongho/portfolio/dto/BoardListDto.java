package com.jeongho.portfolio.dto;

import com.jeongho.portfolio.entity.Board;
import com.jeongho.portfolio.util.TimeReformer;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BoardListDto {

    public BoardListDto() {
    }

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

    public static BoardListDto toDto(Board board) {
        return BoardListDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .writer(board.getMember().getNickname())
                .regDate(TimeReformer.getBoardRegisteredTime(board.getRegTime()))
                .view(board.getView())
                .build();
    }
}
