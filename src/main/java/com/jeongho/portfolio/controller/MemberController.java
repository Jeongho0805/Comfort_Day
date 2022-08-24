package com.jeongho.portfolio.controller;

import com.jeongho.portfolio.constant.SessionConst;
import com.jeongho.portfolio.dto.LoginFormDto;
import com.jeongho.portfolio.dto.MemberFormDto;
import com.jeongho.portfolio.entity.Member;
import com.jeongho.portfolio.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    final private MemberService memberService;

    @GetMapping("/new")
    public String memberForm(Model model) {
        model.addAttribute("memberFormDto", new MemberFormDto());

        return "member/memberForm";
    }

    @PostMapping("/new")
    public String newMember(@Valid MemberFormDto memberFormDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "member/memberForm";
        }

        Member member = Member.createMember(memberFormDto);
        memberService.saveMember(member);

        return "redirect:/";
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("loginFormDto", new LoginFormDto());

        return "member/memberLoginForm";
    }


    @PostMapping("/login")
    public String loginMember(@Valid LoginFormDto loginFormDto, BindingResult bindingResult, Model model, HttpServletRequest request) {

        // 입력 형식 검증
        if(bindingResult.hasErrors()){
            return "member/memberLoginForm";
        }

        Member loginMember = memberService.login(loginFormDto.getEmail(), loginFormDto.getPassword());

        // 로그인 실패 처리
        if(loginMember == null){
            model.addAttribute("loginFail", "아이디 또는 비밀번호가 일치하지 않습니다.");
            return "member/memberLoginForm";
        }

        // 로그인 성공 처리
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember.getId());
        return "redirect:/";
    }
}
