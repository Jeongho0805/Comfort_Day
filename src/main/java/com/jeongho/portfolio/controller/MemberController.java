package com.jeongho.portfolio.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    final private MemberService memberService;
    /**
     * 새로운 회원 생성
     */
    @GetMapping("/new")
    public String memberForm(Model model) {
        model.addAttribute("memberFormDto", new MemberFormDto());

        return "member/memberForm";
    }
    @PostMapping("/new")
    public String newMember(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "member/memberForm";
        }
        Member member = memberService.saveMember(memberFormDto);
        if(member == null) {
            model.addAttribute("duplicateMemberError", "이미 해당 아이디로 가입된 회원이 있습니다.");
            return "member/memberForm";
        }
        return "redirect:/";
    }

    /**
     * 로그인 처리
     * -> redirectURL이 POST 컨트롤러 까지 전달이 안된다.
     * -> th:action에 경로가 없으면 되는건가? -> 전달받은 url에 그대로 전송된다.
     */
    @GetMapping("/login")
    public String loginForm(Model model, String redirectURL) {
        // 인터셉터에서 미인증 사용자 유무를  redirectURL를 통해 넘겨주면, null값 체크 후 로그인 후 이용해달라는 메세지 출력
        if(redirectURL != null) {
            model.addAttribute("loginRequiredMsg", "로그인 후 이용해주세요.");
        }
        // 로그인 폼 정보 담기
        model.addAttribute("loginFormDto", new LoginFormDto());

        return "member/memberLoginForm";
    }
    // @RequestParam의 defaultValue를 설정하는 이유는 redirectURL이 null일 경우 return값이 아무것도 없기 때문에 기본 값으로 "/"을 설정한다.
    @PostMapping("/login")
    public String loginMember(@RequestParam(defaultValue = "/") String redirectURL, @Valid LoginFormDto loginFormDto, BindingResult bindingResult, Model model, HttpServletRequest request) {
        // 입력 형식 검증
        if(bindingResult.hasErrors()){
            return "member/memberLoginForm";
        }
        // 로그인 처리
        Member loginMember = memberService.login(loginFormDto.getEmail(), loginFormDto.getPassword());
        // 로그인 실패 처리
        if(loginMember == null){
            model.addAttribute("loginFail", "아이디 또는 비밀번호가 일치하지 않습니다.");
            return "member/memberLoginForm";
        }
        // 로그인 성공 처리 세션에 정보 저장
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember.getId());
        session.setAttribute(SessionConst.LOGIN_MEMBER_NAME, loginMember.getName());
        return "redirect:" + redirectURL;
    }
    @GetMapping("/logout")
    public String logoutMember(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession();
        session.invalidate();
        redirectAttributes.addFlashAttribute("logoutCompleteMsg", "로그아웃을 완료하였습니다.");
        return "redirect:/";
    }
}
