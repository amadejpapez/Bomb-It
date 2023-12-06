package com.amadejpapez.bombit.assets;

import com.badlogic.gdx.assets.AssetManager;

public class Assets {
    public static AssetManager assetManager;

    public static void load() {
        assetManager = new AssetManager();
        assetManager.load(AssetDescriptors.GAMEPLAY);
        assetManager.load(AssetDescriptors.FONT);
        assetManager.load(AssetDescriptors.BOMB_HIT);
        assetManager.load(AssetDescriptors.EXPLOSION_EFFECT);
        assetManager.load(AssetDescriptors.TITLE);
        assetManager.load(AssetDescriptors.YOU_WIN);
        assetManager.load(AssetDescriptors.YOU_LOSE);
        assetManager.finishLoading();
    }

    public static void dispose() {
        assetManager.dispose();
    }
}
