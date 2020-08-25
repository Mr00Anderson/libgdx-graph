package com.gempukku.libgdx.graph.shader;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Disposable;

import java.util.LinkedList;
import java.util.List;

public class GraphShader extends UniformCachingShader {
    private List<Disposable> disposableList = new LinkedList<>();
    private String tag;
    private ShaderProgram shaderProgram;

    public GraphShader(String tag) {
        this.tag = tag;
    }

    public void setProgram(ShaderProgram shaderProgram) {
        this.shaderProgram = shaderProgram;
    }

    public void addDisposable(Disposable disposable) {
        disposableList.add(disposable);
    }

    public String getTag() {
        return tag;
    }

    public void init() {
        init(shaderProgram);
    }

    @Override
    public void dispose() {
        for (Disposable disposable : disposableList) {
            disposable.dispose();
        }
        disposableList.clear();

        if (shaderProgram != null)
            shaderProgram.dispose();
        super.dispose();
    }
}
