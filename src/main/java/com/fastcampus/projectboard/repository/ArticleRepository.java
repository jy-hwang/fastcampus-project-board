package com.fastcampus.projectboard.repository;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.QArticle;
import com.fastcampus.projectboard.domain.projection.ArticleProjection;
import com.fastcampus.projectboard.repository.querydsl.ArticleRepositoryCustom;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(excerptProjection = ArticleProjection.class)
public interface ArticleRepository extends
    //@formatter:off
      JpaRepository<Article, Long>
    , ArticleRepositoryCustom
    , QuerydslPredicateExecutor<Article>
    , QuerydslBinderCustomizer<QArticle>
    //@formatter:on

{

  Page<Article> findByTitleContaining(String title, Pageable pageable);

  Page<Article> findByContentContaining(String content, Pageable pageable);

  Page<Article> findByUserAccount_UserIdContaining(String userId, Pageable pageable);

  Page<Article> findByUserAccount_NicknameContaining(String nickname, Pageable pageable);

  void deleteByIdAndUserAccount_UserId(Long articleId, String userId);

  @Override
  default void customize(QuerydslBindings bindings, QArticle root) {
    bindings.excludeUnlistedProperties(true);
    bindings.including(root.title, root.content, root.hashtags, root.createdBy, root.createdAt);
    bindings.bind(root.title).first(StringExpression::containsIgnoreCase);
    bindings.bind(root.content).first(StringExpression::containsIgnoreCase);
    bindings.bind(root.hashtags.any().hashtagName).first(StringExpression::containsIgnoreCase);
    bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);
    bindings.bind(root.createdAt).first(DateTimeExpression::eq);

  }
}
