package com.jeongho.portfolio.entity;

import com.jeongho.portfolio.constant.Role;
import com.jeongho.portfolio.dto.MemberFormDto;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String nickname;

    @Column(unique = true)
    private String account;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "member")
    private List<Board> boardList = new ArrayList<>();


    public static Member createMember(MemberFormDto memberFormDto) {
        return Member.builder()
                .nickname(memberFormDto.getNickname())
                .account(memberFormDto.getAccount())
                .password(memberFormDto.getPassword())
                .role(Role.USER)
                .build();

    }
}
