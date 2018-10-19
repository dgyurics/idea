package idea.model.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
  @Column(nullable = false)
  private Date createTimestamp;
  private Date lastUpdateTimestamp;
}
