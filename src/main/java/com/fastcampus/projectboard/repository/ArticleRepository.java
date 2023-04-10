package com.fastcampus.projectboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.fastcampus.projectboard.domain.Article;

public interface ArticleRepository extends JpaRepository<Article, Long> {

}
