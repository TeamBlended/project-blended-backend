package com.gdsc.blended.post.entity;

import com.gdsc.blended.BaseTime.BaseTimeEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.geo.Point;

import javax.persistence.*;
import java.time.LocalDateTime;
@Getter
@NoArgsConstructor
@Entity
@Table(name = "POST")
public class PostEntity extends BaseTimeEntity {
    // TODO
    // FiXME
    // - 속성 값 제대로 넣어주기
    @Id
    @Column(name = "post_id")
    private Long id;

    private String title;

    // varchar를 넘어서는 큰 데이터를 넣을 때 @Lob 사용
    @Lob
    private String content;

    private String status;

    @Column(columnDefinition = "GEOMETRY")
    private Point reagion;
    private String view_count;
    private String scrab_count;
    private Long category_id;
    //private Long user_id
}