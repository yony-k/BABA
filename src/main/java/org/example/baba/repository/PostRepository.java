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
      "SELECT p FROM Post p JOIN PostHashTagMap pht ON p.id = pht.post.id "
          + "JOIN HashTag ht ON pht.hashtag.id = ht.id WHERE "
          + "(:hashtag IS NULL OR ht.tagName = :hashtag) AND "
          + "(:type IS NULL OR p.type = :type) AND "
          + "(:searchKeyword IS NULL OR "
          + "p.title LIKE %:searchKeyword% OR p.content LIKE %:searchKeyword%)")
  Page<Post> findPosts(
      @Param("hashtag") String hashtag,
      @Param("type") SNSType type,
      @Param("searchKeyword") String searchKeyword,
      Pageable pageable);
}
