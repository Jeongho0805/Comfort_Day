package com.jeongho.portfolio.repository;

import com.jeongho.portfolio.dto.DonationListDto;

import java.util.List;

public interface DonationRepositoryCustom {

    List<DonationListDto> findAllDonationList();
}
