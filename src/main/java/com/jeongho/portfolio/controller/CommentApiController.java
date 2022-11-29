package com.jeongho.portfolio.controller;

import com.jeongho.portfolio.dto.CommentDto;
import com.jeongho.portfolio.entity.Board;
import com.jeongho.portfolio.entity.Member;
import com.jeongho.portfolio.service.BoardService;
import com.jeongho.portfolio.service.CommentService;
import com.jeongho.portfolio.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
@Slf4j
public class CommentApiController {

    private final CommentService commentService;

    private final MemberService memberService;

    private final BoardService boardService;

    @PostMapping("/comments")
    public ResponseEntity<Object> createComment(@RequestBody CommentDto commentDto, HttpServletRequest request) {
        try {
            Member member = memberService.findMemberBySession(request.getSession(false));
            Board board = boardService.findBoardById(commentDto.getBoardId());
            String content = commentDto.getContent();
            commentService.createComment(member, board, content);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        log.info("comment 생성 컨트롤러 성공 여부 파악");
        return ResponseEntity.ok().body("댓글이 등록되었습니다.");
    }

    @PutMapping("/comments")
    public ResponseEntity<Object> updateComment(@RequestBody CommentDto commentDto) {
        try {
            commentService.updateComment(commentDto);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().body("댓글이 수정되었습니다.");
    }

    @DeleteMapping("/comments")
    public ResponseEntity<Object> deleteComment(@RequestBody CommentDto commentDto) {
        try {
            commentService.deleteComment(commentDto);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().body("댓글이 삭제되었습니다.");
    }
}
