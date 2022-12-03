package com.jeongho.portfolio.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class DonationFormDto {

    @NotBlank(message = "기부 제목을 입력해주세요.")
    private String donationTitle;

    @NotBlank(message = "기부 상세 내용을 입력해주세요.")
    private String donationContent;

    @Min(value = 100_000, message = "기부 목표 최소 금액은 10만원 이상이어야 합니다.")
    @NotNull(message = "기부 금액을 입력해주세요.")
    private int targetMoney;

    private List<MultipartFile> donationImgFiles;

}
