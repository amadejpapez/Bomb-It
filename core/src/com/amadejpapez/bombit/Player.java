package com.amadejpapez.bombit;

import com.amadejpapez.bombit.assets.AssetDescriptors;
import com.amadejpapez.bombit.assets.AssetRegionNames;
import com.amadejpapez.bombit.assets.Assets;
import com.amadejpapez.bombit.config.GameConfig;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Player {
    public Integer num;
    private Integer kills;
    public List<Integer> position;
    public String character;
    public TextureRegionDrawable image;
    public Integer numActiveBombs;
    public Integer maxNumOfBombs;
    public Boolean hasBonusHand;

    public List<List<CellActor>> cells;
    public Table grid;

    private static final TextureAtlas gameplayAtlas = Assets.assetManager.get(AssetDescriptors.GAMEPLAY);

    public static final Map<String, TextureRegionDrawable> characterImages = Map.ofEntries(
            Map.entry("blue", new TextureRegionDrawable(gameplayAtlas.findRegion(AssetRegionNames.PLAYER_BLUE))),
            Map.entry("black", new TextureRegionDrawable(gameplayAtlas.findRegion(AssetRegionNames.PLAYER_BLACK))),
            Map.entry("green", new TextureRegionDrawable(gameplayAtlas.findRegion(AssetRegionNames.PLAYER_GREEN))),
            Map.entry("pink", new TextureRegionDrawable(gameplayAtlas.findRegion(AssetRegionNames.PLAYER_PINK))),
            Map.entry("orange", new TextureRegionDrawable(gameplayAtlas.findRegion(AssetRegionNames.PLAYER_ORANGE))),
            Map.entry("purple", new TextureRegionDrawable(gameplayAtlas.findRegion(AssetRegionNames.PLAYER_PURPLE)))
    );

    public static final List<List<Integer>> STARTING_POSITIONS = List.of(
            List.of(1, 1),
            List.of(1, 15),
            List.of(13, 1),
            List.of(13, 15)
    );

    Player(int num, String charColor) {
        // first player num should be zero!
        this.num = num;
        this.character = charColor;
        this.image = characterImages.get(charColor);
        this.kills = 0;
        this.position = new ArrayList<>(STARTING_POSITIONS.get(num));
        this.numActiveBombs = 0;
        this.maxNumOfBombs = GameConfig.MAX_NUMBER_BOMBS_DEFAULT;
        this.cells = new ArrayList<>();
        this.grid = new Table();
        this.hasBonusHand = false;
    }

    public void updateImage(String charColor) {
        this.character = charColor;
        this.image = characterImages.get(charColor);
    }

    public int getKills() {
        return kills;
    }

    public static void playerHit(TextureRegionDrawable img, Integer hitBy) {
        for (Player player: GameManager.playerCharacters) {
            if (player.image == img) {
                if (!Objects.equals(player.num, hitBy))
                    GameManager.playerCharacters.get(hitBy).kills++;
            }
        }
    }
}
