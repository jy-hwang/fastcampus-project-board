package com.fastcampus.projectboard.domain;

import java.util.Collection;
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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnTransformer;

@Getter
@ToString(callSuper = true)
@Table(indexes = {
    @Index(columnList = "title"),
    @Index(columnList = "createdAt"),
    @Index(columnList = "createdBy")
})
@Entity
public class Article extends AuditingFields {

  @Setter @Column(nullable = false) private String title; // 제목

  @Setter @Column(nullable = false, length = 10000) @Size(max=10000)
  @ColumnTransformer(read = "board.pdbDec('normal',content,'',0)", write = "board.pdbEnc('normal',?,'')" )
  private String content; // 본문

  @ToString.Exclude
  @JoinTable(
      name = "article_hashtag",
      joinColumns = @JoinColumn(name = "articleId"),
      inverseJoinColumns = @JoinColumn(name = "hashtagId")
  )
  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private Set<Hashtag> hashtags = new LinkedHashSet<>();

  @ToString.Exclude
  @OrderBy("createdAt DESC")
  @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
  private final Set<ArticleComment> articleComments = new LinkedHashSet<>();
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;

  @Setter
  @ManyToOne(optional = false)
  @JoinColumn(name = "userId")
  private UserAccount userAccount;//유저정보(ID)

  protected Article() {
  }

  private Article(UserAccount userAccount, String title, String content) {
    this.userAccount = userAccount;
    this.title = title;
    this.content = content;
  }

  public static Article of(UserAccount userAccount, String title, String content) {
    return new Article(userAccount, title, content);
  }

  public void addHashtag(Hashtag hashtag) {
    this.getHashtags().add(hashtag);
  }

  public void addHashtags(Collection<Hashtag> hashtags) {
    this.getHashtags().addAll(hashtags);
  }

  public void clearHashtags() {
    this.getHashtags().clear();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    //데이터베이스 영속화 되지 않았다면 같은 개체로 보지 않는다는 처리.
    if (!(obj instanceof Article that)) {
      return false;
    }
    return this.getId() != null && this.getId().equals(that.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.getId());
  }

}
