package com.jeongho.portfolio.service;

import com.jeongho.portfolio.entity.Member;
import com.jeongho.portfolio.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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



    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if(findMember!=null){
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }
}
