package idea.config.security;

import idea.model.entity.User;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;

public interface RefreshTokenService {
  void injectToken(User user, HttpServletResponse res);
  void injectToken(Authentication auth, HttpServletResponse res);
  void expireToken(HttpServletRequest req, HttpServletResponse res);
  User getUser(UUID userId);
}
