package com.jeongho.portfolio.controller;

import com.jeongho.portfolio.dto.BoardDtlDto;
import com.jeongho.portfolio.dto.BoardFormDto;
import com.jeongho.portfolio.dto.BoardListDto;
import com.jeongho.portfolio.entity.Member;
import com.jeongho.portfolio.service.BoardService;
import com.jeongho.portfolio.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final MemberService memberService;


    /**
     * pageable 적용 후
     */
    @GetMapping("/list")
    public String boardList(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<BoardListDto> paging = boardService.findAllBoardList(page);
        model.addAttribute("paging", paging);
        model.addAttribute("maxPage",5);
        return "board/list";

    }

    /**
     * pageable 적용 전
     */
    /*
    @GetMapping("/list")
    public String boardList(Model model) {
        List<BoardListDto> boardListDto = boardService.findAllBoardList();
        model.addAttribute("boardListDto", boardListDto);
        return "board/list";
    }
     */



    @GetMapping("/new")
    public String boardForm(Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {

        // 세션이 없을 경우 처리
        HttpSession session = request.getSession(false);
        if(session == null) {
            redirectAttributes.addAttribute("errorMessage", "로그인 후 글작성이 가능합니다.");
            return "redirect:/board/list";
        }


        // 성공 로직
        model.addAttribute("boardFormDto", new BoardFormDto());
        return "board/new";
    }

    @PostMapping("/new")
    public String boardNew(@Valid BoardFormDto boardFormDto, BindingResult bindingResult, HttpServletRequest request) {
        if(bindingResult.hasErrors()) {
            return "board/new";
        }

        Member member = memberService.findMemberBySession(request);
        boardService.createNewBoard(boardFormDto, member);

        return "redirect:/board/list";
    }

    @GetMapping("/dtl/{boardId}")
    public String boardDtl(HttpServletRequest request, HttpServletResponse response, Model model, @PathVariable("boardId") Long boardId) {

        // 조회수 증가 로직
        Cookie[] cookies = request.getCookies();
        // 해당 boardId와 관련된 cookie 정보가 없으면 쿠키를 만들고 조회수 증가처리
        if(!boardService.checkViewCount(cookies, boardId)) {
            Cookie cookie = new Cookie("viewCheck"+boardId, String.valueOf(boardId));
            response.addCookie(cookie);
            log.info("cookie 생성 로직 검증/ cookieName={}", cookie.getName());
            boardService.updateViewCount(boardId);
        }

        BoardDtlDto boardDtlDto = boardService.findBoardDtlDto(boardId);
        model.addAttribute("boardDtlDto", boardDtlDto);

        return "board/dtl";
    }
}
