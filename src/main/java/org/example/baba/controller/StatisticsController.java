package org.example.baba.controller;

import java.time.LocalDate;
import java.util.Map;

import org.example.baba.common.enums.StatisticsType;
import org.example.baba.common.enums.StatisticsValue;
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
  public ResponseEntity<Map<String, Integer>> getStatistics(
      @RequestParam(required = false) final String hashtag,
      @RequestParam final StatisticsType type,
      @RequestParam(required = false, defaultValue = "COUNT") final StatisticsValue value,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          final LocalDate start,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          final LocalDate end) {

    LocalDate startDate = getOrDefaultStartDate(start);
    LocalDate endDate = getOrDefaultEndDate(end);

    Map<String, Integer> result =
        statisticsService.getStatistics(hashtag, type, value, startDate, endDate);

    return ResponseEntity.ok(result);
  }

  private LocalDate getOrDefaultStartDate(LocalDate start) {
    return (start != null) ? start : LocalDate.now().minusDays(7);
  }

  private LocalDate getOrDefaultEndDate(LocalDate end) {
    return (end != null) ? end : LocalDate.now();
  }
}
