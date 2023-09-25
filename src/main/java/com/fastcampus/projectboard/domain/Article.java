package com.fastcampus.projectboard.domain;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@Table(indexes = {
    @Index(columnList = "title"),
    @Index(columnList = "hashtag"),
    @Index(columnList = "createdAt"),
    @Index(columnList = "createdBy")
})
@Entity
public class Article extends AuditingFields {

  @ToString.Exclude
  @OrderBy("createdAt DESC")
  @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
  private final Set<ArticleComment> articleComments = new LinkedHashSet<>();
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;

  @Setter
  @ManyToOne(optional = false)
  @JoinColumn(name = "userId")
  private UserAccount userAccount;//유저정보(ID)

  @Setter @Column(nullable = false) private String title; // 제목
  @Setter @Column(nullable = false, length = 10000) private String content; // 본문
  @Setter private String hashtag; // 해시태그

  protected Article() {
  }

  private Article(UserAccount userAccount, String title, String content, String hashtag) {
    this.userAccount = userAccount;
    this.title = title;
    this.content = content;
    this.hashtag = hashtag;
  }

  public static Article of(UserAccount userAccount, String title, String content, String hashtag) {
    return new Article(userAccount, title, content, hashtag);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    //데이터베이스 영속화 되지 않았다면 같은 개체로 보지 않는다는 처리.
    if (!(obj instanceof Article article)) {
      return false;
    }
    return id != null && id.equals(article.id);
  }

}
