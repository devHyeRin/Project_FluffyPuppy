package com.fluffypuppy.shop.notice.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.fluffypuppy.shop.notice.constant.NoticeCategory;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MainNoticeDto {

    private Long id;
    private String noticeTitle;
    private String noticeContent;
    private NoticeCategory noticeCategory;

    @QueryProjection
    public MainNoticeDto(Long id, String noticeTitle, String noticeContent, NoticeCategory noticeCategory){
        this.id = id;
        this.noticeTitle = noticeTitle;
        this.noticeContent = noticeContent;
        this.noticeCategory = noticeCategory;
    }
}
