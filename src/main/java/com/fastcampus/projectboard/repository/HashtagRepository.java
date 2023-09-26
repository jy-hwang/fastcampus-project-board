package com.fastcampus.projectboard.repository;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.Hashtag;
import com.fastcampus.projectboard.repository.querydsl.HashtagRepositoryCustom;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface HashtagRepository extends
    //@formatter:off
    JpaRepository<Hashtag, Long>
    , HashtagRepositoryCustom
    , QuerydslPredicateExecutor<Hashtag>
  //@formatter:on
{
  Optional<Hashtag> findByHashtagName(String hashtagName);

  List<Hashtag> findByHashtagNameIn(Set<String> hashtagNames);
}
