package com.jeongho.portfolio.service;

import com.jeongho.portfolio.constant.SessionConst;
import com.jeongho.portfolio.entity.Member;
import com.jeongho.portfolio.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member saveMember(Member member){
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    /**
     * 로그인 기능 구현
     */
    public Member login(String loginId, String password) {
        Member member = memberRepository.findByEmail(loginId);
        if(member.getPassword().equals(password)) {
            return member;
        } else {
            return null;
        }
    }

    /**
     * 세션으로 멤버 정보 조회
     */
    public Member findMemberBySession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Long memberId = (Long) session.getAttribute(SessionConst.LOGIN_MEMBER);

        Optional<Member> byId = memberRepository.findById(memberId);
        Member findMember = byId.get();

        return findMember;
    }



    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if(findMember!=null){
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }
}
