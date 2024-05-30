package nl.groenveldict.gamez.whoamiservice.repository;

import nl.groenveldict.gamez.whoamiservice.domain.Game;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GameRepository extends MongoRepository<Game, String> {
}
