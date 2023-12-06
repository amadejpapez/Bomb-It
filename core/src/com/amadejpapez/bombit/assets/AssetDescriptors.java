package com.amadejpapez.bombit.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class AssetDescriptors {
    public static final AssetDescriptor<TextureAtlas> GAMEPLAY =
            new AssetDescriptor<>(AssetPaths.GAMEPLAY, TextureAtlas.class);

    public static final AssetDescriptor<BitmapFont> FONT =
            new AssetDescriptor<>(AssetPaths.FONT, BitmapFont.class);

    public static final AssetDescriptor<Sound> BOMB_HIT =
            new AssetDescriptor<>(AssetPaths.BOMB_HIT, Sound.class);

    public static final AssetDescriptor<ParticleEffect> EXPLOSION_EFFECT =
            new AssetDescriptor<>(AssetPaths.EXPLOSION_EFFECT, ParticleEffect.class);

    public static final AssetDescriptor<Texture> TITLE =
            new AssetDescriptor<>(AssetPaths.TITLE, Texture.class);

    public static final AssetDescriptor<Texture> YOU_LOSE =
            new AssetDescriptor<>(AssetPaths.YOU_LOSE, Texture.class);

    public static final AssetDescriptor<Texture> YOU_WIN =
            new AssetDescriptor<>(AssetPaths.YOU_WIN, Texture.class);

    private AssetDescriptors() { }
}
