package com.jeongho.portfolio.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardSearchDto {
    private String searchType;
    private String searchQuery;
    private String searchSortType;
}
