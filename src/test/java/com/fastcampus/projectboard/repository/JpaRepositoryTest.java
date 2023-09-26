package com.fastcampus.projectboard.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.ArticleComment;
import com.fastcampus.projectboard.domain.Hashtag;
import com.fastcampus.projectboard.domain.UserAccount;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@DisplayName("Jpa 연결 테스트")
@Import(JpaRepositoryTest.TestJpaConfig.class)
@DataJpaTest
class JpaRepositoryTest {

  private final ArticleRepository articleRepository;

  private final ArticleCommentRepository articleCommentRepository;

  private final UserAccountRepository userAccountRepository;

  private final HashtagRepository hashtagRepository;

  JpaRepositoryTest(
      @Autowired ArticleRepository articleRepository,
      @Autowired ArticleCommentRepository articleCommentRepository,
      @Autowired UserAccountRepository userAccountRepository,
      @Autowired HashtagRepository hashtagRepository
  ) {
    this.articleRepository = articleRepository;
    this.articleCommentRepository = articleCommentRepository;
    this.userAccountRepository = userAccountRepository;
    this.hashtagRepository = hashtagRepository;
  }

  @DisplayName("select 테스트")
  @Test
  void givenTestData_whenSelecting_thenWorksFine() {
    // given

    // when
    List<Article> articles = articleRepository.findAll();

    // then
    assertThat(articles).isNotNull().hasSize(123);// classpath:resources/data.sql 참조

  }

  @DisplayName("insert 테스트")
  @Test
  void givenTestData_whenInserting_thenWorksFine() {

    // given
    long previousCount = articleRepository.count();
    UserAccount userAccount = userAccountRepository.save(
        UserAccount.of("newJY", "pw", null, null, null));
    Article article = Article.of(userAccount, "new article title", "new article Content");
    article.addHashtags(Set.of(Hashtag.of("spring")));

    // when
    articleRepository.save(article);

    // then
    assertThat(articleRepository.count()).isEqualTo(previousCount + 1);

  }

  @DisplayName("update 테스트")
  @Test
  void givenTestData_whenUpdating_thenWorksFine() {

    // given
    Article article = articleRepository.findById(1L).orElseThrow();
    Hashtag updatedHashTag = Hashtag.of("#springboot");
    article.clearHashtags();
    article.addHashtags(Set.of(updatedHashTag));

    // when
    // Article savedArticle = articleRepository.save(article);
    /*
     * @formatter:off
     * 기본적으로 @DataJpaTest 어노테이션에 의해 트랜잭션이 걸려있어서 의도와는 다르게 생략되는 메서드가 종종생김.
     * 그리고 개별 테스트 메서드 실행 후 롤백되는 구조이므로, 제대로 확인할 수 없는 문제가 생김.
     * 그래서 save 후에 flush 작업을 강제로 해줘서 DB의 영속성을 맞춰줌.
     * @formatter:on
     */
    Article savedArticle = articleRepository.saveAndFlush(article);

    // then
    assertThat(savedArticle.getHashtags())
        .hasSize(1)
        .extracting("hashtagName", String.class)
        .containsExactly(updatedHashTag.getHashtagName());

  }

  @DisplayName("delete 테스트")
  @Test
  void givenTestData_whenDeleting_thenWorksFine() {

    // given
    Article article = articleRepository.findById(1L).orElseThrow();
    long previousArticleCount = articleRepository.count();
    long previousArticleCommentCount = articleCommentRepository.count();
    int deleteCommentSize = article.getArticleComments().size();

    // when
    articleRepository.delete(article);

    // then
    assertThat(articleRepository.count()).isEqualTo(previousArticleCount - 1);
    assertThat(articleCommentRepository.count())
        .isEqualTo(previousArticleCommentCount - deleteCommentSize);

  }

  @DisplayName("대댓글 조회 테스트")
  @Test
  void givenParentCommentId_whenSelecting_thenReturnsChildComments() {
    // Given

    // When
    Optional<ArticleComment> parentComment = articleCommentRepository.findById(1L);

    // Then
    assertThat(parentComment).get()
        .hasFieldOrPropertyWithValue("parentCommentId", null)
        .extracting("childComments", InstanceOfAssertFactories.COLLECTION)
        .hasSize(5)
    ;
  }

  @DisplayName("댓글에 대댓글 삽입 테스트")
  @Test
  void givenParentComment_whenSaving_thenInsertsChildComment() {
    // Given
    ArticleComment parentComment = articleCommentRepository.getReferenceById(1L);
    ArticleComment childComment = ArticleComment.of(parentComment.getArticle(), parentComment.getUserAccount(), "대댓글");

    // When
    parentComment.addChildComment(childComment);
    articleCommentRepository.flush();

    // Then
    assertThat(articleCommentRepository.findById(1L)).get()
        .hasFieldOrPropertyWithValue("parentCommentId", null)
        .extracting("childComments", InstanceOfAssertFactories.COLLECTION)
        .hasSize(6)
    ;
  }


  @DisplayName("댓글 삭제와 대댓글 전체 연동 삭제 테스트 - 댓글 ID + 유저 ID")
  @Test
  void givenArticleCommentIdHavingChildCommentsAndUserId_whenDeletingParentComment_thenDeletesEveryComment() {
    // Given
    long previousArticleCommentCount = articleCommentRepository.count();

    // When
    articleCommentRepository.deleteByIdAndUserAccount_UserId(1L, "admin4");

    // Then
    assertThat(articleCommentRepository.count()).isEqualTo(previousArticleCommentCount - 6);

  }

  @DisplayName("댓글 삭제와 대댓글 전체 연동 삭제 테스트")
  @Test
  void givenArticleCommentIdHavingChildComments_whenDeletingParentComment_thenDeletesEveryComment() {
    // Given
    ArticleComment parentComment = articleCommentRepository.getReferenceById(1L);
    long previousArticleCommentCount = articleCommentRepository.count();

    // When
    articleCommentRepository.delete(parentComment);

    // Then
    assertThat(articleCommentRepository.count()).isEqualTo(previousArticleCommentCount - 6);

  }

  @DisplayName("[Querydsl] 전체 hashtag 리스트에서 이름만 조회하기")
  @Test
  void givenNothing_whenQueryingHashtags_thenReturnsHashtagNames() {
    // Given

    // When
    List<String> hashtagNames = hashtagRepository.findAllHashtagNames();
    // Then
    assertThat(hashtagNames).hasSize(19);

  }

  @DisplayName("[Querydsl] hashtag로 페이징된 게시글 검색하기")
  @Test
  void givenHashtagNamesAndPageable_whenQueryingArticles_thenReturnsArticlePage() {
    // Given
    List<String> hashtagNames = List.of("olive", "orange", "indigo");
    Pageable pageable = PageRequest.of(
        0, 5, Sort.by(
            Sort.Order.desc("hashtags.hashtagName"),
            Sort.Order.asc("title")));
    // When
    Page<Article> articlePage = articleRepository.findByHashtagNames(hashtagNames, pageable);
    // Then
    assertThat(articlePage.getContent()).hasSize(pageable.getPageSize());
    assertThat(articlePage.getContent().get(0).getTitle()).isEqualTo(
        "In hac habitasse platea dictumst.");
    assertThat(articlePage.getContent().get(0).getHashtags()).extracting("hashtagName", String.class
    ).containsExactly("olive");
    assertThat(articlePage.getTotalElements()).isEqualTo(17);
    assertThat(articlePage.getTotalPages()).isEqualTo(4);
  }

  @EnableJpaAuditing
  @TestConfiguration
  static class TestJpaConfig {

    @Bean
    AuditorAware<String> auditorAware() {
      return () -> Optional.of("jackie");
    }
  }


}
