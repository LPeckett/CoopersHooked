package com.lukepeckett.coopershooked.game.invaders;

import android.graphics.RectF;

public abstract class GameObject {

    protected int xPos, yPos;

    protected abstract void tick();
    protected abstract void render();
    protected abstract RectF getBounds();

}
