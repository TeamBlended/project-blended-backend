package com.gdsc.blended.comment.entity;

import com.gdsc.blended.BaseTime.BaseTimeEntity;
import com.gdsc.blended.post.entity.PostEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@NoArgsConstructor
@Getter
@Entity
public class CommentEntity extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private PostEntity post;

    public void setPost(PostEntity post) {
        this.post = post;
    }


    @Builder
    public CommentEntity(String content, PostEntity post) {
        this.content = content;
        this.post = post;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void deletComment(String content) {
        this.content = "해당 댓글이 삭제되었습니다.";
    }
}


