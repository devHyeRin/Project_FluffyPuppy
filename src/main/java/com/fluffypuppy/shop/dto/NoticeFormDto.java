package com.fluffypuppy.shop.dto;

import com.fluffypuppy.shop.constant.ItemCategory;
import com.fluffypuppy.shop.constant.NoticeCategory;
import com.fluffypuppy.shop.entity.Item;
import com.fluffypuppy.shop.entity.Notice;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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

    @NotNull(message = "내용을 입력해주세요.")
    private String noticeContent;

    private List<NoticeImgDto> noticeImgDtoList = new ArrayList<>();

    private List<Long> noticeImgIds = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();

    public Notice createNotice(){
        return modelMapper.map(this, Notice.class);
    }

    public static NoticeFormDto of(Notice notice){
        return modelMapper.map(notice, NoticeFormDto.class);
    }
}
