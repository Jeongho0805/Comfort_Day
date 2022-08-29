package com.jeongho.portfolio.entity;

import com.jeongho.portfolio.constant.Role;
import com.jeongho.portfolio.dto.MemberFormDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "member")
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "member_name")
    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "member")
    private List<Board> boardList = new ArrayList<>();

    // DTO에서 entity로 변환 메서드
    public static Member createMember(MemberFormDto memberFormDto) {
        Member member = new Member();
        member.setAddress(memberFormDto.getAddress());
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setPassword(memberFormDto.getPassword());
        member.setRole(Role.USER);

        return member;


    }





}
