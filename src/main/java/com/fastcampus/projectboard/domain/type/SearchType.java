package com.fastcampus.projectboard.domain.type;

import lombok.Getter;

public enum SearchType {
  //@formatter:off
    TITLE("제목")
  , CONTENT("본문")
  , ID("유저 ID")
  , NICKNAME("닉네임")
  , HASHTAG("해시태그");
  //@formatter:on

  @Getter private final String description;

  SearchType(String description) {
    this.description = description;
  }


}
