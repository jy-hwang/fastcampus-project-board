package com.fastcampus.projectboard.dto;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.UserAccount;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public record ArticleDto(
    //@formatter:off
      Long id
    , UserAccountDto userAccountDto
    , String title
    , String content
    , Set<HashtagDto> hashtagDtos
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
      , Set<HashtagDto> hashtagDtos
      //@formatter:off
  ) {
    return new ArticleDto(
        //@formatter:off
          null
        , userAccountDto
        , title
        , content
        , hashtagDtos
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
      , Set<HashtagDto> hashtagDtos
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
        , hashtagDtos
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
        , entity.getHashtags().stream().map(HashtagDto::from).collect(Collectors.toUnmodifiableSet())
        , entity.getCreatedAt()
        , entity.getCreatedBy()
        , entity.getModifiedAt()
        , entity.getModifiedBy()
        //@formatter:on
    );
  }

  public Article toEntity(UserAccount userAccount) {
    return Article.of(userAccount, title, content);
  }

}
