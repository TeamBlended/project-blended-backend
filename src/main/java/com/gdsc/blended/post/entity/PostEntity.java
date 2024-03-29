package com.gdsc.blended.post.entity;

import com.gdsc.blended.baseTime.BaseTimeEntity;
import com.gdsc.blended.category.entity.CategoryEntity;
import com.gdsc.blended.comment.entity.CommentEntity;
import com.gdsc.blended.common.image.entity.ImageEntity;
import com.gdsc.blended.post.heart.entity.HeartEntity;
import com.gdsc.blended.user.entity.UserEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


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
    @Size(max = 3000)
    @Nullable
    private String content;

    private Boolean liked;
    private Boolean completed;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "existence_status")
    private ExistenceStatus existenceStatus;

    /*@ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JoinColumn(name = "category_id")
    private CategoryEntity category;*/

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    private UserEntity userId;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<CommentEntity> comments = new ArrayList<>();

    @OneToMany(mappedBy = "postEntity", cascade = CascadeType.ALL)
    private List<PostInAlcoholEntity> postInAlcoholEntities = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<ImageEntity> images = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<HeartEntity> hearts = new ArrayList<>();


    public void increaseViewCount() {
        this.viewCount++;
    }

}