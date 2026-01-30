package com.fluffypuppy.shop.controller;


import com.fluffypuppy.shop.constant.ItemCategory;
import com.fluffypuppy.shop.dto.ItemSearchDto;
import com.fluffypuppy.shop.dto.MainItemDto;
import com.fluffypuppy.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {
    private final ItemService itemService;
    /*강아지 사료 카테고리 조회*/
    @GetMapping(value = "/food")
    public String categoryFood(Optional<Integer> page, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
        ItemSearchDto itemSearchDto = new ItemSearchDto();
        itemSearchDto.setSearchCategory(ItemCategory.FOOD);
        Page<MainItemDto> getItemCategoryPage = itemService.getCategoryItemPage(itemSearchDto, pageable);

        model.addAttribute("items", getItemCategoryPage);
        model.addAttribute("itemSearchDto",itemSearchDto);
        model.addAttribute("maxPage",5);
        return "category/itemFood";
    }
    /*영양간식 카테고리 조회*/
    @GetMapping(value = "/snack")
    public String categorySnack(Optional<Integer> page, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
        ItemSearchDto itemSearchDto = new ItemSearchDto();
        itemSearchDto.setSearchCategory(ItemCategory.SNACK);
        Page<MainItemDto> getItemCategoryPage = itemService.getCategoryItemPage(itemSearchDto, pageable);

        model.addAttribute("items", getItemCategoryPage);
        model.addAttribute("itemSearchDto",itemSearchDto);
        model.addAttribute("maxPage",5);
        return "category/itemSnack";
    }
    /*목욕용품 카테고리 조회*/
    @GetMapping(value = "/bath")
    public String categoryBath(Optional<Integer> page, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
        ItemSearchDto itemSearchDto = new ItemSearchDto();
        itemSearchDto.setSearchCategory(ItemCategory.BATH);
        Page<MainItemDto> getItemCategoryPage = itemService.getCategoryItemPage(itemSearchDto, pageable);

        model.addAttribute("items", getItemCategoryPage);
        model.addAttribute("itemSearchDto",itemSearchDto);
        model.addAttribute("maxPage",5);
        return "category/itemBath";
    }
    /*장난감/옷 카테고리 조회*/
    @GetMapping(value = "/clothes")
    public String categoryClothes(Optional<Integer> page, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
        ItemSearchDto itemSearchDto = new ItemSearchDto();
        itemSearchDto.setSearchCategory(ItemCategory.CLOTHES);
        Page<MainItemDto> getItemCategoryPage = itemService.getCategoryItemPage(itemSearchDto, pageable);

        model.addAttribute("items", getItemCategoryPage);
        model.addAttribute("itemSearchDto",itemSearchDto);
        model.addAttribute("maxPage",5);
        return "category/itemClothes";
    }
    /*잡화 카테고리 조회*/
    @GetMapping(value = "/stuff")
    public String categoryStuff(Optional<Integer> page, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
        ItemSearchDto itemSearchDto = new ItemSearchDto();
        itemSearchDto.setSearchCategory(ItemCategory.STUFF);
        Page<MainItemDto> getItemCategoryPage = itemService.getCategoryItemPage(itemSearchDto, pageable);

        model.addAttribute("items", getItemCategoryPage);
        model.addAttribute("itemSearchDto",itemSearchDto);
        model.addAttribute("maxPage",5);
        return "category/itemStuff";
    }

    /*배변용품 카테고리 조회*/
    @GetMapping(value = "/bowel")
    public String categoryBowel(Optional<Integer> page, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
        ItemSearchDto itemSearchDto = new ItemSearchDto();
        itemSearchDto.setSearchCategory(ItemCategory.BOWEL);
        Page<MainItemDto> getItemCategoryPage = itemService.getCategoryItemPage(itemSearchDto, pageable);

        model.addAttribute("items", getItemCategoryPage);
        model.addAttribute("itemSearchDto",itemSearchDto);
        model.addAttribute("maxPage",5);
        return "category/itemBowel";
    }

}
