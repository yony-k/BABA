package org.example.baba.repository;

import java.util.Optional;

import org.example.baba.domain.Post;
import org.example.baba.domain.enums.SNSType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

  Optional<Post> findByIdAndType(Long postId, SNSType type);

  Optional<Post> findById(Long postId);
}
