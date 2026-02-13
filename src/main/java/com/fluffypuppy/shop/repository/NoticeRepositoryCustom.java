package com.fluffypuppy.shop.repository;

import com.fluffypuppy.shop.dto.NoticeSearchDto;
import com.fluffypuppy.shop.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeRepositoryCustom {

    Page<Notice> getNoticePage(NoticeSearchDto noticeSearchDto, Pageable pageable);
}
