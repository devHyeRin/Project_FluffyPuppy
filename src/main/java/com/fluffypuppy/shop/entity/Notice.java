package com.fluffypuppy.shop.entity;

import com.fluffypuppy.shop.dto.NoticeFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name="notice")
@Getter
@Setter
@ToString
public class Notice extends BaseEntity{
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


    public void updateNotice(NoticeFormDto noticeFormDto){
        this.noticeTitle = noticeFormDto.getNoticeTitle();
        this.noticeContent = noticeFormDto.getNoticeContent();
        this.author = noticeFormDto.getAuthor();
    }
}
