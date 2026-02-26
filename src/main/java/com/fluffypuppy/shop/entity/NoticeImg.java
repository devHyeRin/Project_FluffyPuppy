package com.fluffypuppy.shop.entity;

import com.fluffypuppy.shop.global.entity.BaseEntity;
import lombok.*;
import javax.persistence.*;

@Entity
@Table(name="notice_img")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoticeImg extends BaseEntity {

    @Id
    @Column(name = "notice_img_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imgName;
    private String oriImgName;
    private String imgUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_id")
    private Notice notice;

    public void setNotice(Notice notice) {
        this.notice = notice;
    }

    public static NoticeImg createNoticeImg(Notice notice) {
        NoticeImg noticeImg = new NoticeImg();
        noticeImg.setNotice(notice);
        return noticeImg;
    }

    public void updateNoticeImg(String oriImgName, String imgName, String imgUrl){
        this.oriImgName = oriImgName;
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }
}
