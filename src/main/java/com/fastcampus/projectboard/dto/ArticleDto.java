package com.fastcampus.projectboard.dto;

import java.time.LocalDateTime;

public record ArticleDto(
    //@formatter:off
      LocalDateTime createdAt
    , String createdBy
    , String title
    , String content
    , String hashtag
    //@formatter:on
    ) {

  public static ArticleDto of(
      //@formatter:off
      LocalDateTime createdAt
      , String createdBy
      , String title
      , String content
      , String hashtag
  //@formatter:on
  ){
    return new ArticleDto(createdAt,createdBy,title,content,hashtag);
  }

}
