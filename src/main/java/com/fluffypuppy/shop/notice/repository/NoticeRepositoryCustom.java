package com.fluffypuppy.shop.notice.repository;

import com.fluffypuppy.shop.notice.dto.NoticeSearchDto;
import com.fluffypuppy.shop.notice.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeRepositoryCustom {

    Page<Notice> getNoticePage(NoticeSearchDto noticeSearchDto, Pageable pageable);
}
