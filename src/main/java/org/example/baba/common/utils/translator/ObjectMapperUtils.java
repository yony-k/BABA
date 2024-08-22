package org.example.baba.common.utils.translator;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;

import org.example.baba.exception.CustomException;
import org.example.baba.exception.exceptionType.CommonExceptionType;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ObjectMapperUtils {

  private final ObjectMapper objectMapper;

  public <T> T toEntity(HttpServletRequest request, Class<T> valueType) {
    try {
      return objectMapper.readValue(request.getInputStream(), valueType);
    } catch (IOException e) {
      throw new CustomException(CommonExceptionType.TRANS_ENTITY_ERROR);
    }
  }

  public <T> T toEntity(String content, Class<T> valueType) {
    try {
      return objectMapper.readValue(content, valueType);
    } catch (IOException e) {
      throw new CustomException(CommonExceptionType.TRANS_ENTITY_ERROR);
    }
  }

  public String toStringValue(Object value) {
    try {
      return objectMapper.writeValueAsString(value);
    } catch (JsonProcessingException e) {
      throw new CustomException(CommonExceptionType.TRANS_JSON_ERROR);
    }
  }
}
