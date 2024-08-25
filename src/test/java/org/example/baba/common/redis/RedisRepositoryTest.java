package org.example.baba.common.redis;

import static org.assertj.core.api.Assertions.*;

import org.example.baba.common.security.dto.MemberInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@MockBean(JpaMetamodelMappingContext.class)
@ExtendWith(SpringExtension.class)
@DataRedisTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Import({RedisRepository.class, ObjectMapper.class})
class RedisRepositoryTest {

  @Autowired RedisRepository repository;

  @Autowired ObjectMapper objectMapper;

  @Test
  @DisplayName("레디스 저장 테스트")
  void save_test() throws JsonProcessingException {
    // given
    String key = "Test";
    MemberInfo data = new MemberInfo("test", "TEST");
    String value = objectMapper.writeValueAsString(data);
    repository.save(key, value, 10);
    // when
    MemberInfo result = objectMapper.readValue(repository.findByKey(key), MemberInfo.class);
    // then
    assertThat(result).isInstanceOf(MemberInfo.class);
    assertThat(result.getEmail()).isEqualTo(data.getEmail());
  }

  @Test
  @DisplayName("레디스 삭제 테스트")
  void delete_test() {
    // given
    String key = "Test001";
    String value = "Test";
    // when
    repository.save(key, value, 10);
    repository.delete(key);
    Object result = repository.findByKey(key);
    // then
    assertThat(result).isNull();
  }

  @Test
  @DisplayName("레디스 동일한 key값에 value 연속 저장")
  void save_and_save_test() {
    // given
    String key = "Test001";
    String value = "Test";
    // when
    repository.save(key, value, 10);
    value = "answerTest";
    repository.save(key, value, 10);
    Object result = repository.findByKey(key);
    // then
    assertThat(result).isEqualTo(value);
  }
}
