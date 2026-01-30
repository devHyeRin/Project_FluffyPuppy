package com.fluffypuppy.shop.repository;

import com.fluffypuppy.shop.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long>, NoticeRepositoryCustom{

}
