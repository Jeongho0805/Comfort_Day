package com.jeongho.portfolio.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "board")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "board_id")
    private Long id;

    private String title;

    private String content;

    private int view;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Board(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Board(String title, String content, int view, Member member) {
        this.title = title;
        this.content = content;
        this.view = view;
        this.member = member;
    }

    // 글쓴이와 연관관계 편의 메서드
    public void setMember(Member member) {
        this.member = member;
        member.getBoardList().add(this);
    }

}
