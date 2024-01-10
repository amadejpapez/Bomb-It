package com.amadejpapez.bombit.screen;

import com.amadejpapez.bombit.BombIt;
import com.amadejpapez.bombit.GameManager;
import com.amadejpapez.bombit.assets.Assets;
import com.amadejpapez.bombit.config.GameConfig;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Map;

public class SettingsScreen extends ScreenAdapter {
    private final BombIt game;

    private Viewport viewport;
    private Stage stage;

    private TextButton onePhysicalPlayer;
    private TextButton twoPhysicalPlayers;

    private CheckBox computerPlayersEnabled;
    private CheckBox musicEnabled;
    private CheckBox soundsEnabled;

    public SettingsScreen(BombIt game) {
        this.game = game;
    }

    @Override
    public void show() {
        viewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT);
        stage = new Stage(viewport, game.getBatch());

        onePhysicalPlayer = new TextButton("1", Assets.skin);
        twoPhysicalPlayers = new TextButton("2", Assets.skin);

        CheckBoxStyle checkBoxStyle = new CheckBoxStyle();
        checkBoxStyle.fontColor = Color.BLACK;
        checkBoxStyle.font = Assets.font;

        computerPlayersEnabled = new CheckBox("On", checkBoxStyle);
        musicEnabled = new CheckBox("On", checkBoxStyle);
        soundsEnabled = new CheckBox("On", checkBoxStyle);

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
        table.setBackground(new TextureRegionDrawable(Assets.background));

        Table grid = new Table();
        grid.defaults().padLeft(30).padRight(30);

        // TITLE
        Label tmpLabel = new Label("Game Settings", Assets.skin);
        tmpLabel.setColor(Color.BROWN);
        grid.add(tmpLabel).padBottom(50).colspan(3).row();

        // PHYSICAL PLAYERS
        tmpLabel = new Label("Number of physical players:", Assets.skin);
        tmpLabel.setColor(Color.BLACK);
        grid.add(tmpLabel).left();

        Table tablePlayers = new Table();
        tablePlayers.defaults();

        onePhysicalPlayer.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameManager.INSTANCE.setNumPhysicalPlayers(1);
                checkDisabledEnabledButtons();
            }
        });
        twoPhysicalPlayers.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameManager.INSTANCE.setNumPhysicalPlayers(2);
                checkDisabledEnabledButtons();
            }
        });

        tablePlayers.add(onePhysicalPlayer);
        tablePlayers.add(twoPhysicalPlayers);
        grid.add(tablePlayers).padBottom(5).row();

        // GAME MODE
        tmpLabel = new Label("Select game mode:", Assets.skin);
        tmpLabel.setColor(Color.BLACK);
        grid.add(tmpLabel).left();

        SelectBox<String> selectBox = new SelectBox<String>(Assets.skin);
        selectBox.setColor(Color.BROWN);
        selectBox.setItems(GameConfig.GAME_MODES.values().toArray(new String[0]));
        selectBox.setSelected(GameConfig.GAME_MODES.get(GameManager.INSTANCE.getGameMode()));

        grid.add(selectBox).padBottom(5).left().row();

        // COMPUTER PLAYERS
        tmpLabel = new Label("Add computer players:", Assets.skin);
        tmpLabel.setColor(Color.BLACK);
        grid.add(tmpLabel).left();

        computerPlayersEnabled.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameManager.INSTANCE.setAddComputerPlayers(!GameManager.INSTANCE.getAddComputerPlayers());
                checkDisabledEnabledButtons();
            }
        });
        grid.add(computerPlayersEnabled).colspan(2).padBottom(5).row();

        // MUSIC
        tmpLabel = new Label("Music:", Assets.skin);
        tmpLabel.setColor(Color.BLACK);
        grid.add(tmpLabel).left();

        musicEnabled.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameManager.INSTANCE.setMusicEnabled(!GameManager.INSTANCE.getMusicEnabled());
                checkDisabledEnabledButtons();
                GameManager.INSTANCE.playStartMusic();
            }
        });
        grid.add(musicEnabled).colspan(2).padBottom(5).row();

        // SOUNDS
        tmpLabel = new Label("Sounds:", Assets.skin);
        tmpLabel.setColor(Color.BLACK);
        grid.add(tmpLabel).left();

        soundsEnabled.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameManager.INSTANCE.setSoundsEnabled(!GameManager.INSTANCE.getSoundsEnabled());
                checkDisabledEnabledButtons();
            }
        });
        grid.add(soundsEnabled).colspan(2).padBottom(5).row();

        // CONTINUE
        TextButton playButton = new TextButton("Continue", Assets.skin);
        playButton.setColor(Color.ORANGE);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                for (Map.Entry<String, String> entry: GameConfig.GAME_MODES.entrySet()) {
                    if (entry.getValue().equals(selectBox.getSelected()))
                        GameManager.INSTANCE.setGameMode(entry.getKey());
                }

                GameManager.generatePhysicalPlayers();
                game.setScreen(new SelectCharacterScreen(game, GameManager.players.get(0)));
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
        if (GameManager.INSTANCE.getNumPhysicalPlayers() == 1)
            onePhysicalPlayer.setColor(Color.ORANGE);
        else
            onePhysicalPlayer.setColor(Color.BROWN);

        if (GameManager.INSTANCE.getNumPhysicalPlayers() == 2)
            twoPhysicalPlayers.setColor(Color.ORANGE);
        else
            twoPhysicalPlayers.setColor(Color.BROWN);

        if (GameManager.INSTANCE.getAddComputerPlayers())
            computerPlayersEnabled.setText("On");
        else
            computerPlayersEnabled.setText("Off");

        if (GameManager.INSTANCE.getSoundsEnabled())
            soundsEnabled.setText("On");
        else
            soundsEnabled.setText("Off");

        if (GameManager.INSTANCE.getMusicEnabled())
            musicEnabled.setText("On");
        else
            musicEnabled.setText("Off");
    }
}