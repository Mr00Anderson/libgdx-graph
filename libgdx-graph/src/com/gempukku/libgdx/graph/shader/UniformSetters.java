package com.gempukku.libgdx.graph.shader;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.math.Matrix4;
import com.gempukku.libgdx.graph.shader.environment.GraphShaderEnvironment;
import com.gempukku.libgdx.graph.shader.models.GraphShaderModelInstanceImpl;

public class UniformSetters {
    private UniformSetters() {
    }

    public final static UniformRegistry.UniformSetter projViewTrans = new UniformRegistry.UniformSetter() {
        @Override
        public void set(BasicShader shader, int location, Camera camera, GraphShaderEnvironment environment, GraphShaderModelInstanceImpl graphShaderModelInstance, Renderable renderable) {
            shader.setUniform(location, camera.combined);
        }
    };
    public final static UniformRegistry.UniformSetter cameraPosition = new UniformRegistry.UniformSetter() {
        @Override
        public void set(BasicShader shader, int location, Camera camera, GraphShaderEnvironment environment, GraphShaderModelInstanceImpl graphShaderModelInstance, Renderable renderable) {
            shader.setUniform(location, camera.position.x, camera.position.y, camera.position.z,
                    1.1881f / (camera.far * camera.far));
        }
    };
    public final static UniformRegistry.UniformSetter cameraDirection = new UniformRegistry.UniformSetter() {
        @Override
        public void set(BasicShader shader, int location, Camera camera, GraphShaderEnvironment environment, GraphShaderModelInstanceImpl graphShaderModelInstance, Renderable renderable) {
            shader.setUniform(location, camera.direction);
        }
    };
    public final static UniformRegistry.UniformSetter worldTrans = new UniformRegistry.UniformSetter() {
        @Override
        public void set(BasicShader shader, int location, Camera camera, GraphShaderEnvironment environment, GraphShaderModelInstanceImpl graphShaderModelInstance, Renderable renderable) {
            shader.setUniform(location, renderable.worldTransform);
        }
    };
    public final static UniformRegistry.UniformSetter ambientLight = new UniformRegistry.UniformSetter() {
        @Override
        public void set(BasicShader shader, int location, Camera camera, GraphShaderEnvironment environment, GraphShaderModelInstanceImpl graphShaderModelInstance, Renderable renderable) {
            if (environment != null && environment.getAmbientColor() != null) {
                Color ambientColor = environment.getAmbientColor();
                shader.setUniform(location, ambientColor.r, ambientColor.g, ambientColor.b);
            } else {
                shader.setUniform(location, 0f, 0f, 0f);
            }
        }
    };

    public static class Bones implements UniformRegistry.UniformSetter {
        private final static Matrix4 idtMatrix = new Matrix4();
        public final float bones[];

        public Bones(final int numBones) {
            this.bones = new float[numBones * 16];
        }

        @Override
        public void set(BasicShader shader, int location, Camera camera, GraphShaderEnvironment environment, GraphShaderModelInstanceImpl graphShaderModelInstance, Renderable renderable) {
            Matrix4[] modelBones = renderable.bones;
            for (int i = 0; i < bones.length; i += 16) {
                final int idx = i / 16;
                if (modelBones == null || idx >= modelBones.length || modelBones[idx] == null)
                    System.arraycopy(idtMatrix.val, 0, bones, i, 16);
                else
                    System.arraycopy(modelBones[idx].val, 0, bones, i, 16);
            }
            shader.setUniformMatrixArray(location, bones);
        }
    }

    public static class MaterialTexture implements UniformRegistry.UniformSetter {
        private long type;

        public MaterialTexture(long type) {
            this.type = type;
        }

        @Override
        public void set(BasicShader shader, int location, Camera camera, GraphShaderEnvironment environment, GraphShaderModelInstanceImpl graphShaderModelInstance, Renderable renderable) {
            final int unit = shader.getContext().textureBinder.bind(((TextureAttribute) (renderable.material.get(type))).textureDescription);
            shader.setUniform(location, unit);
        }
    }

    public static class MaterialTextureUV implements UniformRegistry.UniformSetter {
        private long type;

        public MaterialTextureUV(long type) {
            this.type = type;
        }

        @Override
        public void set(BasicShader shader, int location, Camera camera, GraphShaderEnvironment environment, GraphShaderModelInstanceImpl graphShaderModelInstance, Renderable renderable) {
            final TextureAttribute ta = (TextureAttribute) (renderable.material.get(type));
            shader.setUniform(location, ta.offsetU, ta.offsetV, ta.scaleU, ta.scaleV);
        }
    }
}
