package com.fastcampus.projectboard.repository;

import com.fastcampus.projectboard.domain.ArticleComment;
import com.fastcampus.projectboard.domain.QArticleComment;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ArticleCommentRepository extends
    //@formatter:off
      JpaRepository<ArticleComment, Long>
    , QuerydslPredicateExecutor<ArticleComment>
    , QuerydslBinderCustomizer<QArticleComment>
    //@formatter:on

{

  List<ArticleComment> findByArticle_Id(Long articleId);

  void deleteByIdAndUserAccount_UserId(Long articleCommentId, String userId);

  @Override
  default void customize(QuerydslBindings bindings, QArticleComment root) {
    bindings.excludeUnlistedProperties(true);
    bindings.including(root.content, root.createdBy, root.createdAt);
    bindings.bind(root.content).first(StringExpression::containsIgnoreCase);
    bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);
    bindings.bind(root.createdAt).first(DateTimeExpression::eq);

  }

}
