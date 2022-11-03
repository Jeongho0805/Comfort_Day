package com.jeongho.portfolio.entity;

import com.jeongho.portfolio.constant.DonationStatus;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private int targetMoney;

    private int currentMoney;

    private int cheer;

    @Enumerated(EnumType.STRING)
    private DonationStatus donationStatus;

}
