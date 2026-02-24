package com.fluffypuppy.shop.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ItemCategory {

    FOOD("사료", "food"),
    SNACK("영양간식", "snack"),
    BATH("목욕용품", "bath"),
    BOWEL("위생/배변", "bowel"),
    CLOTHES("장난감/옷", "toy"),
    STUFF("굿즈", "stuff");

    private final String description;
    private final String imageName;
}
