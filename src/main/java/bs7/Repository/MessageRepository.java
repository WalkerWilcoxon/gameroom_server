package bs7.Repository;

import org.springframework.stereotype.Service;

import bs7.Entities.DMessage;
import bs7.Entities.User;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

@Service
public interface MessageRepository extends CrudRepository<DMessage, Integer>{

  List<DMessage> findAllByFromAndTo(User from,User to);
}
