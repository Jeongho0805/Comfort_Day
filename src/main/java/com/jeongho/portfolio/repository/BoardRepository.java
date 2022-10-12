package com.jeongho.portfolio.repository;

import com.jeongho.portfolio.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {
    Page<Board> findAll(Pageable pageable);

    @Modifying
    @Query("update Board b set b.view = b.view + 1 where b.id = :boardId")
    void updateViewCount(@Param("boardId") Long boardId);

}
