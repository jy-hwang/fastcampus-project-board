package com.fastcampus.projectboard.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.fastcampus.projectboard.config.SecurityConfig;
import com.fastcampus.projectboard.domain.ArticleComment;
import com.fastcampus.projectboard.dto.ArticleCommentDto;
import com.fastcampus.projectboard.dto.ArticleDto;
import com.fastcampus.projectboard.dto.request.ArticleCommentRequest;
import com.fastcampus.projectboard.service.ArticleCommentService;
import com.fastcampus.projectboard.util.FormDataEncoder;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@DisplayName("View 컨트롤러 - 댓글")
@Import({SecurityConfig.class, FormDataEncoder.class})
@WebMvcTest(ArticleCommentController.class)
class ArticleCommentControllerTest {

  private final MockMvc mvc;

  private final FormDataEncoder formDataEncoder;

  @MockBean private ArticleCommentService articleCommentService;

  public ArticleCommentControllerTest(
      @Autowired MockMvc mvc,
      @Autowired FormDataEncoder formDataEncoder) {
    this.mvc = mvc;
    this.formDataEncoder = formDataEncoder;
  }


  @DisplayName("[view][POST] 댓글 등록 - 정상 호출")
  @Test
  void givenArticleCommentInfo_whenRequesting_thenSavesArticleComment() throws Exception{
    // Given
    long articleId = 1L;
    ArticleCommentRequest request = ArticleCommentRequest.of(articleId, "testComment");
    willDoNothing().given(articleCommentService).saveArticleComment(any(ArticleCommentDto.class));

    // When & Then
    mvc.perform(post("/comments/new")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .content(formDataEncoder.encode(request))
            .with(csrf())
        )
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/articles/"+articleId))
        .andExpect((redirectedUrl("/articles/" + articleId)));
    then(articleCommentService).should().saveArticleComment(any(ArticleCommentDto.class));
  }

  @DisplayName("[view][GET] 댓글 삭제 - 정상 호출")
  @Test
  void givenArticleCommentIdToDelete_whenRequesting_thenDeleteArticleComment() throws Exception{
    // Given
    long articleId = 1L;
    long articleCommentId = 1L;
    willDoNothing().given(articleCommentService).deleteArticleComment(articleCommentId);

    // When & Then
    mvc.perform(
            post("/comments/" + articleCommentId + "/delete")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(formDataEncoder.encode(Map.of("articleId", articleId)))
                .with(csrf())
        )
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/articles/"+articleId))
        .andExpect((redirectedUrl("/articles/" + articleId)));
    then(articleCommentService).should().deleteArticleComment(articleCommentId);
  }
}