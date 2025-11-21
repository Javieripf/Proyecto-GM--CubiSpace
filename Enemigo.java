package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

// GM2.2 – Template Method
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

    // TEMPLATE METHOD: algoritmo fijo de actualización
    public final void actualizar(float delta) {
        if (!destruido) {
            mover(delta);      // paso 1: movimiento (definido por las subclases)
            atacar(delta);     // paso 2: ataque (hook opcional)
        }
        actualizarEstado(delta); // paso 3: lógica común/post-estado (hook opcional)
    }

    // TEMPLATE METHOD: algoritmo fijo de renderizado
    public final void render(SpriteBatch batch) {
        if (!destruido && textura != null) {
            batch.draw(textura, rect.x, rect.y, rect.width, rect.height);
        }
        renderExtra(batch); // hook para dibujar cosas adicionales (balas, efectos, etc.)
    }

    // 🔹 Pasos que deben definir las subclases
    protected abstract void mover(float delta);

    // 🔹 Hooks opcionales
    protected void atacar(float delta) {
        // por defecto no hace nada
    }

    protected void actualizarEstado(float delta) {
        // por defecto no hace nada
    }

    protected void renderExtra(SpriteBatch batch) {
        // por defecto no hace nada
    }

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

    // 🔹 Setter opcional
    public void setVida(int vida) {
        this.vida = vida;
    }
}
