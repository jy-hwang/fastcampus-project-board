package com.fastcampus.projectboard.dto;

import com.fastcampus.projectboard.domain.UserAccount;
import java.time.LocalDateTime;

public record UserAccountDto(
    //@formatter:off
      String userId
    , String userPassword
    , String email
    , String nickname
    , String  memo
    , LocalDateTime createdAt
    , String createdBy
    , LocalDateTime modifiedAt
    , String modifiedBy
    //@formatter:on
) {

  public static UserAccountDto of(String userId, String userPassword, String email
      , String nickname, String memo, LocalDateTime createdAt, String createdBy
      , LocalDateTime modifiedAt, String modifiedBy) {
    return new UserAccountDto(userId, userPassword, email, nickname, memo, createdAt, createdBy,
        modifiedAt, modifiedBy);
  }

  public static UserAccountDto of(String userId, String userPassword, String email
      , String nickname, String memo) {
    return new UserAccountDto(userId, userPassword, email, nickname, memo, null, null, null, null);
  }

  public static UserAccountDto from(UserAccount entity) {
    return new UserAccountDto(
        //@formatter:off
          entity.getUserId()
        , entity.getUserPassword()
        , entity.getEmail()
        , entity.getNickname()
        , entity.getMemo()
        , entity.getCreatedAt()
        , entity.getCreatedBy()
        , entity.getModifiedAt()
        , entity.getModifiedBy()
        //@formatter:on
    );
  }

  public UserAccount toEntity() {
    return UserAccount.of(userId, userPassword, email, nickname, memo);
  }
}
