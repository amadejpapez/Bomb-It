package com.amadejpapez.bombit;

import com.amadejpapez.bombit.assets.AssetDescriptors;
import com.amadejpapez.bombit.assets.Assets;
import com.amadejpapez.bombit.config.GameConfig;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.TimeUtils;

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

    public static List<Player> playerCharacters;
    public static List<Bomb> activeBombs;

    public static float gameStartedTime;

    private static final String NUM_PHYSICAL_PLAYERS = "num_physical_players";
    private static final String ADD_COMPUTER_PLAYERS = "add_computer_players";
    private static final String MUSIC_ENABLED = "music_enabled";
    private static final String SOUNDS_ENABLED = "sounds_enabled";

    public static final Music startMusic = Assets.assetManager.get(AssetDescriptors.START_MUSIC);
    public static final Music gameMusic = Assets.assetManager.get(AssetDescriptors.GAME_MUSIC);

    private GameManager() {
        PREFS = Gdx.app.getPreferences(BombIt.class.getSimpleName());

        numPhysicalPlayers = PREFS.getInteger(NUM_PHYSICAL_PLAYERS, 1);
        addComputerPlayers = PREFS.getBoolean(ADD_COMPUTER_PLAYERS, false);
        musicEnabled = PREFS.getBoolean(MUSIC_ENABLED, true);
        soundsEnabled = PREFS.getBoolean(SOUNDS_ENABLED, true);

        playerCharacters = new ArrayList<>();
        activeBombs = new ArrayList<>();
    }

    public static void generatePhysicalPlayers() {
        // called after first startup screen is complete and before users selects his characters
        // this acts like a default character if user does not select any
        String tmp;
        for (int i = 0; i < GameManager.numPhysicalPlayers; i++) {
            do {
                tmp = GameConfig.AVAILABLE_PLAYERS.get(ThreadLocalRandom.current().nextInt(GameConfig.AVAILABLE_PLAYERS.size()));
            } while (characterAlreadyInUse(tmp));

            playerCharacters.add(new Player(i, tmp));
        }
    }

    public static void generateOtherPlayers() {
        // called after user selects his physical player characters
        // this is for players that are computers
        String tmp;
        for (int i = playerCharacters.size(); i < GameConfig.MAX_NUMBER_PLAYERS; i++) {
            do {
                tmp = GameConfig.AVAILABLE_PLAYERS.get(ThreadLocalRandom.current().nextInt(GameConfig.AVAILABLE_PLAYERS.size()));
            } while (characterAlreadyInUse(tmp));

            playerCharacters.add(new Player(i, tmp));
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
            GameManager.gameMusic.stop();
            GameManager.startMusic.setLooping(true);
            GameManager.startMusic.play();
        } else {
            GameManager.startMusic.stop();
        }
    }

    public void playGameMusic() {
        if (GameManager.INSTANCE.getMusicEnabled()) {
            GameManager.startMusic.stop();
            GameManager.gameMusic.setLooping(true);
            GameManager.gameMusic.play();
        } else {
            GameManager.gameMusic.stop();
        }
    }

    public static boolean characterAlreadyInUse(String character) {
        for (Player player : playerCharacters) {
            if (player.image == Player.characterImages.get(character))
                return true;
        }
        return false;
    }
}