package com.fluffypuppy.shop.notice.entity;

import com.fluffypuppy.shop.notice.dto.NoticeFormDto;
import com.fluffypuppy.shop.global.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="notice")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Notice extends BaseEntity {

    @Id
    @Column(name="notice_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false, length = 50)
    private String noticeTitle;

    @Lob
    @Column(nullable = false)
    private String noticeContent;

    @OneToMany(mappedBy = "notice", cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.LAZY)
    private List<NoticeImg> noticeImgs = new ArrayList<>();

    public void updateNotice(NoticeFormDto noticeFormDto){
        this.noticeTitle = noticeFormDto.getNoticeTitle();
        this.noticeContent = noticeFormDto.getNoticeContent();
        this.author = noticeFormDto.getAuthor();
    }
}
