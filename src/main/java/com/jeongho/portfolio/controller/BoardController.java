package com.jeongho.portfolio.controller;

import com.jeongho.portfolio.constant.SessionConst;
import com.jeongho.portfolio.dto.BoardDtlDto;
import com.jeongho.portfolio.dto.BoardFormDto;
import com.jeongho.portfolio.dto.BoardListDto;
import com.jeongho.portfolio.dto.CommentDto;
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
import java.util.List;

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
        if(session == null ) {
            redirectAttributes.addFlashAttribute("errorMessage", "로그인 후 글작성이 가능합니다.");
            return "redirect:/board/list";
        } else {
            if(session.getAttribute(SessionConst.LOGIN_MEMBER)==null) {
                redirectAttributes.addFlashAttribute("errorMessage", "로그인 후 글작성이 가능합니다.");
                return "redirect:/board/list";
            }
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
        // 댓글 목록 가져오기
        List<CommentDto> commentDtoList = boardService.viewAllComment(boardId);

        model.addAttribute("boardDtlDto", boardDtlDto);
        model.addAttribute("boardId", boardId);
        model.addAttribute("commentDtoList", commentDtoList);



        return "board/dtl";
    }

    /**
     * 게시글 삭제 로직
     * 1. 로그인 세션 받아오기
     * 2. 작성이와 일치하는지 검증
     * 3. 삭제 기능 수행
     */
    @GetMapping("/delete/{boardId}")
    public String boardDelete(HttpServletRequest request, @PathVariable("boardId") Long boardId, RedirectAttributes redirectAttributes) {

        // 로그인 세션 정보 찾기
        HttpSession session = request.getSession(false);
        if(session == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "로그인 이후 이용해주세요.");
            return "redirect:/board/dtl/"+boardId;
        }

        //세션에 LoginMember 관련 Attribute가 있는지 체크
        Object attribute = session.getAttribute(SessionConst.LOGIN_MEMBER);
        if(attribute == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "로그인 이후 이용해주세요.");
            return "redirect:/board/dtl/"+boardId;
        }

        Long loginMemberId = (Long)attribute;
        if(!boardService.AuthorizationCheck(boardId, loginMemberId)) {
            redirectAttributes.addFlashAttribute("errorMessage", "해당 게시물의 삭제 권한이 없습니다.");
            return "redirect:/board/dtl/"+boardId;
        }

        // 인가가 확인되면 해당 게시물 delete 작업 수행
        boardService.deleteBoard(boardId);

        // 삭제 기능 수행 후 게시판 화면으로 이동
        return "redirect:/board/list";

    }

    /**
     * 게시글 수정 로직
     * 1. 로그인 세션과 글 작성자 일치하는지 확인
     * 2. 일치 하지 않으면 게시글 상세화면으로 redirect // 일치하면 model에 게시글 번호 정보 담아서! update화면 반환
     */
    @GetMapping("/update/{boardId}")
    public String boardUpdateForm(HttpServletRequest request, @PathVariable("boardId") Long boardId, Model model, RedirectAttributes redirectAttributes) {
        log.info("update controller확인");

        // 로그인 세션 정보 찾기
        HttpSession session = request.getSession(false);
        if(session == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "로그인 이후 이용해주세요.");
            return "redirect:/board/dtl/"+boardId;
        }

        //세션이 있으면 LoginMember 관련 세션이 있는지 체크
        Object attribute = session.getAttribute(SessionConst.LOGIN_MEMBER);
        if(attribute == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "로그인 이후 이용해주세요.");
            return "redirect:/board/dtl/"+boardId;
        }

        // LoginMember 관련 세션이 확인 되면 작성자와 일치하는지 인가 체크 작업 수행
        Long loginMemberId = (Long)attribute;
        if(!boardService.AuthorizationCheck(boardId, loginMemberId)) {
            redirectAttributes.addFlashAttribute("errorMessage", "해당 게시물의 수정 권한이 없습니다.");
            return "redirect:/board/dtl/"+boardId;
        }

        // 모든 세션과 인가가 확인되면 boardFormDto정보와 boardId 정보를 model에 담아 update.html에 반환
        BoardFormDto boardFormDto = boardService.findBoardFormDto(boardId);
        model.addAttribute("boardFormDto", boardFormDto);
        model.addAttribute("boardId", boardId);
        return "board/update";
    }

    @PostMapping("/update/{boardId}")
    public String boardUpdate(@Valid BoardFormDto boardFormDto, BindingResult bindingResult, RedirectAttributes redirectAttributes, @PathVariable("boardId")Long boardId) {

        // 수정한 내용에 valid 오류가 있으면 다시 반환
        if(bindingResult.hasErrors()) {
            return "board/update";
        }

        // 수정한 내용에 오류가 없으면 수정 작업 수행
        boardService.updateBoard(boardId, boardFormDto);
        redirectAttributes.addFlashAttribute("errorMessage", "수정이 완료되었습니다.");

        return "redirect:/board/dtl/"+boardId;
    }


}
