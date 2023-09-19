package com.fastcampus.projectboard.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.ArticleComment;
import com.fastcampus.projectboard.domain.UserAccount;
import com.fastcampus.projectboard.dto.ArticleCommentDto;
import com.fastcampus.projectboard.dto.UserAccountDto;
import com.fastcampus.projectboard.repository.ArticleCommentRepository;
import com.fastcampus.projectboard.repository.ArticleRepository;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("비즈니스 로직 - 댓글")
@ExtendWith(MockitoExtension.class)
class ArticleCommentsServiceTest {

  @InjectMocks
  private ArticleCommentService sut;//system under test

  @Mock
  private ArticleCommentRepository articleCommentRepository;
  @Mock
  private ArticleRepository articleRepository;


  @DisplayName("게시글 ID로 조회하면, 해당하는 댓글 리스트를 반환한다.")
  @Test
  void givenArticleId_whenSearchingArticleComments_thenReturnsArticleComments() {
    // Given
    Long articleId = 1L;
    //UserAccount userAccount1 = new UserAccount("jyhwang","1234","jyhwang@abc.com","jackie","개발자");
    //given(articleRepository.findById(articleId)).willReturn(Optional.of(Article.of("title", "content", "#java")));
    ArticleComment expected = createArticleComment("content");

    // When
    List<ArticleCommentDto> articleComments = sut.searchArticleComments(articleId);

    // Then
    assertThat(articleComments).isNotNull();
    then(articleRepository).should().findById(articleId);
  }

  @DisplayName("댓글 정보를 입력하면, 댓글을 저장한다.")
  @Test
  void givenArticleCommentInfo_whenSavingArticleComment_thenSavesArticleComment() {
    // Given
    ArticleCommentDto dto = createArticleCommentDto("댓글");
    given(articleRepository.getReferenceById(dto.articleId())).willReturn(createArticle());
    given(articleCommentRepository.save(any(ArticleComment.class))).willReturn(null);

    // When
    sut.saveArticleComment(dto);

    // Then
    then(articleCommentRepository).should().save(any(ArticleComment.class));
  }


  @DisplayName("댓글 저장을 시도했는데, 맞는 게시글이 없으면 경고 로그를 찍고 아무것도 안 한다..")
  @Test
  void givenNonexitentArticle_whenSavingArticleComment_thenLogsSituationAndDoesNothing() {
    // Given
    ArticleCommentDto dto = createArticleCommentDto("댓글");
    given(articleRepository.getReferenceById(dto.articleId())).willThrow(EntityNotFoundException.class);

    // When
    sut.saveArticleComment(dto);

    // Then
    then(articleRepository).should().getReferenceById(dto.articleId());
    then(articleCommentRepository).shouldHaveNoInteractions();
  }


  @DisplayName("댓글 정보를 입력하면, 댓글을 수정한다.")
  @Test
  void givenArticleCommentInfo_whenUpdateingArticleComment_thenUpdatesArticleComment() {
    // Given
    String oldContent = "content";
    String updatedContent = "댓글";
    ArticleComment articleComment = createArticleComment(oldContent);
    ArticleCommentDto dto = createArticleCommentDto(updatedContent);
    given(articleCommentRepository.getReferenceById(dto.id())).willReturn(articleComment);

    // When
    sut.saveArticleComment(dto);

    // Then
    assertThat(articleComment.getContent()).isNotEqualTo(oldContent).isEqualTo(updatedContent);
    then(articleCommentRepository).should().getReferenceById(dto.id());
  }


  @DisplayName("없는 댓글 정보를 수정하려고 하면, 경고 로그를 찍고 아무 것도 안한다.")
  @Test
  void givenNonexitentArticle_whenUpdatingArticleComment_thenLogsWarningAndDoesNothing() {
    // Given
    ArticleCommentDto dto = createArticleCommentDto("댓글");
    given(articleRepository.getReferenceById(dto.articleId())).willReturn(createArticle());
    given(articleCommentRepository.save(any(ArticleComment.class))).willReturn(null);

    // When
    sut.saveArticleComment(dto);

    // Then
    then(articleCommentRepository).should().save(any(ArticleComment.class));
  }


  private ArticleCommentDto createArticleCommentDto(String content) {
    return ArticleCommentDto.of(
        1L,
        1L,
        createUserAccountDto(),
        content,
        LocalDateTime.now(),
        "jackieHwang",
        LocalDateTime.now(),
        "jackieHwang"
    );
  }

  private UserAccountDto createUserAccountDto() {
    return UserAccountDto.of(
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

  private ArticleComment createArticleComment(String content) {
    return ArticleComment.of(
        Article.of(createUserAccount(), "title", "content", "hashtag"),
        createUserAccount(),
        content
    );
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

}

