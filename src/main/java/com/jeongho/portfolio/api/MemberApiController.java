package com.jeongho.portfolio.api;

import com.jeongho.portfolio.dto.ApiMemberDto;
import com.jeongho.portfolio.dto.ApiMemberWithCommentDto;
import com.jeongho.portfolio.dto.ApiMemberWithBoardDto;
import com.jeongho.portfolio.service.ApiMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberApiController {

    private final ApiMemberService apiMemberService;

    @GetMapping("/members")
    public List<ApiMemberDto> getAllMembers() {
        List<ApiMemberDto> memberDtoList = apiMemberService.findAllMember();
        return memberDtoList;
    }

    @GetMapping("/members-board")
    public List<ApiMemberWithBoardDto> getAllMembersWithBoard() {
        List<ApiMemberWithBoardDto> result = apiMemberService.findAllMemberWithBoard();
        return result;
    }

    @GetMapping("/members-comment")
    public List<ApiMemberWithCommentDto> getAllMemberWithComment() {
        List<ApiMemberWithCommentDto> result = apiMemberService.findAllMemberWithComment();
        return result;
    }
}
