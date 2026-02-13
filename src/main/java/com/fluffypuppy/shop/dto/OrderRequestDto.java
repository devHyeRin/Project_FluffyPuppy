package com.fluffypuppy.shop.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
public class OrderRequestDto {
    @NotNull(message = "상품 아이디는 필수 입력 값입니다.")
    private Long itemId;

    @Positive(message = "최소 주문 수량은 1개입니다.")
    @Max(value = 999, message = "최대 주문 수량은 999개입니다.")
    private int count;
}
