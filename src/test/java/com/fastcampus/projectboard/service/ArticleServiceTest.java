package com.fastcampus.projectboard.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.type.SearchType;
import com.fastcampus.projectboard.dto.ArticleDto;
import com.fastcampus.projectboard.dto.ArticleUpdateDto;
import com.fastcampus.projectboard.repository.ArticleRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;

import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

@DisplayName("비즈니스 로직 - 게시글")
@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

  @InjectMocks
  private ArticleService sut;//system under test
  @Mock
  private ArticleRepository articleRepository;
/*
검색
각 게시글 페이지로 이동
페이지네이션
홈 버튼 -> 게시판 페이지로 리다이렉션
정렬기능
 */

  /*
  #20 - 서비스로직의 테스트와 골격잡기에서는 전체를 검색하는 것을 먼저하고
        -> 반환타입을 List<>에서 Page<> 로 변경
  차후 진행해볼 것 정리
   전체 또는 검색조건으로 검색된 게시글 조회 이후 페이지네이션처리
    -> 컨트롤러 단에서 테스트 진행
    -> 추가적으로 정렬기능까지 가능.
   */
  @DisplayName("전체검색 또는 키워드 검색 :: 게시글을 검색하면, 게시글 리스트를 반환한다.")
  @Test
  void givenSearchingParameters_whenSearchingArticles_thenReturnsArticleList() {
    // Given

    // When
    //List<ArticleDto> articles = sut.searchArticles(SearchType.TITLE,"search keyword");
    Page<ArticleDto> articles = sut.searchArticles(SearchType.TITLE, "search keyword");

    // Then
    assertThat(articles).isNotNull();
  }

  @DisplayName("단건조회 :: 게시글을 ID 조회하면, 게시글을 반환한다.")
  @Test
  void givenArticleId_whenSearchingArticle_thenReturnsArticle() {
    // Given
    //Long articleId = 1L;
    // When
    ArticleDto article = sut.searchArticle(1L);

    // Then
    assertThat(article).isNotNull();
  }

  @DisplayName("게시글 정보를 입력하면, 게시글을 생성한다.")
  @Test
  void givenArticleInfo_whenSavingArticle_thenSavesArticle() {
    // Given
    ArticleDto dto = ArticleDto.of(LocalDateTime.now(),"jackieHwang","title","content","#springJPA")  ;
    given(articleRepository.save(any(Article.class))).willReturn(null);
    // When
    sut.saveArticle(dto);

    // Then
    then(articleRepository).should().save(any(Article.class));

  }

  @DisplayName("게시글의 ID와 수정 정보를 입력하면, 게시글을 수정한다.")
  @Test
  void givenArticleIdAndModifiedInfo_whenUpdatingArticle_thenUpdatesArticle() {
    // Given
    ArticleDto dto = ArticleDto.of(LocalDateTime.now(),"jackieHwang","title","content","#springJPA")  ;
    given(articleRepository.save(any(Article.class))).willReturn(null);
    // When
    sut.updateArticle(1L, ArticleUpdateDto.of("title1","content1","#java"));

    // Then
    then(articleRepository).should().save(any(Article.class));

  }

  @DisplayName("게시글의 ID를 입력하면, 게시글을 삭제한다.")
  @Test
  void givenArticleId_whenDeletingArticle_thenDeletesArticle() {
    // Given
    ArticleDto dto = ArticleDto.of(LocalDateTime.now(),"jackieHwang","title","content","#springJPA")  ;
    willDoNothing().given(articleRepository).delete(any(Article.class));
    // When
    sut.deleteArticle(1L);

    // Then
    then(articleRepository).should().delete(any(Article.class));

  }
}
