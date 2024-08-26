package org.example.baba.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.example.baba.controller.dto.response.PostDetailResponseDto;
import org.example.baba.controller.dto.response.PostSimpleResponseDto;
import org.example.baba.domain.Post;
import org.example.baba.domain.enums.SNSType;
import org.example.baba.exception.CustomException;
import org.example.baba.exception.exceptionType.PostExceptionType;
import org.example.baba.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

  private final PostRepository postRepository;

  @Transactional
  public void likePost(Long postId, SNSType type) {
    Post post =
        postRepository
            .findByIdAndType(postId, type)
            .orElseThrow(() -> new CustomException(PostExceptionType.NOT_FOUND_POST));
    String uri = getUriForSNSType(postId, type, "likes");

    callApiProcess(HttpMethod.POST, uri, post::like);
  }

  @Transactional
  public void sharePost(Long postId, SNSType type) {
    Post post =
        postRepository
            .findByIdAndType(postId, type)
            .orElseThrow(() -> new CustomException(PostExceptionType.NOT_FOUND_POST));
    String uri = getUriForSNSType(postId, type, "likes");

    callApiProcess(HttpMethod.POST, uri, post::share);
  }

  private String getUriForSNSType(Long postId, SNSType type, String action) {
    // SNS 타입에 따라 URI 매핑
    Map<SNSType, String> uriMap = new HashMap<>();
    uriMap.put(SNSType.FACEBOOK, "https://www.facebook.com/" + action + "/" + postId);
    uriMap.put(SNSType.TWITTER, "https://www.twitter.com/" + action + "/" + postId);
    uriMap.put(SNSType.INSTAGRAM, "https://www.instagram.com/" + action + "/" + postId);
    uriMap.put(SNSType.THREADS, "https://www.threads.com/" + action + "/" + postId);

    // 위의 SNS 가 아닐 경우, 예외 발생
    String uri = uriMap.get(type);
    if (uri == null) {
      throw new CustomException(PostExceptionType.UNSUPPORTED_SNS);
    }

    return uri;
  }

  private void callApiProcess(HttpMethod method, String uri, Runnable onSuccess) {
    // API 를 실제로 호출을 하지 않고, 항상 성공으로 처리
    log.info("callApiProcess - URI : " + uri);

    try {
      onSuccess.run();
      log.info("callApiProcess -  success");
    } catch (Exception e) {
      log.error("callApiProcess -  failure : ", e);
      throw new CustomException(PostExceptionType.API_CALL_FAILED);
    }
  }

  @Transactional
  public PostDetailResponseDto getPostDetail(Long postId) {
    log.info("postId: " + postId);
    Post post =
        postRepository
            .findById(postId)
            .orElseThrow(() -> new CustomException(PostExceptionType.NOT_FOUND_POST));

    List<String> hashtags =
        post.getPostHashTags().stream()
            .map(postHashTagMap -> postHashTagMap.getHashtag().getTagName())
            .collect(Collectors.toList());

    post.view();
    return PostDetailResponseDto.from(post, hashtags);
  }

  @Transactional(readOnly = true)
  public Page<PostSimpleResponseDto> getPosts(
      String hashtag,
      SNSType type,
      String searchBy,
      String searchKeyword,
      String orderBy,
      String orderDirection,
      int page,
      int size) {

    // 정렬
    Sort.Direction direction = Sort.Direction.fromString(orderDirection);
    Sort sort = Sort.by(direction, orderBy);

    // 페이징 (page 는 0부터 시작)
    Pageable pageable = PageRequest.of(page, size, sort);

    Page<Post> posts = postRepository.findPosts(hashtag, type, searchBy, searchKeyword, pageable);

    // Post 를 PostSimpleResponseDto 로 변환
    return posts.map(
        post -> {
          // 해시태그 찾기
          List<String> hashTags =
              post.getPostHashTags().stream()
                  .map(postHashTagMap -> postHashTagMap.getHashtag().getTagName())
                  .collect(Collectors.toList());

          return PostSimpleResponseDto.builder()
              .id(post.getId())
              .memberId(post.getMemberId())
              .type(post.getType())
              .title(post.getTitle())
              .content(post.getContent())
              .hashTags(hashTags) // 해시태그 포함
              .build();
        });
  }
}
