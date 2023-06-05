package com.jeongho.portfolio.service;

import com.jeongho.portfolio.constant.SessionConst;
import com.jeongho.portfolio.dto.MemberFormDto;
import com.jeongho.portfolio.entity.Member;
import com.jeongho.portfolio.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member saveMember(MemberFormDto memberFormDto){
        validateDuplicateMember(memberFormDto.getNickname());
        Member member = Member.createMember(memberFormDto);
        return memberRepository.save(member);
    }

    public Member login(String loginId, String passwordInput) {
        validateId(loginId);
        Member member = memberRepository.findByAccount(loginId);
        validatePassword(member.getPassword(), passwordInput);
        return member;
    }

    public Member findMemberBySession(HttpSession session) {
        Long memberId = (Long) session.getAttribute(SessionConst.LOGIN_MEMBER);
        return memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("해당하는 회원이 없습니다."));
    }

    private void validateDuplicateMember(String email) {
        Member findMember = memberRepository.findByAccount(email);
        if(findMember!=null){
            throw new IllegalArgumentException("이미 가입된 회원입니다.");
        }
    }

    private void validateId(String loginId) {
        Member member = memberRepository.findByAccount(loginId);
        if (member == null) {
            throw new IllegalArgumentException("등록된 아이디가 존재하지 않습니다.");
        }
    }

    private void validatePassword(String password, String passwordInput) {
        if (!password.equals(passwordInput)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }
}
