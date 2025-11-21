package com.mygdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.Iterator;

public class PantallaJuego implements Screen {

    private enum Estado { JUGANDO, PAUSA, GAME_OVER }

    private final JuegoProgra game;
    private SpriteBatch batch;

    // 🎨 Recursos
    private Texture texFondo, texNave, texBala, texEnemigo, texEnemigo2, texLaserEnemigo, overlayNegro;
    private Sound enemyHit, laserShot, enemyShoot, explosionSound;
    private Music mainTheme;
    private BitmapFont font;

    // 🚀 Entidades
    private NaveJugador nave;
    private ArrayList<EnemigoBasico> enemigosBasicos;
    private ArrayList<Enemigo2> enemigosAvanzados;

    private float tiempoEnemigo = 0f;
    private int enemigosNormalesContados = 0;
    private Estado estado = Estado.JUGANDO;

    // 📸 Cámara y viewport
    private OrthographicCamera camera;
    private Viewport viewport;

    // 🌍 Mundo lógico
    public static final float WORLD_WIDTH = 800;
    public static final float WORLD_HEIGHT = 600;

    // 🧮 Puntos y récord
    private int puntajeActual = 0;
    private int highScore = 0;

    // 🎚️ Dificultad
    private float factorDificultad = 1.0f;   // 1.0 = velocidad base
    private float tiempoDificultad = 0f;     // para ir subiendo la dificultad con el tiempo

    public PantallaJuego(final JuegoProgra game) {
        this.game = game;
    }

    @Override
    public void show() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        camera.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);
        camera.update();

        try {
            texFondo = new Texture("fondo.png");
            texNave = new Texture("nave.png");
            texBala = new Texture("laser.png");
            texEnemigo = new Texture("enemigo.png");
            texEnemigo2 = new Texture("enemigo2.png");
            texLaserEnemigo = new Texture("laserEnemigo.png");
        } catch (Exception e) {
            Gdx.app.error("PantallaJuego", "❌ Error cargando texturas", e);
        }

        Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pm.setColor(0, 0, 0, 1);
        pm.fill();
        overlayNegro = new Texture(pm);
        pm.dispose();

        try {
            enemyHit = Gdx.audio.newSound(Gdx.files.internal("enemyHit.mp3"));
            laserShot = Gdx.audio.newSound(Gdx.files.internal("disparo.mp3"));
            enemyShoot = Gdx.audio.newSound(Gdx.files.internal("enemyShoot.mp3"));
            explosionSound = Gdx.audio.newSound(Gdx.files.internal("explosion.mp3"));
        } catch (Exception e) {
            Gdx.app.error("PantallaJuego", "❌ Error cargando sonidos", e);
        }

        try {
            mainTheme = Gdx.audio.newMusic(Gdx.files.internal("mainTheme.mp3"));
            mainTheme.setLooping(true);
            mainTheme.setVolume(0.6f);
            mainTheme.play();
        } catch (Exception e) {
            Gdx.app.error("PantallaJuego", "❌ Error cargando música", e);
        }

        font = new BitmapFont();
        font.getData().setScale(2f);

        inicializarJuego();
    }

    private void inicializarJuego() {
        nave = new NaveJugador(texNave, texBala, laserShot, enemyHit);
        enemigosBasicos = new ArrayList<>();
        enemigosAvanzados = new ArrayList<>();
        tiempoEnemigo = 0f;
        enemigosNormalesContados = 0;
        estado = Estado.JUGANDO;
        puntajeActual = 0;
        factorDificultad = 1.0f;   // al reiniciar, dejamos la dificultad base
        tiempoDificultad = 0f;
    }

    private void actualizarJuego(float delta) {
        tiempoEnemigo += delta;

        // 🔼 Subir dificultad con el tiempo
        tiempoDificultad += delta;
        if (tiempoDificultad >= 10f) { // cada 10 segundos aumenta un poco
            factorDificultad += 0.1f;
            tiempoDificultad = 0f;
            // opcional: Gdx.app.log("DIFICULTAD", "Nuevo factor: " + factorDificultad);
        }

        // Generación de enemigos con dificultad aplicada
        if (tiempoEnemigo > 1.5f) {
            boolean crearAvanzado = Math.random() < 0.1f || enemigosNormalesContados >= 5;

            if (crearAvanzado && enemigosAvanzados.size() < 3) {
                enemigosAvanzados.add(new Enemigo2(
                        texEnemigo2,
                        texLaserEnemigo,
                        enemyShoot,
                        explosionSound,
                        WORLD_WIDTH,
                        factorDificultad
                ));
                enemigosNormalesContados = 0;
            } else if (!crearAvanzado && enemigosBasicos.size() < 5) {
                enemigosBasicos.add(new EnemigoBasico(
                        texEnemigo,
                        explosionSound,
                        WORLD_WIDTH,
                        factorDificultad
                ));
                enemigosNormalesContados++;
            }

            tiempoEnemigo = 0f;
        }

        nave.actualizar(delta, WORLD_WIDTH, WORLD_HEIGHT);

        if (nave.getVidas() <= 0) {
            estado = Estado.GAME_OVER;
            if (mainTheme != null) mainTheme.stop();
            if (puntajeActual > highScore) highScore = puntajeActual;
            return;
        }

        // Colisiones jugador-enemigo
        Iterator<EnemigoBasico> itB = enemigosBasicos.iterator();
        while (itB.hasNext()) {
            EnemigoBasico e = itB.next();
            e.actualizar(delta);
            if (e.getRect().overlaps(nave.getRect())) {
                e.colisionConJugador();
                nave.recibirGolpe();
                itB.remove();
            } else if (e.getRect().y + e.getRect().height < 0) itB.remove();
        }

        // Colisiones balas jugador-enemigos básicos
        for (Iterator<Bala> itBala = nave.getBalas().iterator(); itBala.hasNext();) {
            Bala b = itBala.next();
            boolean impacto = false;
            for (Iterator<EnemigoBasico> itE = enemigosBasicos.iterator(); itE.hasNext();) {
                EnemigoBasico eb = itE.next();
                if (b.getRect().overlaps(eb.getRect())) {
                    itBala.remove();
                    itE.remove();
                    if (enemyHit != null) enemyHit.play(0.6f);
                    puntajeActual += 100;
                    impacto = true;
                    break;
                }
            }
            if (impacto) break;
        }

        // Colisiones con enemigos avanzados
        Iterator<Enemigo2> itA = enemigosAvanzados.iterator();
        while (itA.hasNext()) {
            Enemigo2 e2 = itA.next();
            e2.actualizar(delta);

            for (Iterator<Bala> itBala = nave.getBalas().iterator(); itBala.hasNext();) {
                Bala b = itBala.next();
                if (b.getRect().overlaps(e2.getRect())) {
                    e2.recibirDano(1);
                    itBala.remove();
                    if (enemyHit != null) enemyHit.play(0.6f);
                    if (e2.estaDestruido()) puntajeActual += 300;
                    break;
                }
            }

            for (Iterator<BalaEnemigo> itBE = e2.getBalas().iterator(); itBE.hasNext();) {
                BalaEnemigo be = itBE.next();
                if (be.getRect().overlaps(nave.getRect())) {
                    nave.recibirGolpe();
                    itBE.remove();
                    break;
                }
            }

            if (e2.listoParaEliminar()) itA.remove();
        }
    }

    @Override
    public void render(float delta) {
        float dt = Math.min(delta, 0.1f);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // 🔹 Control de pausa / reanudar
        if (estado == Estado.JUGANDO) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                estado = Estado.PAUSA;
            } else {
                actualizarJuego(dt);
            }
        } else if (estado == Estado.PAUSA) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                estado = Estado.JUGANDO;
            }
        } else if (estado == Estado.GAME_OVER) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                inicializarJuego();
                if (mainTheme != null) mainTheme.play();
            }
        }

        batch.begin();

        batch.draw(texFondo, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
        nave.render(batch);
        for (EnemigoBasico e : enemigosBasicos) e.render(batch);
        for (Enemigo2 e2 : enemigosAvanzados) e2.render(batch);

        font.setColor(Color.WHITE);
        font.draw(batch, "Vidas: " + nave.getVidas(), 20, WORLD_HEIGHT - 20);
        font.draw(batch, "Puntos: " + puntajeActual, 20, WORLD_HEIGHT - 60);
        font.draw(batch, "Highscore: " + highScore, 20, WORLD_HEIGHT - 100);

        if (estado == Estado.GAME_OVER) {
            font.setColor(Color.RED);
            font.draw(batch, "GAME OVER - Presiona ENTER para reiniciar",
                    WORLD_WIDTH / 2f - 280, WORLD_HEIGHT / 2f);
        }

        if (estado == Estado.PAUSA) {
            batch.setColor(0, 0, 0, 0.5f);
            batch.draw(overlayNegro, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
            batch.setColor(1, 1, 1, 1);

            font.setColor(Color.YELLOW);
            font.draw(batch, "PAUSA",
                    WORLD_WIDTH / 2f - 70, WORLD_HEIGHT / 2f + 40);
            font.setColor(Color.WHITE);
            font.draw(batch, "Presiona ESC para continuar",
                    WORLD_WIDTH / 2f - 220, WORLD_HEIGHT / 2f - 10);
        }

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);
        camera.update();
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        if (texFondo != null) texFondo.dispose();
        if (texNave != null) texNave.dispose();
        if (texBala != null) texBala.dispose();
        if (texEnemigo != null) texEnemigo.dispose();
        if (texEnemigo2 != null) texEnemigo2.dispose();
        if (texLaserEnemigo != null) texLaserEnemigo.dispose();
        if (overlayNegro != null) overlayNegro.dispose();
        if (enemyHit != null) enemyHit.dispose();
        if (laserShot != null) laserShot.dispose();
        if (enemyShoot != null) enemyShoot.dispose();
        if (explosionSound != null) explosionSound.dispose();
        if (nave != null) nave.dispose();
        if (font != null) font.dispose();
        if (mainTheme != null) {
            mainTheme.stop();
            mainTheme.dispose();
        }
    }
}