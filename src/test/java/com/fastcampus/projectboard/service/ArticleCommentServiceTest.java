package com.fastcampus.projectboard.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.never;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.ArticleComment;
import com.fastcampus.projectboard.domain.Hashtag;
import com.fastcampus.projectboard.domain.UserAccount;
import com.fastcampus.projectboard.dto.ArticleCommentDto;
import com.fastcampus.projectboard.dto.UserAccountDto;
import com.fastcampus.projectboard.repository.ArticleCommentRepository;
import com.fastcampus.projectboard.repository.ArticleRepository;
import com.fastcampus.projectboard.repository.UserAccountRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@DisplayName("비즈니스 로직 - 댓글")
@ExtendWith(MockitoExtension.class)
class ArticleCommentServiceTest {

  @InjectMocks
  private ArticleCommentService sut;//system under test

  @Mock
  private ArticleCommentRepository articleCommentRepository;
  @Mock
  private ArticleRepository articleRepository;

  @Mock private UserAccountRepository userAccountRepository;

  @DisplayName("게시글 ID로 조회하면, 해당하는 댓글 리스트를 반환한다.")
  @Test
  void givenArticleId_whenSearchingArticleComments_thenReturnsArticleComments() {
    // Given
    Long articleId = 1L;
    ArticleComment expectedParentComment = createArticleComment(1L, "parent content");
    ArticleComment expectedChildComment = createArticleComment(2L, "child content");
    expectedChildComment.setParentCommentId(expectedParentComment.getId());

    given(articleCommentRepository.findByArticle_Id(articleId)).willReturn(List.of(
        expectedParentComment,
        expectedChildComment
    ));

    // When
    List<ArticleCommentDto> actual = sut.searchArticleComments(articleId);

    // Then
    assertThat(actual)
        .hasSize(2);
    assertThat(actual)
        .extracting("id", "articleId", "parentCommentId", "content")
        .containsExactlyInAnyOrder(
            tuple(1L, 1L, null, "parent content"),
            tuple(2L, 1L, 1L, "child content")
        );

    then(articleCommentRepository).should().findByArticle_Id(articleId);
  }

  @DisplayName("댓글 정보를 입력하면, 댓글을 저장한다.")
  @Test
  void givenArticleCommentInfo_whenSavingArticleComment_thenSavesArticleComment() {
    // Given
    ArticleCommentDto dto = createArticleCommentDto("댓글");
    given(articleRepository.getReferenceById(dto.articleId())).willReturn(createArticle());
    given(userAccountRepository.getReferenceById(dto.userAccountDto().userId())).willReturn(
        createUserAccount());
    given(articleCommentRepository.save(any(ArticleComment.class))).willReturn(null);

    // When
    sut.saveArticleComment(dto);

    // Then
    then(articleRepository).should().getReferenceById(dto.articleId());
    then(userAccountRepository).should().getReferenceById(dto.userAccountDto().userId());
    then(articleCommentRepository).should(never()).getReferenceById(anyLong());
    then(articleCommentRepository).should().save(any(ArticleComment.class));
  }

  @DisplayName("댓글 저장을 시도했는데, 맞는 게시글이 없으면 경고 로그를 찍고 아무것도 안 한다..")
  @Test
  void givenNonexitentArticle_whenSavingArticleComment_thenLogsSituationAndDoesNothing() {
    // Given
    ArticleCommentDto dto = createArticleCommentDto("댓글");
    given(articleRepository.getReferenceById(dto.articleId())).willThrow(
        EntityNotFoundException.class);

    // When
    sut.saveArticleComment(dto);

    // Then
    then(articleRepository).should().getReferenceById(dto.articleId());
    then(userAccountRepository).shouldHaveNoInteractions();
    then(articleCommentRepository).shouldHaveNoInteractions();
  }


  @DisplayName("댓글 ID를 입력하면, 댓글을 삭제한다.")
  @Test
  void givenArticleCommentId_whenDeletingArticleComment_thenDeletesArticleComment() {
    // Given
    Long articleCommentId = 1L;
    String userId = "jackieTest";
    willDoNothing().given(articleCommentRepository)
        .deleteByIdAndUserAccount_UserId(articleCommentId, userId);

    // When
    sut.deleteArticleComment(articleCommentId, userId);

    // Then
    then(articleCommentRepository).should()
        .deleteByIdAndUserAccount_UserId(articleCommentId, userId);
  }

  private ArticleCommentDto createArticleCommentDto(String content) {
    return createArticleCommentDto(null, content);
  }

  private ArticleCommentDto createArticleCommentDto(Long parentCommentId, String content) {
    return createArticleCommentDto(1L, parentCommentId, content);
  }

  private ArticleCommentDto createArticleCommentDto(Long id, Long parentCommentId, String content) {
    return ArticleCommentDto.of(
        id,
        1L,
        createUserAccountDto(),
        parentCommentId,
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

  private ArticleComment createArticleComment(Long id, String content) {
    ArticleComment articleComment = ArticleComment.of(
        createArticle(),
        createUserAccount(),
        content
    );

    ReflectionTestUtils.setField(articleComment, "id", id);

    return articleComment;
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
    Article article = Article.of(
        createUserAccount(),
        "title",
        "content"
    );
    ReflectionTestUtils.setField(article, "id", 1L);
    article.addHashtags(Set.of(createHashtag(article)));
    return article;
  }

  private Hashtag createHashtag(Article article) {
    return Hashtag.of("java");
  }
}

