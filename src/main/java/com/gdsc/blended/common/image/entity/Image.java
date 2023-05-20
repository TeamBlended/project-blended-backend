package com.gdsc.blended.common.image.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.gdsc.blended.post.entity.PostEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity(name = "IMAGE")
@Getter
@NoArgsConstructor
public class Image {
    @Id
    @Column(name = "Image_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    @ManyToOne
    @JoinColumn(name = "Post_id")
    @JsonBackReference
    private PostEntity post;

    @Column(name = "Path", length = 500)
    private String path;

    @Builder
    public Image(Long imageId, PostEntity post, String path){
        this.imageId = imageId;
        this.post = post;
        this.path = path;
    }
}
