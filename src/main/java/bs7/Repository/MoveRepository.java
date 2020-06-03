package bs7.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import bs7.Entities.Move;


@Service
public interface MoveRepository extends CrudRepository<Move, Integer>{
  
}
