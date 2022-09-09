package com.jeongho.portfolio.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "content_id")
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Comment(String content) {
        this.content = content;
    }

    // 연관관계 편의메서드
    public void setMemberAndBoard(Board board, Member member) {
        if(this.board != null) {
            this.board.getCommentList().remove(this);
        }

        if(this.member != null) {
            this.member.getCommentList().remove(this);
        }

        this.member = member;
        this.board = board;
        member.getCommentList().add(this);
        board.getCommentList().add(this);
    }
}
