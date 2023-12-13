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

    private static Integer numPhysicalPlayers;
    private static Boolean addComputerPlayers;

    public static List<String> playerCharacters;

    public static Integer numActiveBombs = 0;

    private static final String NUM_PHYSICAL_PLAYERS = "num_physical_players";
    private static final String ADD_COMPUTER_PLAYERS = "add_computer_players";

    private GameManager() {
        PREFS = Gdx.app.getPreferences(BombIt.class.getSimpleName());

        numPhysicalPlayers = PREFS.getInteger(NUM_PHYSICAL_PLAYERS, 1);
        addComputerPlayers = PREFS.getBoolean(ADD_COMPUTER_PLAYERS, false);

        playerCharacters = new ArrayList<>();
    }

    public static void generatePhysicalPlayers() {
        // called after first startup screen is complete and before users selects his characters
        // this acts like a default character if user does not select any
        String tmp;
        for (int i = 0; i < GameManager.numPhysicalPlayers; i++) {
            do {
                tmp = GameConfig.AVAILABLE_PLAYERS.get(ThreadLocalRandom.current().nextInt(GameConfig.AVAILABLE_PLAYERS.size()));
            } while(playerCharacters.contains(tmp));

            playerCharacters.add(tmp);
        }
    }

    public static void generateOtherPlayers() {
        // called after user selects his physical player characters
        // this is for players that are computers
        String tmp;
        for (int i = playerCharacters.size(); i < GameConfig.MAX_NUMBER_PLAYERS; i++) {
            do {
                tmp = GameConfig.AVAILABLE_PLAYERS.get(ThreadLocalRandom.current().nextInt(GameConfig.AVAILABLE_PLAYERS.size()));
            } while(playerCharacters.contains(tmp));

            playerCharacters.add(tmp);
        }
    }

    public int getNumPhysicalPlayers() {
        return numPhysicalPlayers;
    }

    public boolean getAddComputerPlayers() {
        return addComputerPlayers;
    }

    public void setNumPhysicalPlayers(Integer num) {
        numPhysicalPlayers = num;
        PREFS.putInteger(NUM_PHYSICAL_PLAYERS, num);
        PREFS.flush();
    }

    public void setAddComputerPlayers(Boolean arg) {
        addComputerPlayers = arg;
        PREFS.putBoolean(ADD_COMPUTER_PLAYERS, arg);
        PREFS.flush();
    }
}