package com.amadejpapez.bombit.screen;

import com.amadejpapez.bombit.BombIt;
import com.amadejpapez.bombit.GameManager;
import com.amadejpapez.bombit.Player;
import com.amadejpapez.bombit.assets.Assets;
import com.amadejpapez.bombit.config.GameConfig;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SelectCharacterScreen extends ScreenAdapter {
    private final BombIt game;

    private Viewport viewport;
    private Stage stage;

    private Map<String, TextureRegionDrawable> characters;
    private Image selectedPlayer;

    // 0 for the first player
    private final Integer numPlayer;

    public SelectCharacterScreen(BombIt game, Integer numPlayer) {
        this.game = game;
        this.numPlayer = numPlayer;
    }

    @Override
    public void show() {
        viewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT);
        stage = new Stage(viewport, game.getBatch());

        // remove image that was selected for the first player
        characters = new HashMap<>(Player.characterImages);
        if (numPlayer == 1)
            characters.remove(GameManager.playerCharacters.get(0).character);

        selectedPlayer = new Image(GameManager.playerCharacters.get(numPlayer).image);

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

        // TITLE
        Label tmpLabel = new Label("Player " + (numPlayer + 1), Assets.skin);
        tmpLabel.setColor(Color.BROWN);
        table.add(tmpLabel).padRight(20).row();

        // USERNAME
        Table tableUsername = new Table();
        tableUsername.defaults();

        tmpLabel = new Label("Enter username:", Assets.skin);
        tmpLabel.setColor(Color.BLACK);
        tableUsername.add(tmpLabel).padRight(20);

        TextFieldStyle textFieldStyle = new TextFieldStyle();
        textFieldStyle.font = Assets.skin.getFont("font");
        textFieldStyle.cursor = Assets.skin.get("default", TextFieldStyle.class).cursor;
        textFieldStyle.fontColor = Color.DARK_GRAY;

        TextField test = new TextField("Guest", Assets.skin);
        test.setMaxLength(15);
        test.setStyle(textFieldStyle);
        test.setFocusTraversal(true);
        tableUsername.add(test).row();

        tableUsername.center();
        table.add(tableUsername).padBottom(0).row();

        // SELECT CHARACTER
        Table tableCharacters = new Table();
        tableCharacters.defaults();

        tmpLabel = new Label("Select character:", Assets.skin);
        tmpLabel.setColor(Color.BLACK);
        tableCharacters.add(tmpLabel).padRight(20);

        for (Map.Entry<String, TextureRegionDrawable> entry : characters.entrySet()) {
            ImageButton tmp = new ImageButton(entry.getValue());
            tmp.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    GameManager.playerCharacters.get(numPlayer).updateImage(entry.getKey());
                    selectedPlayer.setDrawable(GameManager.playerCharacters.get(numPlayer).image);
                }
            });
            tableCharacters.add(tmp).height(60).width(60).padBottom(5);
        }

        tableCharacters.center();
        table.add(tableCharacters).padBottom(0).row();

        // STATUS
        Table tableStatus = new Table();
        tableStatus.defaults();

        tmpLabel = new Label("Selected:", Assets.skin);
        tmpLabel.setColor(Color.BLACK);
        tableStatus.add(tmpLabel).padRight(20);

        tableStatus.add(selectedPlayer).height(50).width(40).padRight(30);

        table.add(tableStatus).row();

        // BUTTON
        TextButton playButton;
        if (Objects.equals(numPlayer + 1, GameManager.INSTANCE.getNumPhysicalPlayers())) {
            playButton = new TextButton("Start game", Assets.skin);
            playButton.setColor(Color.ORANGE);
            playButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    GameManager.generateOtherPlayers();
                    game.setScreen(new GameScreen(game));
                }
            });
        } else {
            playButton = new TextButton("Continue", Assets.skin);
            playButton.setColor(Color.ORANGE);
            playButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.setScreen(new SelectCharacterScreen(game, 1));
                }
            });
        }

        table.add(playButton).padTop(50).width(250).expandX().fill().colspan(table.getColumns()).row();

        table.center();
        table.setFillParent(true);
        table.pack();

        return table;
    }
}