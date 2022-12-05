package com.jeongho.portfolio.repository;

import com.jeongho.portfolio.entity.Donation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonationRepository extends JpaRepository<Donation, Long>, DonationRepositoryCustom {
}
