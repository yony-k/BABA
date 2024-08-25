package org.example.baba.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.example.baba.domain.QPost;
import org.springframework.stereotype.Repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class StatisticsRepository {

  private final JPAQueryFactory queryFactory;

  // 날짜별로 그룹핑하여 데이터 조회
  public List<Tuple> findPostsGroupedByDate(String hashtag, LocalDate start, LocalDate end) {
    QPost post = QPost.post;

    return queryFactory
        .select(
            post.createdAt.year(),
            post.createdAt.month(),
            post.createdAt.dayOfMonth(),
            post.viewCount.sum(),
            post.likeCount.sum(),
            post.shareCount.sum(),
            post.id.count())
        .from(post)
        .where(
            post.createdAt.between(start.atStartOfDay(), end.atTime(23, 59)),
            post.postHashTags.any().hashtag.tagName.eq(hashtag))
        .groupBy(post.createdAt.year(), post.createdAt.month(), post.createdAt.dayOfMonth())
        .fetch();
  }

  // 시간별로 그룹핑하여 데이터 조회
  public List<Tuple> findPostsGroupedByHour(
      String hashtag, LocalDateTime start, LocalDateTime end) {
    QPost post = QPost.post;

    return queryFactory
        .select(
            post.createdAt.year(),
            post.createdAt.month(),
            post.createdAt.dayOfMonth(),
            post.createdAt.hour(),
            post.viewCount.sum(),
            post.likeCount.sum(),
            post.shareCount.sum(),
            post.id.count())
        .from(post)
        .where(
            post.createdAt.between(start, end), post.postHashTags.any().hashtag.tagName.eq(hashtag))
        .groupBy(
            post.createdAt.year(),
            post.createdAt.month(),
            post.createdAt.dayOfMonth(),
            post.createdAt.hour())
        .fetch();
  }
}
