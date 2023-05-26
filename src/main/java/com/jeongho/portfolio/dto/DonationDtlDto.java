package com.jeongho.portfolio.dto;

import com.jeongho.portfolio.entity.Donation;
import com.jeongho.portfolio.entity.DonationImg;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
public class DonationDtlDto {

    private Long id;

    private String title;

    private String content;

    private String writer;

    private int targetMoney;

    private int currentMoeny;

    private int cheer;

    private List<String> imgNameList;

    public static DonationDtlDto toDto(Donation donation, List<DonationImg> donationImgs) {
        return DonationDtlDto.builder()
                .id(donation.getId())
                .title(donation.getName())
                .content(donation.getContent())
                .writer(donation.getMember().getName())
                .targetMoney(donation.getTargetMoney())
                .currentMoeny(donation.getCurrentMoney())
                .cheer(donation.getCheer())
                .imgNameList(donationImgs.stream().map(DonationImg::getImgName).collect(Collectors.toList()))
                .build();
    }
}
