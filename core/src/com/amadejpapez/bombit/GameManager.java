package com.amadejpapez.bombit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class GameManager {
    public static final GameManager INSTANCE = new GameManager();

    // ~/.prefs/BombIt
    private final Preferences PREFS;

    public static Integer numActiveBombs = 0;

    private static final String INIT_MOVE_KEY = "initMove";
    private CellState initMove = CellState.FLOOR;

    private GameManager() {
        PREFS = Gdx.app.getPreferences(BombIt.class.getSimpleName());
        String moveName = PREFS.getString(INIT_MOVE_KEY, CellState.FLOOR.name());
        initMove = CellState.valueOf(moveName);
    }

    public CellState getInitMove() {
        return initMove;
    }

    public void setInitMove(CellState move) {
        initMove = move;

        PREFS.putString(INIT_MOVE_KEY, move.name());
        PREFS.flush();
    }
}