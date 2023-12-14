package com.amadejpapez.bombit.assets;

import com.badlogic.gdx.assets.AssetManager;

public class Assets {
    public static AssetManager assetManager;

    public static void load() {
        assetManager = new AssetManager();
        assetManager.load(AssetDescriptors.GAMEPLAY);
        assetManager.load(AssetDescriptors.FONT);
        assetManager.load(AssetDescriptors.SKIN);
        assetManager.load(AssetDescriptors.BOMB_SOUND);
        assetManager.load(AssetDescriptors.POWER_UP_SOUND);
        assetManager.load(AssetDescriptors.EXPLOSION_EFFECT);
        assetManager.finishLoading();
    }

    public static void dispose() {
        assetManager.dispose();
    }
}
