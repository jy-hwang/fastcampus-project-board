package com.fastcampus.projectboard.dto;

import java.time.LocalDateTime;

public record ArticleCommentDto(
    //@formatter:off
      LocalDateTime createdAt
    , String createdBy
    , LocalDateTime modifiedAt
    , String modifiedBy
    , String content
    //@formatter:on
    ) {

  public static ArticleCommentDto of(
      //@formatter:off
      LocalDateTime createdAt
      , String createdBy
      , LocalDateTime modifiedAt
      , String modifiedBy
      , String content
      //@formatter:on
  ) {
    return new ArticleCommentDto(createdAt,createdBy,modifiedAt,modifiedBy,content);
  }

}
