package org.example.baba.service;

import org.example.baba.controller.dto.response.PostDetailResponseDto;
import org.example.baba.domain.Post;
import org.example.baba.domain.enums.SNSType;
import org.example.baba.exception.CustomException;
import org.example.baba.exception.exceptionType.PostExceptionType;
import org.example.baba.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

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
    post.like();
  }

  @Transactional
  public void sharePost(Long postId, SNSType type) {
    Post post =
        postRepository
            .findByIdAndType(postId, type)
            .orElseThrow(() -> new CustomException(PostExceptionType.NOT_FOUND_POST));
    post.share();
  }

  @Transactional
  public PostDetailResponseDto getPostDetail(Long postId) {
    Post post =
        postRepository
            .findById(postId)
            .orElseThrow(() -> new CustomException(PostExceptionType.NOT_FOUND_POST));

    post.view();
    postRepository.save(post);
    return PostDetailResponseDto.from(post);
  }
}
