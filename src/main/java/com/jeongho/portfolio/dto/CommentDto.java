package com.jeongho.portfolio.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDto {

    Long boardId;
    String content;
    String username;

}
