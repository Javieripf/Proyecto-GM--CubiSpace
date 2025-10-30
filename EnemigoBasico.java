package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;

public class EnemigoBasico extends Enemigo {
    private static final float ESCALA = 0.3f;
    private int direccion;
    private Sound explosionSound;

    public EnemigoBasico(Texture textura, Sound explosionSound, float limiteX) {
        super(
            textura,
            MathUtils.random(0, PantallaJuego.WORLD_WIDTH - textura.getWidth() * ESCALA),
            PantallaJuego.WORLD_HEIGHT,
            100f, 80f, 1
        );

        float ancho = textura.getWidth() * ESCALA;
        float alto = textura.getHeight() * ESCALA;
        this.rect.setSize(ancho, alto);

        this.direccion = Math.random() < 0.5 ? -1 : 1;
        this.explosionSound = explosionSound;
    }

    @Override
    public void actualizar(float delta) {
        if (destruido) return;

        rect.y -= velocidadVertical * delta;
        rect.x += velocidadHorizontal * direccion * delta;

        float anchoPantalla = PantallaJuego.WORLD_WIDTH;

        if (rect.x <= 0 || rect.x + rect.width >= anchoPantalla) {
            direccion *= -1;
            rect.x = Math.max(0, Math.min(rect.x, anchoPantalla - rect.width));
        }

        if (rect.y + rect.height < 0) destruido = true;
    }

    @Override
    public void render(SpriteBatch batch) {
        if (!destruido && textura != null) {
            batch.draw(textura, rect.x, rect.y, rect.width, rect.height);
        }
    }

    public void colisionConJugador() {
        if (!destruido) {
            destruido = true;
            if (explosionSound != null) explosionSound.play(0.8f);
        }
    }
}
