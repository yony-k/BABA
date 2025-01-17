package org.example.baba.controller.dto.response;

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
public class PostSimpleResponseDto {

  private Long id;
  private Long memberId;
  private SNSType type;
  private String title;
  private String content;
  private int viewCount;
  private int likeCount;
  private int shareCount;
  private List<String> hashTags;

  public static PostSimpleResponseDto from(Post post, List<String> hashTags) {
    return PostSimpleResponseDto.builder()
        .id(post.getId())
        .memberId(post.getMemberId())
        .type(post.getType())
        .title(post.getTitle())
        .content(post.getContent())
        .viewCount(post.getViewCount())
        .likeCount(post.getLikeCount())
        .shareCount(post.getShareCount())
        .hashTags(hashTags)
        .build();
  }
}
