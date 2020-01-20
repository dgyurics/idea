package idea.model.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@Entity(name = "topic")
public class Topic implements Serializable {
  @Id
  @GeneratedValue
  private Long id;

  @Column(nullable = false)
  private Long userId;

  @Column(nullable = false)
  private String name;

  @Column
  private String backgroundImageUrl;

  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  private Date createTimestamp;

  @Column
  @UpdateTimestamp
  private Date lastUpdateTimestamp;
}
