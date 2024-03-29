package com.fastcampus.projectboard.repository.querydsl;

import com.fastcampus.projectboard.domain.Article;
import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleRepositoryCustom {

  /**
   * @deprecated 해시태그 도메인을 새로 만들었으므로 이 코드는 더이상 사용할 필요가 없다.
   * @see HashtagRepositoryCustom#findAllHashtagNames()
   */
  @Deprecated
  List<String> findAllDistinctHashtags();

  Page<Article> findByHashtagNames(Collection<String> hashtagNames, Pageable pageable);
}
