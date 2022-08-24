package com.jeongho.portfolio.service;

import com.jeongho.portfolio.dto.BoardDtlDto;
import com.jeongho.portfolio.dto.BoardFormDto;
import com.jeongho.portfolio.dto.BoardListDto;
import com.jeongho.portfolio.entity.Board;
import com.jeongho.portfolio.entity.Member;
import com.jeongho.portfolio.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public List<BoardListDto> findAllBoardList() {
        List<Board> boardList = boardRepository.findAll();
        return toBoardListDto(boardList);
    }

    public List<BoardListDto> toBoardListDto(List<Board> boardList) {

        ArrayList<BoardListDto> list = new ArrayList<>();
        for (Board board : boardList) {
            Long id = board.getId();
            String regDate = timeReform(board.getRegTime());
            String title = board.getTitle();
            String writer = board.getMember().getName();
            int view = 1;
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
}
