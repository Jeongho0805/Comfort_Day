package com.jeongho.portfolio.service;

import com.jeongho.portfolio.dto.BoardDtlDto;
import com.jeongho.portfolio.dto.BoardFormDto;
import com.jeongho.portfolio.dto.BoardListDto;
import com.jeongho.portfolio.dto.CommentDto;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;

    // 세션 보유 여부 체크 로직

    public void createNewBoard(BoardFormDto boardFormDto, Member member) {
        Board board = new Board(boardFormDto.getTitle(), boardFormDto.getContent());
        board.setMember(member);
        boardRepository.save(board);
    }

    /**
     * pageable 적용 게시판 리스트 찾기 로직
     * page size를 10으로 하면 footer와 글쓰기 버튼이 겹치는 문제 발생
     */
    public Page<BoardListDto> findAllBoardList(int page) {
        // 최근 작성 일 기준으로 정렬
        Pageable pageable = PageRequest.of(page, 5, Sort.by("regTime").descending());
        Page<Board> boardPage = boardRepository.findAll(pageable);
        Page<BoardListDto> boardListDtoPage = boardPage.map(board -> new BoardListDto(
                board.getId(),
                board.getTitle(),
                board.getMember().getName(),
                timeReform(board.getRegTime()),
                board.getView()));

        return boardListDtoPage;

        /*
        Page<BoardListDto> boardListDtoPage = boardPage.map(new Function<Board, BoardListDto>() {
            @Override
            public BoardListDto apply(Board board) {
                BoardListDto boardListDto = new BoardListDto();
                boardListDto.setId(board.getId());
                boardListDto.setView(board.getView());
                boardListDto.setWriter(board.getMember().getName());
                boardListDto.setTitle(board.getTitle());
                boardListDto.setRegDate(timeReform(board.getRegTime()));
                return boardListDto;
            }
        });
         */

    }


//    public List<BoardListDto> findAllBoardList() {
//        List<Board> boardList = boardRepository.findAll();
//        return toBoardListDto(boardList);
//    }

    public List<BoardListDto> toBoardListDto(List<Board> boardList) {

        ArrayList<BoardListDto> list = new ArrayList<>();
        for (Board board : boardList) {
            Long id = board.getId();
            String regDate = timeReform(board.getRegTime());
            String title = board.getTitle();
            String writer = board.getMember().getName();
            int view = board.getView();
            BoardListDto boardListDto = new BoardListDto(id, title, writer, regDate, view);
            list.add(boardListDto);
        }

        return list;
    }

    public String timeReform(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm:ss"));
    }

    public BoardDtlDto findBoardDtlDto(Long boardId) {
        Board board = boardRepository.findById(boardId).get();
        String title = board.getTitle();
        String content = board.getContent();
        String regDate = timeReform(board.getRegTime());
        String writer = board.getMember().getName();
        return new BoardDtlDto(writer, title, content, regDate);
    }

    public boolean checkViewCount(Cookie[] cookies, Long boardId) {
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("viewCheck"+boardId)) {
                return true;
            }
        }
        return false;
    }

    public void updateViewCount(Long boardId) {
        boardRepository.updateViewCount(boardId);
    }


    /**
     * 삭제 인가 체크
     * 1. boardId에 저장된 member 정보 확인
     * 2. 받아온 loginMemberId와 member정보가 같은지 확인
     * 3. 다르면 false 반환
     * 4. if문에 해당하지 않으면 true 반환
     */
    public boolean AuthorizationCheck(Long boardId, Long loginMemberId) {
        Board board = boardRepository.findById(boardId).get();
        Long findMemberId = board.getMember().getId();
        if(loginMemberId != findMemberId) {
            return false;
        }
        return true;
    }

    /**
     * 삭제 기능 구현
     *
     */
    public void deleteBoard(Long boardId) {
        boardRepository.deleteById(boardId);
    }

    /**
     * update기능을 위한 boardFormDto에 데이터 담는 작업
     */
    public BoardFormDto findBoardFormDto(Long boardId) {
        BoardFormDto boardFormDto = new BoardFormDto();
        Board board = boardRepository.findById(boardId).get();
        boardFormDto.setTitle(board.getTitle());
        boardFormDto.setContent(board.getContent());
        return boardFormDto;
    }

    public void updateBoard(Long boardId, BoardFormDto boardFormDto) {
        Board board = boardRepository.findById(boardId).get();
        board.updateBoard(boardFormDto.getTitle(), boardFormDto.getContent());
    }

    /**
     * 댓글 작성 기능
     */
    public void createComment(CommentDto commentDto, Long loginMemberId) {

        Long boardId = commentDto.getBoardId();
        String content = commentDto.getContent();
        Member member = memberRepository.findById(loginMemberId).orElseThrow(() -> new IllegalStateException("해당 멤버가 없습니다."));
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalStateException("해당 게시물을 찾을 수 없습니다."));

        Comment comment = new Comment(content);
        comment.setMemberAndBoard(board, member);
        commentRepository.save(comment);
    }

    public List<CommentDto> viewAllComment(Long boardId) {
        Board findBoard = boardRepository.findById(boardId).orElseThrow(() -> new IllegalStateException());
        List<CommentDto> commentDtoList = new ArrayList<>();
        for (Comment comment : findBoard.getCommentList()) {
            CommentDto commentDto = new CommentDto();
            commentDto.setUsername(comment.getMember().getName());
            commentDto.setContent(comment.getContent());
            commentDtoList.add(commentDto);
        }

        return commentDtoList;
    }
}
