package com.fastcampus.projectboard.domain.projection;

import com.fastcampus.projectboard.domain.UserAccount;
import java.time.LocalDateTime;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "withoutPassword", types = UserAccount.class)
public interface UserAccountProjection {


  String getUserId();

  String getEmail();

  String getNickname();

  String getMemo();

  LocalDateTime getCreatedAt();

  String getCreatedBy();

  LocalDateTime getModifiedAt();

  String getModifiedBy();


}
