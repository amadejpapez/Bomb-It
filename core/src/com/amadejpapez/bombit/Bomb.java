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
                List.of(row, col),
                List.of(row, col + 1),
                List.of(row, col - 1),
                List.of(row + 1, col),
                List.of(row - 1, col)
        );
    }

    public static Bomb getBombByPosition(List<Bomb> bombs, List<Integer> pos) {
        for (Bomb bomb: bombs) {
            if (bomb.position.get(0) == pos.get(0) && bomb.position.get(1) == pos.get(1))
                return bomb;
        }
        return null;
    }
}
