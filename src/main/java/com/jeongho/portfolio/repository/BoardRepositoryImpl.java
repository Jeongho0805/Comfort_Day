package com.jeongho.portfolio.repository;

import com.jeongho.portfolio.dto.BoardSearchDto;
import com.jeongho.portfolio.entity.Board;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static com.jeongho.portfolio.entity.QBoard.board;

public class BoardRepositoryImpl implements BoardRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public BoardRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Board> findBySearchQueryAndType(Pageable pageable, BoardSearchDto boardSearchDto) {

        // content 얻기
        List<Board> content = queryFactory
                .selectFrom(board)
                .where(searchByType(boardSearchDto.getSearchType(), boardSearchDto.getSearchQuery()))
                .orderBy(board.regTime.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // total content 갯수 얻기 -> content.size() 하면 안되나?
        long total = queryFactory
                .select(Wildcard.count).from(board)
                .where(searchByType(boardSearchDto.getSearchType(), boardSearchDto.getSearchQuery()))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression searchByType(String searchType, String searchQuery) {
        if(StringUtils.equals("writer", searchType)) {
            return board.member.nickname.like("%" + searchQuery + "%");
        } else if(StringUtils.equals("title", searchType)) {
            return board.title.like("%"+searchQuery+"%");
        }
        return null;
    }
}
