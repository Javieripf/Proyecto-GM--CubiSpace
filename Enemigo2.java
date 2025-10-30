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
    public Enemigo2(Texture textura, Texture texLaser, Sound shootSound, Sound explosionSound, float limiteX) {
        super(
            textura,
            (float) Math.random() * (PantallaJuego.WORLD_WIDTH - textura.getWidth() * ESCALA),
            PantallaJuego.WORLD_HEIGHT,
            100f, 50f, 3
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
    }

    @Override
    public void actualizar(float delta) {
        if (!destruido) {
            mover(delta);
            controlarDisparo(delta);
        } else {
            if (!disparoFinalRealizado) {
                disparoFinalTriple();
                if (explosionSound != null) explosionSound.play(0.8f);
                disparoFinalRealizado = true;
            }
            tiempoPostMuerte += delta;
        }

        Iterator<BalaEnemigo> it = balas.iterator();
        while (it.hasNext()) {
            BalaEnemigo b = it.next();
            b.mover(delta);
            if (b.fueraDePantalla(PantallaJuego.WORLD_WIDTH, PantallaJuego.WORLD_HEIGHT)) it.remove();
        }
    }

    private void mover(float delta) {
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

    private void controlarDisparo(float delta) {
        tiempoDisparo += delta;
        if (tiempoDisparo >= TIEMPO_ENTRE_DISPAROS) {
            disparar();
            tiempoDisparo = 0f;
        }
    }

    private void disparar() {
        if (texLaser == null) return;
        float x = rect.x + rect.width / 2 - texLaser.getWidth() * 0.05f;
        float y = rect.y;
        balas.add(new BalaEnemigo(texLaser, x, y));
        if (shootSound != null) shootSound.play(0.3f);
    }

    private void disparoFinalTriple() {
        if (texLaser == null) return;
        float x = rect.x + rect.width / 2 - texLaser.getWidth() * 0.05f;
        float y = rect.y;
        balas.add(new BalaEnemigo(texLaser, x, y, -20, 300f));
        balas.add(new BalaEnemigo(texLaser, x, y, 0, 300f));
        balas.add(new BalaEnemigo(texLaser, x, y, 20, 300f));
    }

    @Override
    public void render(SpriteBatch batch) {
        if (!destruido && textura != null) {
            batch.draw(textura, rect.x, rect.y, rect.width, rect.height);
        }

        for (BalaEnemigo b : balas) b.render(batch);
    }

    public boolean listoParaEliminar() {
        return destruido && tiempoPostMuerte > TIEMPO_DESAPARICION && balas.isEmpty();
    }

    public ArrayList<BalaEnemigo> getBalas() {
        return balas;
    }
}
