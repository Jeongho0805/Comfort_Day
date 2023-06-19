package com.jeongho.portfolio.controller.page;

import com.jeongho.portfolio.constant.SessionConst;
import com.jeongho.portfolio.dto.LoginFormDto;
import com.jeongho.portfolio.dto.MemberFormDto;
import com.jeongho.portfolio.entity.Member;
import com.jeongho.portfolio.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {

    final private MemberService memberService;

    @GetMapping("/register")
    public String memberForm(Model model) {
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "registerPage";
    }
    @PostMapping("/register")
    public String newMember(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "registerPage";
        }
        try {
            Member member = memberService.saveMember(memberFormDto);
        } catch (IllegalArgumentException e) {
            model.addAttribute("duplicateMemberError", e.getMessage());
            return "registerPage";
        }
        return "redirect:/";
    }

    @GetMapping("/login")
    public String loginForm(Model model, String redirectURL) {
        /* 인터셉터에서 미인증 사용자의 요청 edirectURL을 넘겨주면 로그인 후 이용해달라는 메세지 출력 */
        if(redirectURL != null) {
            model.addAttribute("loginRequiredMsg", "로그인 후 이용해주세요.");
        }
        model.addAttribute("loginFormDto", new LoginFormDto());
        return "member/memberLoginForm";
    }

    @PostMapping("/login")
    public String loginMember(@RequestParam(defaultValue = "/") String redirectURL, @Valid LoginFormDto loginFormDto, BindingResult bindingResult, Model model, HttpServletRequest request) {
        if(bindingResult.hasErrors()){
            return "member/memberLoginForm";
        }
        Member loginMember;
        try {
            loginMember = memberService.login(loginFormDto.getEmail(), loginFormDto.getPassword());
        } catch (IllegalArgumentException e) {
            model.addAttribute("loginFail", e.getMessage());
            return "member/memberLoginForm";
        }
        setSessionAttributes(request, loginMember);
        return "redirect:" + redirectURL;
    }

    private void setSessionAttributes(HttpServletRequest request, Member loginMember) {
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember.getId());
        session.setAttribute(SessionConst.LOGIN_MEMBER_name, loginMember.getNickname());
    }

    @GetMapping("/logout")
    public String logoutMember(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession();
        session.invalidate();
        redirectAttributes.addFlashAttribute("logoutCompleteMsg", "로그아웃을 완료하였습니다.");
        return "redirect:/";
    }
}
