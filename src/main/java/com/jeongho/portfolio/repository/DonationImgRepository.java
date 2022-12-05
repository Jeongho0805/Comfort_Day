package com.jeongho.portfolio.repository;

import com.jeongho.portfolio.entity.Donation;
import com.jeongho.portfolio.entity.DonationImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DonationImgRepository extends JpaRepository<DonationImg, Long> {

    List<DonationImg> findByDonation(Donation donation);

}
