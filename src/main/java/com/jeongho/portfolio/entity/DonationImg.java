package com.jeongho.portfolio.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "donation_img")
public class DonationImg {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "donation_img_id")
    private Long id;

    private String imgName; // 이미지 파일명

    private String oriImgName; // 이미지 원본 파일 명

    private String imgUrl; // 이미지 조회 경로

    private String repimgYn; // 대표 이미지 여부

    // Donation과 단방향 연관관계 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donation_id")
    private Donation donation;

    public void updateItemImg(String oriImgName, String imgName, String imgUrl) {
        this.oriImgName = oriImgName;
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }
}
