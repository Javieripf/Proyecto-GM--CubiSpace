package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class BalaEnemigo implements Movible {
    private Texture textura;
    private Rectangle rect;
    private Vector2 velocidad;
    private float escala = 0.15f;

    public BalaEnemigo(Texture textura, float x, float y) {
        this.textura = textura;
        float ancho = textura.getWidth() * escala;
        float alto = textura.getHeight() * escala;
        this.rect = new Rectangle(x, y, ancho, alto);
        this.velocidad = new Vector2(0f, -250f);
    }

    public BalaEnemigo(Texture textura, float x, float y, float angulo, float velocidadBase) {
        this.textura = textura;
        float ancho = textura.getWidth() * escala;
        float alto = textura.getHeight() * escala;
        this.rect = new Rectangle(x, y, ancho, alto);
        float rad = (float) Math.toRadians(angulo);
        this.velocidad = new Vector2((float) Math.sin(rad) * velocidadBase, -(float) Math.cos(rad) * velocidadBase);
    }

    @Override
    public void mover(float delta) {
        rect.x += velocidad.x * delta;
        rect.y += velocidad.y * delta;
    }

    public boolean fueraDePantalla(float worldWidth, float worldHeight) {
        return rect.y + rect.height < 0 || rect.x + rect.width < 0 || rect.x > worldWidth;
    }

    public void render(Batch batch) {
        batch.draw(textura, rect.x, rect.y, rect.width, rect.height);
    }

    public Rectangle getRect() { return rect; }
}
