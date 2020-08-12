package com.gempukku.libgdx.graph.ui.preview;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

public interface NavigableCanvas {
    void getCanvasPosition(Vector2 result);

    void getCanvasSize(Vector2 result);

    void getVisibleSize(Vector2 result);

    void navigateTo(float x, float y);

    Array<Actor> getElements();
}
