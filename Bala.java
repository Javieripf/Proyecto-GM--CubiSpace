package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;

public class Bala implements Movible {
    private Texture textura;
    private Rectangle rect;
    private float velocidad = 500f;
    private float dx = 0, dy = 1;
    public static final float ESCALA = 0.1f;

    public Bala(Texture textura, float x, float y) {
        this.textura = textura;
        float ancho = textura.getWidth() * ESCALA;
        float alto = textura.getHeight() * ESCALA;
        this.rect = new Rectangle(x, y, ancho, alto);
    }

    @Override
    public void mover(float delta) {
        rect.y += dy * velocidad * delta;
    }

    public boolean fueraDePantalla(float worldWidth, float worldHeight) {
        return rect.y > worldHeight || rect.x < -rect.width || rect.x > worldWidth;
    }

    public void render(Batch batch) {
        batch.draw(textura, rect.x, rect.y, rect.width, rect.height);
    }

    public Rectangle getRect() { return rect; }
}

