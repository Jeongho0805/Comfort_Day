package com.jeongho.portfolio.service;

import com.jeongho.portfolio.dto.ApiMemberDto;
import com.jeongho.portfolio.dto.ApiMemberWithCommentDto;
import com.jeongho.portfolio.dto.ApiMemberWithBoardDto;
import com.jeongho.portfolio.entity.Member;
import com.jeongho.portfolio.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberApiService {

    private final MemberRepository memberRepository;

    public List<ApiMemberDto> findAllMember() {
        List<Member> members = memberRepository.findAll();

        List<ApiMemberDto> result = members.stream()
                .map(m -> new ApiMemberDto(m))
                .collect(Collectors.toList());

        return result;
    }

    public List<ApiMemberWithBoardDto> findAllMemberWithBoard() {
        List<Member> members = memberRepository.findAllWithBoard();

        List<ApiMemberWithBoardDto> result = members.stream()
                .map(m -> new ApiMemberWithBoardDto(m))
                .collect(Collectors.toList());

        return result;
    }

    public List<ApiMemberWithCommentDto> findAllMemberWithComment() {

        List<Member> memberList = memberRepository.findAllWithComment();
        List<ApiMemberWithCommentDto> result = memberList.stream()
                .map(m -> new ApiMemberWithCommentDto(m))
                .collect(Collectors.toList());

        return result;
    }
}
