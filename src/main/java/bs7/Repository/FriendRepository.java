package bs7.Repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import bs7.Entities.Friend;
import bs7.Entities.User;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete
@Service
public interface FriendRepository extends CrudRepository<Friend, Integer> {
  Friend findOneByUser1_IdAndUser2_Id(Integer user1, Integer user2);
  List<Friend> findAllByConfirmedFalseAndUser2_Id(Integer user2);
  List<Friend> findAllByConfirmedFalseAndUser1_Id(Integer user2);
}