package idea.config;

import java.time.Clock;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
  private final Environment env;

  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }

  @Bean
  public Clock clock() {
    return Clock.systemDefaultZone();
  }

  @Bean
  @ConditionalOnProperty({"spring.redis.host", "spring.redis.password", "spring.redis.port"})
  public JedisConnectionFactory jedisConnectionFactory() {
    final String host = env.getProperty("spring.redis.host");
    final int port = Integer.parseInt(env.getProperty("spring.redis.port"));
    final String password = env.getProperty("spring.redis.password");
    RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(host, port);
    redisStandaloneConfiguration.setPassword(RedisPassword.of(password));
    return new JedisConnectionFactory(redisStandaloneConfiguration);
  }

  @Bean
  @ConditionalOnBean(JedisConnectionFactory.class)
  public CacheManager cacheManager() {
    return RedisCacheManager.create(jedisConnectionFactory());
  }
}
