package idea.config.security.impl;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import idea.model.entity.User;
import idea.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
  private final UserRepository repository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> optionalUsers = repository.findByUsername(username);
    if (!optionalUsers.isPresent()) {
      throw new UsernameNotFoundException(username + " not found");
    }
    return optionalUsers.map(UserDetailsImpl::new).get();
  }
}
