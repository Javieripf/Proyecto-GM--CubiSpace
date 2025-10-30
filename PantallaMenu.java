package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PantallaMenu implements Screen {

    private final JuegoProgra game;
    private SpriteBatch batch;
    private BitmapFont font;
    private Texture fondo;

    private OrthographicCamera camera;
    private Viewport viewport;

    private int opcionSeleccionada = 0; // 0 = Jugar, 1 = Salir
    private String[] opciones = {"JUGAR", "SALIR"};

    public PantallaMenu(JuegoProgra game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(PantallaJuego.WORLD_WIDTH, PantallaJuego.WORLD_HEIGHT, camera);
        camera.position.set(PantallaJuego.WORLD_WIDTH / 2f, PantallaJuego.WORLD_HEIGHT / 2f, 0);
        camera.update();

        font = new BitmapFont();
        font.getData().setScale(3f);
        fondo = new Texture("fondo.png");
    }

    @Override
    public void render(float delta) {
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
            opcionSeleccionada = (opcionSeleccionada - 1 + opciones.length) % opciones.length;
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN))
            opcionSeleccionada = (opcionSeleccionada + 1) % opciones.length;

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            if (opcionSeleccionada == 0) {
                game.setScreen(new PantallaJuego(game));
                dispose();
            } else if (opcionSeleccionada == 1) {
                Gdx.app.exit();
            }
        }

        batch.begin();
        batch.draw(fondo, 0, 0, PantallaJuego.WORLD_WIDTH, PantallaJuego.WORLD_HEIGHT);

        font.setColor(Color.WHITE);
        font.draw(batch, "         CubiSpace", PantallaJuego.WORLD_WIDTH / 2f - 200,
                PantallaJuego.WORLD_HEIGHT - 150);

        for (int i = 0; i < opciones.length; i++) {
            if (i == opcionSeleccionada) font.setColor(Color.YELLOW);
            else font.setColor(Color.WHITE);
            font.draw(batch, opciones[i],
                    PantallaJuego.WORLD_WIDTH / 2f - 60,
                    PantallaJuego.WORLD_HEIGHT / 2f - i * 80 + 50);
        }

        batch.end();
    }

    @Override public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(PantallaJuego.WORLD_WIDTH / 2f, PantallaJuego.WORLD_HEIGHT / 2f, 0);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        fondo.dispose();
    }
}
