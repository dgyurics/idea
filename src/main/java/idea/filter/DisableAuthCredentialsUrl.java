package idea.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Filter to remove credentials from http login request URL. 
 * Use 'Content-Type': 'application/x-www-form-urlencoded' instead
 * https://stackoverflow.com/questions/38716703/how-do-i-disable-resolving-login-parameters-passed-as-url-parameters-from-the
 *
 */
@Component
@Order(1)
public class DisableAuthCredentialsUrl implements Filter {

  @Override
  public void destroy() {
    // TODO Auto-generated method stub
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
      throws IOException, ServletException {
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

  @Override
  public void init(FilterConfig arg0) throws ServletException {
    // TODO Auto-generated method stub
    
  }
}
