package idea.filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Filter to remove credentials from http login request URL. 
 * Use 'Content-Type': 'application/x-www-form-urlencoded' instead
 * https://stackoverflow.com/questions/38716703/how-do-i-disable-resolving-login-parameters-passed-as-url-parameters-from-the
 *
 */
@Component
@Order(1)
public class DisableAuthCredentialsUrl extends OncePerRequestFilter {
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    filterChain.doFilter(
        new HttpServletRequestWrapper((HttpServletRequest) request) {
            @Override
            public String getParameter(String name) {
                if (("login".equals(name) && getQueryString().contains("login"))
                        || ("password".equals(name) && getQueryString().contains("password"))) {
                    return null;
                } else {
                    return super.getParameter(name);
                }
            }
        },response
      );
  }
}
