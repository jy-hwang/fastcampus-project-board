package com.fastcampus.projectboard.service;

import com.fastcampus.projectboard.domain.type.SearchType;
import com.fastcampus.projectboard.dto.ArticleDto;
import com.fastcampus.projectboard.dto.ArticleUpdateDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class ArticleService {

  @Transactional(readOnly = true)
  public Page<ArticleDto> searchArticles(SearchType searchType, String searchKeyword) {
    return null;
  }

  @Transactional(readOnly = true)
  public ArticleDto searchArticle(Long articleId) {
    return null;
  }

  public void saveArticle(ArticleDto dto) {

  }

  public void updateArticle(long articleId, ArticleUpdateDto dto) {

  }

  public void deleteArticle(long articleId) {

  }
}
