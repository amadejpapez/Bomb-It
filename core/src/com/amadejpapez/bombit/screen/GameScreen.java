package com.amadejpapez.bombit.screen;

import com.amadejpapez.bombit.Bomb;
import com.amadejpapez.bombit.CellState;
import com.amadejpapez.bombit.GameManager;
import com.amadejpapez.bombit.Player;
import com.amadejpapez.bombit.assets.Assets;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import com.amadejpapez.bombit.CellActor;
import com.amadejpapez.bombit.BombIt;
import com.amadejpapez.bombit.config.GameConfig;

public class GameScreen extends ScreenAdapter {
    private final BombIt game;

    private Viewport viewport;
    private Viewport hudViewport;

    private Stage gameplayStage;
    private Stage hudStage;

    public static final List<List<CellActor>> bgCells = new ArrayList<>();
    public static final Table bgCellGrid = new Table();

    public static final List<List<CellActor>> cells = new ArrayList<>();
    public static final Table cellGrid = new Table();

    private final Map<Integer, Label> killLabels = new HashMap<>();
    private final Label timeLabel = new Label("", Assets.skin);

    private float lastAiMove;

    public GameScreen(BombIt game) {
        this.game = game;
    }

    @Override
    public void show() {
        resetAfterGameEnd();

        viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);
        hudViewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT);

        gameplayStage = new Stage(viewport, game.getBatch());
        hudStage = new Stage(hudViewport, game.getBatch());

        gameplayStage.addActor(createGridBackground());
        gameplayStage.addActor(createGridBombs());
        for (Player player : GameManager.players)
            gameplayStage.addActor(createGridPlayer(player));
        gameplayStage.addActor(createGridMain());
        gameplayStage.addActor(createMiddle());

        hudStage.addActor(createGridHud());

        gameplayStage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (GameManager.gameEnded)
                    return false;

                // PLAYER 1
                if (keycode == Input.Keys.UP
                        || keycode == Input.Keys.DOWN
                        || keycode == Input.Keys.LEFT
                        || keycode == Input.Keys.RIGHT
                ) {
                    GameManager.players.get(0).inputMove(keycode);
                } else if (keycode == Input.Keys.SPACE) {
                    GameManager.players.get(0).inputAddBomb();
                }

                // PLAYER 2
                if (GameManager.INSTANCE.getNumPhysicalPlayers() == 1)
                    return false;

                if (keycode == Input.Keys.W
                        || keycode == Input.Keys.S
                        || keycode == Input.Keys.A
                        || keycode == Input.Keys.D
                ) {
                    GameManager.players.get(1).inputMove(keycode);
                } else if (keycode == Input.Keys.X) {
                    GameManager.players.get(1).inputAddBomb();
                }

                return false;
            }
        });

        Gdx.input.setInputProcessor(new InputMultiplexer(gameplayStage, hudStage));

        GameManager.INSTANCE.playGameMusic();
        GameManager.gameStartedTime = TimeUtils.nanosToMillis(TimeUtils.nanoTime()) / 1000f;
        GameManager.gameEnded = false;
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
        updateStatus();
        Bomb.updateBombs();
        moveAllAi();
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
        Table table = new Table();
        bgCellGrid.defaults().size(GameConfig.CELL_SIZE);

        for (int i = 0; i < GameConfig.NUM_ROWS; i++) {
            bgCells.add(new ArrayList<>());
            for (int j = 0; j < GameConfig.NUM_COLUMNS; j++) {
                bgCells.get(i).add(new CellActor(CellState.FLOOR));
                bgCellGrid.add(bgCells.get(i).get(j));
            }
            bgCellGrid.row();
        }

        table.add(bgCellGrid).row();
        table.right();
        table.padRight(3);
        table.setFillParent(true);
        table.pack();

        return table;
    }

    private Actor createGridBombs() {
        Table table = new Table();
        Bomb.grid.defaults().size(GameConfig.CELL_SIZE);

        for (int i = 0; i < GameConfig.NUM_ROWS; i++) {
            Bomb.cells.add(new ArrayList<>());
            for (int j = 0; j < GameConfig.NUM_COLUMNS; j++) {
                Bomb.cells.get(i).add(new CellActor(CellState.EMPTY));
                Bomb.grid.add(Bomb.cells.get(i).get(j));
            }
            Bomb.grid.row();
        }

        table.add(Bomb.grid).row();
        table.right();
        table.padRight(3);
        table.setFillParent(true);
        table.pack();

        return table;
    }

    private Actor createGridPlayer(Player player) {
        Table table = new Table();
        player.grid.defaults().size(GameConfig.CELL_SIZE);

        for (int i = 0; i < GameConfig.NUM_ROWS; i++) {
            player.cells.add(new ArrayList<>());
            for (int j = 0; j < GameConfig.NUM_COLUMNS; j++) {
                player.cells.get(i).add(new CellActor(CellState.EMPTY));
                player.grid.add(player.cells.get(i).get(j));
            }
            player.grid.row();
        }

        table.add(player.grid).row();
        table.right();
        table.padRight(3);
        table.setFillParent(true);
        table.pack();

        return table;
    }

    private Actor createMiddle() {
        Image mid = new Image(Assets.middle);
        mid.setPosition(viewport.getWorldWidth() / 2f + 7f,
                viewport.getWorldHeight() / 2f - 6f);
        mid.setScale(0.035f);
        return mid;
    }

    private Actor createLost() {
        Image lost = new Image(Assets.youLose);
        lost.setPosition(viewport.getWorldWidth() / 2f - 13f,
                viewport.getWorldHeight() / 2f - 12f);
        lost.setScale(0.12f);
        return lost;
    }

    private Actor createWon() {
        Image won = new Image(Assets.youWin);
        won.setPosition(viewport.getWorldWidth() / 2f - 13f,
                viewport.getWorldHeight() / 2f - 12f);
        won.setScale(0.1f);
        return won;
    }

    private Actor createGridHud() {
        final Table table = new Table();
        table.padLeft(20);

        for (Player player : GameManager.players) {
            Image tmpImg = new Image(CellActor.getImageFromState(player.image));
            table.add(tmpImg).height(60).width(50).left().row();

            Label tmpLabel1 = new Label("Kills: " + player.getKills(), Assets.skin);
            tmpLabel1.setColor(Color.BROWN);
            table.add(tmpLabel1).left().row();

            killLabels.put(player.num, tmpLabel1);
        }

        timeLabel.setColor(Color.BLACK);
        table.add(timeLabel).padTop(30).left().row();

        TextButton quitButton = new TextButton("Exit Game", Assets.skin);
        quitButton.setColor(Color.ORANGE);
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new IntroScreen(game));
                GameManager.activeBombs.clear();
                GameManager.players.clear();
            }
        });

        table.add(quitButton).left().row();

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
                cells.get(i).add(new CellActor(CellState.EMPTY));
            }
        }

        for (int row = 0; row < GameConfig.NUM_ROWS; row++) {
            for (int column = 0; column < GameConfig.NUM_COLUMNS; column++) {
                List<Integer> location = List.of(row, column);

                for (Player player : GameManager.players) {
                    if (row == player.position.get(0) && column == player.position.get(1)) {
                        player.cells.get(row).get(column).setState(player.image);

                        if (Objects.equals(GameManager.INSTANCE.getGameMode(), "TILE_TAG"))
                            colorATile(row, column, player);
                    }
                }

                if (row == 0 || row == GameConfig.NUM_ROWS - 1 || column == 0 || column == GameConfig.NUM_COLUMNS - 1) {
                    cells.get(row).get(column).setState(CellState.CONE_RED);
                } else if (GameConfig.LOC_FIXED_OBSTACLES.contains(location)) {
                    int num = ThreadLocalRandom.current().nextInt(CellActor.FIXED_OBSTACLES.size());
                    cells.get(row).get(column).setState(CellActor.FIXED_OBSTACLES.get(num));
                } else if (!GameConfig.LOC_SKIP_OBSTACLES.contains(location)) {
                    int num = ThreadLocalRandom.current().nextInt(CellActor.TMP_OBSTACLES.size() + 3);
                    if (num < CellActor.TMP_OBSTACLES.size()) {
                        cells.get(row).get(column).setState(CellActor.TMP_OBSTACLES.get(num));
                    } else if (ThreadLocalRandom.current().nextInt(6) == 0) {
                        cells.get(row).get(column).setState(CellState.BONUS_BOMB);
                    } else if (ThreadLocalRandom.current().nextInt(13) == 0) {
                        cells.get(row).get(column).setState(CellState.BONUS_HAND);
                    }
                }

                cellGrid.add(cells.get(row).get(column));
            }
            cellGrid.row();
        }

        table.add(cellGrid).row();
        table.right();
        table.padRight(3);
        table.setFillParent(true);
        table.pack();

        return table;
    }

    private void updateStatus() {
        if (GameManager.gameEnded)
            return;

        for (Player player : GameManager.players) {
            if (Objects.equals(GameManager.INSTANCE.getGameMode(), "ARCADE"))
                killLabels.get(player.num).setText("Kills: " + player.getKills());
            else if (Objects.equals(GameManager.INSTANCE.getGameMode(), "TILE_TAG"))
                killLabels.get(player.num).setText("Tiles: " + player.tiles + "/50");
        }

        float currentTime = TimeUtils.nanosToMillis(TimeUtils.nanoTime()) / 1000f;
        int remainingTime = (int) (GameConfig.GAME_TIME - (currentTime - GameManager.gameStartedTime));
        timeLabel.setText("Time: " + remainingTime);

        if (remainingTime <= 0)
            timeIsUp();
    }

    public void timeIsUp() {
        ArrayList<Integer> allKills = new ArrayList<>();
        for (Player player : GameManager.players)
            allKills.add(player.getKills());

        int maxKill = Collections.max(allKills);
        // if everyone is at zero, no one wins
        if (maxKill == 0)
            maxKill = -1;

        for (Player player : GameManager.players) {
            if (player.getKills() == maxKill) {
                killLabels.get(player.num).setText("Kills: " + player.getKills() + " *");
                LeaderboardScreen.addResult(player);
            }
        }

        if (maxKill == allKills.get(0) || (GameManager.INSTANCE.getNumPhysicalPlayers() == 2 && maxKill == allKills.get(1)))
            gameplayStage.addActor(createWon());
        else
            gameplayStage.addActor(createLost());

        GameManager.gameEnded = true;
    }

    public void moveAllAi() {
        if (GameManager.gameEnded)
            return;

        float currentTime = TimeUtils.nanosToMillis(TimeUtils.nanoTime()) / 1000f;
        if (currentTime - lastAiMove < 0.5)
            return;
        lastAiMove = currentTime;

        for (Player player : GameManager.players)
            player.moveAi();
    }

    public static boolean isMainCellEmpty(int x, int y) {
        return cellGrid.getCell(cells.get(x).get(y)).getActor().isEmpty();
    }

    public static void colorATile(int x, int y, Player player) {
        CellState curState = bgCellGrid.getCell(bgCells.get(x).get(y)).getActor().getState();

        // if tile is already the same color
        if (curState == player.imageTile)
            return;

        // if tile has been colored before
        if (CellActor.COLORED_TILES.contains(curState)) {
            for (Player player2 : GameManager.players) {
                if (curState == player2.imageTile) {
                    player2.tiles--;
                    break;
                }
            }
        }

        // if tile has not been colored before
        bgCellGrid.getCell(bgCells.get(x).get(y)).getActor().setState(player.imageTile);
        player.tiles++;
    }

    public void resetAfterGameEnd() {
        cells.clear();
        cellGrid.clear();
        killLabels.clear();

        Bomb.grid.clear();
        Bomb.cells.clear();

        GameManager.activeBombs.clear();
    }
}
