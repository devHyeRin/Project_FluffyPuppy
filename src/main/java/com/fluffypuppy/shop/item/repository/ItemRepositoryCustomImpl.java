package com.fluffypuppy.shop.item.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.fluffypuppy.shop.constant.ItemCategory;
import com.fluffypuppy.shop.constant.ItemSellStatus;
import com.fluffypuppy.shop.item.dto.ItemSearchDto;
import com.fluffypuppy.shop.item.dto.MainItemDto;
import com.fluffypuppy.shop.dto.QMainItemDto;
import com.fluffypuppy.shop.item.entity.Item;
import com.fluffypuppy.shop.entity.QItem;
import com.fluffypuppy.shop.entity.QItemImg;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom{
    private JPAQueryFactory queryFactory;

    public ItemRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }
    /*판매상태 조회*/
    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus){
        return searchSellStatus == null ?
                null : QItem.item.itemSellStatus.eq(searchSellStatus);
    }
    /*카테고리별 조회*/
    private BooleanExpression searchCategoryEq(ItemCategory searchCategory){
        return searchCategory == null ? null : QItem.item.category.eq(searchCategory);
    }
    private BooleanExpression regDtsAfter(String searchDateType){  //all, 1d, 1w, 1m 6m
        LocalDateTime dateTime = LocalDateTime.now();

        if(StringUtils.equals("all", searchDateType) || searchDateType == null){
            return null;
        }
        else if (StringUtils.equals("1d", searchDateType)){
            dateTime = dateTime.minusDays(1);
        }
        else if (StringUtils.equals("1w", searchDateType)){
            dateTime = dateTime.minusWeeks(1);
        }
        else if (StringUtils.equals("1m", searchDateType)){
            dateTime = dateTime.minusMonths(1);
        }
        else if (StringUtils.equals("6m", searchDateType)){
            dateTime = dateTime.minusMonths(6);
        }
        return QItem.item.createTime.after(dateTime);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery){
        if(StringUtils.equals("itemNm",searchBy)){ // 상품명
            return QItem.item.itemNm.like("%"+searchQuery+"%");
        }
        else if(StringUtils.equals("createdBy",searchBy)){ // 작성자
            return QItem.item.createdBy.like("%"+searchQuery+"%");
        }
        return null;
    }

    private BooleanExpression itemNmLike(String searchQuery){
        return StringUtils.isEmpty(searchQuery) ? null : QItem.item.itemNm.like("%"+searchQuery+"%");
    }

    @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        QueryResults<Item> results = queryFactory.selectFrom(QItem.item)
                        .where(regDtsAfter(itemSearchDto.getSearchDateType()),
                                searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                                searchByLike(itemSearchDto.getSearchBy(),itemSearchDto.getSearchQuery()))
                        .orderBy(QItem.item.id.desc())
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize()).fetchResults();
        List<Item> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content,pageable,total);
    }

    @Override
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        //QMainItemDto @QueryProjection을 하용하면 DTO로 바로 조회 가능
        QueryResults<MainItemDto> results = queryFactory.select(new QMainItemDto(item.id, item.itemNm,
                        item.itemDetail,itemImg.imgUrl,item.price, item.itemSellStatus))
                // join 내부조인 .repImgYn.eq("Y") 대표이미지만 가져온다.
                .from(itemImg).join(itemImg.item, item).where(itemImg.repImgYn.eq("Y"))
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .orderBy(item.id.desc()).offset(pageable.getOffset()).limit(pageable.getPageSize()).fetchResults();
        List<MainItemDto> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<MainItemDto> getCategoryItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        QueryResults<MainItemDto> results = queryFactory.select(new QMainItemDto(item.id, item.itemNm, item.itemDetail, itemImg.imgUrl, item.price, item.itemSellStatus))
                .from(itemImg).join(itemImg.item, item)
                .where(itemImg.repImgYn.eq("Y"))
                .where(searchCategoryEq(itemSearchDto.getSearchCategory()))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize()).fetchResults();
        List<MainItemDto> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }
}