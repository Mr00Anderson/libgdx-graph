package com.gempukku.libgdx.graph;

public class DefaultTimeKeeper implements TimeKeeper {
    private float timeCumulative = -1;
    private float delta;

    @Override
    public void updateTime(float delta) {
        this.delta = delta;
        if (timeCumulative > -1)
            timeCumulative += delta;
        else
            timeCumulative = 0;
    }

    @Override
    public float getTime() {
        return timeCumulative;
    }

    @Override
    public float getDelta() {
        return delta;
    }
}
