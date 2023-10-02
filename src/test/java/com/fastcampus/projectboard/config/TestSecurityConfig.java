package com.fastcampus.projectboard.config;


import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.fastcampus.projectboard.dto.UserAccountDto;
import com.fastcampus.projectboard.service.UserAccountService;
import java.util.Optional;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

@Import(SecurityConfig.class)
public class TestSecurityConfig {

  @MockBean private UserAccountService userAccountService;

  @BeforeTestMethod
  public void securitySetUp() {
    given(userAccountService.searchUser(anyString()))
        .willReturn(Optional.of(createUserAccountDto()));
    given(userAccountService.saveUser(anyString(), anyString(), anyString(), anyString(), anyString()))
        .willReturn(createUserAccountDto());
  }

  private UserAccountDto createUserAccountDto() {
    return UserAccountDto.of(
        "jackieTest",
        "pw",
        "jackie-test@email.com",
        "jackie-test",
        "test-memo"
    );
  }

}
