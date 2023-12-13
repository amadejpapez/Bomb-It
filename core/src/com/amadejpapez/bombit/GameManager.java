package com.amadejpapez.bombit;

import com.amadejpapez.bombit.config.GameConfig;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class GameManager {
    public static final GameManager INSTANCE = new GameManager();

    // ~/.prefs/BombIt
    private final Preferences PREFS;

    public static Integer numPhysicalPlayers = 1;
    public static Boolean addComputerPlayers = GameConfig.COMPUTER_PLAYERS_DEFAULT;

    public static List<String> playerCharacters;

    public static Integer numActiveBombs = 0;

    private static final String INIT_MOVE_KEY = "initMove";
    private CellState initMove = CellState.FLOOR;

    private GameManager() {
        PREFS = Gdx.app.getPreferences(BombIt.class.getSimpleName());
        String moveName = PREFS.getString(INIT_MOVE_KEY, CellState.FLOOR.name());
        initMove = CellState.valueOf(moveName);

        playerCharacters = new ArrayList<>();
        for (int i = 0; i < GameConfig.MAX_NUMBER_PLAYERS; i++)
            playerCharacters.add("");
    }

    public static void generateOtherPLayers() {
        String tmp;
        for (int i = playerCharacters.size(); i < GameConfig.MAX_NUMBER_PLAYERS; i++) {
            do {
                tmp = GameConfig.AVAILABLE_PLAYERS.get(ThreadLocalRandom.current().nextInt(GameConfig.AVAILABLE_PLAYERS.size()));
            } while(playerCharacters.contains(tmp));

            playerCharacters.add(tmp);
        }
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