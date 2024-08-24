package org.example.baba.controller;

import static org.example.baba.exception.exceptionType.StatisticsExceptionType.*;

import java.time.LocalDate;
import java.util.Map;

import org.example.baba.common.enums.StatisticsType;
import org.example.baba.common.enums.StatisticsValue;
import org.example.baba.exception.CustomException;
import org.example.baba.service.StatisticsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
@Slf4j
public class StatisticsController {

  private final StatisticsService statisticsService;

  @GetMapping
  public ResponseEntity<Map<String, Long>> getStatistics(
      @RequestParam(required = false) final String hashtag,
      @RequestParam final StatisticsType type,
      @RequestParam(required = false, defaultValue = "COUNT") final StatisticsValue value,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          final LocalDate start,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          final LocalDate end) {

    LocalDate startDate = getOrDefaultStartDate(start);
    LocalDate endDate = getOrDefaultEndDate(end);

    validateDate(startDate, endDate, type);

    Map<String, Long> result =
        statisticsService.getStatistics(hashtag, type, value, startDate, endDate);

    return ResponseEntity.ok(result);
  }

  private LocalDate getOrDefaultStartDate(LocalDate start) {
    return (start != null) ? start : LocalDate.now().minusDays(7);
  }

  private LocalDate getOrDefaultEndDate(LocalDate end) {
    return (end != null) ? end : LocalDate.now();
  }

  private void validateDate(LocalDate start, LocalDate end, StatisticsType type) {
    if (type == StatisticsType.DATE && start.isBefore(end.minusDays(30))) {
      // 30일 이상 조회 불가
      throw new CustomException(INVALID_DATE_RANGE_EXCEPTION);
    }
    // 시간별 조회는 7일 이상 조회 불가
    if (type == StatisticsType.HOUR && start.isBefore(end.minusDays(7))) {
      throw new CustomException(INVALID_HOUR_RANGE_EXCEPTION);
    }
  }
}
