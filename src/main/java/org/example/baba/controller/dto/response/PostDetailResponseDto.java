package org.example.baba.controller.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import org.example.baba.domain.Post;
import org.example.baba.domain.enums.SNSType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class PostDetailResponseDto {

  private Long id;
  private Long memberId;
  private SNSType type;
  private String title;
  private String content;
  private List<String> hashtags;
  private int likeCount;
  private int shareCount;
  private int viewCount;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public static PostDetailResponseDto from(Post post, List<String> postHashtags) {
    return PostDetailResponseDto.builder()
        .id(post.getId())
        .memberId(post.getMemberId())
        .type(post.getType())
        .title(post.getTitle())
        .content(post.getContent())
        .hashtags(postHashtags)
        .likeCount(post.getLikeCount())
        .shareCount(post.getShareCount())
        .viewCount(post.getViewCount())
        .createdAt(post.getCreatedAt())
        .updatedAt(post.getUpdatedAt())
        .build();
  }
}
