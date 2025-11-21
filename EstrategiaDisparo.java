package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

import java.util.List;

// GM2.3 – Strategy (Estrategia)
public interface EstrategiaDisparo {
    void disparar(List<BalaEnemigo> balas, Texture texLaser, Rectangle rect);
}