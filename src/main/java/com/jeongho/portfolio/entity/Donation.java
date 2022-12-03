package com.jeongho.portfolio.entity;

import com.jeongho.portfolio.constant.DonationStatus;
import com.jeongho.portfolio.dto.DonationFormDto;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Donation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "donation_id")
    private Long id;

    @Column(name = "donation_name")
    private String name;

    @Column(name = "donation_content")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private int targetMoney;

    private int currentMoney;

    private int cheer;

    @Enumerated(EnumType.STRING)
    private DonationStatus donationStatus;

    public Donation(DonationFormDto donationFormDto, Member member) {
        this.name = donationFormDto.getDonationTitle();
        this.content = donationFormDto.getDonationContent();
        this.member = member;
        this.targetMoney = donationFormDto.getTargetMoney();
        this.donationStatus = DonationStatus.ONGOING;
        this.currentMoney = 0;
        this.cheer = 0;
    }
}
