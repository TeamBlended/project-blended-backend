package com.gdsc.blended.post.entity;

import com.gdsc.blended.BaseTime.BaseTimeEntity;
import com.gdsc.blended.category.entity.CategoryEntity;
import com.gdsc.blended.user.entity.User;
import lombok.*;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "POST")
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

    private Boolean status;

    private String locationName;
    private Double latitude; //위도
    private Double longitude; //경도

    @Column(name = "view_count")
    private Long viewCount;

    @Column(name = "scrap_count")
    private Long scrapCount;

    @Column(name = "max_recruits")
    private Long maxRecruits;

    @Column(name = "recruited")
    private Long recruited;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    public void increaseViewCount() {
        this.viewCount++;
    }

    //TODO .. 사진 추가
    //TODO .. 로그인 이휴 유저정보 추가
    //TODO .. maxRecruits를 0일떄 어떻게 해야할까?
    //TODO .. 지리정보 추가

   @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;


}