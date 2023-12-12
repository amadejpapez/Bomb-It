package com.amadejpapez.bombit.screen;

import com.amadejpapez.bombit.assets.Assets;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.amadejpapez.bombit.assets.AssetDescriptors;
import com.amadejpapez.bombit.assets.AssetRegionNames;
import com.amadejpapez.bombit.CellActor;
import com.amadejpapez.bombit.BombIt;
import com.amadejpapez.bombit.config.GameConfig;

public class GameScreen extends ScreenAdapter {
    private final BombIt game;

    private Viewport viewport;
    private Viewport hudViewport;

    private Stage gameplayStage;
    private Stage hudStage;

    private final TextureAtlas gameplayAtlas = Assets.assetManager.get(AssetDescriptors.GAMEPLAY);

    final TextureRegion coneRed = gameplayAtlas.findRegion(AssetRegionNames.CONE_RED);
    final TextureRegion playerBlue = gameplayAtlas.findRegion(AssetRegionNames.PLAYER_BLUE);
    final TextureRegion basketBlue = gameplayAtlas.findRegion(AssetRegionNames.BASKET_BLUE);
    final TextureRegion basketOrange = gameplayAtlas.findRegion(AssetRegionNames.BASKET_ORANGE);
    final TextureRegion empty = gameplayAtlas.findRegion(AssetRegionNames.EMPTY);
    final TextureRegion floor = gameplayAtlas.findRegion(AssetRegionNames.FLOOR);

    private final List<List<CellActor>> cells = new ArrayList<>();

    private final List<Integer> playerPosition = new ArrayList<>(GameConfig.PLAYER1_START);

    public GameScreen(BombIt game) {
        this.game = game;
    }

    @Override
    public void show() {
        viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);
        hudViewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT);

        gameplayStage = new Stage(viewport, game.getBatch());
        hudStage = new Stage(hudViewport, game.getBatch());

        gameplayStage.addActor(createGridBackground());
        gameplayStage.addActor(createGridMain());
        gameplayStage.addActor(createMiddle());

        Gdx.input.setInputProcessor(new InputMultiplexer(gameplayStage, hudStage));
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        hudViewport.update(width, height, true);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(195 / 255f, 195 / 255f, 195 / 255f, 0f);

        // update
        gameplayStage.act(delta);
        hudStage.act(delta);

        // draw
        gameplayStage.draw();
        hudStage.draw();
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        gameplayStage.dispose();
        hudStage.dispose();
    }

    private Actor createGridBackground() {
        Table bgTable = new Table();
        Table bgGrid = new Table();
        bgGrid.defaults().size(GameConfig.CELL_SIZE);

        for (int i = 0; i < GameConfig.NUM_ROWS; i++) {
            for (int j = 0; j < GameConfig.NUM_COLUMNS; j++)
                bgGrid.add(new CellActor(floor));
            bgGrid.row();
        }

        bgTable.add(bgGrid).row();
        bgTable.center();
        bgTable.setFillParent(true);
        bgTable.pack();

        return bgTable;
    }

    private Actor createMiddle() {
        Image mid = new Image(gameplayAtlas.findRegion(AssetRegionNames.MIDDLE));
        mid.setPosition(viewport.getWorldWidth() / 2f - 6f,
                viewport.getWorldHeight() / 2f - 6f);
        mid.setScale(0.035f);
        return mid;
    }

    private Actor createGridMain() {
        final Table table = new Table();

        final Table grid = new Table();
        grid.defaults().size(GameConfig.CELL_SIZE);   // all cells will be the same size

        TextureRegion[] obstacles = {
                gameplayAtlas.findRegion(AssetRegionNames.POM_PINK),
                gameplayAtlas.findRegion(AssetRegionNames.POM_YELLOW),
                gameplayAtlas.findRegion(AssetRegionNames.WHISTLE),
        };
        TextureRegion[] fixedObstacles = {
                gameplayAtlas.findRegion(AssetRegionNames.CONE_BLUE),
                coneRed
        };

        // init all cells as empty
        for (int i = 0; i < GameConfig.NUM_ROWS; i++) {
            cells.add(new ArrayList<>());
            for (int j = 0; j < GameConfig.NUM_COLUMNS; j++) {
                cells.get(i).add(new CellActor(empty));
            }
        }

        for (int row = 0; row < GameConfig.NUM_ROWS; row++) {
            for (int column = 0; column < GameConfig.NUM_COLUMNS; column++) {
                List<Integer> location = List.of(row, column);

                if (row == 0 || row == GameConfig.NUM_ROWS - 1 || column == 0 || column == GameConfig.NUM_COLUMNS - 1)
                    cells.get(row).get(column).setDrawable(coneRed);
                else if (row == playerPosition.get(0) && column == playerPosition.get(1))
                    cells.get(row).get(column).setDrawable(playerBlue);
                else if (row == 5 && column == 8)
                    cells.get(row).get(column).setDrawable(basketBlue);
                else if (row == 7 && column == 6)
                    cells.get(row).get(column).setDrawable(basketOrange);
                else if (GameConfig.LOC_FIXED_OBSTACLES.contains(location))
                    cells.get(row).get(column).setDrawable(fixedObstacles[ThreadLocalRandom.current().nextInt(fixedObstacles.length)]);
                else if (ThreadLocalRandom.current().nextBoolean())
                    // TODO: except around player
                    cells.get(row).get(column).setDrawable(obstacles[ThreadLocalRandom.current().nextInt(obstacles.length)]);

                grid.add(cells.get(row).get(column));
            }
            grid.row();
        }

        table.add(grid).row();
        table.center();
        table.setFillParent(true);
        table.pack();

        gameplayStage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.UP
                        || keycode == Input.Keys.DOWN
                        || keycode == Input.Keys.LEFT
                        || keycode == Input.Keys.RIGHT
                ) {
                    Integer rowPlayer = playerPosition.get(0);
                    Integer colPlayer = playerPosition.get(1);

                    Integer rowFuture = playerPosition.get(0);
                    Integer colFuture = playerPosition.get(1);

                    if (keycode == Input.Keys.UP)
                        rowFuture -= 1;
                    else if (keycode == Input.Keys.DOWN)
                        rowFuture += 1;
                    else if (keycode == Input.Keys.LEFT)
                        colFuture -= 1;
                    else colFuture += 1;

                    // only move if the future cell is "empty"
                    CellActor futureCell = grid.getCell(cells.get(rowFuture).get(colFuture)).getActor();
                    TextureRegion futureImg = ((TextureRegionDrawable) futureCell.getDrawable()).getRegion();
                    if (!futureImg.equals(empty))
                        return false;

                    futureCell.setDrawable(playerBlue);
                    grid.getCell(cells.get(rowPlayer).get(colPlayer)).getActor().setDrawable(empty);

                    if (rowPlayer.equals(rowFuture))
                        playerPosition.set(1, colFuture);
                    else
                        playerPosition.set(0, rowFuture);

                    return true;
                }

                return false;
            }
        });

        return table;
    }
}