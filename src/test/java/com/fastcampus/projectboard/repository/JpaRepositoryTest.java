package com.fastcampus.projectboard.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.fastcampus.projectboard.config.JpaConfig;
import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.UserAccount;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@DisplayName("Jpa 연결 테스트")
@Import(JpaRepositoryTest.TestJpaConfig.class)
@DataJpaTest
class JpaRepositoryTest {

  private final ArticleRepository articleRepository;

  private final ArticleCommentRepository articleCommentRepository;

  private final UserAccountRepository userAccountRepository;

  protected JpaRepositoryTest(@Autowired ArticleRepository articleRepository,
      @Autowired ArticleCommentRepository articleCommentRepository,
      @Autowired UserAccountRepository userAccountRepository) {
    this.articleRepository = articleRepository;
    this.articleCommentRepository = articleCommentRepository;
    this.userAccountRepository = userAccountRepository;
  }

  @DisplayName("select 테스트")
  @Test
  void givenTestData_whenSelecting_thenWorksFine() {
    // given

    // when
    List<Article> articles = articleRepository.findAll();

    // then
    assertThat(articles).isNotNull().hasSize(123);

  }

  @DisplayName("insert 테스트")
  @Test
  void givenTestData_whenInserting_thenWorksFine() {

    // given
    long previousCount = articleRepository.count();
    UserAccount userAccount = userAccountRepository.save(UserAccount.of("newJY", "pw",null,null,null));
    Article article = Article.of(userAccount,"new article title", "new article Content", "#spring");

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
    String updatedHashTag = "#springboot";
    article.setHashtag(updatedHashTag);

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
    assertThat(savedArticle).hasFieldOrPropertyWithValue("hashtag", updatedHashTag);

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

  @EnableJpaAuditing
  @TestConfiguration
  public static class TestJpaConfig {
    @Bean
    public AuditorAware<String> auditorAware() {
      return () -> Optional.of("jackie");
    }
  }


}
