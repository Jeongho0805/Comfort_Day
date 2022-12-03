package com.jeongho.portfolio.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileUploaderDto {

    private String originFileName;

    private String uploadFileName;

    private String imgUrl;

    public FileUploaderDto(String originFileName, String uploadFileName, String imgUrl) {
        this.originFileName = originFileName;
        this.uploadFileName = uploadFileName;
        this.imgUrl = imgUrl;
    }
}
