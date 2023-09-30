package com.fastcampus.projectboard.repository.querydsl;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.QArticle;
import com.fastcampus.projectboard.domain.QHashtag;
import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class HashtagRepositoryCustomImpl extends QuerydslRepositorySupport implements
    HashtagRepositoryCustom {


  public HashtagRepositoryCustomImpl() {
    super(Article.class);
  }


  @Override public List<String> findAllHashtagNames() {
    QHashtag hashtag = QHashtag.hashtag;

    return from(hashtag)
        .select(hashtag.hashtagName)
        .fetch();
  }


}
