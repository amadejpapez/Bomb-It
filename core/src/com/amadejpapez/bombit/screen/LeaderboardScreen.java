package com.amadejpapez.bombit.screen;

import com.amadejpapez.bombit.GameManager;
import com.amadejpapez.bombit.Player;
import com.amadejpapez.bombit.Result;
import com.amadejpapez.bombit.assets.Assets;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import com.amadejpapez.bombit.BombIt;
import com.amadejpapez.bombit.config.GameConfig;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LeaderboardScreen extends ScreenAdapter {
    private final BombIt game;

    private Viewport viewport;
    private Stage stage;

    private static final List<Result> resultsArcade = readJson("ARCADE");
    private static final List<Result> resultsTileTag = readJson("TILE_TAG");

    private static Table contentTable;
    private static final Table resultsTable = new Table();

    public LeaderboardScreen(BombIt game) {
        this.game = game;
    }

    @Override
    public void show() {
        viewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT);
        stage = new Stage(viewport, game.getBatch());

        stage.addActor(createUi());
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0f, 0f, 0f, 0f);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    private Actor createUi() {
        Table table = new Table();
        table.defaults().pad(20);

        table.setBackground(new TextureRegionDrawable(Assets.background));

        TextButton backButton = new TextButton("Back", Assets.skin);
        backButton.setColor(Color.ORANGE);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game));
            }
        });

        contentTable = new Table(Assets.skin);
        contentTable.setBackground(new TextureRegionDrawable(Assets.backgroundLb));

        Label tmpLabel = new Label("Leaderboard - Top 8", Assets.skin);
        tmpLabel.setColor(Color.BROWN);
        contentTable.add(tmpLabel).padBottom(50).colspan(2).row();

        SelectBox<String> gameModeDropdown = new SelectBox<>(Assets.skin);
        gameModeDropdown.setColor(Color.BROWN);
        gameModeDropdown.setItems("Arcade", "Tile Tag");
        gameModeDropdown.setSelected("Arcade");
        gameModeDropdown.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (gameModeDropdown.getSelected().equals("Arcade"))
                    updateResultsTable(resultsArcade);
                else if (gameModeDropdown.getSelected().equals("Tile Tag"))
                    updateResultsTable(resultsTileTag);
            }
        });

        contentTable.add(gameModeDropdown).padBottom(5).colspan(2).row();

        contentTable.add(resultsTable).width(100).padTop(20).colspan(2).row();
        updateResultsTable(resultsArcade);

        contentTable.add(backButton).width(100).padTop(50).colspan(2).row();

        table.add(contentTable);
        table.center();
        table.setFillParent(true);
        table.pack();

        return table;
    }

    public void updateResultsTable(List<Result> results) {
        Table cell = contentTable.getCell(resultsTable).getActor();
        cell.clear();

        Label tmpLabel = new Label("Username:", Assets.skin);
        tmpLabel.setColor(Color.DARK_GRAY);
        cell.add(tmpLabel).padRight(50).padBottom(10).left();

        tmpLabel = new Label("Number of kills:", Assets.skin);
        tmpLabel.setColor(Color.DARK_GRAY);
        if (results == resultsTileTag)
            tmpLabel.setText("Speed to color " + GameConfig.TAG_TILES_GOAL + " tiles:");
        cell.add(tmpLabel).right().padBottom(10).row();

        for (int i = 0; i < results.size(); i++) {
            if (i >= 8)
                break;

            tmpLabel = new Label(results.get(i).username, Assets.skin);
            tmpLabel.setColor(Color.BLACK);
            cell.add(tmpLabel).left().padRight(20);

            tmpLabel = new Label(results.get(i).score.toString(), Assets.skin);
            tmpLabel.setColor(Color.BLACK);
            cell.add(tmpLabel).right().row();
        }
    }

    public static void addResult(Player player, String gameMode) {
        Result tmp = new Result();
        tmp.username = player.username;

        if (gameMode.equals("ARCADE")) {
            tmp.score = player.getKills();
            resultsArcade.add(tmp);
            saveJson(gameMode, resultsArcade);
        }
        else if (gameMode.equals("TILE_TAG")) {
            float currentTime = TimeUtils.nanosToMillis(TimeUtils.nanoTime()) / 1000f;
            tmp.score = (int) (currentTime - GameManager.gameStartedTime);
            resultsTileTag.add(tmp);
            saveJson(gameMode, resultsTileTag);
        }
    }

    public static void saveJson(String gameMode, List<Result> results) {
        FileHandle file = Gdx.files.local("leaderboard_" + gameMode.toLowerCase() + ".json");
        Json json = new Json();

        if (gameMode.equals("ARCADE"))
            results.sort(Comparator.comparing(Result::getScore).reversed());
        else if (gameMode.equals("TILE_TAG"))
            results.sort(Comparator.comparing(Result::getScore));

        file.writeString(json.toJson(results), false);
    }

    public static List<Result> readJson(String gameMode) {
        FileHandle file = Gdx.files.local("leaderboard_" + gameMode.toLowerCase() + ".json");

        if (!file.exists())
            saveJson(gameMode, new ArrayList<>());

        Json json = new Json();

        List<Result> tmpResults = json.fromJson(ArrayList.class, file);

        if (gameMode.equals("ARCADE"))
            tmpResults.sort(Comparator.comparing(Result::getScore).reversed());
        else if (gameMode.equals("TILE_TAG"))
            tmpResults.sort(Comparator.comparing(Result::getScore));

        return tmpResults;
    }
}
