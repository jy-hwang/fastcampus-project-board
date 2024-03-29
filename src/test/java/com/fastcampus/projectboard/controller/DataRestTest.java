package com.fastcampus.projectboard.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.head;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;


//@WebMvcTest
@Disabled("Spring Data REST 통합 테스트는 불필요하므로 제외시킴.")
@DisplayName("Data REST - API test")
@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class DataRestTest {

  private final MockMvc mvc;

  DataRestTest(@Autowired MockMvc mvc) {
    this.mvc = mvc;
  }

  @DisplayName("[api] 게시글 리스트 조회")
  @Test
  void givenNothing_whenRequestingArticles_thenReturnArticlesJsonResponse() throws Exception {
    // Given

    // When & Then
    mvc.perform(get("/api/articles"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.valueOf("application/hal+json")))
    //   .andDo(print())
    ;

  }

  @DisplayName("[api] 게시글 단건 조회")
  @Test
  void givenNothing_whenRequestingArticle_thenReturnArticleJsonResponse() throws Exception {
    // Given

    // When & Then
    mvc.perform(get("/api/articles/1"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.valueOf("application/hal+json")));

  }

  @DisplayName("[api] 게시글 -> 댓글 리스트 조회")
  @Test
  void givenNothing_whenRequestingArticleCommentsFromArticle_thenReturnArticleCommentsJsonResponse()
      throws Exception {
    // Given

    // When & Then
    mvc.perform(get("/api/articles/1/articleComments"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.valueOf("application/hal+json")));

  }

  @DisplayName("[api] 댓글 리스트 조회")
  @Test
  void givenNothing_whenRequestingArticleComments_thenReturnArticleCommentsJsonResponse()
      throws Exception {
    // Given

    // When & Then
    mvc.perform(get("/api/articleComments"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.valueOf("application/hal+json")));

  }

  @DisplayName("[api] 댓글 단건 조회")
  @Test
  void givenNothing_whenRequestingArticleComment_thenReturnArticleCommentJsonResponse()
      throws Exception {
    // Given

    // When & Then
    mvc.perform(get("/api/articleComments/1"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.valueOf("application/hal+json")));

  }

  @DisplayName("[api] 회원 관련 API는 일체제공하지 않는다.")
  @Test
  void givenNothing_whenRequestingUserAccount_thenThrowException()
      throws Exception {
    // Given

    // When & Then
    mvc.perform(get("/api/userAccounts")).andExpect(status().isNotFound());
    mvc.perform(post("/api/userAccounts")).andExpect(status().isNotFound());
    mvc.perform(put("/api/userAccounts")).andExpect(status().isNotFound());
    mvc.perform(patch("/api/userAccounts")).andExpect(status().isNotFound());
    mvc.perform(delete("/api/userAccounts")).andExpect(status().isNotFound());
    mvc.perform(head("/api/userAccounts")).andExpect(status().isNotFound());

  }


}
