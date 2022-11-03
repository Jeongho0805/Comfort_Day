package com.jeongho.portfolio.dto;

import lombok.Getter;

@Getter
public class CommentDto {

    Long boardId;

    Long commentId;
    String content;
    String username;

    public CommentDto() {
    }
    public CommentDto(Long commentId, String content, String username) {
        this.commentId = commentId;
        this.content = content;
        this.username = username;
    }
}
