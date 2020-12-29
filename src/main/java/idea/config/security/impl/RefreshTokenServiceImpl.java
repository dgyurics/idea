package idea.config.security.impl;

import idea.config.security.CookieConfig;
import idea.config.security.RefreshTokenService;
import idea.model.entity.Refresh;
import idea.model.entity.User;
import idea.repository.RefreshRepository;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
  private final CookieConfig cookieConfig;
  private final RefreshRepository refreshTokenRepository;

  // TODO encrypt refresh token

  @Override
  public void injectToken(User user, HttpServletResponse res) {
    refreshTokenRepository.findByUserUsername(user.getUsername()).ifPresentOrElse(token -> {
      res.addCookie(createCookie(token.getId()));
    }, () -> {
      final Refresh entityToSave = Refresh.builder()
          .user(user)
          .id(UUID.randomUUID()).build();
      final Refresh savedEntity = refreshTokenRepository.save(entityToSave);
      res.addCookie(createCookie(savedEntity.getId()));
    });
  }

  @Override
  public void injectToken(Authentication auth, HttpServletResponse res) {
    refreshTokenRepository.findByUserUsername(auth.getName()).ifPresentOrElse(token -> {
      res.addCookie(createCookie(token.getId()));
    }, () -> {
      final Refresh entityToSave = Refresh.builder()
          .user((User) auth.getPrincipal())
          .id(UUID.randomUUID()).build();
      final Refresh savedEntity = refreshTokenRepository.save(entityToSave);
      res.addCookie(createCookie(savedEntity.getId()));
    });
  }

  @Override
  public void expireToken(HttpServletRequest req, HttpServletResponse res) {
    Cookie cookie = WebUtils.getCookie(req, cookieConfig.getName());
    if(cookie != null) {
      refreshTokenRepository.deleteById(UUID.fromString(cookie.getValue()));
      cookie.setMaxAge(-1);
      res.addCookie(cookie);
    }
  }

  @Override
  public Optional<Refresh> getToken(UUID userId) {
    return refreshTokenRepository.findById(userId);
  }

  private Cookie createCookie(UUID token) {
    final Cookie cookie = new Cookie(cookieConfig.getName(), token.toString());
    cookie.setHttpOnly(cookieConfig.isHttpOnly());
    cookie.setSecure(cookieConfig.isSecure());
    cookie.setMaxAge(Math.toIntExact(TimeUnit.DAYS.toSeconds(cookieConfig.getMaxAgeInDays())));
    cookie.setPath(cookieConfig.getPath());
    cookie.setDomain(cookieConfig.getDomain());
    return cookie;
  }
}
