package com.amadejpapez.bombit.screen;

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
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ScreenUtils;
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

    private static List<Result> results = new ArrayList<>();

    public LeaderboardScreen(BombIt game) {
        this.game = game;
        loadJson();
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

        Table contentTable = new Table(Assets.skin);

        contentTable.setBackground(new TextureRegionDrawable(Assets.backgroundLb));

        Label tmpLabel;

        tmpLabel = new Label("Leaderboard", Assets.skin);
        tmpLabel.setColor(Color.BROWN);
        contentTable.add(tmpLabel).padBottom(50).colspan(2).row();

        tmpLabel = new Label("Username:", Assets.skin);
        tmpLabel.setColor(Color.BLACK);
        contentTable.add(tmpLabel).padRight(50).left();

        tmpLabel = new Label("Num of kills:", Assets.skin);
        tmpLabel.setColor(Color.BLACK);
        contentTable.add(tmpLabel).right().row();

        for (int i = 0; i < results.size(); i++) {
            tmpLabel = new Label(results.get(i).username, Assets.skin);
            tmpLabel.setColor(Color.BLACK);
            contentTable.add(tmpLabel).left().padRight(20);

            tmpLabel = new Label(results.get(i).score.toString(), Assets.skin);
            tmpLabel.setColor(Color.BLACK);
            contentTable.add(tmpLabel).right().row();
        }

        contentTable.add(backButton).width(100).padTop(50).colspan(2);

        table.add(contentTable);
        table.center();
        table.setFillParent(true);
        table.pack();

        return table;
    }

    public static void addResult(Player player) {
        Result tmp = new Result();
        tmp.username = player.username;
        tmp.score = player.getKills();

        results.add(tmp);
        saveJson();
    }

    public static void saveJson() {
        FileHandle file = Gdx.files.local("leaderboard.json");
        Json json = new Json();

        results.sort(Comparator.comparing(Result::getScore).reversed());
        file.writeString(json.toJson(results), false);
    }

    public void loadJson() {
        FileHandle file = Gdx.files.local("leaderboard.json");

        if (!file.exists())
            saveJson();

        Json json = new Json();

        results = json.fromJson(ArrayList.class, file);
        results.sort(Comparator.comparing(Result::getScore).reversed());
    }
}
