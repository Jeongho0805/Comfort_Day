//package com.jeongho.portfolio.controller.api;
//
//import com.jeongho.portfolio.constant.SessionConst;
//import com.jeongho.portfolio.dto.*;
//import com.jeongho.portfolio.entity.Member;
//import com.jeongho.portfolio.service.MemberApiService;
//import com.jeongho.portfolio.service.MemberService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;
//import javax.validation.Valid;
//import java.util.List;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api")
//@Slf4j
//public class MemberApiController {
//
//    private final MemberApiService apiMemberService;
//    private final MemberService memberService;
//
//    @GetMapping("/members")
//    public List<ApiMemberDto> getAllMembers() {
//        List<ApiMemberDto> memberDtoList = apiMemberService.findAllMember();
//        return memberDtoList;
//    }
//
//    @GetMapping("/members-board")
//    public List<ApiMemberWithBoardDto> getAllMembersWithBoard() {
//        List<ApiMemberWithBoardDto> result = apiMemberService.findAllMemberWithBoard();
//        return result;
//    }
//
//    @GetMapping("/members-comment")
//    public List<ApiMemberWithCommentDto> getAllMemberWithComment() {
//        List<ApiMemberWithCommentDto> result = apiMemberService.findAllMemberWithComment();
//        return result;
//    }
//
//    @PostMapping("/new-member")
//    public ResponseEntity<Object> createNewMember(@Valid @RequestBody MemberFormDto memberFormDto, BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
//        }
//        Member member = memberService.saveMember(memberFormDto);
//        if (member == null) {
//            return ResponseEntity.badRequest().body("이미 가입된 회원입니다.");
//        }
//        return ResponseEntity.ok().body("회원가입이 완료되었습니다.");
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<Object> loginMember(@Valid @RequestBody LoginFormDto loginFormDto, BindingResult bindingResult, HttpServletRequest request) {
//        if (bindingResult.hasErrors()) {
//            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
//        }
//        Member loginMember = memberService.login(loginFormDto.getEmail(), loginFormDto.getPassword());
//        if (loginMember == null) {
//            return ResponseEntity.badRequest().body("아이디 또는 비밀번호가 일치하지 않습니다.");
//        }
//        HttpSession session = request.getSession();
//        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember.getId());
//        return ResponseEntity.ok().body("로그인을 하였습니다.");
//    }
//
//    @GetMapping("/logout")
//    public ResponseEntity<Object> logoutMember (HttpServletRequest request) {
//        HttpSession session = request.getSession();
//        session.invalidate();
//        return ResponseEntity.ok().body("로그아웃을 완료했습니다.");
//    }
//}
