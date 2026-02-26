package com.fluffypuppy.shop.item.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ItemSellStatus {
    SELL("판매중"),
    SOLD_OUT("품절");

    private final String description;

    public boolean isSell(){
        return this == SELL;
    }
}
