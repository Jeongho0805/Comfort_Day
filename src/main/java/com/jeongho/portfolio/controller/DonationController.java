package com.jeongho.portfolio.controller;

import com.jeongho.portfolio.dto.DonationFormDto;
import com.jeongho.portfolio.entity.Member;
import com.jeongho.portfolio.service.DonationService;
import com.jeongho.portfolio.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping("/donation")
@RequiredArgsConstructor
@Slf4j
public class DonationController {

    private final DonationService donationService;

    private final MemberService memberService;

    @GetMapping("/list")
    public String getDonationList() {
        return "donation/donationList";
    }

    @GetMapping("/new")
    public String getDonationForm(Model model) {
        model.addAttribute("donationFormDto", new DonationFormDto());

        return "donation/donationForm";
    }

    @PostMapping("/new")
    public String createDonation(@Valid DonationFormDto donationFormDto, BindingResult bindingResult, HttpServletRequest request) throws IOException, ServletException {
        if (bindingResult.hasErrors()) {
            return "donation/donationForm";
        }
        Member member = memberService.findMemberBySession(request.getSession(false));
        donationService.saveDonation(donationFormDto, member);

        return "main";
    }

    @GetMapping("comment")
    public String commentpractice() {
        return "commentpractice";
    }

    @GetMapping("/test")
    public String editorTestPage() {
        return "donation/test";
    }
}
