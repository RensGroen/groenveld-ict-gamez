package nl.groenveldict.gamez.whoamiservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Game Not Found")
public class GameNotExistsException extends Exception {
    public GameNotExistsException(String message){
        super(message);
    }
}