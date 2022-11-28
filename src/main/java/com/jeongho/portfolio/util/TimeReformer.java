package com.jeongho.portfolio.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeReformer {

    public static String getBoardRegisteredTime(LocalDateTime input) {
        return input.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm:ss"));
    }
}
