package com.gempukku.libgdx.graph.shader;

import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class GraphShader extends UniformCachingShader {
    private ShaderProgram shaderProgram;

    public void setProgram(ShaderProgram shaderProgram) {
        this.shaderProgram = shaderProgram;
    }

    @Override
    public int compareTo(Shader other) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean canRender(Renderable instance) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void init() {
        init(shaderProgram);
    }

    @Override
    public void dispose() {
        shaderProgram.dispose();
        super.dispose();
    }
}
