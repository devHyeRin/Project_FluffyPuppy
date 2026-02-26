package com.fluffypuppy.shop.notice.repository;

import com.fluffypuppy.shop.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long>, NoticeRepositoryCustom{

}
