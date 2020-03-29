package idea.config.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

/*
 * Filter is only run when Authorization header exists
 * See WebSecurityConfig.class
*/
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements Filter {
  private final JwtTokenService jwtTokenService;

  private String getJwtFromRequest(HttpServletRequest request) {
    final String bearerToken = request.getHeader("Authorization");
    return StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ") ? bearerToken.substring(7) : null;
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    final HttpServletResponse resp = (HttpServletResponse) response;
    final HttpServletRequest req = (HttpServletRequest) request;
    final String jwt = getJwtFromRequest(req);

    // workaround for skipping filter when attempting to refresh token
    // while having an expired jwt
    if(((HttpServletRequest) request).getRequestURI().contains("refresh")) {
      chain.doFilter(request, resp);
    }

    try {
      if (StringUtils.hasText(jwt)) {
        jwtTokenService.validateToken(jwt);
        Authentication authentication = jwtTokenService.getAuthentication(jwt);
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    } catch(RuntimeException ex) {
      SecurityContextHolder.clearContext();
      // exception advice/controller not available at this stage
      resp.sendError(401, ex.getMessage());
    }
    chain.doFilter(request, resp);
  }
}
