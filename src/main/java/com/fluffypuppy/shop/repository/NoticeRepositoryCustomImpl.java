package com.fluffypuppy.shop.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.fluffypuppy.shop.constant.NoticeCategory;
import com.fluffypuppy.shop.dto.*;
import com.fluffypuppy.shop.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

public class NoticeRepositoryCustomImpl implements NoticeRepositoryCustom{

    private JPAQueryFactory queryFactory;

    public NoticeRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }
    /*기간 조회*/
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
        return QNotice.notice.createTime.after(dateTime);
    }

    /*작성자, 작성일자 조회*/
    private BooleanExpression searchByLike(String searchBy, String searchQuery){
        if(StringUtils.equals("noticeTitle", searchBy)){
            return QNotice.notice.noticeTitle.like("%" + searchQuery + "%");
        }
        else if(StringUtils.equals("createdBy", searchBy)){
            return QNotice.notice.createdBy.like("%" + searchQuery + "%");
        }
        return null;
    }

    /*게시판 제목 조회*/
    private BooleanExpression noticeTitleLike(String searchQuery){
        return StringUtils.isEmpty(searchQuery) ? null : QNotice.notice.noticeTitle.like("%" + searchQuery + "%");
    }

    @Override
    public Page<Notice> getAdminNoticePage(NoticeSearchDto noticeSearchDto, Pageable pageable){
        QueryResults<Notice> results = queryFactory.selectFrom(QNotice.notice)
                .where(regDtsAfter(noticeSearchDto.getSearchDateType()),
                        searchByLike(noticeSearchDto.getSearchBy(), noticeSearchDto.getSearchQuery()))
                .orderBy(QNotice.notice.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        List<Notice> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }

    /*사용자 페이지 조회*/
    @Override
    public Page<Notice> getUserNoticePage(NoticeSearchDto noticeSearchDto, Pageable pageable){
        QueryResults<Notice> results = queryFactory.selectFrom(QNotice.notice)
                .where(regDtsAfter(noticeSearchDto.getSearchDateType()),
                        searchByLike(noticeSearchDto.getSearchBy(), noticeSearchDto.getSearchQuery()))
                .orderBy(QNotice.notice.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        List<Notice> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }

}
