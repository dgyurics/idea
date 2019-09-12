package idea.model.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * When a password reset is triggered, the {@link User} reset code is stored here.
 */
@ToString
@Getter @Setter
@Builder
@NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "password_reset")
public class Reset {
  @Id
  private String username;

  @OneToOne
  private User user;

  @Column
  private Integer resetCode;

  @Column
  private boolean valid;

  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  private Date createTimestamp;
}
