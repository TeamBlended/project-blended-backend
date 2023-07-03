package com.gdsc.blended.post.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gdsc.blended.BaseTime.BaseTimeEntity;
import com.gdsc.blended.category.entity.CategoryEntity;
import com.gdsc.blended.common.image.entity.ImageEntity;
import com.gdsc.blended.user.entity.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.locationtech.jts.geom.Point;

import java.text.SimpleDateFormat;
import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "tb_post")
public class PostEntity extends BaseTimeEntity {

    @Id
    @Column(name = "post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotNull
    private String title;

    // varchar를 넘어서는 큰 데이터를 넣을 때 @Lob 사용
    //@Lob
    @NotNull
    private String content;

    private Boolean liked;

    private String locationName;
    private Double latitude; //위도
    private Double longitude; //경도

    @Column(name = "view_count")
    private Long viewCount;

    @Column(name = "like_count")
    private Long likeCount;

    @Column(name = "max_recruits")
    private Long maxRecruits;

    @Column(name = "share_date_time")
    private Date shareDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    private UserEntity userId;


    public void increaseViewCount() {
        this.viewCount++;
    }

    //TODO .. 사진 추가
    //TODO .. 로그인 이휴 유저정보 추가
    //TODO .. maxRecruits를 0일떄 어떻게 해야할까?
    //TODO .. 지리정보 추가

    // @ManyToOne//private User userId;


}