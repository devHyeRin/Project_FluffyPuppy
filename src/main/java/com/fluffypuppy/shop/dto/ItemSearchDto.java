package com.fluffypuppy.shop.dto;

import com.fluffypuppy.shop.constant.ItemCategory;
import com.fluffypuppy.shop.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemSearchDto {
    private String searchDateType;

    private ItemSellStatus searchSellStatus;

    private ItemCategory searchCategory;

    private String searchBy;

    private String searchQuery = "";
}
