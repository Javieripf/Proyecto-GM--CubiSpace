package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;

public class NaveJugador {
    private Texture texNave;
    private Texture texBala;
    private Rectangle rect;
    private ArrayList<Bala> balas;

    private Sound shootSound;
    private Sound damageSound;

    private static final float VELOCIDAD = 300f;
    private static final float TIEMPO_ENTRE_DISPAROS = 0.3f;
    private float tiempoDesdeUltimoDisparo = 0;

    private static final float ESCALA = 0.15f;
    private static final float DURACION_INMUNIDAD = 1.0f;

    private int vidas = 3;
    private boolean inmune = false;
    private float tiempoInmunidad = 0f;
    private boolean destruido = false;

    public NaveJugador(Texture texNave, Texture texBala, Sound shootSound, Sound damageSound) {
        this.texNave = texNave;
        this.texBala = texBala;
        this.shootSound = shootSound;
        this.damageSound = damageSound;
        this.balas = new ArrayList<>();

        float ancho = texNave.getWidth() * ESCALA;
        float alto = texNave.getHeight() * ESCALA;
        this.rect = new Rectangle(PantallaJuego.WORLD_WIDTH / 2f - ancho / 2f, 30, ancho, alto);
    }

    public void actualizar(float delta, float worldWidth, float worldHeight) {
        if (destruido) return;
        tiempoDesdeUltimoDisparo += delta;
        mover(delta, worldWidth);
        actualizarInmunidad(delta);
        manejarDisparo();

        Iterator<Bala> it = balas.iterator();
        while (it.hasNext()) {
            Bala b = it.next();
            b.mover(delta);
            if (b.fueraDePantalla(worldWidth, worldHeight)) it.remove();
        }
    }

    private void mover(float delta, float worldWidth) {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) rect.x -= VELOCIDAD * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) rect.x += VELOCIDAD * delta;
        rect.x = Math.max(0, Math.min(worldWidth - rect.width, rect.x));
    }

    private void manejarDisparo() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && tiempoDesdeUltimoDisparo >= TIEMPO_ENTRE_DISPAROS) {
            disparar();
            tiempoDesdeUltimoDisparo = 0;
        }
    }

    private void disparar() {
        float balaX = rect.x + rect.width / 2 - (texBala.getWidth() * Bala.ESCALA) / 2;
        float balaY = rect.y + rect.height;
        balas.add(new Bala(texBala, balaX, balaY));
        if (shootSound != null) shootSound.play(0.5f);
    }

    private void actualizarInmunidad(float delta) {
        if (inmune) {
            tiempoInmunidad -= delta;
            if (tiempoInmunidad <= 0) inmune = false;
        }
    }

    public void recibirGolpe() {
        if (inmune || destruido) return;
        vidas--;
        if (damageSound != null) damageSound.play(0.7f);
        if (vidas <= 0) destruido = true;
        else activarInmunidad();
    }

    private void activarInmunidad() {
        inmune = true;
        tiempoInmunidad = DURACION_INMUNIDAD;
    }

    public void render(SpriteBatch batch) {
        if (destruido) return;

        if (inmune) {
            float alpha = (float) (0.5f + 0.5f * Math.sin(System.currentTimeMillis() * 0.02f * Math.PI));
            batch.setColor(1, 1, 1, alpha);
        }

        batch.draw(texNave, rect.x, rect.y, rect.width, rect.height);
        batch.setColor(1, 1, 1, 1);

        for (Bala b : balas) b.render(batch);
    }

    public void dispose() {
        if (shootSound != null) shootSound.dispose();
        if (damageSound != null) damageSound.dispose();
    }

    public Rectangle getRect() { return rect; }
    public ArrayList<Bala> getBalas() { return balas; }
    public int getVidas() { return vidas; }
    public boolean estaDestruido() { return destruido; }
    public boolean esInmune() { return inmune; }
}
