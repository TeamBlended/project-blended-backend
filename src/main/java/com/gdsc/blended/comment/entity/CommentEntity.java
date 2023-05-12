package com.gdsc.blended.comment.entity;

import com.gdsc.blended.BaseTime.BaseTimeEntity;
import com.gdsc.blended.post.entity.PostEntity;
import com.gdsc.blended.user.entity.User;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    private String content;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private PostEntity post;

    @ManyToOne
    private User user;

    @Builder
    public CommentEntity(String content, PostEntity post, User user) {
        this.content = content;
        this.post = post;
        this.user = user;
    }

}


