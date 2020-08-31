package com.gempukku.libgdx.graph.shader;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Disposable;
import com.gempukku.libgdx.graph.TimeProvider;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GraphShader extends UniformCachingShader implements GraphShaderContext {
    private List<Disposable> disposableList = new LinkedList<>();
    private Map<String, PropertySource> propertySourceMap;
    private ShaderProgram shaderProgram;
    private TimeProvider timeProvider;
    private Environment environment;

    public void setProgram(ShaderProgram shaderProgram) {
        this.shaderProgram = shaderProgram;
    }

    public void init() {
        init(shaderProgram);
    }

    public void setPropertySourceMap(Map<String, PropertySource> propertySourceMap) {
        this.propertySourceMap = propertySourceMap;
    }

    @Override
    public PropertySource getPropertySource(String name) {
        return propertySourceMap.get(name);
    }

    public void setTimeProvider(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public TimeProvider getTimeProvider() {
        return timeProvider;
    }

    @Override
    public Environment getEnvironment() {
        return environment;
    }

    @Override
    public void addManagedResource(Disposable disposable) {
        disposableList.add(disposable);
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
