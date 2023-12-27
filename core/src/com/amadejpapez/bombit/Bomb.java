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
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class Bomb {
    public Integer createdByPlayer;
    public List<Integer> position;
    public Float timeCreated;

    public static final List<List<CellActor>> cells = new ArrayList<>();
    public static final Table grid = new Table();

    public Bomb(int createdByPlayer, List<Integer> position, float timeCreated) {
        this.createdByPlayer = createdByPlayer;
        this.position = position;
        this.timeCreated = timeCreated;
    }

    public static List<List<Integer>> getPositionToCheck(int row, int col) {
        return List.of(
                List.of(row, col),
                List.of(row, col + 1),
                List.of(row, col - 1),
                List.of(row + 1, col),
                List.of(row - 1, col)
        );
    }

    public static Bomb getBombByPosition(List<Bomb> bombs, List<Integer> pos) {
        for (Bomb bomb : bombs) {
            if (Objects.equals(bomb.position.get(0), pos.get(0)) && Objects.equals(bomb.position.get(1), pos.get(1)))
                return bomb;
        }
        return null;
    }

    public static boolean isBombCellEmpty(int x, int y) {
        CellActor cell = Bomb.grid.getCell(cells.get(x).get(y)).getActor();
        TextureRegion cellImg = ((TextureRegionDrawable) cell.getDrawable()).getRegion();
        return cellImg.equals(Assets.empty);
    }

    public static void updateBombs() {
        if (GameManager.gameEnded)
            return;

        float elapsedTime = TimeUtils.nanosToMillis(TimeUtils.nanoTime()) / 1000f;

        for (Iterator<Bomb> it = GameManager.activeBombs.iterator(); it.hasNext(); ) {
            Bomb bomb = it.next();

            List<List<Integer>> checkLocations = Bomb.getPositionToCheck(bomb.position.get(0), bomb.position.get(1));

            if (elapsedTime - bomb.timeCreated > GameConfig.TIME_BOMB_ACTIVE) {
                for (List<Integer> loc : checkLocations) {
                    CellActor cellTmp = GameScreen.cellGrid.getCell(GameScreen.cells.get(loc.get(0)).get(loc.get(1))).getActor();
                    TextureRegionDrawable drawTmp = (TextureRegionDrawable) cellTmp.getDrawable();
                    if (Arrays.asList(GameScreen.obstacles).contains(drawTmp.getRegion()))
                        cellTmp.setDrawable(Assets.empty);
                    if (Player.characterImages.containsValue(drawTmp))
                        Player.playerHit(drawTmp, bomb.createdByPlayer);
                }

                grid.getCell(cells.get(bomb.position.get(0)).get(bomb.position.get(1))).getActor().setDrawable(Assets.empty);
//                bombCell.setActor(explosionEffectActor);

                if (GameManager.INSTANCE.getSoundsEnabled())
                    Assets.explosionSound.play();
//                explosionEffect.setPosition(bomb.getX(), bomb.getY());
//                explosionEffect.start();
                GameManager.playerCharacters.get(bomb.createdByPlayer).numActiveBombs--;
                it.remove();
            }
        }
    }

    public static void handleBonusHand(int row, int col, int keycode) {
        int tmpRow = row;
        int tmpCol = col;

        Bomb tmpBomb = Bomb.getBombByPosition(GameManager.activeBombs, List.of(row, col));
        if (tmpBomb == null)
            return;

        if (keycode == Input.Keys.DOWN) {
            while (true) {
                if (!isBombCellEmpty(tmpRow + 1, tmpCol)) break;
                if (!GameScreen.isMainCellEmpty(tmpRow + 1, tmpCol)) break;

                grid.getCell(cells.get(tmpRow).get(tmpCol)).getActor().setDrawable(Assets.empty);
                grid.getCell(cells.get(tmpRow + 1).get(tmpCol)).getActor().setDrawable(Assets.bomb);
                tmpBomb.position = List.of(tmpRow + 1, tmpCol);
                tmpRow++;
            }
        } else if (keycode == Input.Keys.RIGHT) {
            while (true) {
                if (!isBombCellEmpty(tmpRow, tmpCol + 1)) break;
                if (!GameScreen.isMainCellEmpty(tmpRow, tmpCol + 1)) break;

                grid.getCell(cells.get(tmpRow).get(tmpCol)).getActor().setDrawable(Assets.empty);
                grid.getCell(cells.get(tmpRow).get(tmpCol + 1)).getActor().setDrawable(Assets.bomb);
                tmpBomb.position = List.of(tmpRow, tmpCol + 1);
                tmpCol++;
            }
        } else if (keycode == Input.Keys.LEFT) {
            while (true) {
                if (!isBombCellEmpty(tmpRow, tmpCol - 1)) break;
                if (!GameScreen.isMainCellEmpty(tmpRow, tmpCol - 1)) break;

                grid.getCell(cells.get(tmpRow).get(tmpCol)).getActor().setDrawable(Assets.empty);
                grid.getCell(cells.get(tmpRow).get(tmpCol - 1)).getActor().setDrawable(Assets.bomb);
                tmpBomb.position = List.of(tmpRow, tmpCol - 1);
                tmpCol--;
            }
        } else if (keycode == Input.Keys.UP) {
            while (true) {
                if (!isBombCellEmpty(tmpRow - 1, tmpCol)) break;
                if (!GameScreen.isMainCellEmpty(tmpRow - 1, tmpCol)) break;

                grid.getCell(cells.get(tmpRow).get(tmpCol)).getActor().setDrawable(Assets.empty);
                grid.getCell(cells.get(tmpRow - 1).get(tmpCol)).getActor().setDrawable(Assets.bomb);
                tmpBomb.position = List.of(tmpRow - 1, tmpCol);
                tmpRow--;
            }
        }
    }
}
