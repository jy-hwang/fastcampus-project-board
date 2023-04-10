package com.fastcampus.projectboard.domain;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@Table(indexes  = {
    @Index(columnList = "title"),
    @Index(columnList = "hashtag"),
    @Index(columnList = "createdAt"),
    @Index(columnList = "createdBy")
})
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Article {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @Setter @Column(nullable = false) private String title; // 제목
  @Setter @Column(nullable = false, length = 10000) private String content; // 본문
  
  @Setter  private String hashtag; // 해시태그

  @ToString.Exclude
  @OrderBy("id")
  @OneToMany(mappedBy = "article",cascade = CascadeType.ALL)
  private final Set<ArticleComment> articleComment = new LinkedHashSet<>();
  
  @CreatedDate  @Column(nullable = false) private LocalDateTime createdAt; // 생성일시
  @CreatedBy  @Column(nullable = false, length = 100) private String createdBy; // 생성자
  @LastModifiedDate  @Column(nullable = false) private LocalDateTime modifiedAt; // 수정일시
  @LastModifiedBy @Column(nullable = false,length = 100) private String modifiedBy; // 수정자

  protected Article() {}

  private Article(String title, String content, String hashtag) {
    this.title = title;
    this.content = content;
    this.hashtag = hashtag;
  }
  
  public static Article of(String title, String content, String hashtag) {
    return new Article(title, content, hashtag);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    //데이터베이스 영속화 되지 않았다면 같은 개체로 보지 않는다는 처리.
    /*
     * if (obj == null) return false; if (getClass() != obj.getClass()) return false; Article other
     * = (Article) obj;
     */
    // pattern variable
    if(!(obj instanceof Article article)) return false;
    return id != null && id.equals(article.id);
  }

  
  
}
