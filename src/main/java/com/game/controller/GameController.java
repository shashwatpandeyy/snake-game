package com.game.controller;

import com.game.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameController {

    @Autowired
    GameService gameService;


    @PostMapping("/game/start")
    ResponseEntity<String> prepareGame(@RequestBody String s) {
        if (s.equalsIgnoreCase("START")) {
            gameService.reset();
            return ResponseEntity.ok("READY");
        } else {
            return ResponseEntity.badRequest().body("Give start as payload to restart the game.");
        }
    }

    @PostMapping("/game/player1move")
    ResponseEntity<String> player1Move(@RequestBody String diceCount) {
        int nextPosition = 0;
        try {
            nextPosition = gameService.updatePlayer1Position(diceCount);
            if (gameService.isPlayer1Won()) return ResponseEntity.ok("Player1 wins");
            return ResponseEntity.ok("Player1 Position:" + nextPosition);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping("/game/player2move")
    ResponseEntity<String> player2Move(@RequestBody String diceCount) {
        int nextPosition = 0;
        try {
            nextPosition = gameService.updatePlayer2Position(diceCount);
            if (gameService.isPlayer2Won()) return ResponseEntity.ok("Player2 wins");
            return ResponseEntity.ok("Player2 Position:" + nextPosition);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
