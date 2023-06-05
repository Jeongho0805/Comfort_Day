//package com.jeongho.portfolio.dto;
//
//import com.jeongho.portfolio.entity.Member;
//import lombok.Getter;
//import lombok.Setter;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Getter
//@Setter
//public class ApiMemberWithCommentDto {
//
//    private String name;
//
//    private String email;
//
//    private String address;
//
//    private List<ApiCommentDto> commentList = new ArrayList<>();
//
//    public ApiMemberWithCommentDto(Member member) {
//        this.name = member.getNickname();
//        this.email = member.getAccount();
//        this.commentList = member.getCommentList().stream()
//                .map(c -> new ApiCommentDto(c))
//                .collect(Collectors.toList());
//    }
//}
//
