package com.amadejpapez.bombit.screen;

import com.amadejpapez.bombit.BombIt;
import com.amadejpapez.bombit.GameManager;
import com.amadejpapez.bombit.assets.AssetDescriptors;
import com.amadejpapez.bombit.assets.AssetRegionNames;
import com.amadejpapez.bombit.assets.Assets;
import com.amadejpapez.bombit.config.GameConfig;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class SettingsScreen extends ScreenAdapter {
    private final BombIt game;

    private Viewport viewport;
    private TextureAtlas gameplayAtlas;
    private Stage stage;
    private Skin skin;

    private TextButton onePhysicalPlayer;
    private TextButton twoPhysicalPlayers;

    private CheckBox computerPlayersEnabled;
    private CheckBoxStyle checkBoxStyle;

    public SettingsScreen(BombIt game) {
        this.game = game;
    }

    @Override
    public void show() {
        viewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT);
        stage = new Stage(viewport, game.getBatch());

        skin = Assets.assetManager.get(AssetDescriptors.SKIN);
        gameplayAtlas = Assets.assetManager.get(AssetDescriptors.GAMEPLAY);

        onePhysicalPlayer = new TextButton("1", skin);
        twoPhysicalPlayers = new TextButton("2", skin);

        checkBoxStyle = new CheckBoxStyle();
        checkBoxStyle.fontColor = Color.BLACK;
        checkBoxStyle.font = Assets.assetManager.get(AssetDescriptors.FONT);
        computerPlayersEnabled = new CheckBox("On", checkBoxStyle);

        checkDisabledEnabledButtons();

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
        table.setBackground(new TextureRegionDrawable(gameplayAtlas.findRegion(AssetRegionNames.BACKGROUND)));

        Table grid = new Table();
        grid.defaults().padLeft(30).padRight(30);

        // TITLE
        Label tmpLabel = new Label("Gameplay", skin);
        tmpLabel.setColor(Color.BROWN);
        grid.add(tmpLabel).padBottom(50).colspan(3).row();

        // PHYSICAL PLAYERS
        tmpLabel = new Label("Number of physical players:", skin);
        tmpLabel.setColor(Color.BLACK);
        grid.add(tmpLabel);

        Table tablePlayers = new Table();
        tablePlayers.defaults();

        onePhysicalPlayer.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameManager.numPhysicalPlayers = 1;
                checkDisabledEnabledButtons();
            }
        });
        twoPhysicalPlayers.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameManager.numPhysicalPlayers = 2;
                checkDisabledEnabledButtons();
            }
        });

        tablePlayers.add(onePhysicalPlayer);
        tablePlayers.add(twoPhysicalPlayers);
        grid.add(tablePlayers).padBottom(5).row();

        // COMPUTER PLAYERS
        tmpLabel = new Label("Add computer players:", skin);
        tmpLabel.setColor(Color.BLACK);
        grid.add(tmpLabel);

        computerPlayersEnabled.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameManager.addComputerPlayers = !GameManager.addComputerPlayers;
                checkDisabledEnabledButtons();
            }
        });
        grid.add(computerPlayersEnabled).colspan(2).padBottom(5).row();

        // CONTINUE
        TextButton playButton = new TextButton("Continue", skin);
        playButton.setColor(Color.ORANGE);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameManager.generatePhysicalPlayers();
                game.setScreen(new SelectCharacterScreen(game, 0));
            }
        });
        grid.add(playButton).padTop(50).width(250).expandX().fill().colspan(grid.getColumns()).row();

        // FINISH
        grid.center();
        table.add(grid);
        table.center();
        table.setFillParent(true);
        table.pack();

        return table;
    }

    private void checkDisabledEnabledButtons() {
        if (GameManager.numPhysicalPlayers == 1)
            onePhysicalPlayer.setColor(Color.ORANGE);
        else
            onePhysicalPlayer.setColor(Color.BROWN);

        if (GameManager.numPhysicalPlayers == 2)
            twoPhysicalPlayers.setColor(Color.ORANGE);
        else
            twoPhysicalPlayers.setColor(Color.BROWN);

        if (GameManager.addComputerPlayers)
            computerPlayersEnabled.setText("On");
        else
            computerPlayersEnabled.setText("Off");
    }
}