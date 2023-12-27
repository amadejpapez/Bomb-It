package com.amadejpapez.bombit;

import com.amadejpapez.bombit.assets.Assets;
import com.amadejpapez.bombit.config.GameConfig;
import com.amadejpapez.bombit.screen.GameScreen;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

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

    public static final Map<String, TextureRegionDrawable> characterImages = Map.ofEntries(
            Map.entry("blue", new TextureRegionDrawable(Assets.playerBlue)),
            Map.entry("black", new TextureRegionDrawable(Assets.playerBlack)),
            Map.entry("green", new TextureRegionDrawable(Assets.playerGreen)),
            Map.entry("pink", new TextureRegionDrawable(Assets.playerPink)),
            Map.entry("orange", new TextureRegionDrawable(Assets.playerOrange)),
            Map.entry("purple", new TextureRegionDrawable(Assets.playerPurple))
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

    public void hit(int hitBy) {
        // called on the player that created the bomb!
        if (!Objects.equals(num, hitBy))
            GameManager.playerCharacters.get(hitBy).kills++;
    }

    public void inputMove(int keycode) {
        Integer nextRow = position.get(0);
        Integer nextCol = position.get(1);

        if (keycode == Input.Keys.W || keycode == Input.Keys.UP)
            nextRow -= 1;
        else if (keycode == Input.Keys.S || keycode == Input.Keys.DOWN)
            nextRow += 1;
        else if (keycode == Input.Keys.A || keycode == Input.Keys.LEFT)
            nextCol -= 1;
        else nextCol += 1;

        // only move if the future cell is "empty"
        CellActor nextCell = GameScreen.cellGrid.getCell(GameScreen.cells.get(nextRow).get(nextCol)).getActor();
        TextureRegion nextCellImg = ((TextureRegionDrawable) nextCell.getDrawable()).getRegion();
        if (!nextCellImg.equals(Assets.empty)) {
            if (nextCellImg.equals(Assets.bonusBombs)) {
                GameScreen.cellGrid.getCell(GameScreen.cells.get(nextRow).get(nextCol)).getActor().setDrawable(Assets.empty);
                maxNumOfBombs++;
            } else if (nextCellImg.equals(Assets.bonusHand)) {
                GameScreen.cellGrid.getCell(GameScreen.cells.get(nextRow).get(nextCol)).getActor().setDrawable(Assets.empty);
                hasBonusHand = true;
            } else {
                return;
            }
        }

        // player cannot move across bombs
        if (!Bomb.isBombCellEmpty(nextRow, nextCol)) {
            if (hasBonusHand)
                Bomb.handleBonusHand(nextRow, nextCol, keycode);
            return;
        }

        grid.getCell(cells.get(nextRow).get(nextCol)).getActor().setDrawable(image);
        grid.getCell(cells.get(position.get(0)).get(position.get(1))).getActor().setDrawable(Assets.empty);

        if (position.get(0).equals(nextRow))
            position.set(1, nextCol);
        else
            position.set(0, nextRow);
    }

    public void inputAddBomb() {
        if (numActiveBombs >= maxNumOfBombs)
            return;

        Bomb.grid.getCell(Bomb.cells.get(position.get(0)).get(position.get(1))).getActor().setDrawable(Assets.bomb);

        float time = TimeUtils.nanosToMillis(TimeUtils.nanoTime()) / 1000f;
        GameManager.activeBombs.add(new Bomb(num, new ArrayList<>(position), time));

        numActiveBombs++;
    }

    public void moveAi() {
        if (num < GameManager.INSTANCE.getNumPhysicalPlayers())
            return;

        final List<Integer> possibleMoves = List.of(
                Input.Keys.UP,
                Input.Keys.DOWN,
                Input.Keys.LEFT,
                Input.Keys.RIGHT
        );

        inputMove(possibleMoves.get(ThreadLocalRandom.current().nextInt(possibleMoves.size())));

        if (ThreadLocalRandom.current().nextInt(4) == 0)
            inputAddBomb();
    }
}
