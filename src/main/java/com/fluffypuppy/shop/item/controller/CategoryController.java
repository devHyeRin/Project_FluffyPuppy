package com.fluffypuppy.shop.item.controller;


import com.fluffypuppy.shop.constant.ItemCategory;
import com.fluffypuppy.shop.item.dto.ItemSearchDto;
import com.fluffypuppy.shop.item.dto.MainItemDto;
import com.fluffypuppy.shop.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final ItemService itemService;

    @GetMapping(value = "/{category}")
    public String categoryPage(@PathVariable("category") String category,
                               Optional<Integer> page,
                               Model model) {
        Pageable pageable = PageRequest.of(page.orElse(0), 10);
        ItemSearchDto itemSearchDto = new ItemSearchDto();

        try {
            ItemCategory itemCategory = ItemCategory.valueOf(category.toUpperCase());
            itemSearchDto.setSearchCategory(itemCategory);
        } catch (IllegalArgumentException e) {
            return "redirect:/";
        }

        Page<MainItemDto> getItemCategoryPage = itemService.getCategoryItemPage(itemSearchDto, pageable);

        model.addAttribute("items", getItemCategoryPage);
        model.addAttribute("itemSearchDto",itemSearchDto);
        model.addAttribute("maxPage",5);

        return "category/itemCategory";
    }
}
