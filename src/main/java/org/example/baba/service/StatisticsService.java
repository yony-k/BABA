package org.example.baba.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.baba.common.enums.StatisticsType;
import org.example.baba.common.enums.StatisticsValue;
import org.example.baba.domain.Post;
import org.example.baba.repository.StatisticsRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class StatisticsService {

  private final StatisticsRepository statisticsRepository;

  public Map<String, Integer> getStatistics(
      final String hashtag,
      final StatisticsType type,
      final StatisticsValue value,
      final LocalDate start,
      final LocalDate end) {
    Map<String, Integer> result;
    String hashTag = getOrDefaultEndDate(hashtag);

    List<Post> posts = statisticsRepository.findPosts(hashTag, start, end);

    switch (type) {
      case DATE:
        result = groupByDate(posts, start, end, value);
        break;
      case HOUR:
        result = groupByHour(posts, start, end, value);
        break;
      default:
        throw new IllegalArgumentException("Invalid type parameter");
    }

    return result;
  }

  private String getOrDefaultEndDate(String hashtag) {
    if (hashtag == null) {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      return authentication.getName();
    }
    return hashtag;
  }

  private Map<String, Integer> groupByDate(
      List<Post> posts, LocalDate start, LocalDate end, StatisticsValue value) {
    Map<String, Integer> result = new HashMap<>();

    for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
      LocalDate finalDate = date;
      int sum =
          posts.stream()
              .filter(post -> post.getCreatedAt().toLocalDate().isEqual(finalDate))
              .mapToInt(post -> getValue(post, value))
              .sum();
      result.put(date.toString(), sum);
    }

    return result;
  }

  private Map<String, Integer> groupByHour(
      List<Post> posts, LocalDate start, LocalDate end, StatisticsValue value) {
    Map<String, Integer> result = new HashMap<>();

    for (LocalDateTime dateTime = start.atStartOfDay();
        !dateTime.isAfter(end.atTime(23, 59));
        dateTime = dateTime.plusHours(1)) {
      LocalDateTime finalDateTime = dateTime;
      int sum =
          posts.stream()
              .filter(
                  post -> {
                    LocalDateTime createdAt = post.getCreatedAt();
                    return createdAt.isAfter(finalDateTime)
                        && createdAt.isBefore(finalDateTime.plusHours(1));
                  })
              .mapToInt(post -> getValue(post, value))
              .sum();
      result.put(dateTime.toString(), sum);
    }

    return result;
  }

  private int getValue(Post post, StatisticsValue value) {
    switch (value) {
      case VIEW_COUNT:
        return post.getViewCount();
      case LIKE_COUNT:
        return post.getLikeCount();
      case SHARE_COUNT:
        return post.getShareCount();
      default:
        return 1;
    }
  }
}
