package com.jeongho.portfolio.api;

import com.jeongho.portfolio.dto.ApiMemberDto;
import com.jeongho.portfolio.dto.ApiMemberWithBoardDto;
import com.jeongho.portfolio.dto.ApiMemberWithCommentDto;
import com.jeongho.portfolio.dto.MemberFormDto;
import com.jeongho.portfolio.entity.Member;
import com.jeongho.portfolio.service.ApiMemberService;
import com.jeongho.portfolio.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class MemberApiController {

    private final ApiMemberService apiMemberService;
    private final MemberService memberService;

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

    @PostMapping("/new-member")
    public ResponseEntity<Object> createNewMember(@Valid @RequestBody MemberFormDto memberFormDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        Member member = memberService.saveMember(memberFormDto);
        if (member == null) {
            return ResponseEntity.badRequest().body("이미 가입된 회원입니다.");
        }
        return ResponseEntity.ok().body("요청 성공");
    }
}
