package com.fastcampus.projectboard.dto.response;

import com.fastcampus.projectboard.dto.ArticleWithCommentsDto;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public record ArticleWithCommentResponse(
    Long id,
    String title,
    String content,
    String hashtag,
    LocalDateTime createAt,
    String email,
    String nickname,
    Set<ArticleCommentResponse> articleCommentsResponse

) implements Serializable {

  public static ArticleWithCommentResponse of(Long id, String title, String content, String hashtag,
      LocalDateTime createAt, String email, String nickname,
      Set<ArticleCommentResponse> articleCommentResponses) {
    return new ArticleWithCommentResponse(id, title, content, hashtag, createAt, email, nickname,
        articleCommentResponses);
  }

  public static ArticleWithCommentResponse from(ArticleWithCommentsDto dto) {
    String nickname = dto.userAccountDto().nickname();
    if (nickname == null || nickname.isBlank()) {
      nickname = dto.userAccountDto().userId();
    }

    return new ArticleWithCommentResponse(
        dto.id(),
        dto.title(),
        dto.content(),
        dto.hashtag(),
        dto.createdAt(),
        dto.userAccountDto().email(),
        nickname,
        dto.articleCommentDtos().stream().map(ArticleCommentResponse::from).collect(
            Collectors.toCollection(LinkedHashSet::new))
    );

  }

}
