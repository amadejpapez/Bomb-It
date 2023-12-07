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
import com.badlogic.gdx.scenes.scene2d.ui.Image;
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

public class MenuScreen extends ScreenAdapter {
    private final BombIt game;

    private Viewport viewport;
    private TextureAtlas gameplayAtlas;
    private Stage stage;
    private Skin skin;

    public MenuScreen(BombIt game) {
        this.game = game;
    }

    @Override
    public void show() {
        viewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT);
        stage = new Stage(viewport, game.getBatch());

        skin = Assets.assetManager.get(AssetDescriptors.SKIN);
        gameplayAtlas = Assets.assetManager.get(AssetDescriptors.GAMEPLAY);

        stage.addActor(createUi());
        Gdx.input.setInputProcessor(stage);
        stage.addActor(createTitle());
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

    private Actor createTitle() {
        Image title = new Image(gameplayAtlas.findRegion(AssetRegionNames.TITLE));
        title.setPosition(viewport.getWorldWidth() / 2f - title.getWidth() / 2f,
                viewport.getWorldHeight() - title.getHeight() - 100f);
        return title;
    }

    private Actor createUi() {
        Table table = new Table();
        table.defaults().pad(20).padTop(150);

        TextureRegion backgroundRegion = gameplayAtlas.findRegion(AssetRegionNames.BACKGROUND);
        table.setBackground(new TextureRegionDrawable(backgroundRegion));

        TextButton playButton = new TextButton("Play", skin);
        playButton.setColor(Color.ORANGE);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));
            }
        });

        TextButton leaderboardButton = new TextButton("Leaderboard", skin);
        leaderboardButton.setColor(Color.ORANGE);
        leaderboardButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LeaderboardScreen(game));
            }
        });

        TextButton quitButton = new TextButton("Quit", skin);
        quitButton.setColor(Color.ORANGE);
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        Table buttonTable = new Table();
        buttonTable.defaults().padLeft(30).padRight(30);

        buttonTable.add(playButton).padBottom(15).width(250).expandX().fill().row();
        buttonTable.add(leaderboardButton).padBottom(15).fillX().row();
        buttonTable.add(quitButton).fillX();

        buttonTable.center();

        table.add(buttonTable);
        table.center();
        table.setFillParent(true);
        table.pack();

        return table;
    }
}