package com.fastcampus.projectboard.dto;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.ArticleComment;
import com.fastcampus.projectboard.domain.UserAccount;
import java.time.LocalDateTime;

public record ArticleCommentDto(
    //@formatter:off
      Long id
    , Long articleId
    , UserAccountDto userAccountDto
    , String content
    , LocalDateTime createdAt
    , String createdBy
    , LocalDateTime modifiedAt
    , String modifiedBy
    //@formatter:on
) {

  public static ArticleCommentDto of(
      //@formatter:off
      Long articleId
      , UserAccountDto userAccountDto
      , String content
      //@formatter:on
  ) {
    return new ArticleCommentDto(
        //@formatter:off
        null
        , articleId
        , userAccountDto
        , content
        ,null
        ,null
        ,null
        ,null
        //@formatter:on
    );
  }

  public static ArticleCommentDto of(
      //@formatter:off
        Long id
      , Long articleId
      , UserAccountDto userAccountDto
      , String content
      , LocalDateTime createdAt
      , String createdBy
      , LocalDateTime modifiedAt
      , String modifiedBy
      //@formatter:on
  ) {
    return new ArticleCommentDto(
        //@formatter:off
          id
        , articleId
        , userAccountDto
        , content
        , createdAt
        , createdBy
        , modifiedAt
        , modifiedBy);
        //@formatter:on
  }

  public static ArticleCommentDto from(ArticleComment entity) {
    return new ArticleCommentDto(
        //@formatter:off
          entity.getId()
        , entity.getArticle().getId()
        , UserAccountDto.from(entity.getUserAccount())
        , entity.getContent()
        , entity.getCreatedAt()
        , entity.getCreatedBy()
        , entity.getModifiedAt()
        , entity.getModifiedBy()
        //@formatter:on
    );
  }

  public ArticleComment toEntity(Article article, UserAccount userAccount) {
    return ArticleComment.of(article, userAccount, content);
  }


}
