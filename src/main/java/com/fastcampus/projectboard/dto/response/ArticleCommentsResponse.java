package com.fastcampus.projectboard.dto.response;

import com.fastcampus.projectboard.dto.ArticleCommentDto;
import java.time.LocalDateTime;

public record ArticleCommentsResponse(
    //@formatter:off
    Long id, String content, LocalDateTime createdAt, String email, String nickname, String userId
    //@formatter:on
) {

  public static ArticleCommentsResponse of(Long id, String content, LocalDateTime createdAt,
      String email, String nickname, String userId) {
    return new ArticleCommentsResponse(id, content, createdAt, email, nickname, userId);
  }

  public static ArticleCommentsResponse from(ArticleCommentDto dto) {
    String nickname = dto.userAccountDto().nickname();
    if (nickname == null || nickname.isBlank()) {
      nickname = dto.userAccountDto().userId();
    }

    return new ArticleCommentsResponse(
        dto.id(),
        dto.content(),
        dto.createdAt(),
        dto.userAccountDto().email(),
        nickname,
        dto.userAccountDto().userId());
  }

}
