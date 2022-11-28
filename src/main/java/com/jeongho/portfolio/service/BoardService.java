package com.jeongho.portfolio.service;

import com.jeongho.portfolio.dto.*;
import com.jeongho.portfolio.entity.Board;
import com.jeongho.portfolio.entity.Comment;
import com.jeongho.portfolio.entity.Member;
import com.jeongho.portfolio.repository.BoardRepository;
import com.jeongho.portfolio.repository.CommentRepository;
import com.jeongho.portfolio.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;

    public Page<BoardListDto> findAllBoardList(int page, BoardSearchDto boardSearchDto) {
        Pageable pageable = PageRequest.of(page, 5, Sort.by("regTime").descending());
        Page<Board> boardPage =  boardRepository.findBySearchQueryAndType(pageable, boardSearchDto);
        return boardPage.map(BoardListDto::toDto);
    }

    public List<BoardListDto> findAllBoardListForApi() {
        List<Board> boardList = boardRepository.findAll();
        return boardList.stream().map(BoardListDto::toDto).collect(Collectors.toList());
    }

    public void createNewBoard(BoardFormDto boardFormDto, Member member) {
        Board board = new Board(boardFormDto.getTitle(), boardFormDto.getContent());
        board.setMember(member);
        boardRepository.save(board);
    }

    public BoardDtlDto findBoardDtlDto(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("해당하는 게시판이 없습니다."));
        return BoardDtlDto.toDto(board);
    }

    public boolean hasCookie(Cookie[] cookies, Long boardId) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("viewCheck" + boardId)) {
                return true;
            }
        }
        return false;
    }

    public void updateViewCount(Long boardId) {
        boardRepository.updateViewCount(boardId);
    }

    public boolean authorizationCheck(Long boardId, Long loginMemberId) {
        Board board = boardRepository.findById(boardId).orElseThrow(IllegalStateException::new);
        Long findMemberId = board.getMember().getId();
        if (loginMemberId != findMemberId) {
            return false;
        }
        return true;
    }

    public void deleteBoard(Long boardId) {
        boardRepository.deleteById(boardId);
    }

    public BoardFormDto findBoardFormDto(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(IllegalArgumentException::new);
        return BoardFormDto.toDto(board);
    }

    public void updateBoard(Long boardId, BoardFormDto boardFormDto) {
        Board board = boardRepository.findById(boardId).orElseThrow(IllegalArgumentException::new);
        board.updateBoard(boardFormDto.getTitle(), boardFormDto.getContent());
    }


    public void createComment(CommentDto commentDto, Long loginMemberId) {

        Long boardId = commentDto.getBoardId();
        String content = commentDto.getContent();
        Member member = memberRepository.findById(loginMemberId).orElseThrow(() -> new IllegalStateException("해당 멤버가 없습니다."));
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalStateException("해당 게시물을 찾을 수 없습니다."));

        Comment comment = new Comment(content);
        comment.setMemberAndBoard(board, member);
        commentRepository.save(comment);
    }

    public List<CommentDto> findAllCommentDtos(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalStateException());
        return board.getCommentList().stream().map(CommentDto::toDto).collect(Collectors.toList());
    }

    public void updateComment(CommentDto commentDto) {
        Comment comment = findCommentByDto(commentDto);

        if (!isEqualWriter(comment, commentDto)) {
            throw new IllegalStateException("작성자와 수정자가 일치하지 않습니다.");
        }
        comment.updateComment(commentDto.getContent());
    }

    public void deleteComment(CommentDto commentDto) {
        Comment comment = findCommentByDto(commentDto);

        if (!isEqualWriter(comment, commentDto)) {
            throw new IllegalStateException("작성자와 삭제를 요청하는 회원이 일치하지 않습니다.");
        }
        commentRepository.delete(comment);
    }

    private boolean isEqualWriter(Comment comment, CommentDto commentDto) {
        String originWriter = comment.getMember().getName();
        String writer = commentDto.getUsername();
        return originWriter.equals(writer);
    }

    private Comment findCommentByDto(CommentDto commentDto) {
        Long commentId = commentDto.getCommentId();
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalStateException("해당 댓글을 찾을 수 없습니다"));
        return comment;
    }


}
