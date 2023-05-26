package com.jeongho.portfolio.querydsl;

import com.jeongho.portfolio.entity.Member;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.jeongho.portfolio.entity.QMember.member;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @Autowired
    EntityManager em;

    JPAQueryFactory queryFactory;

    @Autowired
    DonationRepository donationRepository;

    @BeforeEach
    public void before() {
        queryFactory = new JPAQueryFactory(em);
    }

    @Test
    public void startJPQL() {
        Member findMember = em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", "박정호")
                .getSingleResult();

        assertThat(findMember.getName()).isEqualTo("박정호");
    }

    @Test
    public void startQuerydsl() {
//        QMember m = QMember.member;
        /**
         *  위에 선언을 하지 않고 Qmember.member을  static선언해서 쓸 것.
         *  querydsl은 JPQL 빌더 역할 -> sql문 말고 작성되는 JPQL을 볼 수 있다 -> 약간의 설정 필요 use_sql_comments = true
         */
        Member findMember = queryFactory
                .select(member)
                .from(member)
                .where(member.name.eq("박정호"))
                .fetchOne();

        assertThat(findMember.getName()).isEqualTo("박정호");
    }
    @Test
    public void search() {
        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.name.eq("박정호").and(member.id.eq(1L)))
                .fetchOne();

        assertThat(findMember.getName()).isEqualTo("박정호");
    }
    // and는 쉼표로 표현 가능!
    @Test
    public void searchAndParam() {
        Member findMember = queryFactory
                .selectFrom(member)
                .where(
                        member.name.eq("박정호"),
                        member.id.eq(1L)
                )
                .fetchOne();

        assertThat(findMember.getName()).isEqualTo("박정호");
    }
    @Test
    public void resultFetch() {
//        List<Member> fetch = queryFactory
//                .selectFrom(member)
//                .fetch();
//
//        Member fetchOne = queryFactory
//                .selectFrom(member)
//                .fetchOne();
//
//        Member fetchFirst = queryFactory
//                .selectFrom(member)
//                .fetchFirst();

        // depreacted된 fetchResult() -> 쿼리가 두번 실행된다 (count, 내용)
        QueryResults<Member> result = queryFactory
                .selectFrom(member)
                .fetchResults();

        result.getTotal();
        List<Member> content = result.getResults();
    }

    @Test
    void 쿼리프로젝션_테스트() {
        List<DonationListDto> allDonationList = donationRepository.findAllDonationList();
        System.out.println(allDonationList);
    }
}
