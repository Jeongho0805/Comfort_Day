package com.jeongho.portfolio.dto;

import com.jeongho.portfolio.entity.Member;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class ApiMemberWithBoardDto {

    private String name;

    private String email;

    private String address;

    private List<ApiBoardDto> boardList = new ArrayList<>();

    public ApiMemberWithBoardDto(Member member) {
        this.name = member.getName();
        this.email = member.getEmail();
        this.address = member.getAddress();
        this.boardList = member.getBoardList().stream()
                .map(b -> new ApiBoardDto(b))
                .collect(Collectors.toList());
    }
}
