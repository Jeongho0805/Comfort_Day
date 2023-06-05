package com.jeongho.portfolio.repository;

import com.jeongho.portfolio.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByAccount(String account);
    List<Member> findAll();

    @Query("select distinct m from Member m join fetch m.boardList")
    List<Member> findAllWithBoard();
}
