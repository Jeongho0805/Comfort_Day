package com.jeongho.portfolio.dto;

import com.jeongho.portfolio.entity.Member;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiMemberDto {

    private String name;

    private String email;

    private String address;


    public ApiMemberDto(Member member) {
        this.name = member.getName();
        this.email = member.getEmail();
        this.address = member.getAddress();
    }
}
