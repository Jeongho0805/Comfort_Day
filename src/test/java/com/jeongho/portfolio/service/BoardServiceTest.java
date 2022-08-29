package com.jeongho.portfolio.service;


import com.jeongho.portfolio.entity.Board;
import com.jeongho.portfolio.entity.Member;
import com.jeongho.portfolio.repository.BoardRepository;
import com.jeongho.portfolio.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BoardServiceTest {

    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    void creatTestData() {
        for(int i=0; i<=100; i++) {
            Member member = memberRepository.findById(1L).get();
            Board board = new Board("테스트"+i, "테스트데이터", 0, member);
            boardRepository.save(board);
        }
    }

}