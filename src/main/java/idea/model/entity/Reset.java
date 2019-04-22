package idea.model.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * When a password reset is trigger, the {@link User} reset code is stored here.
 */
@ToString
@Getter @Setter
@Entity
@Table(name = "password_reset")
public class Reset {
  @Id
  @Column(name = "email")
  private String email;

  @OneToOne
  private User user;

  @Column
  private Integer resetCode;

  @Column
  private boolean valid;

  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  private Date createTimestamp;

  public Reset() {}

  public Reset(String email, Integer resetCode, boolean valid) {
    this.email = email;
    this.resetCode = resetCode;
    this.valid = valid;
  }
}
