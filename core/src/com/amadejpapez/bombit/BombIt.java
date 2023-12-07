package com.amadejpapez.bombit;

import com.amadejpapez.bombit.assets.Assets;
import com.amadejpapez.bombit.screen.IntroScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BombIt extends Game {
	SpriteBatch batch;

	@Override
	public void create () {
		batch = new SpriteBatch();
		Assets.load();

		setScreen(new IntroScreen(this));
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		Assets.dispose();
	}

	public SpriteBatch getBatch() {
		return batch;
	}
}
