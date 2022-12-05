package com.jeongho.portfolio.dto;

import com.jeongho.portfolio.entity.Donation;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class DonationListDto {

    private Long id;

    private String title;

    private String writer;

    private int targetMoney;

    private int currentMoeny;

    private int cheer;

    private String imgName;

    @QueryProjection
    public DonationListDto(Long id, String title, String writer, int targetMoney, int currentMoeny, int cheer, String imgName) {
        this.id = id;
        this.title = title;
        this.writer = writer;
        this.targetMoney = targetMoney;
        this.currentMoeny = currentMoeny;
        this.cheer = cheer;
        this.imgName = imgName;
    }

    public static DonationListDto toDto(Donation donation, String imgName) {
        return DonationListDto.builder()
                .id(donation.getId())
                .title(donation.getName())
                .writer(donation.getMember().getName())
                .targetMoney(donation.getTargetMoney())
                .currentMoeny(donation.getCurrentMoney())
                .cheer(donation.getCheer())
                .imgName(imgName)
                .build();
    }
}
