package bs7.Repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import bs7.Entities.User;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete
@Service
public interface UserRepository extends CrudRepository<User, Integer> {
  List<User> findAllByUsernameContains(String username);
  List<User> findByUsername(String username);
  User findOneByUsernameAndPassword(String username, String password);
}