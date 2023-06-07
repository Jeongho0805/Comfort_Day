package com.jeongho.portfolio.controller.page;

import com.jeongho.portfolio.constant.SessionConst;
import com.jeongho.portfolio.dto.BoardDtlDto;
import com.jeongho.portfolio.dto.BoardFormDto;
import com.jeongho.portfolio.dto.BoardListDto;
import com.jeongho.portfolio.dto.BoardSearchDto;
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

    @GetMapping("/list")
    public String boardList(Model model, BoardSearchDto boardSearchDto, @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<BoardListDto> paging = boardService.findAllBoardList(page, boardSearchDto);
        model.addAttribute("paging", paging);
        model.addAttribute("boardSearchDto", boardSearchDto);
        model.addAttribute("maxPage",5);
        return "board/list";
    }
    @GetMapping("/new")
    public String boardForm(Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        model.addAttribute("boardFormDto", new BoardFormDto());
        return "board/new";
    }
    @PostMapping("/new")
    public String boardNew(@Valid BoardFormDto boardFormDto, BindingResult bindingResult, HttpServletRequest request) {
        if(bindingResult.hasErrors()) {
            return "board/new";
        }
        Member member = memberService.findMemberBySession(request.getSession());
        boardService.createNewBoard(boardFormDto, member);
        return "redirect:/board/list";
    }

    @GetMapping("/dtl/{boardId}")
    public String boardDtl(HttpServletRequest request, HttpServletResponse response, Model model, @PathVariable("boardId") Long boardId) {
        /* 게시글 조회수 증가 처리 로직 */
        Cookie[] cookies = request.getCookies();
        if(!boardService.hasCookie(cookies, boardId)) {
            Cookie cookie = new Cookie("viewCheck"+boardId, String.valueOf(boardId));
            response.addCookie(cookie);
            boardService.updateViewCount(boardId);
        }
        /* 게시글 상세 화면 처리 로직 */
        BoardDtlDto boardDtlDto = boardService.findBoardDtlDto(boardId);
        model.addAttribute("boardDtlDto", boardDtlDto);
        model.addAttribute("boardId", boardId);
        return "board/dtl";
    }

    @GetMapping("/delete/{boardId}")
    public String boardDelete(HttpServletRequest request, @PathVariable("boardId") Long boardId, RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession(false);
        Long loginMemberId = (Long) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if(!boardService.authorizationCheck(boardId, loginMemberId)) {
            redirectAttributes.addFlashAttribute("errorMessage", "해당 게시물의 삭제 권한이 없습니다.");
            return "redirect:/board/dtl/"+boardId;
        }
        boardService.deleteBoard(boardId);
        return "redirect:/board/list";
    }

    @GetMapping("/update/{boardId}")
    public String boardUpdateForm(HttpServletRequest request, @PathVariable("boardId") Long boardId, Model model, RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession(false);
        Long loginMemberId = (Long) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (!boardService.authorizationCheck(boardId, loginMemberId)) {
            redirectAttributes.addFlashAttribute("errorMessage", "해당 게시물의 수정 권한이 없습니다.");
            return "redirect:/board/dtl/"+boardId;
        }
        BoardFormDto boardFormDto = boardService.findBoardFormDto(boardId);
        model.addAttribute("boardFormDto", boardFormDto);
        model.addAttribute("boardId", boardId);
        return "board/update";
    }

    @PostMapping("/update/{boardId}")
    public String boardUpdate(@Valid BoardFormDto boardFormDto, BindingResult bindingResult, RedirectAttributes redirectAttributes, @PathVariable("boardId")Long boardId) {
        if (bindingResult.hasErrors()) {
            return "board/update";
        }
        boardService.updateBoard(boardId, boardFormDto);
        redirectAttributes.addFlashAttribute("errorMessage", "수정이 완료되었습니다.");
        return "redirect:/board/dtl/"+boardId;
    }
}
