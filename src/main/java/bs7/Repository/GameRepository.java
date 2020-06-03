package bs7.Repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import bs7.Entities.Game;

@Service
public interface GameRepository extends CrudRepository<Game, Integer> {
  List<Game> findAllByCompletedTrueAndPlayer1_id(Integer id);
  List<Game> findAllByCompletedTrueAndPlayer2_id(Integer id);
}
