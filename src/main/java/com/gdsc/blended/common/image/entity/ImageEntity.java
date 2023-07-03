package com.gdsc.blended.common.image.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.gdsc.blended.post.entity.PostEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class ImageEntity {
    @Id
    @Column(name = "image_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    @ManyToOne
    @JoinColumn(name = "post_id")
    @JsonBackReference
    private PostEntity post;

    @Column(name = "path", length = 500)
    private String path;

    @Builder
    public ImageEntity(PostEntity post,String path){
        this.post = post;
        this.path = path;
    }
}
