package com.jeongho.portfolio.dto;

import com.jeongho.portfolio.entity.Comment;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiCommentDto {

    private String content;

    public ApiCommentDto(Comment comment) {
        this.content = comment.getContent();
    }
}
