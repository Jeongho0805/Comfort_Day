package com.jeongho.portfolio.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "board")
@Getter
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "board_id")
    private Long id;

    private String title;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

}
