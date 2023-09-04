package com.gdsc.blended.comment.replies.entity;

import com.gdsc.blended.common.baseTime.BaseTimeEntity;
import com.gdsc.blended.comment.entity.CommentEntity;
import com.gdsc.blended.post.entity.ExistenceStatus;
import com.gdsc.blended.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;



@NoArgsConstructor
@Getter
@Entity
@Table(name = "tb_replies")
public class RepliesEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "existence_status")
    private ExistenceStatus existenceStatus;

    @ManyToOne(cascade = CascadeType.ALL)
    private CommentEntity comment;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public void setPost(CommentEntity comment) {
        this.comment = comment;
    }


    @Builder
    public RepliesEntity(String content, ExistenceStatus existenceStatus, CommentEntity comment, UserEntity user) {
        this.content = content;
        this.existenceStatus = existenceStatus;
        this.comment = comment;
        this.user = user;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void deleteReplies(ExistenceStatus existenceStatus) {
        this.existenceStatus = existenceStatus;
    }
}
