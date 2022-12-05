package com.jeongho.portfolio.repository;

import com.jeongho.portfolio.dto.DonationListDto;
import com.jeongho.portfolio.dto.QDonationListDto;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.jeongho.portfolio.entity.QDonation.donation;
import static com.jeongho.portfolio.entity.QDonationImg.donationImg;

public class DonationRepositoryImpl implements DonationRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public DonationRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<DonationListDto> findAllDonationList() {

        List<DonationListDto> content = queryFactory
                .select(new QDonationListDto(
                        donation.id,
                        donation.name,
                        donation.member.name,
                        donation.targetMoney,
                        donation.currentMoney,
                        donation.cheer,
                        donationImg.imgName)
                )
                .from(donationImg)
                .join(donationImg.donation, donation)
                .fetch();

        return content;
    }
}
