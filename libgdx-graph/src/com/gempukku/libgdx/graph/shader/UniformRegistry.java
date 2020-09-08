package com.gempukku.libgdx.graph.shader;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.gempukku.libgdx.graph.shader.environment.GraphShaderEnvironment;
import com.gempukku.libgdx.graph.shader.models.GraphShaderModelInstanceImpl;

public interface UniformRegistry {
    void registerAttribute(final String alias);

    void registerUniform(final String alias, final boolean global, final UniformSetter setter);

    void registerStructArrayUniform(final String alias, String[] fieldNames, final boolean global, StructArrayUniformSetter setter);

    interface UniformSetter {
        void set(final BasicShader shader, final int location, Camera camera, GraphShaderEnvironment environment, GraphShaderModelInstanceImpl graphShaderModelInstance, Renderable renderable);
    }

    interface StructArrayUniformSetter {
        void set(final BasicShader shader, final int startingLocation, int[] fieldOffsets, int structSize, Camera camera, GraphShaderEnvironment environment, GraphShaderModelInstanceImpl graphShaderModelInstance, Renderable renderable);
    }
}
