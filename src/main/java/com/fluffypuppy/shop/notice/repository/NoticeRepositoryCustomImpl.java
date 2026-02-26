package com.fluffypuppy.shop.notice.repository;

import com.fluffypuppy.shop.notice.dto.NoticeSearchDto;
import com.fluffypuppy.shop.notice.entity.Notice;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.fluffypuppy.shop.notice.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
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
                .where(searchByLike(noticeSearchDto.getSearchBy(), noticeSearchDto.getSearchQuery()))
                .orderBy(QNotice.notice.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(Wildcard.count)
                .from(QNotice.notice)
                .where(searchByLike(noticeSearchDto.getSearchBy(), noticeSearchDto.getSearchQuery()))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    /*작성자, 제목 조회*/
    private BooleanExpression searchByLike(String searchBy, String searchQuery) {
        if (StringUtils.isEmpty(searchQuery)) return null;

        if (StringUtils.equals("noticeTitle", searchBy)) {
            return QNotice.notice.noticeTitle.like("%" + searchQuery + "%");
        }
        else if (StringUtils.equals("author", searchBy)) {
            return QNotice.notice.author.like("%" + searchQuery + "%");
        }

        return null;
    }

}
