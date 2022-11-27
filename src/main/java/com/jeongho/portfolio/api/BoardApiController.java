package com.jeongho.portfolio.api;

import com.jeongho.portfolio.constant.SessionConst;
import com.jeongho.portfolio.dto.BoardFormDto;
import com.jeongho.portfolio.dto.BoardListDto;
import com.jeongho.portfolio.dto.BoardSearchDto;
import com.jeongho.portfolio.entity.Member;
import com.jeongho.portfolio.service.BoardService;
import com.jeongho.portfolio.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class BoardApiController {

    private final BoardService boardService;

    private final MemberService memberService;

    @GetMapping("/board-all")
    public ResponseEntity<Object> getAllBoardList() {
        List<BoardListDto> boardListDtos = boardService.findAllBoardListForApi();
        return ResponseEntity.ok().body(boardListDtos);
    }

    @GetMapping("boards")
    public ResponseEntity<Object> getBoardListPage(BoardSearchDto boardSearchDto, @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<BoardListDto> boardList = boardService.findAllBoardList(page, boardSearchDto);
        return ResponseEntity.ok(boardList);
    }

    @PostMapping("/boards")
    public ResponseEntity<Object> createNewBoard(@Valid @RequestBody BoardFormDto boardFormDto, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        Member member = memberService.findMemberBySession(request);
        boardService.createNewBoard(boardFormDto, member);
        return ResponseEntity.ok().body("새로운 게시글이 생성되었습니다.");
    }

    @PutMapping("/boards/{boardId}")
    public ResponseEntity<Object> updateBoard(@PathVariable("boardId") Long boardId, @Valid @RequestBody BoardFormDto boardFormDto, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        HttpSession session = request.getSession(false);
        Long loginMemberId = (Long) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (!boardService.authorizationCheck(boardId, loginMemberId)) {
            return ResponseEntity.badRequest().body("게시글 수정 권한이 없습니다.");
        }
        boardService.updateBoard(boardId, boardFormDto);
        return ResponseEntity.ok().body("게시글 수정이 완료되었습니다.");
    }

    @DeleteMapping("/boards/{boardId}")
    public ResponseEntity<Object> deleteBoard(@PathVariable("boardId") Long boardId, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Long loginMemberId = (Long) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (!boardService.authorizationCheck(boardId, loginMemberId)) {
            return ResponseEntity.badRequest().body("게시글 삭제 권한이 없습니다.");
        }
        boardService.deleteBoard(boardId);
        return ResponseEntity.ok().body("게시글 삭제가 완료되었습니다.");
    }
}
