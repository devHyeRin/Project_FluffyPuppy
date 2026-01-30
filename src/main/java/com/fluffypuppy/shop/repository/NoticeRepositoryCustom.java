package com.fluffypuppy.shop.repository;

import com.fluffypuppy.shop.dto.NoticeSearchDto;
import com.fluffypuppy.shop.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeRepositoryCustom {
    /*관리자 공지사항 페이지*/
    Page<Notice> getAdminNoticePage(NoticeSearchDto noticeSearchDto, Pageable pageable);

    /*사용자 공지사항 페이지*/
    Page<Notice> getUserNoticePage(NoticeSearchDto noticeSearchDto, Pageable pageable);

}
