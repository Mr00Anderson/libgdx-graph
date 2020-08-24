package com.gempukku.libgdx.graph.shader;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class GraphShader extends UniformCachingShader {
    private String tag;
    private ShaderProgram shaderProgram;

    public GraphShader(String tag) {
        this.tag = tag;
    }

    public void setProgram(ShaderProgram shaderProgram) {
        this.shaderProgram = shaderProgram;
    }

    public String getTag() {
        return tag;
    }

    public void init() {
        init(shaderProgram);
    }

    @Override
    public void dispose() {
        shaderProgram.dispose();
        super.dispose();
    }
}
