package org.example.baba.controller.dto.response;

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

  public static PostSimpleResponseDto from(Post post) {
    return PostSimpleResponseDto.builder()
        .id(post.getId())
        .memberId(post.getMemberId())
        .type(post.getType())
        .title(post.getTitle())
        .content(post.getContent())
        .build();
  }
}
