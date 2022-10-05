package com.jeongho.portfolio.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();

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
        if(this.member != null) {
            this.member.getBoardList().remove(this);
        }
        this.member = member;
        member.getBoardList().add(this);
    }

    public void updateBoard(String title, String content) {
        this.title = title;
        this.content = content;
    }

}
