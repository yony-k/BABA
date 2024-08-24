package org.example.baba.repository;

import java.util.Optional;

import org.example.baba.domain.Post;
import org.example.baba.domain.enums.SNSType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import io.lettuce.core.dynamic.annotation.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

  Optional<Post> findByIdAndType(Long postId, SNSType type);

  @Query(
      value =
          "SELECT p FROM Post p "
              + "JOIN PostHashTagMap map ON map.post.id = p.id "
              + "JOIN HashTag tag ON tag.id = map.hashtag.id "
              + "WHERE (:hashtag IS NULL OR tag.tagName = :hashtag) "
              + "AND (:type IS NULL OR p.type = :type) "
              + "AND ((:searchKeyword is NULL) "
              + "OR (:searchBy = 'title' AND p.title LIKE %:searchKeyword%) "
              + "OR (:searchBy = 'content' AND p.content LIKE %:searchKeyword%)"
              + "OR (:searchBy = 'title,content' AND (p.title LIKE %:searchKeyword% OR p.content LIKE %:searchKeyword%))) ")
  Page<Post> findPosts(
      @Param("hashtag") String hashtag,
      @Param("type") SNSType type,
      @Param("searchBy") String searchBy,
      @Param("searchKeyword") String searchKeyword,
      Pageable pageable);
}
