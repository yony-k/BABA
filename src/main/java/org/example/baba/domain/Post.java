package org.example.baba.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

import org.example.baba.common.entity.BaseTimeEntity;
import org.example.baba.domain.enums.SNSType;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long memberId; // ID 매핑

  @Enumerated(value = EnumType.STRING)
  private SNSType type;

  private String title;

  @Column(columnDefinition = "TEXT")
  private String content;

  private int viewCount;

  private int likeCount;

  private int shareCount;

  @Builder
  public Post(
      SNSType type, String title, String content, int viewCount, int likeCount, int shareCount) {
    this.type = type;
    this.title = title;
    this.content = content;
    this.viewCount = viewCount;
    this.likeCount = likeCount;
    this.shareCount = shareCount;
  }

  public void like() {
    this.likeCount++;
  }

  public void share() {
    this.shareCount++;
  }

  public void view() {
    this.viewCount++;
  }

  @OneToMany(mappedBy = "post", orphanRemoval = true)
  private List<PostHashTagMap> postHashTags = new ArrayList<>();
}
