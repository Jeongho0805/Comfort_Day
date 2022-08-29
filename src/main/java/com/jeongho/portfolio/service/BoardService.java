package com.jeongho.portfolio.service;

import com.jeongho.portfolio.dto.BoardDtlDto;
import com.jeongho.portfolio.dto.BoardFormDto;
import com.jeongho.portfolio.dto.BoardListDto;
import com.jeongho.portfolio.entity.Board;
import com.jeongho.portfolio.entity.Member;
import com.jeongho.portfolio.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        Pageable pageable = PageRequest.of(page, 5);
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
}
