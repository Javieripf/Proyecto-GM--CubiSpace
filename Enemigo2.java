package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.audio.Sound;

import java.util.ArrayList;
import java.util.Iterator;

public class Enemigo2 extends Enemigo {

    private static final float ESCALA = 0.35f;

    private Texture texLaser;
    private Sound shootSound;
    private Sound explosionSound;

    private ArrayList<BalaEnemigo> balas;
    private float tiempoDisparo = 0f;
    private static final float TIEMPO_ENTRE_DISPAROS = 2.5f;

    private boolean bajando = true;
    private float alturaObjetivo;
    private int direccion = 1;

    private boolean disparoFinalRealizado = false;
    private float tiempoPostMuerte = 0f;
    private static final float TIEMPO_DESAPARICION = 0.3f;

    private float limiteX;

    // GM2.3 – Estrategias de disparo
    private EstrategiaDisparo estrategiaDisparoNormal;
    private EstrategiaDisparo estrategiaDisparoMuerte;

    public Enemigo2(Texture textura, Texture texLaser, Sound shootSound, Sound explosionSound, float limiteX, float factorDificultad) {
        super(
                textura,
                (float) Math.random() * (PantallaJuego.WORLD_WIDTH - textura.getWidth() * ESCALA),
                PantallaJuego.WORLD_HEIGHT,
                100f * factorDificultad, // velocidad vertical escalada
                50f * factorDificultad,  // velocidad horizontal escalada
                3
        );

        float ancho = textura.getWidth() * ESCALA;
        float alto = textura.getHeight() * ESCALA;
        this.rect.setSize(ancho, alto);

        this.limiteX = limiteX;
        this.texLaser = texLaser;
        this.shootSound = shootSound;
        this.explosionSound = explosionSound;

        this.balas = new ArrayList<>();
        this.alturaObjetivo = (PantallaJuego.WORLD_HEIGHT * 0.5f) + (float) Math.random() * 50f;

        // Estrategias de disparo configurables
        this.estrategiaDisparoNormal = new DisparoSimpleStrategy();
        this.estrategiaDisparoMuerte = new DisparoTripleStrategy();
    }

    // Paso de movimiento (Template Method)
    @Override
    protected void mover(float delta) {
        if (bajando) {
            rect.y -= velocidadVertical * delta;
            if (rect.y <= alturaObjetivo) {
                rect.y = alturaObjetivo;
                bajando = false;
            }
        } else {
            rect.x += velocidadHorizontal * direccion * delta;

            float anchoPantalla = PantallaJuego.WORLD_WIDTH;
            if (rect.x <= 0 || rect.x + rect.width >= anchoPantalla) {
                direccion *= -1;
                rect.x = Math.max(0, Math.min(rect.x, anchoPantalla - rect.width));
            }
        }
    }

    // Paso de ataque (Template Method) usando Strategy
    @Override
    protected void atacar(float delta) {
        if (texLaser == null) return;

        tiempoDisparo += delta;
        if (tiempoDisparo >= TIEMPO_ENTRE_DISPAROS) {
            if (estrategiaDisparoNormal != null) {
                estrategiaDisparoNormal.disparar(balas, texLaser, rect);
            }
            tiempoDisparo = 0f;

            if (shootSound != null) {
                shootSound.play(0.3f);
            }
        }
    }

    // Estado adicional: disparo final + movimiento de las balas
    @Override
    protected void actualizarEstado(float delta) {
        if (destruido) {
            if (!disparoFinalRealizado) {
                if (texLaser != null && estrategiaDisparoMuerte != null) {
                    estrategiaDisparoMuerte.disparar(balas, texLaser, rect);
                }
                if (explosionSound != null) {
                    explosionSound.play(0.8f);
                }
                disparoFinalRealizado = true;
            }
            tiempoPostMuerte += delta;
        }

        // Actualizar balas enemigas
        Iterator<BalaEnemigo> it = balas.iterator();
        while (it.hasNext()) {
            BalaEnemigo b = it.next();
            b.mover(delta);
            if (b.fueraDePantalla(PantallaJuego.WORLD_WIDTH, PantallaJuego.WORLD_HEIGHT)) {
                it.remove();
            }
        }
    }

    // Render extra: las balas (Template Method)
    @Override
    protected void renderExtra(SpriteBatch batch) {
        for (BalaEnemigo b : balas) {
            b.render(batch);
        }
    }

    public boolean listoParaEliminar() {
        return destruido && tiempoPostMuerte > TIEMPO_DESAPARICION && balas.isEmpty();
    }

    public ArrayList<BalaEnemigo> getBalas() {
        return balas;
    }
}
