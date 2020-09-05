package com.gempukku.libgdx.graph.shader.models;

import com.badlogic.gdx.math.Matrix4;

public interface GraphShaderModelInstance {
    String getId();

    void addTag(String tag);

    void removeTag(String tag);

    void setProperty(String name, Object value);

    void unsetProperty(String name);

    boolean hasTag(String tag);

    Object getProperty(String name);

    Matrix4 getTransformMatrix();
}
