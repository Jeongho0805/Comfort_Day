package com.jeongho.portfolio.repository;

import com.jeongho.portfolio.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
