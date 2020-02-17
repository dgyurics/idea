package idea.config.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private final JwtTokenService jwtTokenService;

  @Override
  public void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    final String jwt = getJwtFromRequest(request);
    try {
      if (StringUtils.hasText(jwt)) {
        jwtTokenService.validateToken(jwt);
        Authentication authentication = jwtTokenService.getAuthentication(jwt);
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    } catch(RuntimeException ex) {
      SecurityContextHolder.clearContext();
      // exception advice/controller not available at this stage
      response.sendError(401, ex.getMessage());
    }
    filterChain.doFilter(request, response);
  }

  private String getJwtFromRequest(HttpServletRequest request) {
    final String bearerToken = request.getHeader("Authorization");
    return StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ") ? bearerToken.substring(7) : null;
  }
}
