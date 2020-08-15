package idea.model.entity;

import idea.model.Category;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@Entity(name = "product")
public class Product {
  @Id
  @GeneratedValue
  private Long id;

  @Enumerated(EnumType.STRING)
  private Category category;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String description;

  @Column(nullable = false)
  private String imageUri;

  @Column
  private String imageAlt;

  @Column
  private String imageStyle;

  @Column
  @CreationTimestamp
  private Date createTimestamp;

  @Column
  @UpdateTimestamp
  private Date lastUpdateTimestamp;

  @PrePersist
  void preInsert() {
    if (this.category == null)
      this.category = Category.MISC;
  }
}
