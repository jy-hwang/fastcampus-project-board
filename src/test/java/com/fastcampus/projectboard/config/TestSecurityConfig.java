package com.fastcampus.projectboard.config;


import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.fastcampus.projectboard.domain.UserAccount;
import com.fastcampus.projectboard.repository.UserAccountRepository;
import java.util.Optional;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

@Import(SecurityConfig.class)
public class TestSecurityConfig {

  @MockBean private UserAccountRepository userAccountRepository;

  @BeforeTestMethod
  public void securitySetUp() {
    given(userAccountRepository.findById(anyString()))
        .willReturn(Optional.of(UserAccount.of(
            "jackieTest",
            "pw",
            "jackie-test@abc.com",
            "jackie-test",
            "test memo" )));  }

}
