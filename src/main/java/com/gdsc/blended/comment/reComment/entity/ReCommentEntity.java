package com.gdsc.blended.comment.reComment.entity;

import com.gdsc.blended.BaseTime.BaseTimeEntity;
import com.gdsc.blended.comment.entity.CommentEntity;
import com.gdsc.blended.post.entity.PostEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class ReCommentEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne
    private CommentEntity comment;

    public void setPost(CommentEntity comment) {
        this.comment = comment;
    }


    @Builder
    public ReCommentEntity(String content, CommentEntity comment) {
        this.content = content;
        this.comment = comment;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void deletReComment(String content) {
        this.content = "해당 대댓글이 삭제되었습니다.";
    }
}
