package com.amadejpapez.bombit;

import com.amadejpapez.bombit.assets.Assets;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class BombIt extends ApplicationAdapter {
	SpriteBatch batch;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		Assets.load();
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		Assets.dispose();
	}
}
