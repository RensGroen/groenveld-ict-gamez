package nl.groenveldict.gamez.whoamiservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.groenveldict.gamez.whoamiservice.domain.Game;
import nl.groenveldict.gamez.whoamiservice.dto.NewGameRequest;
import nl.groenveldict.gamez.whoamiservice.dto.NewGameResponse;
import nl.groenveldict.gamez.whoamiservice.repository.GameRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameService {

    private final GameRepository gameRepository;

    public NewGameResponse createNewGame(NewGameRequest newGameRequest){
        Game game = Game.builder()
                .ended(false)
                .userIdsPlaying(newGameRequest.getUserIdsPlaying())
                .userIdLost(0)
                .build();

        gameRepository.save(game);
        log.info("Game with id {} is saved", game.getVersion());

        return NewGameResponse.builder()
                .id(game.getId())
                .playerDetails(WhoGetsWhoRandomizer.randomizeWhoGetsWho(newGameRequest.getUserIdsPlaying()))
                .build();
    }

}
