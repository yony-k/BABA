package org.example.baba.controller;

import java.util.List;

import org.example.baba.controller.dto.response.PostDetailResponseDto;
import org.example.baba.controller.dto.response.PostSimpleResponseDto;
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
  public ResponseEntity<Void> likePost(@PathVariable Long postId, @PathVariable String type) {
    SNSType snsType = SNSType.valueOf(type.toUpperCase());
    postService.likePost(postId, snsType);
    return ResponseEntity.ok().build();
  }

  @PutMapping("/{postId}/share/{type}")
  public ResponseEntity<Void> sharePost(@PathVariable Long postId, @PathVariable String type) {
    SNSType snsType = SNSType.valueOf(type.toUpperCase());
    postService.sharePost(postId, snsType);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/{postId}")
  public ResponseEntity<PostDetailResponseDto> getPostDetail(@PathVariable Long postId) {
    PostDetailResponseDto post = postService.getPostDetail(postId);
    return ResponseEntity.ok(post);
  }

  @GetMapping
  public List<PostSimpleResponseDto> getPosts(
      @RequestParam(required = false) String hashtag,
      @RequestParam(required = false) SNSType type,
      @RequestParam(required = false) String searchKeyword,
      @RequestParam(defaultValue = "created_at") String orderBy,
      @RequestParam(defaultValue = "title,content") String searchBy,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int pageCount) {

    return postService.getPosts(hashtag, type, searchKeyword, orderBy, searchBy, page, pageCount);
  }
}
