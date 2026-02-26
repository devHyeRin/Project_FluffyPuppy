package com.fluffypuppy.shop.notice.dto;

import com.fluffypuppy.shop.notice.entity.Notice;
import com.fluffypuppy.shop.notice.entity.NoticeImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class NoticeFormDto {

    private Long id;

    @NotBlank(message = "작성자를 입력해주세요.")
    private String author;

    @NotBlank(message = "제목을 입력해주세요.")
    private String noticeTitle;

    @NotBlank(message = "내용을 입력해주세요.")
    private String noticeContent;

    private LocalDateTime createTime;

    private List<NoticeImgDto> noticeImgDtoList = new ArrayList<>();

    private List<Long> noticeImgIds = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();

    //dto -> entity
    public Notice createNotice(){
        return modelMapper.map(this, Notice.class);
    }

    //entity -> dto
    public static NoticeFormDto of(Notice notice){
        NoticeFormDto noticeFormDto = modelMapper.map(notice, NoticeFormDto.class);
        noticeFormDto.setCreateTime(notice.getCreateTime());

        return noticeFormDto;
    }


    @Getter @Setter
    public static class NoticeImgDto {
        private Long id;
        private String imgName;
        private String oriImgName;
        private String imgUrl;

        private static ModelMapper modelMapper = new ModelMapper();

        public static NoticeImgDto of(NoticeImg noticeImg) {
            return modelMapper.map(noticeImg, NoticeImgDto.class);
        }
    }
}
