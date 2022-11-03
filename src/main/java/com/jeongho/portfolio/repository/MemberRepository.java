package com.jeongho.portfolio.repository;

import com.jeongho.portfolio.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByEmail(String email);
    List<Member> findAll();

    @Query("select distinct m from Member m join fetch m.boardList")
    List<Member> findAllWithBoard();

    @Query("select distinct m from Member m join fetch m.commentList")
    List<Member> findAllWithComment();

}
