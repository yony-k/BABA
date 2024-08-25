package org.example.baba.service;

import static org.example.baba.exception.exceptionType.AuthorizedExceptionType.*;
import static org.example.baba.exception.exceptionType.StatisticsExceptionType.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.example.baba.common.enums.StatisticsType;
import org.example.baba.common.enums.StatisticsValue;
import org.example.baba.exception.CustomException;
import org.example.baba.repository.MemberRepository;
import org.example.baba.repository.StatisticsRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.Tuple;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class StatisticsService {
  private final MemberRepository memberRepository;
  private final StatisticsRepository statisticsRepository;

  public Map<String, Long> getStatistics(
      final String hashtag,
      final StatisticsType type,
      final StatisticsValue value,
      final LocalDate start,
      final LocalDate end) {

    Map<String, Long> result;
    String hashTag = getOrDefaultEndDate(hashtag);
    List<Tuple> posts;

    if (type == StatisticsType.DATE) {
      posts = statisticsRepository.findPostsGroupedByDate(hashTag, start, end);
      result = groupByDate(posts, value, start, end);
    } else if (type == StatisticsType.HOUR) {
      LocalDateTime startDateTime = start.atStartOfDay();
      LocalDateTime endDateTime = end.atTime(23, 59);
      posts = statisticsRepository.findPostsGroupedByHour(hashTag, startDateTime, endDateTime);
      result = groupByHour(posts, value, startDateTime, endDateTime);
    } else {
      throw new CustomException(INVALID_TYPE_EXCEPTION);
    }

    return result;
  }

  private String getOrDefaultEndDate(String hashtag) {
    if (hashtag == null) {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      Long memberId = (long) authentication.getPrincipal();
      return memberRepository
          .findById(memberId)
          .orElseThrow(() -> new CustomException(UNAUTHENTICATED))
          .getMemberName();
    }
    return hashtag;
  }

  private Map<String, Long> groupByDate(
      List<Tuple> results, StatisticsValue value, LocalDate start, LocalDate end) {
    Map<String, Long> result = new TreeMap<>();

    LocalDate current = start;
    while (!current.isAfter(end)) {
      result.put(current.toString(), 0L);
      current = current.plusDays(1);
    }

    for (Tuple tuple : results) {
      LocalDate date =
          LocalDate.of(
              tuple.get(0, Integer.class),
              tuple.get(1, Integer.class),
              tuple.get(2, Integer.class));
      String dateStr = date.toString();

      long sumValue =
          getValue(
              tuple.get(3, Integer.class),
              tuple.get(4, Integer.class),
              tuple.get(5, Integer.class),
              tuple.get(6, Long.class),
              value);
      result.put(dateStr, sumValue);
    }

    return result;
  }

  private Map<String, Long> groupByHour(
      List<Tuple> results, StatisticsValue value, LocalDateTime start, LocalDateTime end) {
    Map<String, Long> result = new TreeMap<>();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:00");

    LocalDateTime current = start;
    while (!current.isAfter(end)) {
      String formattedDateTime = current.format(formatter);
      result.put(formattedDateTime, 0L);
      current = current.plusHours(1);
    }

    for (Tuple tuple : results) {
      String dateTimeStr =
          String.format(
              "%d-%02d-%02dT%02d:00",
              tuple.get(0, Integer.class),
              tuple.get(1, Integer.class),
              tuple.get(2, Integer.class),
              tuple.get(3, Integer.class));

      long sumValue =
          getValue(
              tuple.get(4, Integer.class),
              tuple.get(5, Integer.class),
              tuple.get(6, Integer.class),
              tuple.get(7, Long.class),
              value);
      result.put(dateTimeStr, sumValue);
    }

    return result;
  }

  private long getValue(
      int viewCount, int likeCount, int shareCount, long postCount, StatisticsValue value) {
    switch (value) {
      case VIEW_COUNT:
        return viewCount;
      case LIKE_COUNT:
        return likeCount;
      case SHARE_COUNT:
        return shareCount;
      case COUNT:
        return postCount;
      default:
        return 0;
    }
  }
}
