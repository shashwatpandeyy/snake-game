package com.game.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Objects;


/**
 * This class handles game service related API requests;
 */
@Service
public class GameService {
    private static final Integer HUNDRED = 100; //Game final position
    Integer player1Position = 0;
    Integer player2Position = 0;
    @Autowired
    PropService propService;

    public boolean isPlayer1Won() {
        return Objects.equals(player1Position, HUNDRED);
    }

    public boolean isPlayer2Won() {
        return Objects.equals(player2Position, HUNDRED);
    }
    public int updatePlayer1Position(String diceCount) throws Exception {

        int diceInt = verifyDiceCount(diceCount);

        player1Position = updatePlayerPosition(player1Position, diceInt);

        return player1Position;
    }

    public int updatePlayer2Position(String diceCount) throws Exception {
        int diceInt = verifyDiceCount(diceCount);

        player2Position = updatePlayerPosition(player2Position, diceInt);

        return player2Position;
    }

    private int updatePlayerPosition(int playerPosition, int diceInt) {

        if (Objects.equals(playerPosition, HUNDRED) || playerPosition + diceInt > 100) return playerPosition;
        playerPosition += diceInt;

        /*Integer laddersTop = propService.getLadders().get(playerPosition);
        if (laddersTop != null) {
            playerPosition = laddersTop;
        }

        Integer snakesTail = propService.getSnakes().get(playerPosition);
        if (snakesTail != null) {
            playerPosition = snakesTail;
        }*/

        HashMap<Integer, Integer> props = new HashMap<>(propService.getLadders());
        props.putAll(propService.getSnakes());

        playerPosition = getFinalPosition(props, playerPosition, playerPosition);

        return playerPosition;
    }

    private int getFinalPosition(HashMap<Integer, Integer> props, Integer playerPrePosition, Integer playerPosition) {

        while (playerPosition != null) {
            playerPrePosition = playerPosition;
            playerPosition = props.get(playerPosition);
        }
        return playerPrePosition;
    }

    private int verifyDiceCount(String diceCount) throws Exception{
        int diceInt = -1;
        try {
            diceInt = Integer.parseInt(diceCount);
            if (diceInt>6 || diceInt<1) throw new Exception("Wrong Number, dice roll should give 1, 2, 3, 4, 5, 6 !");
        } catch (NumberFormatException ex) {
            throw new NumberFormatException("We could not find the number!");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return diceInt;
    }
}
