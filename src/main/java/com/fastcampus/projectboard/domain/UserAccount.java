package com.fastcampus.projectboard.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@ToString(callSuper = true)
@Table(indexes = {
    //@formatter:off
      @Index(columnList= "email", unique=true)
    , @Index(columnList= "createdAt")
    , @Index(columnList= "createdBy")
    //@formatter:on
})
@Entity
public class UserAccount extends AuditingFields {

  @Id
  @Column(length = 50)
  private String userId;

  @Setter @Column(nullable = false) private String userPassword;

  @Setter @Column(length = 100) private String email;
  @Setter @Column(length = 100) private String nickname;
  @Setter private String memo;

  protected UserAccount() {
  }

  private UserAccount(
      String userId,
      String userPassword,
      String email,
      String nickname,
      String memo,
      String createdBy) {
    this.userId = userId;
    this.userPassword = userPassword;
    this.email = email;
    this.nickname = nickname;
    this.memo = memo;
    this.createdBy = createdBy;
    this.modifiedBy = createdBy;
  }

  public static UserAccount of(
      String userId,
      String userPassword,
      String email,
      String nickname,
      String memo) {
    return new UserAccount(
        userId,
        userPassword,
        email,
        nickname,
        memo,
        null);
  }
  public static UserAccount of(
      String userId,
      String userPassword,
      String email,
      String nickname,
      String memo,
      String createBy) {
    return new UserAccount(
        userId,
        userPassword,
        email,
        nickname,
        memo,
        createBy);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof UserAccount that)) {
      return false;
    }
    return this.getUserId() != null && this.getUserId().equals(that.getUserId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.getUserId());
  }

}
