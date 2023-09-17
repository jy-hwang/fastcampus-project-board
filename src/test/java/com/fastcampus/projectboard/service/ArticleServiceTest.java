package com.fastcampus.projectboard.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.UserAccount;
import com.fastcampus.projectboard.domain.type.SearchType;
import com.fastcampus.projectboard.dto.ArticleDto;
import com.fastcampus.projectboard.dto.ArticleWithCommentsDto;
import com.fastcampus.projectboard.dto.UserAccountDto;
import com.fastcampus.projectboard.repository.ArticleRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@DisplayName("비즈니스 로직 - 게시글")
@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

  @InjectMocks
  private ArticleService sut;//system under test
  @Mock
  private ArticleRepository articleRepository;

  /*
  #20 - 서비스로직의 테스트와 골격잡기에서는 전체를 검색하는 것을 먼저하고
        -> 반환타입을 List<>에서 Page<> 로 변경
  차후 진행해볼 것 정리
   전체 또는 검색조건으로 검색된 게시글 조회 이후 페이지네이션처리
    -> 컨트롤러 단에서 테스트 진행
    -> 추가적으로 정렬기능까지 가능.
   */
  @DisplayName("검색어 없이 게시글을 검색하면, 게시글 페이지를 반환한다..")
  @Test
  void givenNoSearchingParameters_whenSearchingArticles_thenReturnsArticlePage() {
    // Given
    Pageable pageable = Pageable.ofSize(20);
    given(articleRepository.findAll(pageable)).willReturn(Page.empty());

    // When
    //List<ArticleDto> articles = sut.searchArticles(SearchType.TITLE,"search keyword");
    Page<ArticleDto> articles = sut.searchArticles(null, null, pageable);

    // Then - 원래의 경우, 실패하는 테스트 코드를 작성해야함.
    assertThat(articles).isEmpty();
    then(articleRepository).should().findAll(pageable);
  }

  @DisplayName("검색어와 함께 게시글을 검색하면, 게시글 페이지를 반환한다..")
  @Test
  void givenSearchingParameters_whenSearchingArticles_thenReturnsArticlePage() {
    // Given
    SearchType searchType = SearchType.TITLE;
    String searchKeyword = "title";
    Pageable pageable = Pageable.ofSize(20);
    given(articleRepository.findByTitleContaining(searchKeyword, pageable)).willReturn(Page.empty());

    // When
    //List<ArticleDto> articles = sut.searchArticles(SearchType.TITLE,"search keyword");
    Page<ArticleDto> articles = sut.searchArticles(searchType, searchKeyword, pageable);

    // Then - 원래의 경우, 실패하는 테스트 코드를 작성해야함.
    assertThat(articles).isEmpty();
    then(articleRepository).should().findByTitleContaining(searchKeyword, pageable);
  }

  @DisplayName("게시글을 조회하면, 게시글을 반환한다..")
  @Test
  void givenArticleId_whenSearchingArticle_thenReturnsArticle() {
    // Given
    Long articleId = 1L;
    Article article = createArticle();
    given(articleRepository.findById(articleId)).willReturn(Optional.of(article));

    // When
    ArticleWithCommentsDto dto = sut.getArticle(articleId);

    // Then
    assertThat(article)
        .hasFieldOrPropertyWithValue("title", article.getTitle())
        .hasFieldOrPropertyWithValue("content", article.getContent())
        .hasFieldOrPropertyWithValue("hashtag", article.getHashtag());

    then(articleRepository).should().findById(articleId);
  }

  @DisplayName("없는 게시글을 조회하면, 예외를 던진다.")
  @Test
  void givenNonexistentArticleId_whenSearchingArticle_thenThrowException() {
    // Given
    Long articleId = 0L;
    given(articleRepository.findById(articleId)).willReturn(Optional.empty());

    // When
    Throwable t = catchThrowable(() -> sut.getArticle(articleId));

    // Then
    assertThat(t).isInstanceOf(EntityNotFoundException.class)
        .hasMessage("게시글이 없습니다. - articleId: " + articleId);
  }

  @DisplayName("게시글 정보를 입력하면, 게시글을 생성한다.")
  @Test
  void givenArticleInfo_whenSavingArticle_thenSavesArticle() {
    // Given
    ArticleDto dto = createArticleDto();
    given(articleRepository.save(any(Article.class))).willReturn(createArticle());
    //willDoNothing().given(articleRepository).save(any(Article.class));

    // When
    sut.saveArticle(dto);

    // Then - save 메서드를 호출했는가
    then(articleRepository).should().save(any(Article.class));
  }

  @DisplayName("게시글의 수정 정보를 입력하면, 게시글을 수정한다.")
  @Test
  void givenModifiedInfo_whenUpdatingArticle_thenUpdatesArticle() {
    // Given
    Article article = createArticle();
    ArticleDto dto = createArticleDto("새 타이틀", "새 내용", "#springboot");
    given(articleRepository.getReferenceById(dto.id())).willReturn(article);

    // When
    sut.updateArticle(dto);

    // Then
    assertThat(article)
        .hasFieldOrPropertyWithValue("title", dto.title())
        .hasFieldOrPropertyWithValue("content", dto.content())
        .hasFieldOrPropertyWithValue("hashtag", dto.hashtag());

    then(articleRepository).should().getReferenceById(dto.id());
  }

  @DisplayName("없는 게시글의 수정 정보를 입력하면, 경고 로그를 찍고 아무것도 하지 않는다.")
  @Test
  void givenNonexistentArticleInfo_whenUpdatingArticle_thenLogsWarningAndDoesNothing() {
    // Given
    ArticleDto dto = createArticleDto("새 타이틀", "새 내용", "#springboot");
    given(articleRepository.getReferenceById(dto.id())).willThrow(EntityNotFoundException.class);

    // When
    sut.updateArticle(dto);

    // Then
    then(articleRepository).should().getReferenceById(dto.id());
  }

  @DisplayName("게시글의 ID를 입력하면, 게시글을 삭제한다.")
  @Test
  void givenArticleId_whenDeletingArticle_thenDeletesArticle() {
    // Given
    Long articleId = 1L;
    willDoNothing().given(articleRepository).deleteById(articleId);

    // When
    sut.deleteArticle(1L);

    // Then
    then(articleRepository).should().deleteById(articleId);
  }


  private UserAccount createUserAccount() {
    return UserAccount.of(
        "jackie",
        "password",
        "jackieHwang@abc.com",
        "jackieHwang",
        null);
  }

  private Article createArticle() {
    return Article.of(
        createUserAccount(),
        "title",
        "content",
        "#java");
  }

  private ArticleDto createArticleDto() {
    return createArticleDto("title", "content", "#java");

  }

  private ArticleDto createArticleDto(String title, String content, String hashtag) {
    return ArticleDto.of(
        1L,
        createUserAccountDto(),
        title,
        content,
        hashtag,
        LocalDateTime.now(),
        "jackieHwang",
        LocalDateTime.now(),
        "jackieHwang"
    );

  }

  private UserAccountDto createUserAccountDto() {
    return UserAccountDto.of(
        1L,
        "jackieHwang",
        "password",
        "jackieHwang@abc.com",
        "jackieHwang",
        "IamWeb-Programmer",
        LocalDateTime.now(),
        "jackieHwang",
        LocalDateTime.now(),
        "jackieHwang"
    );
  }

}
