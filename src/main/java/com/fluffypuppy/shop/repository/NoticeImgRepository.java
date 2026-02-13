package com.fluffypuppy.shop.repository;

import com.fluffypuppy.shop.entity.NoticeImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeImgRepository extends JpaRepository<NoticeImg, Long> {

    List<NoticeImg> findByNoticeIdOrderByIdAsc(Long noticeId);

}
