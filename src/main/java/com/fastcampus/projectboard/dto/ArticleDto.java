package com.fastcampus.projectboard.dto;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.UserAccount;
import java.time.LocalDateTime;

public record ArticleDto(
    //@formatter:off
      Long id
    , UserAccountDto userAccountDto
    , String title
    , String content
    , String hashtag
    , LocalDateTime createdAt
    , String createdBy
    , LocalDateTime modifiedAt
    , String modifiedBy
    //@formatter:on
) {

  public static ArticleDto of(
      //@formatter:off
        UserAccountDto userAccountDto
      , String title
      , String content
      , String hashtag
      //@formatter:off
  ) {
    return new ArticleDto(
        //@formatter:off
          null
        , userAccountDto
        , title
        , content
        , hashtag
        , null
        , null
        , null
        , null
        //@formatter:on
    );
  }

  public static ArticleDto of(
      //@formatter:off
        Long id
      , UserAccountDto userAccountDto
      , String title
      , String content
      , String hashtag
      , LocalDateTime createdAt
      , String createdBy
      , LocalDateTime modifiedAt
      , String modifiedBy
      //@formatter:off
  ) {
    return new ArticleDto(
        //@formatter:off
        id
        , userAccountDto
        , title
        , content
        , hashtag
        , createdAt
        , createdBy
        , modifiedAt
        , modifiedBy
        //@formatter:on
    );
  }

  public static ArticleDto from(Article entity) {
    return new ArticleDto(
        //@formatter:off
        entity.getId()
        , UserAccountDto.from(entity.getUserAccount())
        , entity.getTitle()
        , entity.getContent()
        , entity.getHashtag()
        , entity.getCreatedAt()
        , entity.getCreatedBy()
        , entity.getModifiedAt()
        , entity.getModifiedBy()
        //@formatter:on
    );
  }

  public Article toEntity(UserAccount userAccount) {
    return Article.of(userAccount, title, content, hashtag);
  }

}
