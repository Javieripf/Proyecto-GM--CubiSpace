package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

import java.util.List;

public class DisparoTripleStrategy implements EstrategiaDisparo {

    private static final float ESCALA_LASER = 0.05f;
    private final float velocidadBase;
    private final float anguloDesviacion;

    public DisparoTripleStrategy() {
        this(300f, 20f);
    }

    public DisparoTripleStrategy(float velocidadBase, float anguloDesviacion) {
        this.velocidadBase = velocidadBase;
        this.anguloDesviacion = anguloDesviacion;
    }

    @Override
    public void disparar(List<BalaEnemigo> balas, Texture texLaser, Rectangle rect) {
        if (texLaser == null || rect == null) return;

        float x = rect.x + rect.width / 2f - texLaser.getWidth() * ESCALA_LASER;
        float y = rect.y;

        balas.add(new BalaEnemigo(texLaser, x, y, -anguloDesviacion, velocidadBase));
        balas.add(new BalaEnemigo(texLaser, x, y, 0, velocidadBase));
        balas.add(new BalaEnemigo(texLaser, x, y, anguloDesviacion, velocidadBase));
    }
}