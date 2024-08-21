package org.example.baba.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.baba.domain.enums.SNSType;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId; // ID 매핑

    @Enumerated(value = EnumType.STRING)
    private SNSType type;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private int viewCount;

    private int likeCount;

    private int shareCount;

}
