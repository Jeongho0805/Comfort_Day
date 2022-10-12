package com.jeongho.portfolio.service;

import com.jeongho.portfolio.constant.SessionConst;
import com.jeongho.portfolio.dto.MemberFormDto;
import com.jeongho.portfolio.entity.Member;
import com.jeongho.portfolio.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member saveMember(MemberFormDto memberFormDto){
        try {
            validateDuplicateMember(memberFormDto.getEmail());
        } catch (IllegalStateException e) {
            return null;
        }
        Member member = Member.createMember(memberFormDto);
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
        if(session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
            return null;
        }
        Long memberId = (Long) session.getAttribute(SessionConst.LOGIN_MEMBER);
        Member findMember = memberRepository.findById(memberId).get();

        return findMember;
    }



    private void validateDuplicateMember(String email) {
        Member findMember = memberRepository.findByEmail(email);
        if(findMember!=null){
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }
}
