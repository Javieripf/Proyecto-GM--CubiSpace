package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;

public class EnemigoBasico extends Enemigo {

    private static final float ESCALA = 0.3f;
    private int direccion;
    private Sound explosionSound;

    public EnemigoBasico(Texture textura, Sound explosionSound, float limiteX, float factorDificultad) {
        super(
                textura,
                MathUtils.random(0, PantallaJuego.WORLD_WIDTH - textura.getWidth() * ESCALA),
                PantallaJuego.WORLD_HEIGHT,
                100f * factorDificultad, // velocidad vertical escalada
                80f * factorDificultad,  // velocidad horizontal escalada
                1
        );

        float ancho = textura.getWidth() * ESCALA;
        float alto = textura.getHeight() * ESCALA;
        this.rect.setSize(ancho, alto);

        this.direccion = Math.random() < 0.5 ? -1 : 1;
        this.explosionSound = explosionSound;
    }

    // Implementa solo el paso de movimiento del Template Method
    @Override
    protected void mover(float delta) {
        rect.y -= velocidadVertical * delta;
        rect.x += velocidadHorizontal * direccion * delta;

        float anchoPantalla = PantallaJuego.WORLD_WIDTH;
        if (rect.x <= 0 || rect.x + rect.width >= anchoPantalla) {
            direccion *= -1;
            rect.x = Math.max(0, Math.min(anchoPantalla - rect.width, rect.x));
        }

        if (rect.y + rect.height < 0) {
            destruido = true;
        }
    }

    public void colisionConJugador() {
        if (!destruido) {
            destruido = true;
            if (explosionSound != null) explosionSound.play(0.8f);
        }
    }
}
