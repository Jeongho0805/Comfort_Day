package com.jeongho.portfolio.controller;

import com.jeongho.portfolio.dto.BoardDtlDto;
import com.jeongho.portfolio.dto.BoardFormDto;
import com.jeongho.portfolio.dto.BoardListDto;
import com.jeongho.portfolio.entity.Member;
import com.jeongho.portfolio.service.BoardService;
import com.jeongho.portfolio.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final MemberService memberService;

    @GetMapping("/list")
    public String boardList(Model model) {
        List<BoardListDto> boardListDto = boardService.findAllBoardList();
        model.addAttribute("boardListDto", boardListDto);
        return "board/list";
    }

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
    public String boardDtl(Model model, @PathVariable("boardId") Long boardId) {

        BoardDtlDto boardDtlDto = boardService.findBoardDtlDto(boardId);
        model.addAttribute("boardDtlDto", boardDtlDto);

        return "board/dtl";
    }
}
