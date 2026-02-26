package com.fluffypuppy.shop.item.repository;

import com.fluffypuppy.shop.item.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {
    List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);

    List<ItemImg> findByItemIdInAndRepImgYn(List<Long> itemIds, String repImgYn);
}
