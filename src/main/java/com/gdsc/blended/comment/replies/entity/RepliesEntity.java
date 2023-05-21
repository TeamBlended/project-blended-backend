package com.gdsc.blended.comment.replies.entity;

import com.gdsc.blended.BaseTime.BaseTimeEntity;
import com.gdsc.blended.comment.entity.CommentEntity;
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

    @ManyToOne
    private CommentEntity comment;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public void setPost(CommentEntity comment) {
        this.comment = comment;
    }


    @Builder
    public RepliesEntity(String content, CommentEntity comment, UserEntity user) {
        this.content = content;
        this.comment = comment;
        this.user = user;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void deleteReComment(String content) {
        this.content = "해당 대댓글이 삭제되었습니다.";
    }
}
