package nl.groenveldict.gamez.whoamiservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.groenveldict.gamez.whoamiservice.controller.GameNotExistsException;
import nl.groenveldict.gamez.whoamiservice.domain.Game;
import nl.groenveldict.gamez.whoamiservice.domain.PlayerDetails;
import nl.groenveldict.gamez.whoamiservice.dto.NewGameRequest;
import nl.groenveldict.gamez.whoamiservice.dto.NewGameResponse;
import nl.groenveldict.gamez.whoamiservice.dto.UpdatingExistingGameRequest;
import nl.groenveldict.gamez.whoamiservice.repository.GameRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

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
        log.info("Game with id {} is saved.", game.getVersion());

        return NewGameResponse.builder()
                .id(game.getId())
                .playerDetails(WhoGetsWhoRandomizer.randomizeWhoGetsWho(newGameRequest.getUserIdsPlaying()))
                .build();
    }

    public void updateExistingGame(UpdatingExistingGameRequest existingGameRequest) throws GameNotExistsException {
        Optional<Game> game = gameRepository.findById(existingGameRequest.getId());

        if (game.isPresent()){
            game.get().setPlayerDetails(existingGameRequest.getPlayerDetails().stream().map(pd -> {
                return PlayerDetails.builder().userId(pd.getUserId())
                        .opponentId(pd.getOpponentId())
                        .person(pd.getPerson())
                        .build();
            }).collect(Collectors.toList()));
            gameRepository.save(game.get());
            log.info("Game with id {} is updated.", game.get().getId());
        } else {
            log.info("Game with id {} not found in database.", existingGameRequest.getId());
            throw new GameNotExistsException(String.format("Game with id %s not found.", existingGameRequest.getId()));
        }
    }
}