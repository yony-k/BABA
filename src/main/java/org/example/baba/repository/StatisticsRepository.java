package org.example.baba.repository;

import java.time.LocalDate;
import java.util.List;

import org.example.baba.domain.Post;
import org.example.baba.domain.QHashTag;
import org.example.baba.domain.QPost;
import org.example.baba.domain.QPostHashTagMap;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class StatisticsRepository {
  private final JPAQueryFactory queryFactory;

  public List<Post> findPosts(String hashtag, LocalDate start, LocalDate end) {
    QPost post = QPost.post;
    QPostHashTagMap postHashTagMap = QPostHashTagMap.postHashTagMap;
    QHashTag hashTag = QHashTag.hashTag;

    BooleanExpression hashtagCondition = hashTag.tagName.eq(hashtag);
    BooleanExpression dateCondition =
        post.createdAt.between(start.atStartOfDay(), end.atTime(23, 59, 59));

    return queryFactory
        .selectDistinct(post)
        .from(post)
        .leftJoin(postHashTagMap)
        .on(post.id.eq(postHashTagMap.post.id))
        .leftJoin(hashTag)
        .on(postHashTagMap.hashtag.id.eq(hashTag.id))
        .where(hashtagCondition.and(dateCondition))
        .fetch();
  }
}
