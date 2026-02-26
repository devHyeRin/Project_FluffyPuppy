package com.fluffypuppy.shop.notice.repository;

import com.fluffypuppy.shop.notice.entity.NoticeImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeImgRepository extends JpaRepository<NoticeImg, Long> {

    List<NoticeImg> findByNoticeIdOrderByIdAsc(Long noticeId);

}
