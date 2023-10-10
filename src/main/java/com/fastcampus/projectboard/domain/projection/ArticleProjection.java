package com.fastcampus.projectboard.domain.projection;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.UserAccount;
import java.time.LocalDateTime;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "withUserAccount", types = Article.class)
public interface ArticleProjection {

  Long getId();

  UserAccount getUserAccount();

  String getTitle();

  String getContent();

  LocalDateTime getCreatedAt();

  String getCreatedBy();

  LocalDateTime getModifiedAt();

  String getModifiedBy();


}
