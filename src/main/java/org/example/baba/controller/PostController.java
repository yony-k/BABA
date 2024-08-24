package org.example.baba.controller;

import org.example.baba.controller.dto.response.PostDetailResponseDto;
import org.example.baba.controller.dto.response.PostSimpleResponseDto;
import org.example.baba.domain.enums.SNSType;
import org.example.baba.service.PostService;
import org.springframework.data.domain.Page;
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
  public ResponseEntity<Page<PostSimpleResponseDto>> getPosts(
      @RequestParam(required = false) String hashtag,
      @RequestParam(required = false) SNSType type,
      @RequestParam(defaultValue = "title,content") String searchBy,
      @RequestParam(required = false) String searchKeyword,
      @RequestParam(defaultValue = "createdAt") String orderBy,
      @RequestParam(defaultValue = "asc") String orderDirection,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "5") int size) {
    log.info(
        "getPosts - hashtag: {}, type: {}, searchBy: {}, searchKeyword: {}, orderBy: {}, orderDirection: {}, page: {}, size: {}",
        hashtag,
        type,
        searchBy,
        searchKeyword,
        orderBy,
        orderDirection,
        page,
        size);

    Page<PostSimpleResponseDto> posts =
        postService.getPosts(
            hashtag, type, searchBy, searchKeyword, orderBy, orderDirection, page, size);

    return ResponseEntity.ok(posts);
  }
}
