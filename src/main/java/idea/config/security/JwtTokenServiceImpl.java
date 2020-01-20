package idea.config.security;

import idea.model.entity.Refresh;
import idea.model.entity.User;
import idea.repository.RefreshRepository;
import idea.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.Clock;
import java.util.Optional;
import java.util.UUID;
import javax.ws.rs.WebApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenServiceImpl implements JwtTokenService {
  private static final Logger logger = LoggerFactory.getLogger(JwtTokenService.class);
  private final String jwtSecret;
  private final int jwtExpirationInSec;
  private final UserDetailsService userDetailsService;
  private final UserRepository userRepository;
  private final RefreshRepository refreshTokenRepository;
  private final Clock clock;

  public JwtTokenServiceImpl(@Value("${jwt.secret}") String jwtSecret,
      @Value("${jwt.expiration-in-seconds}") int jwtExpirationInSec,
      UserDetailsService userDetailsService, UserRepository userRepository,
      RefreshRepository refreshTokenRepository, Clock clock) {
    this.jwtSecret = jwtSecret;
    this.jwtExpirationInSec = jwtExpirationInSec;
    this.userDetailsService = userDetailsService;
    this.userRepository = userRepository;
    this.refreshTokenRepository = refreshTokenRepository;
    this.clock = clock;
  }

  @Override
  public String generateToken(Authentication authentication) {
    final UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    final long timeInSeconds = clock.millis() / 1000;
    return Jwts.builder()
        .claim(CLAIM_ID, userDetails.getId())
        .claim(CLAIM_USERNAME, userDetails.getUsername())
        .claim(CLAIM_ADMIN, userDetails.getRole().equalsIgnoreCase("admin"))
        .claim(Claims.ISSUED_AT, timeInSeconds)
        .claim(Claims.EXPIRATION, timeInSeconds + jwtExpirationInSec)
        .setIssuer(CLAIM_ISSUER)
        .signWith(SignatureAlgorithm.HS512, jwtSecret)
        .compact();
  }

  @Override
  public String generateToken(User user) {
    final long timeInSeconds = clock.millis() / 1000;
    return Jwts.builder()
        .claim(CLAIM_ID, user.getId())
        .claim(CLAIM_USERNAME, user.getUsername())
        .claim(CLAIM_ADMIN, false)
        .setIssuer(CLAIM_ISSUER)
        .claim(Claims.ISSUED_AT, timeInSeconds)
        .claim(Claims.EXPIRATION, timeInSeconds + jwtExpirationInSec)
        .signWith(SignatureAlgorithm.HS512, jwtSecret)
        .compact();
  }

  @Override
  public String generateToken(UUID refreshToken) {
    final User user = refreshTokenRepository.findById(refreshToken)
        .orElseThrow(() -> new WebApplicationException("Invalid refresh token " + refreshToken, 403))
        .getUser();
    return generateToken(user);
  }

  @Override
  public UUID generateRefreshToken(String username) {
    final Optional<Refresh> entry = refreshTokenRepository.findByUserUsername(username);
    if(entry.isPresent()) return entry.get().getId();
    final User usr = userRepository.findByUsername(username).orElseThrow(() -> new IllegalStateException("Unknown user " + username));
    return refreshTokenRepository.save(Refresh.builder()
        .user(usr)
        .build()).getId();
  }

  @Override
  public void invalidateRefreshToken(String username) {
    refreshTokenRepository.findByUserUsername(username).ifPresent(refreshTokenRepository::delete);
  }

  @Override
  public Authentication getAuthentication(String token) {
    final UserDetails userDetails = userDetailsService.loadUserByUsername(getUserNameFromToken(token));
    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }

  private String getUserNameFromToken(String token) {
    final Claims claims = Jwts.parser()
        .setSigningKey(jwtSecret)
        .parseClaimsJws(token)
        .getBody();
    return String.valueOf(claims.get("username"));
  }

  @Override
  public void validateToken(String token) throws RuntimeException {
    Jwts.parser()
        .setSigningKey(jwtSecret)
        .requireIssuer(CLAIM_ISSUER)
        .parseClaimsJws(token);
  }
}
