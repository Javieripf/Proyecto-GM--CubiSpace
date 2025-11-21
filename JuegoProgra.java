package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

// GM2.1 – Singleton
public class JuegoProgra extends Game {

    // Instancia única del juego
    private static JuegoProgra instancia;

    private SpriteBatch batch;
    private BitmapFont font;

    // Constructor privado para evitar new desde fuera
    private JuegoProgra() {
    }

    // Punto de acceso global
    public static JuegoProgra getInstancia() {
        if (instancia == null) {
            instancia = new JuegoProgra();
        }
        return instancia;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        setScreen(new PantallaMenu(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        if (batch != null) batch.dispose();
        if (font != null) font.dispose();
        if (getScreen() != null) getScreen().dispose();
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public BitmapFont getFont() {
        return font;
    }
}
