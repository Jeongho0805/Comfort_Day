package com.jeongho.portfolio.dto;

import com.jeongho.portfolio.entity.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class CommentDto {

    private Long boardId;

    private Long commentId;
    private String content;
    private String username;

    public CommentDto(Long boardId, Long commentId, String content, String username) {
        this.boardId = boardId;
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
