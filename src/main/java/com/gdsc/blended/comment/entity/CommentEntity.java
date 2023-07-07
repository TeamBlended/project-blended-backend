package com.gdsc.blended.comment.entity;

import com.gdsc.blended.baseTime.BaseTimeEntity;
import com.gdsc.blended.post.entity.PostEntity;
import com.gdsc.blended.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;




@NoArgsConstructor
@Getter
@Entity
@Table(name = "tb_comment")
public class CommentEntity extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id")
    private PostEntity post;

    @ManyToOne(cascade = CascadeType.ALL  )
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public void setPost(PostEntity post) {
        this.post = post;
    }

    @Builder
    public CommentEntity(String content, PostEntity post, UserEntity user) {
        this.content = content;
        this.post = post;
        this.user = user;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void deletComment(String content) {
        this.content = "해당 댓글이 삭제되었습니다.";
    }
}


