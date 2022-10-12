package com.jeongho.portfolio.repository;

import com.jeongho.portfolio.dto.BoardSearchDto;
import com.jeongho.portfolio.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {

    Page<Board> findBySearchQueryAndType(Pageable pageable, BoardSearchDto boardSearchDto);
}
