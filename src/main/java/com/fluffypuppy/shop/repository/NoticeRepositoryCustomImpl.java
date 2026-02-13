package com.fluffypuppy.shop.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
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

    private final JPAQueryFactory queryFactory;

    public NoticeRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Notice> getNoticePage(NoticeSearchDto noticeSearchDto, Pageable pageable) {

        List<Notice> content = queryFactory
                .selectFrom(QNotice.notice)
                .where(regDtsAfter(noticeSearchDto.getSearchDateType()),
                        searchByLike(noticeSearchDto.getSearchBy(), noticeSearchDto.getSearchQuery()))
                .orderBy(QNotice.notice.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(QNotice.notice.count())
                .from(QNotice.notice)
                .where(regDtsAfter(noticeSearchDto.getSearchDateType()),
                        searchByLike(noticeSearchDto.getSearchBy(), noticeSearchDto.getSearchQuery()))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    /*기간 조회*/
    private BooleanExpression regDtsAfter(String searchDateType) {
        LocalDateTime dateTime = LocalDateTime.now();

        if (StringUtils.equals("all", searchDateType) || searchDateType == null) {
            return null;
        } else if (StringUtils.equals("1d", searchDateType)) {
            dateTime = dateTime.minusDays(1);
        } else if (StringUtils.equals("1w", searchDateType)) {
            dateTime = dateTime.minusWeeks(1);
        } else if (StringUtils.equals("1m", searchDateType)) {
            dateTime = dateTime.minusMonths(1);
        } else if (StringUtils.equals("6m", searchDateType)) {
            dateTime = dateTime.minusMonths(6);
        }
        return QNotice.notice.createTime.after(dateTime);
    }

    /*작성자, 작성일자 조회*/
    private BooleanExpression searchByLike(String searchBy, String searchQuery) {
        if (StringUtils.isEmpty(searchQuery)) return null;

        if (StringUtils.equals("noticeTitle", searchBy)) {
            return QNotice.notice.noticeTitle.like("%" + searchQuery + "%");
        } else if (StringUtils.equals("createdBy", searchBy)) {
            return QNotice.notice.createdBy.like("%" + searchQuery + "%");
        }
        return null;
    }

}
