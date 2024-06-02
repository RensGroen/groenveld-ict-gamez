package nl.groenveldict.gamez.whoamiservice.service;

import nl.groenveldict.gamez.whoamiservice.controller.GameNotExistsException;
import nl.groenveldict.gamez.whoamiservice.domain.Game;
import nl.groenveldict.gamez.whoamiservice.domain.PlayerDetails;
import nl.groenveldict.gamez.whoamiservice.dto.NewGameRequest;
import nl.groenveldict.gamez.whoamiservice.dto.NewGameResponse;
import nl.groenveldict.gamez.whoamiservice.dto.PlayerDetailsRequest;
import nl.groenveldict.gamez.whoamiservice.dto.UpdatingExistingGameRequest;
import nl.groenveldict.gamez.whoamiservice.repository.GameRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class GameServiceTest {

    @Mock
    private GameRepository gameRepositoryMock;

    @Captor
    ArgumentCaptor<Game> gameCaptor;

    @InjectMocks
    private GameService gameService;

    @Test
    public void gameServiceShouldCreateNewGameAndSaveItToDatabaseCorrectly(){
        //given
        NewGameRequest ngr = NewGameRequest.builder()
                .userIdsPlaying(Arrays.asList(1, 2))
                .build();

        //when
        gameService.createNewGame(ngr);

        //then
        verify(gameRepositoryMock).save(gameCaptor.capture());
        Game savedGame = gameCaptor.getValue();
        Assertions.assertFalse(savedGame.isEnded());
        Assertions.assertTrue(savedGame.getUserIdsPlaying().contains(1));
        Assertions.assertTrue(savedGame.getUserIdsPlaying().contains(2));
        Assertions.assertEquals(2, savedGame.getUserIdsPlaying().size());
    }

    @Test
    public void gameServiceShouldCreateNewGameResponseCorrectly(){
        //given
        NewGameRequest ngr = NewGameRequest.builder()
                .userIdsPlaying(Arrays.asList(1, 2))
                .build();

        //when
        NewGameResponse response = gameService.createNewGame(ngr);

        //then
        Assertions.assertNotNull(response.getPlayerDetails());
        Assertions.assertEquals(2, response.getPlayerDetails().size());
    }

    @Test
    public void gameServiceShouldAddPlayerDetailsToExistingGameCorrectly() throws GameNotExistsException {
        //given
        UpdatingExistingGameRequest gameRequest = UpdatingExistingGameRequest.builder()
                .id("anyId")
                .playerDetails(Arrays.asList(
                        PlayerDetailsRequest.builder()
                                .userId(1)
                                .opponentId(2)
                                .person("King Kong")
                                .build(),
                        PlayerDetailsRequest.builder()
                                .userId(2)
                                .opponentId(1)
                                .person("Linus Torvalds")
                                .build()
                )).build();

        Game game = Game.builder()
                        .id("anyId")
                                .playerDetails(Arrays.asList(
                                        PlayerDetails.builder()
                                                .userId(1)
                                                .opponentId(2)
                                                .person("King Kong")
                                                .build(),
                                        PlayerDetails.builder()
                                                .userId(2)
                                                .opponentId(1)
                                                .person("Linus Torvalds")
                                                .build()
                                )).build();

        when(gameRepositoryMock.findById("anyId")).thenReturn(Optional.of(game));

        //when
        gameService.addPlayerDetailsToExistingGame(gameRequest);

        //then
        verify(gameRepositoryMock).save(gameCaptor.capture());
        Game savedGame = gameCaptor.getValue();
        Assertions.assertEquals(1, savedGame.getPlayerDetails().getFirst().getUserId());
        Assertions.assertEquals(2, savedGame.getPlayerDetails().getFirst().getOpponentId());
        Assertions.assertEquals("King Kong", savedGame.getPlayerDetails().getFirst().getPerson());

        Assertions.assertEquals(2, savedGame.getPlayerDetails().getLast().getUserId());
        Assertions.assertEquals(1, savedGame.getPlayerDetails().getLast().getOpponentId());
        Assertions.assertEquals("Linus Torvalds", savedGame.getPlayerDetails().getLast().getPerson());
    }

    @Test()
    public void gameServiceShouldThrowCorrectExceptionWhenGameCannotBeFoundUponAddingUserDetails() {
        //given
        UpdatingExistingGameRequest gameRequest = UpdatingExistingGameRequest.builder()
                .id("anyId")
                .playerDetails(Arrays.asList(
                        PlayerDetailsRequest.builder()
                                .userId(1)
                                .opponentId(2)
                                .person("King Kong")
                                .build(),
                        PlayerDetailsRequest.builder()
                                .userId(2)
                                .opponentId(1)
                                .person("Linus Torvalds")
                                .build()
                )).build();

        when(gameRepositoryMock.findById("anyId")).thenReturn(Optional.empty());

        //then
        GameNotExistsException exception = assertThrows(GameNotExistsException.class, () -> {
            gameService.addPlayerDetailsToExistingGame(gameRequest);
        });

        Assertions.assertEquals("Game with id anyId not found.", exception.getMessage());
    }
}