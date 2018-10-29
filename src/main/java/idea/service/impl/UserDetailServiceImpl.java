package idea.service.impl;

import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import idea.config.security.UserDetailsImpl;
import idea.model.entity.User;
import idea.repository.UserRepository;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
  private UserRepository repository;

  public UserDetailServiceImpl(UserRepository usersRepository) {
    this.repository = usersRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> optionalUsers = repository.findByUsername(username);
    return optionalUsers.map(UserDetailsImpl::new).get();
  }
}
