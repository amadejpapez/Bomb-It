package com.amadejpapez.bombit.screen;

import com.amadejpapez.bombit.assets.Assets;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
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
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import com.amadejpapez.bombit.assets.AssetDescriptors;
import com.amadejpapez.bombit.assets.AssetRegionNames;
import com.amadejpapez.bombit.BombIt;
import com.amadejpapez.bombit.config.GameConfig;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class LeaderboardScreen extends ScreenAdapter {
    private final BombIt game;

    private Viewport viewport;
    private Stage stage;

    private final HashMap<String, Integer> sortedMap;

    public LeaderboardScreen(BombIt game) {
        this.game = game;

        HashMap<String, Integer> results = new HashMap<>();

        results.put("BlackHole9", 2);
        results.put("BlackHole10", 0);
        results.put("BlackHole", 25);
        results.put("BlackHole2", 17);
        results.put("BlackHole1", 18);
        results.put("BlackHole8", 4);

        sortedMap =
                results.entrySet().stream()
                        .sorted(Entry.<String, Integer>comparingByValue().reversed())
                        .collect(
                                Collectors.toMap(
                                        Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new
                                ));
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
        tmpLabel.setColor(Color.BLACK);
        contentTable.add(tmpLabel).padBottom(50).colspan(2).row();

        tmpLabel = new Label("Player:", uiSkin);
        tmpLabel.setColor(Color.BLACK);
        contentTable.add(tmpLabel).left();

        tmpLabel = new Label("Num of kills:", uiSkin);
        tmpLabel.setColor(Color.BLACK);
        contentTable.add(tmpLabel).right().row();

        for (Map.Entry<String, Integer> set : sortedMap.entrySet()) {
            tmpLabel = new Label(set.getKey(), uiSkin);
            tmpLabel.setColor(Color.BLACK);
            contentTable.add(tmpLabel).left();

            tmpLabel = new Label(set.getValue().toString(), uiSkin);
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
}
