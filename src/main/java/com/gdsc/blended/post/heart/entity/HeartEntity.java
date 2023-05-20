package com.gdsc.blended.post.heart.entity;

import com.gdsc.blended.post.entity.PostEntity;
import com.gdsc.blended.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class HeartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // 좋아요 아이디
    private Long likePostId;

    @ManyToOne
    @JoinColumn(name = "post_id")
    // 게시글 아이디
    private PostEntity post;

    @ManyToOne
    @JoinColumn
    // 유저 아이디
    private UserEntity user;

    @Column(nullable = false)
    private boolean status; // true = 좋아요, false = 좋아요 취소

    public HeartEntity(PostEntity post, UserEntity user) {
        this.post = post;
        this.user = user;
        this.status = true;
    }
    public void unLikeBoard(PostEntity post) {
        this.status = false;
        post.setLikeCount(post.getLikeCount() - 1);
    }
}
