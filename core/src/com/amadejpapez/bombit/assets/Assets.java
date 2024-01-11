package com.amadejpapez.bombit.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Assets {
    public static AssetManager assetManager;

    public static BitmapFont font;

    public static Skin skin;

    public static Sound explosionSound;

    public static Music startMusic;
    public static Music gameMusic;

    public static TextureAtlas gameplayAtlas;

    public static TextureRegion background;
    public static TextureRegion backgroundLb;
    public static TextureRegion bomb;
    public static TextureRegion bonusBombs;
    public static TextureRegion bonusHand;
    public static TextureRegion coneBlue;
    public static TextureRegion coneRed;
    public static TextureRegion empty;
    public static TextureRegion floor;
    public static TextureRegion middle;
    public static TextureRegion playerBlack;
    public static TextureRegion playerBlue;
    public static TextureRegion playerGreen;
    public static TextureRegion playerOrange;
    public static TextureRegion playerPink;
    public static TextureRegion playerPurple;
    public static TextureRegion pomPink;
    public static TextureRegion pomYellow;
    public static TextureRegion tileBlack;
    public static TextureRegion tileBlue;
    public static TextureRegion tileGreen;
    public static TextureRegion tileOrange;
    public static TextureRegion tilePink;
    public static TextureRegion tilePurple;
    public static TextureRegion title;
    public static TextureRegion whistle;
    public static TextureRegion youLose;
    public static TextureRegion youWin;
    public static TextureRegion pause;

    public static void load() {
        assetManager = new AssetManager();
        assetManager.load(AssetDescriptors.GAMEPLAY);
        assetManager.load(AssetDescriptors.FONT);
        assetManager.load(AssetDescriptors.SKIN);
        assetManager.load(AssetDescriptors.BOMB_SOUND);
        assetManager.load(AssetDescriptors.POWER_UP_SOUND);
        assetManager.load(AssetDescriptors.GAME_MUSIC);
        assetManager.load(AssetDescriptors.START_MUSIC);
        assetManager.finishLoading();

        font = Assets.assetManager.get(AssetDescriptors.FONT);

        skin = Assets.assetManager.get(AssetDescriptors.SKIN);

        explosionSound = Assets.assetManager.get(AssetDescriptors.BOMB_SOUND);

        startMusic = Assets.assetManager.get(AssetDescriptors.START_MUSIC);
        gameMusic = Assets.assetManager.get(AssetDescriptors.GAME_MUSIC);

        gameplayAtlas = Assets.assetManager.get(AssetDescriptors.GAMEPLAY);

        background = gameplayAtlas.findRegion(AssetRegionNames.BACKGROUND);
        backgroundLb = gameplayAtlas.findRegion(AssetRegionNames.BACKGROUND_LB);
        bomb = gameplayAtlas.findRegion(AssetRegionNames.BOMB);
        bonusBombs = gameplayAtlas.findRegion(AssetRegionNames.BONUS_BOMB);
        bonusHand = gameplayAtlas.findRegion(AssetRegionNames.BONUS_HAND);
        coneBlue = gameplayAtlas.findRegion(AssetRegionNames.CONE_BLUE);
        coneRed = gameplayAtlas.findRegion(AssetRegionNames.CONE_RED);
        empty = gameplayAtlas.findRegion(AssetRegionNames.EMPTY);
        floor = gameplayAtlas.findRegion(AssetRegionNames.FLOOR);
        middle = gameplayAtlas.findRegion(AssetRegionNames.MIDDLE);
        pause = gameplayAtlas.findRegion(AssetRegionNames.PAUSE);
        playerBlack = gameplayAtlas.findRegion(AssetRegionNames.PLAYER_BLACK);
        playerBlue = gameplayAtlas.findRegion(AssetRegionNames.PLAYER_BLUE);
        playerGreen = gameplayAtlas.findRegion(AssetRegionNames.PLAYER_GREEN);
        playerOrange = gameplayAtlas.findRegion(AssetRegionNames.PLAYER_ORANGE);
        playerPink = gameplayAtlas.findRegion(AssetRegionNames.PLAYER_PINK);
        playerPurple = gameplayAtlas.findRegion(AssetRegionNames.PLAYER_PURPLE);
        pomPink = gameplayAtlas.findRegion(AssetRegionNames.POM_PINK);
        pomYellow = gameplayAtlas.findRegion(AssetRegionNames.POM_YELLOW);
        tileBlack = gameplayAtlas.findRegion(AssetRegionNames.TILE_BLACK);
        tileBlue = gameplayAtlas.findRegion(AssetRegionNames.TILE_BLUE);
        tileGreen = gameplayAtlas.findRegion(AssetRegionNames.TILE_GREEN);
        tileOrange = gameplayAtlas.findRegion(AssetRegionNames.TILE_ORANGE);
        tilePink = gameplayAtlas.findRegion(AssetRegionNames.TILE_PINK);
        tilePurple = gameplayAtlas.findRegion(AssetRegionNames.TILE_PURPLE);
        title = gameplayAtlas.findRegion(AssetRegionNames.TITLE);
        whistle = gameplayAtlas.findRegion(AssetRegionNames.WHISTLE);
        youLose = gameplayAtlas.findRegion(AssetRegionNames.YOU_LOSE);
        youWin = gameplayAtlas.findRegion(AssetRegionNames.YOU_WIN);
    }

    public static void dispose() {
        assetManager.dispose();
    }
}
