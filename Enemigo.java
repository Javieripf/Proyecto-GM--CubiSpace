package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class Enemigo {
    // 🔒 Encapsulación y herencia segura
    protected Texture textura;
    protected Rectangle rect;
    protected float velocidadVertical;
    protected float velocidadHorizontal;
    protected int vida;
    protected boolean destruido;

    public Enemigo(Texture textura, float x, float y, float velocidadV, float velocidadH, int vida) {
        this.textura = textura;
        this.velocidadVertical = velocidadV;
        this.velocidadHorizontal = velocidadH;
        this.vida = vida;

        if (textura != null) {
            this.rect = new Rectangle(x, y, textura.getWidth(), textura.getHeight());
        } else {
            this.rect = new Rectangle(x, y, 50, 50); // evita null si textura no se cargó
        }

        this.destruido = false;
    }

    // 🔹 Métodos abstractos
    public abstract void actualizar(float delta);
    public abstract void render(SpriteBatch batch);

    // 🔹 Lógica común
    public void recibirDano(int dano) {
        vida -= dano;
        if (vida <= 0) destruido = true;
    }

    // 🔹 Getters
    public Rectangle getRect() {
        return rect;
    }

    public boolean estaDestruido() {
        return destruido;
    }

    public int getVida() {
        return vida;
    }

    // 🔹 Setter opcional (por si luego necesitas modificar)
    public void setVida(int vida) {
        this.vida = vida;
    }
}