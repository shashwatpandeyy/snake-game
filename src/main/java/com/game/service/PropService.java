package com.game.service;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * This service class can help to set up props;
 */
@Service
public class PropService implements ApplicationListener<ApplicationReadyEvent> {

    Map<Integer, Integer> ladders = new HashMap<>();
    Map<Integer, Integer> snakes = new HashMap<>();

    /**
     * In this constructor set ladders and snakes.
     * It will be validated at start of application.
     *
     * Note: it is assumed that number of props won't be more than 10000 (this assumption is to validate infinite loop)
     * @param ladders
     * @param snakes
     */
    public PropService(Map<Integer, Integer> ladders, Map<Integer, Integer> snakes) {
        //set ladders as put(bottom, top):
        ladders.put(10, 95);
        ladders.put(3, 32);
        ladders.put(5, 90);
        ladders.put(31, 98);

        //set snakes as put(head, tail):
        snakes.put(99, 33);
        snakes.put(33, 9);
        snakes.put(91, 31);

        this.ladders = ladders;
        this.snakes = snakes;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        validateLadder();
        validateSnakes();
        validateNoInfiniteLoop();
    }

    private void validateSnakes() {
        for (HashMap.Entry<Integer, Integer> entry : this.snakes.entrySet()) {
            if (entry.getKey() <= entry.getValue()) {
                throw new RuntimeException("Invalid values for snakes");
            }
        }
    }

    private void validateLadder() {
        for (HashMap.Entry<Integer, Integer> entry : this.ladders.entrySet()) {
            if (entry.getKey() >= entry.getValue()) {
                throw new RuntimeException("Invalid values for ladders");
            }
        }
    }

    private void validateNoInfiniteLoop() {
        HashMap<Integer, Integer> props = new HashMap<>(getLadders());
        props.putAll(getSnakes());

        int count = 0;
        for(Integer key : props.keySet()) {
            while (key != null) {
                key = props.get(key);
                if (count++ == 10000) {
                    throw new RuntimeException("Loop detected at head/bottom:" + key + ", please update value of prop");
                }
            }
        }
    }

    public Map<Integer, Integer> getLadders() {
        return ladders;
    }

    public void setLadders(Map<Integer, Integer> ladders) {
        this.ladders = ladders;
    }

    public Map<Integer, Integer> getSnakes() {
        return snakes;
    }

    public void setSnakes(Map<Integer, Integer> snakes) {
        this.snakes = snakes;
    }

    @Override
    public String toString() {
        return "PropService{" +
                "ladders=" + ladders +
                ", snakes=" + snakes +
                '}';
    }

}
