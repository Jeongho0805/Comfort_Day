package com.jeongho.portfolio.util;

import com.jeongho.portfolio.dto.FileUploaderDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class FileUploader {

    @Value("${file.dir}")
    private String fileDir;

    public List<FileUploaderDto> uploadeFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<FileUploaderDto> result = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            result.add(uploadeFile(multipartFile));
        }
        return result;
    }

    public FileUploaderDto uploadeFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }
        String originFileName = multipartFile.getOriginalFilename();
        String uploadeFileName = getUploadeFileName(originFileName);
        String imgUrl = getFullPath(uploadeFileName);

        multipartFile.transferTo(new File(imgUrl));
        return new FileUploaderDto(originFileName, uploadeFileName, imgUrl);
    }

    private String getUploadeFileName(String originFileName) {
        String extension = extractExtension(originFileName);
        String uuid = UUID.randomUUID().toString();
        return uuid + extension;
    }

    private String extractExtension(String originFileName) {
        int position = originFileName.lastIndexOf(".");
        return originFileName.substring(position);
    }

    public String getFullPath(String fileName) {
        return fileDir + fileName;
    }

}
