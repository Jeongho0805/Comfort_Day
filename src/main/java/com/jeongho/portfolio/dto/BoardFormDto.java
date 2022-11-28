package com.jeongho.portfolio.dto;

import com.jeongho.portfolio.entity.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
public class BoardFormDto {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "본문을 입력해주세요.")
    private String content;

    public BoardFormDto() {
    }

    public static BoardFormDto toDto(Board board) {
        return BoardFormDto.builder()
                .title(board.getTitle())
                .content(board.getContent())
                .build();
    }
}
