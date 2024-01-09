package com.amadejpapez.bombit.screen;

import com.amadejpapez.bombit.BombIt;
import com.amadejpapez.bombit.CellActor;
import com.amadejpapez.bombit.CellState;
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

import java.util.ArrayList;
import java.util.List;

public class SelectCharacterScreen extends ScreenAdapter {
    private final BombIt game;

    private Viewport viewport;
    private Stage stage;

    private List<CellState> availablePlayers;
    private Image selectedImage;

    private final Player player;

    public SelectCharacterScreen(BombIt game, Player player) {
        this.game = game;
        this.player = player;
    }

    @Override
    public void show() {
        viewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT);
        stage = new Stage(viewport, game.getBatch());

        // remove image that was selected for the first player
        availablePlayers = new ArrayList<>(CellActor.PLAYERS);
        if (player.num == 1)
            availablePlayers.remove(GameManager.players.get(0).image);

        selectedImage = new Image(CellActor.getImageFromState(player.image));

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
        Label tmpLabel = new Label("Player " + (player.num + 1), Assets.skin);
        tmpLabel.setColor(Color.BROWN);
        table.add(tmpLabel).padRight(20).padBottom(40).row();

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

        TextField usernameInput = new TextField("Guest", Assets.skin);
        usernameInput.setMaxLength(15);
        usernameInput.setStyle(textFieldStyle);
        tableUsername.add(usernameInput).row();

        tableUsername.center();
        table.add(tableUsername).row();

        // SELECT CHARACTER
        Table tableCharacters = new Table();
        tableCharacters.defaults();

        tmpLabel = new Label("Select character:", Assets.skin);
        tmpLabel.setColor(Color.BLACK);
        tableCharacters.add(tmpLabel).padRight(20);

        for (CellState state : availablePlayers) {
            ImageButton tmp = new ImageButton(new TextureRegionDrawable(CellActor.getImageFromState(state)));
            tmp.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    player.updateImage(state);
                    selectedImage.setDrawable(new TextureRegionDrawable(CellActor.getImageFromState(state)));
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

        tableStatus.add(selectedImage).height(50).width(40).padRight(30);

        table.add(tableStatus).row();

        // BUTTON
        TextButton playButton;
        if (player.num + 1 == GameManager.INSTANCE.getNumPhysicalPlayers()) {
            playButton = new TextButton("Start game", Assets.skin);
            playButton.setColor(Color.ORANGE);
            playButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    player.username = usernameInput.getText();
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
                    player.username = usernameInput.getText();
                    game.setScreen(new SelectCharacterScreen(game, GameManager.players.get(1)));
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