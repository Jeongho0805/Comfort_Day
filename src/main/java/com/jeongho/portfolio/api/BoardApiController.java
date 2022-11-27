package com.jeongho.portfolio.api;

import com.jeongho.portfolio.dto.BoardListDto;
import com.jeongho.portfolio.dto.BoardSearchDto;
import com.jeongho.portfolio.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
@Slf4j
public class BoardApiController {

    private final BoardService boardService;

    @GetMapping("/list-all")
    public ResponseEntity<Object> getAllBoardList() {

        List<BoardListDto> boardListDtos = boardService.findAllBoardListForApi();
        return ResponseEntity.ok().body(boardListDtos);
    }

    @GetMapping("list-page")
    public ResponseEntity<Object> getBoardListPage(BoardSearchDto boardSearchDto, @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<BoardListDto> boardList = boardService.findAllBoardList(page, boardSearchDto);
        return ResponseEntity.ok(boardList);
    }
}
