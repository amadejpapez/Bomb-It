package com.amadejpapez.bombit.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class AssetDescriptors {
    public static final AssetDescriptor<TextureAtlas> GAMEPLAY =
            new AssetDescriptor<>(AssetPaths.GAMEPLAY, TextureAtlas.class);

    public static final AssetDescriptor<BitmapFont> FONT =
            new AssetDescriptor<>(AssetPaths.FONT, BitmapFont.class);

    public static final AssetDescriptor<Skin> SKIN =
            new AssetDescriptor<>(AssetPaths.SKIN, Skin.class);

    public static final AssetDescriptor<Sound> BOMB_SOUND =
            new AssetDescriptor<>(AssetPaths.BOMB_SOUND, Sound.class);

    public static final AssetDescriptor<Music> GAME_MUSIC =
            new AssetDescriptor<>(AssetPaths.GAME_MUSIC, Music.class);

    public static final AssetDescriptor<Music> START_MUSIC =
            new AssetDescriptor<>(AssetPaths.START_MUSIC, Music.class);

    private AssetDescriptors() { }
}
