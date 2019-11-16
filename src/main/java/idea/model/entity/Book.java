package idea.model.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@Entity(name = "book")
public class Book {
  @Id
  @GeneratedValue
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String alt;

  private String author;

  @Column(nullable = false)
  private String src;

  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  private Date createTimestamp;

  @Column
  @UpdateTimestamp
  private Date lastUpdateTimestamp;
}
