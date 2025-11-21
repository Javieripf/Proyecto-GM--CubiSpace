package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

import java.util.List;

public class DisparoSimpleStrategy implements EstrategiaDisparo {

    private static final float ESCALA_LASER = 0.05f;

    @Override
    public void disparar(List<BalaEnemigo> balas, Texture texLaser, Rectangle rect) {
        if (texLaser == null || rect == null) return;

        float x = rect.x + rect.width / 2f - texLaser.getWidth() * ESCALA_LASER;
        float y = rect.y;

        balas.add(new BalaEnemigo(texLaser, x, y));
    }
}
