package com.amadejpapez.bombit;

import com.amadejpapez.bombit.assets.Assets;
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
    private static Boolean musicEnabled;
    private static Boolean soundsEnabled;
    private static String gameMode;

    public static List<Player> players;
    public static List<Bomb> activeBombs;

    public static float gameStartedTime;

    private static final String NUM_PHYSICAL_PLAYERS = "num_physical_players";
    private static final String ADD_COMPUTER_PLAYERS = "add_computer_players";
    private static final String MUSIC_ENABLED = "music_enabled";
    private static final String SOUNDS_ENABLED = "sounds_enabled";
    private static final String GAME_MODE = "game_mode";

    public static Boolean gameEnded = false;

    public static Float aiSpeed;

    private GameManager() {
        PREFS = Gdx.app.getPreferences(BombIt.class.getSimpleName());

        numPhysicalPlayers = PREFS.getInteger(NUM_PHYSICAL_PLAYERS, 1);
        addComputerPlayers = PREFS.getBoolean(ADD_COMPUTER_PLAYERS, false);
        musicEnabled = PREFS.getBoolean(MUSIC_ENABLED, true);
        soundsEnabled = PREFS.getBoolean(SOUNDS_ENABLED, true);

        setGameMode(PREFS.getString(GAME_MODE, "ARCADE"));

        players = new ArrayList<>();
        activeBombs = new ArrayList<>();
    }

    public static void generatePhysicalPlayers() {
        // called after first startup screen is complete and before users selects his characters
        // this acts like a default character if user does not select any
        CellState tmp;
        for (int i = 0; i < GameManager.numPhysicalPlayers; i++) {
            do {
                tmp = CellActor.PLAYERS.get(ThreadLocalRandom.current().nextInt(CellActor.PLAYERS.size()));
            } while (characterAlreadyInUse(tmp));

            players.add(new Player(i, tmp));
        }
    }

    public static void generateOtherPlayers() {
        // called after user selects his physical player characters
        // this is for players that are computers
        CellState tmp;
        for (int i = players.size(); i < GameConfig.MAX_NUMBER_PLAYERS; i++) {
            do {
                tmp = CellActor.PLAYERS.get(ThreadLocalRandom.current().nextInt(CellActor.PLAYERS.size()));
            } while (characterAlreadyInUse(tmp));

            Player newPlayer = new Player(i, tmp);
            newPlayer.username = "Bot";
            players.add(newPlayer);
        }
    }

    public int getNumPhysicalPlayers() {
        return numPhysicalPlayers;
    }

    public boolean getAddComputerPlayers() {
        return addComputerPlayers;
    }

    public boolean getMusicEnabled() {
        return musicEnabled;
    }

    public boolean getSoundsEnabled() {
        return soundsEnabled;
    }

    public String getGameMode() {
        return gameMode;
    }

    public void setNumPhysicalPlayers(Integer num) {
        numPhysicalPlayers = num;
        PREFS.putInteger(NUM_PHYSICAL_PLAYERS, num);
        PREFS.flush();
    }

    public void setAddComputerPlayers(Boolean val) {
        addComputerPlayers = val;
        PREFS.putBoolean(ADD_COMPUTER_PLAYERS, val);
        PREFS.flush();
    }

    public void setGameMode(String gameMode) {
        GameManager.gameMode = gameMode;
        PREFS.putString(GAME_MODE, gameMode);
        PREFS.flush();

        if (gameMode.equals("ARCADE"))
            aiSpeed = GameConfig.AI_SPEED_IN_ARCADE;
        else if (gameMode.equals("TILE_TAG"))
            aiSpeed = GameConfig.AI_SPEED_IN_TILE_TAG;
    }

    public void setMusicEnabled(Boolean val) {
        musicEnabled = val;
        PREFS.putBoolean(MUSIC_ENABLED, val);
        PREFS.flush();
    }

    public void setSoundsEnabled(Boolean val) {
        soundsEnabled = val;
        PREFS.putBoolean(SOUNDS_ENABLED, val);
        PREFS.flush();
    }

    public void playStartMusic() {
        if (GameManager.INSTANCE.getMusicEnabled()) {
            Assets.gameMusic.stop();
            Assets.startMusic.setLooping(true);
            Assets.startMusic.play();
        } else {
            Assets.startMusic.stop();
        }
    }

    public void playGameMusic() {
        if (GameManager.INSTANCE.getMusicEnabled()) {
            Assets.startMusic.stop();
            Assets.gameMusic.setLooping(true);
            Assets.gameMusic.play();
        } else {
            Assets.gameMusic.stop();
        }
    }

    public static boolean characterAlreadyInUse(CellState newCharacter) {
        for (Player player : players) {
            if (player.image == newCharacter)
                return true;
        }
        return false;
    }
}