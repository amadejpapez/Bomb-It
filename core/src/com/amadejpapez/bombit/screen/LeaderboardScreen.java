package com.amadejpapez.bombit.screen;

import com.amadejpapez.bombit.Result;
import com.amadejpapez.bombit.assets.Assets;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import com.amadejpapez.bombit.assets.AssetDescriptors;
import com.amadejpapez.bombit.assets.AssetRegionNames;
import com.amadejpapez.bombit.BombIt;
import com.amadejpapez.bombit.config.GameConfig;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LeaderboardScreen extends ScreenAdapter {
    private final BombIt game;

    private Viewport viewport;
    private Stage stage;

    private List<Result> results;

    public LeaderboardScreen(BombIt game) {
        this.game = game;

        results = new ArrayList<>();
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

        Skin uiSkin = Assets.assetManager.get(AssetDescriptors.SKIN);
        TextureAtlas gameplayAtlas = Assets.assetManager.get(AssetDescriptors.GAMEPLAY);

        TextureRegion backgroundRegion = gameplayAtlas.findRegion(AssetRegionNames.BACKGROUND);
        table.setBackground(new TextureRegionDrawable(backgroundRegion));

        TextButton backButton = new TextButton("Back", uiSkin);
        backButton.setColor(Color.ORANGE);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game));
            }
        });

        Table contentTable = new Table(uiSkin);

        TextureRegion menuBackground = gameplayAtlas.findRegion(AssetRegionNames.BACKGROUND_LB);
        contentTable.setBackground(new TextureRegionDrawable(menuBackground));

        Label tmpLabel;

        tmpLabel = new Label("Leaderboard", uiSkin);
        tmpLabel.setColor(Color.BROWN);
        contentTable.add(tmpLabel).padBottom(50).colspan(2).row();

        tmpLabel = new Label("Player:", uiSkin);
        tmpLabel.setColor(Color.BLACK);
        contentTable.add(tmpLabel).left();

        tmpLabel = new Label("Num of kills:", uiSkin);
        tmpLabel.setColor(Color.BLACK);
        contentTable.add(tmpLabel).right().row();

        for (int i = 0; i < 7; i++) {
            tmpLabel = new Label(results.get(i).name, uiSkin);
            tmpLabel.setColor(Color.BLACK);
            contentTable.add(tmpLabel).left().padRight(20);

            tmpLabel = new Label(results.get(i).score.toString(), uiSkin);
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

    public void addResult(Integer num) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd_MM_yyyy_H:m");
        String name = "game_" + LocalDateTime.now().format(formatter);

        Result tmp = new Result();
        tmp.name = name;
        tmp.score = num;

        results.add(tmp);
        saveJson();
    }

    public void saveJson() {
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
