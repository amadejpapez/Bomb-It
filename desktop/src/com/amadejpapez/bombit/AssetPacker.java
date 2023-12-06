package com.amadejpapez.bombit;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class AssetPacker {
    public static void main(String[] args) {
        TexturePacker.Settings settings = new TexturePacker.Settings();

        TexturePacker.process(settings,
                "desktop/assets-raw",   // the directory containing individual images to be packed
                "assets/atlas",   // the directory where the pack file will be written
                "gameplay"   // the name of the pack file / atlas name
        );
    }
}
