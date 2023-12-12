package com.amadejpapez.bombit.screen;

import com.amadejpapez.bombit.GameManager;
import com.amadejpapez.bombit.assets.Assets;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.amadejpapez.bombit.assets.AssetDescriptors;
import com.amadejpapez.bombit.assets.AssetRegionNames;
import com.amadejpapez.bombit.CellActor;
import com.amadejpapez.bombit.CellState;
import com.amadejpapez.bombit.BombIt;
import com.amadejpapez.bombit.config.GameConfig;

public class GameScreen extends ScreenAdapter {
    private final BombIt game;

    private Viewport viewport;
    private Viewport hudViewport;

    private Stage gameplayStage;
    private Stage hudStage;

    private Skin skin;
    private TextureAtlas gameplayAtlas;

    private final CellState move = GameManager.INSTANCE.getInitMove();
    private Image infoImage;

    private final Integer NUM_COLUMNS = 17;
    private final Integer NUM_ROWS = 15;
    private final Integer CELL_SIZE = 4;

    private final List<List<CellActor>> cells = new ArrayList<>();

    private final List<Integer> playerPosition = new ArrayList<>();

    public GameScreen(BombIt game) {
        this.game = game;
    }

    @Override
    public void show() {
        viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);
        hudViewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT);

        gameplayStage = new Stage(viewport, game.getBatch());
        hudStage = new Stage(hudViewport, game.getBatch());

        skin = Assets.assetManager.get(AssetDescriptors.SKIN);
        gameplayAtlas = Assets.assetManager.get(AssetDescriptors.GAMEPLAY);

        gameplayStage.addActor(createGridBackground());
        gameplayStage.addActor(createGrid());
        gameplayStage.addActor(createGridMiddle());

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
        Image bg = new Image(gameplayAtlas.findRegion(AssetRegionNames.GRID));
        bg.setPosition(6, 0);
        bg.setSize(68f, 60);
        return bg;
    }

    private Actor createGridMiddle() {
        Image mid = new Image(gameplayAtlas.findRegion(AssetRegionNames.MIDDLE));
        mid.setPosition(viewport.getWorldWidth() / 2f - 6f,
                viewport.getWorldHeight() / 2f - 6f);
        mid.setScale(0.035f);
        return mid;
    }

    private Actor createGrid() {
        final Table table = new Table();
        table.setDebug(false);   // turn on all debug lines (table, cell, and widget)

        final Table grid = new Table();
        grid.defaults().size(CELL_SIZE);   // all cells will be the same size
        grid.setDebug(false);

        final TextureRegion coneRed = gameplayAtlas.findRegion(AssetRegionNames.CONE_RED);

        final TextureRegion playerBlue = gameplayAtlas.findRegion(AssetRegionNames.PLAYER_BLUE);

        final TextureRegion basketBlue = gameplayAtlas.findRegion(AssetRegionNames.BASKET_BLUE);
        final TextureRegion basketOrange = gameplayAtlas.findRegion(AssetRegionNames.BASKET_ORANGE);

        final TextureRegion empty = gameplayAtlas.findRegion(AssetRegionNames.EMPTY);

        TextureRegion[] obstacles = {
                gameplayAtlas.findRegion(AssetRegionNames.POM_PINK),
                gameplayAtlas.findRegion(AssetRegionNames.POM_YELLOW),
                gameplayAtlas.findRegion(AssetRegionNames.WHISTLE),
        };

        TextureRegion[] fixedObstacles = {
                gameplayAtlas.findRegion(AssetRegionNames.CONE_BLUE),
                coneRed
        };
        List<List<Integer>> drawFixedObstaclesIn = List.of(
                List.of(1, 11),
                List.of(10, 15),
                List.of(10, 7),
                List.of(10, 8),
                List.of(10, 9),
                List.of(11, 11),
                List.of(11, 13),
                List.of(11, 3),
                List.of(11, 5),
                List.of(12, 10),
                List.of(12, 11),
                List.of(12, 13),
                List.of(12, 14),
                List.of(12, 2),
                List.of(12, 3),
                List.of(12, 5),
                List.of(12, 6),
                List.of(12, 8),
                List.of(13, 5),
                List.of(2, 10),
                List.of(2, 11),
                List.of(2, 13),
                List.of(2, 14),
                List.of(2, 2),
                List.of(2, 3),
                List.of(2, 5),
                List.of(2, 6),
                List.of(2, 8),
                List.of(3, 11),
                List.of(3, 13),
                List.of(3, 3),
                List.of(3, 5),
                List.of(4, 1),
                List.of(4, 7),
                List.of(4, 8),
                List.of(4, 9),
                List.of(5, 11),
                List.of(5, 13),
                List.of(5, 3),
                List.of(5, 5),
                List.of(6, 11),
                List.of(6, 13),
                List.of(6, 14),
                List.of(6, 2),
                List.of(6, 3),
                List.of(6, 5),
                List.of(8, 11),
                List.of(8, 13),
                List.of(8, 14),
                List.of(8, 2),
                List.of(8, 3),
                List.of(8, 5),
                List.of(9, 11),
                List.of(9, 13),
                List.of(9, 3),
                List.of(9, 5)
        );

        List<Integer> location;

        // init all cells as empty
        for (int i = 0; i < NUM_ROWS; i++)  {
            cells.add(new ArrayList<>());
            for (int j = 0; j < NUM_COLUMNS; j++)  {
                cells.get(i).add(new CellActor(empty));
            }
        }

        for (int row = 0; row < NUM_ROWS; row++) {
            for (int column = 0; column < NUM_COLUMNS; column++) {
                location = List.of(row, column);

                // draw broder
                if (row == 0 || row == NUM_ROWS - 1 || column == 0 || column == NUM_COLUMNS - 1) {
                    cells.get(row).get(column).setDrawable(coneRed);
                }
                // draw player
                else if (row == 1 && column == 1) {
                    cells.get(row).get(column).setDrawable(playerBlue);
                    playerPosition.add(row);
                    playerPosition.add(column);
                }
                // draw center
                else if (row == 5 && column == 8) {
                    cells.get(row).get(column).setDrawable(basketBlue);
                }
                else if (row == 7 && column == 6) {
                    cells.get(row).get(column).setDrawable(basketOrange);
                }
                // draw fixed obstacles
                else if (drawFixedObstaclesIn.contains(location)) {
                    cells.get(row).get(column).setDrawable(fixedObstacles[ThreadLocalRandom.current().nextInt(fixedObstacles.length)]);
                }
                // draw destroyable obstacles
                else if (ThreadLocalRandom.current().nextBoolean()) {
                    // TODO: except around player
                    cells.get(row).get(column).setDrawable(obstacles[ThreadLocalRandom.current().nextInt(obstacles.length)]);
                }

                grid.add(cells.get(row).get(column));
            }
            grid.row();
        }

        table.add(grid).row();
        table.center();
        table.setFillParent(true);
        table.pack();

        return table;
    }
}