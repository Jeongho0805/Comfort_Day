package com.jeongho.portfolio.service;

import com.jeongho.portfolio.dto.CommentDto;
import com.jeongho.portfolio.entity.Board;
import com.jeongho.portfolio.entity.Comment;
import com.jeongho.portfolio.entity.Member;
import com.jeongho.portfolio.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public void createComment(Member member, Board board, String content) {
        Comment comment = new Comment(content);
        comment.setMemberAndBoard(board, member);
        commentRepository.save(comment);
    }

    public void updateComment(CommentDto commentDto) {
        Comment comment = findCommentByDto(commentDto);
        if (!isEqualWriter(comment, commentDto)) {
            throw new IllegalStateException("작성자와 수정자가 일치하지 않습니다.");
        }
        comment.updateComment(commentDto.getContent());
    }

    public void deleteComment(CommentDto commentDto) {
        Comment comment = findCommentByDto(commentDto);
        if (!isEqualWriter(comment, commentDto)) {
            throw new IllegalStateException("작성자와 삭제를 요청하는 회원이 일치하지 않습니다.");
        }
        commentRepository.delete(comment);
    }

    private boolean isEqualWriter(Comment comment, CommentDto commentDto) {
        String originWriter = comment.getMember().getName();
        String writer = commentDto.getUsername();
        return originWriter.equals(writer);
    }

    private Comment findCommentByDto(CommentDto commentDto) {
        Long commentId = commentDto.getCommentId();
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalStateException("해당 댓글을 찾을 수 없습니다"));
        return comment;
    }
}
