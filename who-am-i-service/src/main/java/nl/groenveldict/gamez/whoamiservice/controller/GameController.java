package nl.groenveldict.gamez.whoamiservice.controller;

import lombok.RequiredArgsConstructor;
import nl.groenveldict.gamez.whoamiservice.dto.FinishExistingGameRequest;
import nl.groenveldict.gamez.whoamiservice.dto.NewGameRequest;
import nl.groenveldict.gamez.whoamiservice.dto.NewGameResponse;
import nl.groenveldict.gamez.whoamiservice.dto.UpdatingExistingGameRequest;
import nl.groenveldict.gamez.whoamiservice.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NewGameResponse createNewGame(@RequestBody NewGameRequest newGameRequest){
        return gameService.createNewGame(newGameRequest);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateExistingGame(@RequestBody UpdatingExistingGameRequest existingGameRequest) throws GameNotExistsException {
        gameService.addPlayerDetailsToExistingGame(existingGameRequest);
    }

    @PutMapping
    @RequestMapping("/finish")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void finishExistingGame(@RequestBody FinishExistingGameRequest finishExistingGameRequest) throws GameNotExistsException {
        gameService.finishExistingGame(finishExistingGameRequest);
    }
}