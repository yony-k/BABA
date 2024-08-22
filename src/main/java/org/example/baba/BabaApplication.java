package org.example.baba;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class BabaApplication {

  public static void main(String[] args) {
    SpringApplication.run(BabaApplication.class, args);
  }
}
