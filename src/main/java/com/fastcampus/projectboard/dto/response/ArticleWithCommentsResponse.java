package com.fastcampus.projectboard.dto.response;

import com.fastcampus.projectboard.dto.ArticleWithCommentsDto;
import com.fastcampus.projectboard.dto.HashtagDto;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public record ArticleWithCommentsResponse(
    Long id,
    String title,
    String content,
    Set<String> hashtags,
    LocalDateTime createdAt,
    String email,
    String nickname,
    String userId,
    Set<ArticleCommentsResponse> articleCommentsResponse

) {

  public static ArticleWithCommentsResponse of(Long id, String title, String content,
      Set<String> hashtags, LocalDateTime createdAt, String email, String nickname, String userId,
      Set<ArticleCommentsResponse> articleCommentsResponse) {
    return new ArticleWithCommentsResponse(id, title, content, hashtags, createdAt, email, nickname,
        userId, articleCommentsResponse);
  }

  public static ArticleWithCommentsResponse from(ArticleWithCommentsDto dto) {
    String nickname = dto.userAccountDto().nickname();
    if (nickname == null || nickname.isBlank()) {
      nickname = dto.userAccountDto().userId();
    }

    return new ArticleWithCommentsResponse(
        dto.id(),
        dto.title(),
        dto.content(),
        dto.hashtagDtos().stream().map(HashtagDto::hashtagName).collect(Collectors.toUnmodifiableSet()),
        dto.createdAt(),
        dto.userAccountDto().email(),
        nickname,
        dto.userAccountDto().userId(),
        dto.articleCommentDtos().stream().map(ArticleCommentsResponse::from).collect(
            Collectors.toCollection(LinkedHashSet::new))
    );

  }

}
