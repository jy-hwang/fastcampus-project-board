package com.fastcampus.projectboard.dto;

public record ArticleUpdateDto(
    //@formatter:off
      String title
    , String content
    , String hashtag
    //@formatter:on
    ) {

  public static ArticleUpdateDto of(
      //@formatter:off
        String title
      , String content
      , String hashtag
  //@formatter:on
  ){
    return new ArticleUpdateDto(title,content,hashtag);
  }

}
