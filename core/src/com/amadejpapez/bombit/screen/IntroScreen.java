package com.amadejpapez.bombit.screen;

import com.amadejpapez.bombit.BombIt;
import com.amadejpapez.bombit.assets.Assets;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.concurrent.ThreadLocalRandom;

import com.amadejpapez.bombit.config.GameConfig;

public class IntroScreen extends ScreenAdapter {
    private final BombIt game;

    private OrthographicCamera camera;
    private Viewport viewport;
    private Stage stage;

    public static final float INTRO_DURATION_IN_SEC = 3f;   // duration of the (intro) animation
    private float duration = 0f;

    public IntroScreen(BombIt game) {
        this.game = game;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT);

        viewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT, camera);
        stage = new Stage(viewport, game.getBatch());

        Assets.load();

        stage.addActor(createBackground());
        stage.addActor(createAnimationTitle());

        stage.addActor(createAnimationPlayerBlack());
        stage.addActor(createAnimationPlayerGreen());
        stage.addActor(createAnimationPlayerOrange());
        stage.addActor(createAnimationPlayerPink());
        stage.addActor(createAnimationPlayerBlue());
        stage.addActor(createAnimationPlayerPurple());
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0f, 0f, 0f, 0f);

        duration += delta;

        // go to the MenuScreen after INTRO_DURATION_IN_SEC seconds
        if (duration > INTRO_DURATION_IN_SEC)
            game.setScreen(new MenuScreen(game));

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

    private Actor createBackground() {
        Image bg = new Image(Assets.background);
        bg.setSize(GameConfig.WIDTH, GameConfig.HEIGHT);
        bg.setPosition(0, 0);
        return bg;
    }

    private Actor createAnimationTitle() {
        Image title = new Image(Assets.title);

        // set positions x, y to center the image to the center of the window
        float posX = (viewport.getWorldWidth() / 2f) - title.getWidth() / 2f;
        float posY = (viewport.getWorldHeight() / 2f) - title.getHeight() / 2f;

        title.setOrigin(Align.center);
        title.addAction(
                /* animationDuration = Actions.sequence + Actions.rotateBy + Actions.scaleTo
                                      = 0 + 0 + + 1.5 + 1.5 = 3 sec */
                Actions.sequence(
                        Actions.moveTo(posX, 0, 0f),   // // move image to the center of the window
                        Actions.scaleTo(0.1f, 0.1f, 0f),    // "minimize"/"hide" image
                        Actions.sequence(
                                Actions.moveTo(posX, posY, 1.5f),   // // move image to the center of the window
                                Actions.scaleTo(1.3f, 1.3f, 1.5f)    // "minimize"/"hide" image
                        ),
                        Actions.removeActor()   // // remove image
                )
        );

        return title;
    }

    private Actor createAnimationPlayerBlack() {
        Image player = new Image(Assets.playerBlack);

        float posX = ThreadLocalRandom.current().nextFloat(0f, GameConfig.WIDTH / 2f);
        float posY = ThreadLocalRandom.current().nextFloat(0f, GameConfig.HEIGHT / 2f);

        player.setOrigin(Align.center);
        player.addAction(
                Actions.sequence(
                        Actions.moveTo(posX, posY, 0f),
                        Actions.scaleTo(0.1f, 0.1f, 0f),
                        Actions.parallel(
                                Actions.rotateBy(360, 3f),
                                Actions.moveTo(posX + 300f, posY + 100f, 3f)
                        ),
                        Actions.removeActor()
                )
        );

        return player;
    }

    private Actor createAnimationPlayerGreen() {
        Image player = new Image(Assets.playerGreen);

        float posX = ThreadLocalRandom.current().nextFloat(0f, GameConfig.WIDTH / 2f);
        float posY = ThreadLocalRandom.current().nextFloat(0f, GameConfig.HEIGHT / 2f);

        player.setOrigin(Align.center);
        player.addAction(
                Actions.sequence(
                        Actions.moveTo(posX, posY, 0f),
                        Actions.scaleTo(0.1f, 0.1f, 0f),
                        Actions.parallel(
                                Actions.rotateBy(-360, 3f),
                                Actions.moveTo(posX - 100f, posY - 100f, 3f)
                        ),
                        Actions.removeActor()
                )
        );

        return player;
    }

    private Actor createAnimationPlayerOrange() {
        Image player = new Image(Assets.playerOrange);

        float posX = ThreadLocalRandom.current().nextFloat(0f, GameConfig.WIDTH / 2f);
        float posY = ThreadLocalRandom.current().nextFloat(0f, GameConfig.HEIGHT / 2f);

        player.setOrigin(Align.center);
        player.addAction(
                Actions.sequence(
                        Actions.moveTo(posX, posY, 0f),
                        Actions.scaleTo(0.1f, 0.1f, 0f),
                        Actions.parallel(
                                Actions.rotateBy(360, 3f),
                                Actions.moveTo(posX + 200f, posY - 100f, 3f)
                        ),
                        Actions.removeActor()
                )
        );

        return player;
    }

    private Actor createAnimationPlayerPink() {
        Image player = new Image(Assets.playerPink);

        float posX = ThreadLocalRandom.current().nextFloat(0f, GameConfig.WIDTH / 2f);
        float posY = ThreadLocalRandom.current().nextFloat(0f, GameConfig.HEIGHT / 2f);

        player.setOrigin(Align.center);
        player.addAction(
                Actions.sequence(
                        Actions.moveTo(posX, posY, 0f),
                        Actions.scaleTo(0.1f, 0.1f, 0f),
                        Actions.parallel(
                                Actions.rotateBy(-360, 3f),
                                Actions.moveTo(posX - 150f, posY + 50f, 3f)
                        ),
                        Actions.removeActor()
                )
        );

        return player;
    }

    private Actor createAnimationPlayerBlue() {
        Image player = new Image(Assets.playerBlue);

        float posX = ThreadLocalRandom.current().nextFloat(0f, GameConfig.WIDTH / 2f);
        float posY = ThreadLocalRandom.current().nextFloat(0f, GameConfig.HEIGHT / 2f);

        player.setOrigin(Align.center);
        player.addAction(
                Actions.sequence(
                        Actions.moveTo(posX, posY, 0f),
                        Actions.scaleTo(0.1f, 0.1f, 0f),
                        Actions.parallel(
                                Actions.rotateBy(360, 3f),
                                Actions.moveTo(posX + 150f, posY - 100f, 3f)
                        ),
                        Actions.removeActor()
                )
        );

        return player;
    }

    private Actor createAnimationPlayerPurple() {
        Image player = new Image(Assets.playerPurple);

        float posX = ThreadLocalRandom.current().nextFloat(0f, GameConfig.WIDTH / 2f);
        float posY = ThreadLocalRandom.current().nextFloat(0f, GameConfig.HEIGHT / 2f);

        player.setOrigin(Align.center);
        player.addAction(
                Actions.sequence(
                        Actions.moveTo(posX, posY, 0f),
                        Actions.scaleTo(0.1f, 0.1f, 0f),
                        Actions.parallel(
                                Actions.rotateBy(-360, 3f),
                                Actions.moveTo(posX - 100f, posY + 100f, 3f)
                        ),
                        Actions.removeActor()
                )
        );

        return player;
    }
}