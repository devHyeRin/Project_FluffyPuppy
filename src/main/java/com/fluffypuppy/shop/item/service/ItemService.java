package com.fluffypuppy.shop.item.service;

import com.fluffypuppy.shop.item.dto.ItemFormDto;
import com.fluffypuppy.shop.item.dto.ItemImgDto;
import com.fluffypuppy.shop.item.dto.ItemSearchDto;
import com.fluffypuppy.shop.item.dto.MainItemDto;
import com.fluffypuppy.shop.item.entity.Item;
import com.fluffypuppy.shop.item.entity.ItemImg;
import com.fluffypuppy.shop.item.repository.ItemImgRepository;
import com.fluffypuppy.shop.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;

    /* 상품 등록*/
    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {
        //상품등록
        Item item = itemFormDto.createItem();
        itemRepository.save(item);
        //이미지 등록
        for (int i =0; i < itemImgFileList.size(); i++){
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);

            if (i == 0)
                itemImg.setRepImgYn("Y");
            else
                itemImg.setRepImgYn("N");

            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
        }
        return item.getId();
    }

    /*상품 수정을 위한 데이터 가져오기*/
    @Transactional(readOnly = true)
    public ItemFormDto getItemDtl(Long itemId){
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);

        List<ItemImgDto> itemImgDtoList = new ArrayList<>();

        for(ItemImg itemImg : itemImgList){
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto);
        }

        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);
        ItemFormDto itemFormDto = ItemFormDto.of(item);    // entity->dto 바로 전달
        itemFormDto.setItemImgDtoList(itemImgDtoList);
        return itemFormDto;
    }

    /*상품 수정 업데이트*/
    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{
        Item item = itemRepository.findById(itemFormDto.getId()).orElseThrow(EntityNotFoundException::new);
        item.updateItem(itemFormDto);

        List<Long> itemImgIds = itemFormDto.getItemImgIds();

        for(int i =0; i<itemImgFileList.size();i++){
            itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i));
        }
        return item.getId();
    }
    /*상품 데이터 조회*/
    @Transactional(readOnly = true)
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getAdminItemPage(itemSearchDto,pageable);
    }

    /*카테고리 상품 조회*/
    @Transactional(readOnly = true)
    public Page<MainItemDto> getCategoryItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getCategoryItemPage(itemSearchDto, pageable);
    }

    /*등록 상품 메인 표시*/
    @Transactional(readOnly = true)
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getMainItemPage(itemSearchDto, pageable);
    }
}