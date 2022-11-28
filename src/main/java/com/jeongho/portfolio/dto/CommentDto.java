package com.jeongho.portfolio.dto;

import com.jeongho.portfolio.entity.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CommentDto {

    Long boardId;

    Long commentId;
    String content;
    String username;

    public CommentDto() {}

    public CommentDto(Long commentId, String content, String username) {
        this.commentId = commentId;
        this.content = content;
        this.username = username;
    }

    public static CommentDto toDto(Comment comment) {
        return CommentDto.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .username(comment.getMember().getName())
                .build();
    }
}
