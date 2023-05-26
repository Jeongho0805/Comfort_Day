package com.jeongho.portfolio.service;

import com.jeongho.portfolio.dto.DonationDtlDto;
import com.jeongho.portfolio.dto.DonationFormDto;
import com.jeongho.portfolio.dto.DonationListDto;
import com.jeongho.portfolio.dto.FileUploaderDto;
import com.jeongho.portfolio.entity.Donation;
import com.jeongho.portfolio.entity.DonationImg;
import com.jeongho.portfolio.entity.Member;
import com.jeongho.portfolio.repository.DonationImgRepository;
import com.jeongho.portfolio.repository.DonationRepository;
import com.jeongho.portfolio.util.FileUploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DonationService {

    private final DonationRepository donationRepository;

    private final DonationImgRepository donationImgRepository;

    private final FileUploader fileUploader;

    public void saveDonation(DonationFormDto donationFormDto, Member member) throws IOException {
        Donation donation = new Donation(donationFormDto, member);
        donationRepository.save(donation);

        List<MultipartFile> donationImgFiles = donationFormDto.getDonationImgFiles();
        List<FileUploaderDto> fileUploaderDtos = fileUploader.uploadeFiles(donationImgFiles);
        for (FileUploaderDto fileUploaderDto : fileUploaderDtos) {
            DonationImg donationImg = new DonationImg();
            donationImg.updateItemImg(fileUploaderDto);
            donationImg.setDonation(donation);
            donationImgRepository.save(donationImg);
        }
    }

    public List<DonationListDto> findAllDonations() {
        List<Donation> donations = donationRepository.findAll();
        List<DonationListDto> donationListDtos = new ArrayList<>();
        for (Donation donation : donations) {
            List<DonationImg> donationImgs = donationImgRepository.findByDonation(donation);
            String imgName = donationImgs.get(0).getImgName();
            donationListDtos.add(DonationListDto.toDto(donation, imgName));
        }
        return donationListDtos;
    }

    public DonationDtlDto findDonationDtl(Long donationId) {
        Donation donation = donationRepository.findById(donationId).orElseThrow(() -> new IllegalArgumentException("해당하는 기부 게시물이 없습니다."));
        List<DonationImg> donationImgs = donationImgRepository.findByDonation(donation);
        return DonationDtlDto.toDto(donation, donationImgs);
    }
}
