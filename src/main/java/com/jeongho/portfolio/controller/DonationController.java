package com.jeongho.portfolio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/donation")
public class DonationController {

    @GetMapping("/list")
    public String getDonationList() {
        return "donation/donationList";
    }

    @GetMapping("new")
    public String getDonationForm() {
        return "donation/donationForm";
    }

    @GetMapping("comment")
    public String commentpractice() {
        return "commentpractice";
    }
}
