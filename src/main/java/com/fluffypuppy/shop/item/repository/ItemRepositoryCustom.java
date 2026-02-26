package com.fluffypuppy.shop.item.repository;

import com.fluffypuppy.shop.item.dto.ItemSearchDto;
import com.fluffypuppy.shop.item.dto.MainItemDto;
import com.fluffypuppy.shop.item.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {
    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

    Page<MainItemDto> getCategoryItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

    Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
}
