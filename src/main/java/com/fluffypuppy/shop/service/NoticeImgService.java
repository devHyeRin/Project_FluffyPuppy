package com.fluffypuppy.shop.service;

import com.fluffypuppy.shop.entity.NoticeImg;
import com.fluffypuppy.shop.repository.NoticeImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class NoticeImgService {
    @Value("${noticeImgLocation}")
    private String noticeImgLocation;

    private final NoticeImgRepository noticeImgRepository;

    private final FileService fileService;

    /*이미지 저장*/
    public void saveNoticeImg(NoticeImg noticeImg, MultipartFile noticeImgFile) throws Exception{
        String oriImgName = noticeImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";
        System.out.println(oriImgName);

        //파일 업로도
        if(!StringUtils.isEmpty(oriImgName)){
            System.out.println("****이미지 업로드****");
            imgName = fileService.uploadFile(noticeImgLocation, oriImgName, noticeImgFile.getBytes());
            System.out.println(imgName);
            imgUrl = "/images/notice/" + imgName;
        }
        //상품 이미지 저장
        noticeImg.updateNoticeImg(oriImgName, imgName, imgUrl);
        noticeImgRepository.save(noticeImg);
    }

    /*이미지 수정*/
    public void updateNoticeImg(Long noticeImgId, MultipartFile noticeImgFile) throws Exception{
        if(!noticeImgFile.isEmpty()){
            NoticeImg savedNoticeImg = noticeImgRepository.findById(noticeImgId)
                    .orElseThrow(EntityNotFoundException::new);

            if(!StringUtils.isEmpty(savedNoticeImg.getImgName())){
                fileService.deleteFile(noticeImgLocation + "/" + savedNoticeImg.getImgName());
            }

            String oriImgName = noticeImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(noticeImgLocation, oriImgName, noticeImgFile.getBytes());
            String imgUrl = "/images/notice/" + imgName;

            savedNoticeImg.updateNoticeImg(oriImgName, imgName, imgUrl);
        }
    }
}
