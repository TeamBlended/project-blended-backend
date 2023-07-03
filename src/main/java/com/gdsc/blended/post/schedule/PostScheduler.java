package com.gdsc.blended.post.schedule;

import com.gdsc.blended.post.service.PostService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PostScheduler {

    private final PostService postService;

    public PostScheduler(PostService postService) {
        this.postService = postService;
    }

    @Scheduled(fixedDelay = 600000) // 10분마다 실행 (10분 = 10 * 60 * 1000)
    public void processCompletedPosts() {
        postService.updateCompletedStatus();
    }
}