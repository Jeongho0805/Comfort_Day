package com.jeongho.portfolio.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
public class ResponseDto<T> {
    HttpStatus status;
    T data;

    public ResponseDto(HttpStatus status, T data) {
        this.status = status;
        this.data = data;
    }
}
