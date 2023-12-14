package com.amadejpapez.bombit;

import java.util.List;

public class Bomb {
    public Integer createdByPlayer;
    public List<Integer> position;
    public Float timeCreated;

    public Bomb(int createdByPlayer, List<Integer> position, float timeCreated) {
        this.createdByPlayer = createdByPlayer;
        this.position = position;
        this.timeCreated = timeCreated;
    }

    public static List<List<Integer>> getPositionToCheck(int row, int col) {
        return List.of(
                List.of(row, col + 1),
                List.of(row, col - 1),
                List.of(row + 1, col),
                List.of(row - 1, col)
        );
    }
}
