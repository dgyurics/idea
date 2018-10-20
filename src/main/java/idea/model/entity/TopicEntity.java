package idea.model.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.hibernate.annotations.CreationTimestamp;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter @Setter
@Entity(name = "topic")
public class TopicEntity {
  @Id
  @GeneratedValue
  private Long id;
  @Column(nullable = false)
  private Long userId;
  @Column(nullable = false)
  private String name;
  private String backgroundImageUrl;
  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  private Date createTimestamp;
  private Date lastUpdateTimestamp;
}
