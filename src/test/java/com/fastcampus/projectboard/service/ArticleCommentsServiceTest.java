package com.fastcampus.projectboard.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.never;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.ArticleComment;
import com.fastcampus.projectboard.dto.ArticleCommentDto;
import com.fastcampus.projectboard.dto.ArticleDto;
import com.fastcampus.projectboard.repository.ArticleCommentRepository;
import com.fastcampus.projectboard.repository.ArticleRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
    given(articleRepository.findById(articleId)).willReturn(Optional.of(
        Article.of("title", "content", "#java")));

    // When
    List<ArticleCommentDto> articleComments = sut.searchArticleComments(articleId);

    // Then
    assertThat(articleComments).isNotNull();
    then(articleRepository).should().findById(articleId);
  }

  @DisplayName("댓글정보를 입력하면 댓글을 저장한다.")
  @Test
  void givenArticleCommentInfo_whenSavingArticleComment_thenSavesArticleComment() {
    // Given
    given(articleCommentRepository.save(any(ArticleComment.class))).willReturn(null);
    // When
    sut.saveArticleComment(ArticleCommentDto.of(
        LocalDateTime.now(), "jackieHwang", LocalDateTime.now(),"jackieHwang","comment"));

    // Then
    then(articleCommentRepository).should().save(any(ArticleComment.class));
  }

}

