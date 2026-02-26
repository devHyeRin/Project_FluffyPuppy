package com.fluffypuppy.shop.item.dto;

import com.fluffypuppy.shop.item.constant.ItemCategory;
import com.fluffypuppy.shop.item.constant.ItemSellStatus;
import com.fluffypuppy.shop.item.entity.Item;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ItemFormDto {
    private Long id;

    @NotBlank(message = "상품명은 필수 항목입니다.")
    private String itemNm;

    @NotNull(message = "가격은 필수 항목입니다.")
    private Integer price;

    @NotBlank(message = "상품 설명은 필수 항목입니다.")
    private String itemDetail;

    @NotNull(message = "재고 수량을 입력해주세요.")
    private Integer stockNumber;

    @NotNull(message = "카테고리를 선택해주세요.")
    private ItemCategory category;

    private ItemSellStatus itemSellStatus;

    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();

    private List<Long> itemImgIds = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();

    public Item createItem(){
        return modelMapper.map(this, Item.class);
    }

    public static ItemFormDto of(Item item){
        return modelMapper.map(item, ItemFormDto.class);
    }
}
