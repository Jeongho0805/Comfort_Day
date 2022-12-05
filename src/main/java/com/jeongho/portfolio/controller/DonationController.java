package com.jeongho.portfolio.controller;

import com.jeongho.portfolio.dto.DonationFormDto;
import com.jeongho.portfolio.dto.DonationListDto;
import com.jeongho.portfolio.entity.Member;
import com.jeongho.portfolio.service.DonationService;
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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/donation")
@RequiredArgsConstructor
@Slf4j
public class DonationController {

    private final DonationService donationService;

    private final MemberService memberService;

    @GetMapping("/list")
    public String getDonationList(Model model) {
        List<DonationListDto> donationListDtos = donationService.findAllDonations();
        model.addAttribute("donationListDtos", donationListDtos);
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

    @GetMapping("/dtl/{donationId}")
    public String getDonationDtl(@PathVariable Long donationId) {
        donationService.findDonationDtl(donationId);


    }
}
