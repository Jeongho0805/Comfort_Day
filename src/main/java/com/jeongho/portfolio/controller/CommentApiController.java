package com.jeongho.portfolio.controller;

import com.jeongho.portfolio.constant.SessionConst;
import com.jeongho.portfolio.dto.CommentDto;
import com.jeongho.portfolio.dto.ResponseDto;
import com.jeongho.portfolio.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@RestController
@Slf4j
public class CommentApiController {

    private final BoardService boardService;

    @PostMapping(value="/comment/new", produces = "application/json")
    public ResponseDto<Integer> createComment(@RequestBody CommentDto commentDto, HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        if(session == null) {
            throw new IllegalStateException("로그인 후 이용해주세요");
        }
        Object attribute = session.getAttribute(SessionConst.LOGIN_MEMBER);
        if(attribute == null) {
            throw new IllegalStateException("로그인 후 이욯애주세요.");
        }
        Long loginMemberId = (Long)attribute;

        String content = commentDto.getContent();
        Long boardId = commentDto.getBoardId();

        log.info("데이터 전달 체크, content값={}, boardId값 ={}, loginMemberId값 ={}", content, boardId,loginMemberId);

        boardService.createComment(commentDto, loginMemberId);

        return new ResponseDto<Integer>(HttpStatus.OK, 1);
    }
}
