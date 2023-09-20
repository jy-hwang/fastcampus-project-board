package com.fastcampus.projectboard.config;

import com.fastcampus.projectboard.dto.UserAccountDto;
import com.fastcampus.projectboard.dto.security.BoardPrincipal;
import com.fastcampus.projectboard.repository.UserAccountRepository;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    //@formatter:off
    return  http
        .authorizeHttpRequests(
                 auth -> auth
                     .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                     .mvcMatchers(
                         HttpMethod.GET,
                         "/",
                         "/articles",
                         "/articles/search-hashtag"
                     ).permitAll()
                     .anyRequest().authenticated()
        )
        .formLogin().and()
        .logout()
          .logoutSuccessUrl("/")
          .and()
        .build();
    //@formatter:on

  }

  @Bean
  public UserDetailsService userDetailsService(UserAccountRepository userAccountRepository) {
    return username -> userAccountRepository
        .findById(username)
        .map(UserAccountDto::from)
        .map(BoardPrincipal::from)
        .orElseThrow(
            () -> new UsernameNotFoundException("유저를 찾을 수 없습니다. - username : " + username));
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }


/*
spring boot 2.7 이상에서는 권장되지 않는 방식임.
  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    // static resource, css - js

    return (web) -> web.ignoring()
        .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
  }
*/

}
