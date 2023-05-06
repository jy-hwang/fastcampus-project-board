package com.fastcampus.projectboard.config;

import java.util.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class JpaConfig {

  @Bean
  public AuditorAware<String> auditorAware() {

    return () -> Optional.of("admin"); //  TODO : 스프링 시큐리티로 인증 기능을 붙이게 될때, 수정하자

  }

}
