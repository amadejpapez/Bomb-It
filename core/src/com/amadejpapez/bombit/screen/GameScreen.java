package com.amadejpapez.bombit.screen;

import com.amadejpapez.bombit.Bomb;
import com.amadejpapez.bombit.GameManager;
import com.amadejpapez.bombit.ParticleEffectActor;
import com.amadejpapez.bombit.Player;
import com.amadejpapez.bombit.assets.Assets;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
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
    final TextureRegion basketBlue = gameplayAtlas.findRegion(AssetRegionNames.BASKET_BLUE);
    final TextureRegion basketOrange = gameplayAtlas.findRegion(AssetRegionNames.BASKET_ORANGE);
    final TextureRegion empty = gameplayAtlas.findRegion(AssetRegionNames.EMPTY);
    final TextureRegion floor = gameplayAtlas.findRegion(AssetRegionNames.FLOOR);
    final TextureRegion bomb = gameplayAtlas.findRegion(AssetRegionNames.BOMB);

    final Skin skin = Assets.assetManager.get(AssetDescriptors.SKIN);

    final TextureRegion[] obstacles = {
            gameplayAtlas.findRegion(AssetRegionNames.POM_PINK),
            gameplayAtlas.findRegion(AssetRegionNames.POM_YELLOW),
            gameplayAtlas.findRegion(AssetRegionNames.WHISTLE),
    };
    final TextureRegion[] fixedObstacles = {
            gameplayAtlas.findRegion(AssetRegionNames.CONE_BLUE),
            coneRed
    };

    final ParticleEffect explosionEffect = Assets.assetManager.get(AssetDescriptors.EXPLOSION_EFFECT);
    final ParticleEffectActor explosionEffectActor = new ParticleEffectActor(Assets.assetManager.get(AssetDescriptors.EXPLOSION_EFFECT));

    final Sound explosionSound = Assets.assetManager.get(AssetDescriptors.BOMB_SOUND);

    private final List<List<CellActor>> cells = new ArrayList<>();
    private final Table cellGrid = new Table();

    private final List<List<CellActor>> bombCells = new ArrayList<>();
    private final Table bombGrid = new Table();

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
        gameplayStage.addActor(createGridBombs());
        gameplayStage.addActor(createGridMain());
        gameplayStage.addActor(createMiddle());
        gameplayStage.addActor(new ParticleEffectActor(explosionEffect));

        hudStage.addActor(createGridHud());

        Gdx.input.setInputProcessor(new InputMultiplexer(gameplayStage, hudStage));

        GameManager.INSTANCE.playGameMusic();
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
        checkBombs();
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

    private void checkBombs() {
        float elapsedTime = TimeUtils.nanosToMillis(TimeUtils.nanoTime()) / 1000f;

        for (Iterator<Bomb> it = GameManager.activeBombs.iterator(); it.hasNext(); ) {
            Bomb bomb = it.next();

            List<List<Integer>> checkLocations = Bomb.getPositionToCheck(bomb.position.get(0), bomb.position.get(1));

            if (elapsedTime - bomb.timeCreated > GameConfig.TIME_BOMB_ACTIVE) {
                for (List<Integer> loc : checkLocations) {
                    CellActor cellTmp = cellGrid.getCell(cells.get(loc.get(0)).get(loc.get(1))).getActor();
                    TextureRegion regTmp = ((TextureRegionDrawable) cellTmp.getDrawable()).getRegion();
                    if (Arrays.asList(obstacles).contains(regTmp))
                        cellTmp.setDrawable(empty);
                }

                bombGrid.getCell(bombCells.get(bomb.position.get(0)).get(bomb.position.get(1))).getActor().setDrawable(empty);
//                bombCell.setActor(explosionEffectActor);

                if (GameManager.INSTANCE.getSoundsEnabled())
                    explosionSound.play();
//                explosionEffect.setPosition(bomb.getX(), bomb.getY());
//                explosionEffect.start();
                GameManager.playerCharacters.get(bomb.createdByPlayer).numActiveBombs--;
                it.remove();
            }
        }
    }

    private Actor createGridBackground() {
        Table table = new Table();
        Table grid = new Table();
        grid.defaults().size(GameConfig.CELL_SIZE);

        for (int i = 0; i < GameConfig.NUM_ROWS; i++) {
            for (int j = 0; j < GameConfig.NUM_COLUMNS; j++)
                grid.add(new CellActor(floor));
            grid.row();
        }

        table.add(grid).row();
        table.right();
        table.padRight(3);
        table.setFillParent(true);
        table.pack();

        return table;
    }

    private Actor createGridBombs() {
        Table table = new Table();
        bombGrid.defaults().size(GameConfig.CELL_SIZE);

        for (int i = 0; i < GameConfig.NUM_ROWS; i++) {
            bombCells.add(new ArrayList<>());
            for (int j = 0; j < GameConfig.NUM_COLUMNS; j++) {
                bombCells.get(i).add(new CellActor(empty));
                bombGrid.add(bombCells.get(i).get(j));
            }
            bombGrid.row();
        }

        table.add(bombGrid).row();
        table.right();
        table.padRight(3);
        table.setFillParent(true);
        table.pack();

        return table;
    }

    private Actor createMiddle() {
        Image mid = new Image(gameplayAtlas.findRegion(AssetRegionNames.MIDDLE));
        mid.setPosition(viewport.getWorldWidth() / 2f + 7f,
                viewport.getWorldHeight() / 2f - 6f);
        mid.setScale(0.035f);
        return mid;
    }

    private Actor createGridHud() {
        final Table table = new Table();
        table.padLeft(20);

        Label tmpLabel;
        for (Player player: GameManager.playerCharacters) {
            Image tmpImg = new Image(player.image);
            table.add(tmpImg).height(60).width(50).left().row();

            tmpLabel = new Label("Health: " + player.getHealth(), skin);
            tmpLabel.setColor(Color.BROWN);
            table.add(tmpLabel).left().row();

            tmpLabel = new Label("Kills: " + player.getKills(), skin);
            tmpLabel.setColor(Color.BROWN);
            table.add(tmpLabel).left().row();
        }

        table.left();
        table.pack();
        table.setPosition(0, GameConfig.HUD_HEIGHT / 2f - (table.getHeight() / 2f));
        return table;
    }

    private Actor createGridMain() {
        final Table table = new Table();

        cellGrid.defaults().size(GameConfig.CELL_SIZE);   // all cells will be the same size

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

                for (Player player: GameManager.playerCharacters) {
                    if (row == player.position.get(0) && column == player.position.get(1))
                        cells.get(row).get(column).setDrawable(player.image);
                }

                if (row == 0 || row == GameConfig.NUM_ROWS - 1 || column == 0 || column == GameConfig.NUM_COLUMNS - 1)
                    cells.get(row).get(column).setDrawable(coneRed);
                else if (row == 5 && column == 8)
                    cells.get(row).get(column).setDrawable(basketBlue);
                else if (row == 7 && column == 6)
                    cells.get(row).get(column).setDrawable(basketOrange);
                else if (GameConfig.LOC_FIXED_OBSTACLES.contains(location))
                    cells.get(row).get(column).setDrawable(fixedObstacles[ThreadLocalRandom.current().nextInt(fixedObstacles.length)]);
                else if (!GameConfig.LOC_SKIP_OBSTACLES.contains(location) && ThreadLocalRandom.current().nextBoolean())
                    cells.get(row).get(column).setDrawable(obstacles[ThreadLocalRandom.current().nextInt(obstacles.length)]);

                cellGrid.add(cells.get(row).get(column));
            }
            cellGrid.row();
        }

        table.add(cellGrid).row();
        table.right();
        table.padRight(3);
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
                    inputMovePlayer(GameManager.playerCharacters.get(0), keycode);
                } else if (keycode == Input.Keys.W
                        || keycode == Input.Keys.S
                        || keycode == Input.Keys.A
                        || keycode == Input.Keys.D
                ) {
                    inputMovePlayer(GameManager.playerCharacters.get(1), keycode);
                } else if (keycode == Input.Keys.SPACE) {
                    inputCreateBomb(GameManager.playerCharacters.get(0));
                } else if (keycode == Input.Keys.ENTER) {
                    inputCreateBomb(GameManager.playerCharacters.get(1));
                }

                return false;
            }
        });

        return table;
    }

    private void inputCreateBomb(Player player) {
        if (player.numActiveBombs >= player.maxNumOfBombs)
            return;

        bombGrid.getCell(bombCells.get(player.position.get(0)).get(player.position.get(1))).getActor().setDrawable(bomb);

        float time = TimeUtils.nanosToMillis(TimeUtils.nanoTime()) / 1000f;

        player.numActiveBombs++;
        GameManager.activeBombs.add(new Bomb(1, new ArrayList<>(player.position), time));
    }

    private void inputMovePlayer(Player player, int keycode) {
        Integer rowFuture = player.position.get(0);
        Integer colFuture = player.position.get(1);

        if (keycode == Input.Keys.W || keycode == Input.Keys.UP)
            rowFuture -= 1;
        else if (keycode == Input.Keys.S || keycode == Input.Keys.DOWN)
            rowFuture += 1;
        else if (keycode == Input.Keys.A || keycode == Input.Keys.LEFT)
            colFuture -= 1;
        else colFuture += 1;

        // only move if the future cell is "empty"
        CellActor futureCell = cellGrid.getCell(cells.get(rowFuture).get(colFuture)).getActor();
        TextureRegion futureImg = ((TextureRegionDrawable) futureCell.getDrawable()).getRegion();
        if (!futureImg.equals(empty))
            return;

        futureCell.setDrawable(player.image);
        cellGrid.getCell(cells.get(player.position.get(0)).get(player.position.get(1))).getActor().setDrawable(empty);

        if (player.position.get(0).equals(rowFuture))
            player.position.set(1, colFuture);
        else
            player.position.set(0, rowFuture);
    }
}