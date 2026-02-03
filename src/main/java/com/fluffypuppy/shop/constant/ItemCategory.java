package com.fluffypuppy.shop.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ItemCategory {

    FOOD("사료"),
    SNACK("간식"),
    BATH("목욕"),
    CLOTHES("의류"),
    STUFF("용품"),
    BOWEL("배변");

    private final String description;
}
