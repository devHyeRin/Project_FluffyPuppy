package com.fluffypuppy.shop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoticeSearchDto {

    private String searchBy;

    private String searchQuery = "";
}
