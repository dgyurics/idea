package idea.config.security;

import idea.model.entity.User;
import java.util.UUID;
import org.springframework.security.core.Authentication;

public interface JwtTokenService {
  String CLAIM_ID = "id";
  String CLAIM_USERNAME = "username";
  String CLAIM_ADMIN = "admin";
  String CLAIM_ISSUER = "lagom.life";
  String generateToken(Authentication authentication);
  String generateToken(User user);
  Authentication getAuthentication(String token);
  void validateToken(String token) throws RuntimeException;
}
