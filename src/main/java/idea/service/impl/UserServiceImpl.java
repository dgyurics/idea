package idea.service.impl;

import java.util.Collection;
import idea.model.entity.UserEntity;
import idea.model.request.UserRequestModel;
import idea.service.UserService;

public class UserServiceImpl implements UserService {

  @Override
  public UserEntity createNewUser(UserRequestModel user) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void resetPassword(long id) {
    // TODO Auto-generated method stub
  }

  @Override
  public void delete(long id) {
    // TODO Auto-generated method stub
  }

  @Override
  public UserEntity findById(long id) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public UserEntity findByUsername(String username) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Collection<UserEntity> findAll() {
    // TODO Auto-generated method stub
    return null;
  }
}
