package com.amadejpapez.bombit;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ParticleEffectActor extends Actor {
    private final ParticleEffect particleEffect;

    public ParticleEffectActor(ParticleEffect particleEffect) {
        this.particleEffect = particleEffect;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        particleEffect.update(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (!particleEffect.isComplete())
            particleEffect.draw(batch);
    }
}