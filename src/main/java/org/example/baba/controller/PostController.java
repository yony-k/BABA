package org.example.baba.controller;

import org.example.baba.domain.enums.SNSType;
import org.example.baba.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

  private final PostService postService;

  @PutMapping("/{postId}/like/{type}")
  public ResponseEntity<Void> likePost(@PathVariable Long postId, @PathVariable SNSType type) {
    postService.likePost(postId, type);
    return ResponseEntity.ok().build();
  }

  @PutMapping("/{postId}/share/{type}")
  public ResponseEntity<Void> sharePost(@PathVariable Long postId, @PathVariable SNSType type) {
    postService.sharePost(postId, type);
    return ResponseEntity.ok().build();
  }
}
