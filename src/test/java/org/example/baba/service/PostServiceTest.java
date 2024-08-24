package org.example.baba.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Optional;

import org.example.baba.controller.dto.response.PostDetailResponseDto;
import org.example.baba.domain.HashTag;
import org.example.baba.domain.Post;
import org.example.baba.domain.PostHashTagMap;
import org.example.baba.exception.CustomException;
import org.example.baba.exception.exceptionType.PostExceptionType;
import org.example.baba.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

  @Mock private PostRepository postRepository;

  @InjectMocks private PostService postService;

  @Test
  @DisplayName("게시글 상세정보 확인")
  void getPostDetail() {

    // given
    Long postId = 1L;
    Post post = mock(Post.class); // 임의 생성
    HashTag hashTag1 = new HashTag(1L, "개발");
    HashTag hashTag2 = new HashTag(1L, "공부");

    PostHashTagMap postHashTagMap1 = new PostHashTagMap(1L, post, hashTag1);
    PostHashTagMap postHashTagMap2 = new PostHashTagMap(1L, post, hashTag2);

    when(post.getId()).thenReturn(postId); // mock 객체는 기본값인 `0`을 반환하기 때문에 설정
    when(post.getPostHashTags()).thenReturn(Arrays.asList(postHashTagMap1, postHashTagMap2));
    when(postRepository.findById(postId)).thenReturn(Optional.of(post));

    // 기존 조회수 & 좋아요 & 공유수 설정
    doReturn(10).when(post).getViewCount(); // mock spy - 기댓값 설정
    when(post.getLikeCount()).thenReturn(5);
    when(post.getShareCount()).thenReturn(10);

    // when
    PostDetailResponseDto result = postService.getPostDetail(postId);

    System.out.println("예상 postId: " + postId);
    System.out.println("실제 postId: " + result.getId());
    System.out.println("View_Count: " + result.getViewCount());
    System.out.println("Like_Count: " + result.getLikeCount());
    System.out.println("Share_Count: " + result.getShareCount());

    // then
    assertNotNull(result);
    assertEquals(postId, result.getId());
    assertEquals(2, result.getHashtags().size());
    assertTrue(result.getHashtags().contains("개발"));
    assertTrue(result.getHashtags().contains("공부"));
    assertEquals(5, result.getLikeCount());
    assertEquals(10, result.getShareCount());
    assertEquals(10, result.getViewCount());

    verify(post).view(); // 조회수 증가 메서드 1회 호출 확인
    // verify(post, times(2)).view(); 메서드 2회 호출 감지 시 (X)
  }

  @Test
  @DisplayName("게시글이 존재하지 않을 때 예외 처리")
  void getPostNotFound() {

    // given
    Long postId = 1L;
    when(postRepository.findById(postId)).thenReturn(Optional.empty());

    // when & then
    CustomException thrown =
        assertThrows(
            CustomException.class,
            () -> {
              postService.getPostDetail(postId);
            });

    // 설정해둔 예외 값과 일치하는 지 확인
    assertEquals(PostExceptionType.NOT_FOUND_POST, thrown.getExceptionType());
  }
}
