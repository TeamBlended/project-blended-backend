package com.gdsc.blended.user;

import com.gdsc.blended.comment.entity.CommentEntity;
import com.gdsc.blended.comment.replies.entity.RepliesEntity;
import com.gdsc.blended.comment.replies.repository.RepliesRepository;
import com.gdsc.blended.comment.repository.CommentRepository;
import com.gdsc.blended.post.entity.PostEntity;
import com.gdsc.blended.post.repository.PostRepository;
import com.gdsc.blended.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@AllArgsConstructor
public class ScheduledTasks {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final RepliesRepository repliesRepository;

    @Scheduled(cron = "0 0 0 * * *") //일주일로 설정하기
    public void deleteExpiredDate(){
        LocalDateTime oneWeekAgo = LocalDateTime.now().minus(1, ChronoUnit.WEEKS);

        userRepository.findAll().forEach(userEntity -> {
            if (userEntity.getWithdrawalDate() != null && userEntity.getWithdrawalDate().isBefore(oneWeekAgo)) {
                List<PostEntity> postToDelete = postRepository.findByUserIdAndCreatedDateBefore(userEntity, oneWeekAgo);
                postRepository.deleteAll(postToDelete);

                List<CommentEntity> commentsToDelete = commentRepository.findByUserAndCreatedDateBefore(userEntity, oneWeekAgo);
                commentRepository.deleteAll(commentsToDelete);

                List<RepliesEntity> RepliesToDelete = repliesRepository.findByComment_UserAndCreatedDateBefore(userEntity, oneWeekAgo);
                repliesRepository.deleteAll(RepliesToDelete);
            }
        });
    }
}
