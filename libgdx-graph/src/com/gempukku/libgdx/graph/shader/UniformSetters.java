package com.gempukku.libgdx.graph.shader;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.math.Matrix4;
import com.gempukku.libgdx.graph.shader.models.GraphShaderModelInstanceImpl;

public class UniformSetters {
    private UniformSetters() {
    }

    public final static UniformRegistry.UniformSetter projViewTrans = new UniformRegistry.UniformSetter() {
        @Override
        public void set(BasicShader shader, int location, Camera camera, Environment environment, GraphShaderModelInstanceImpl graphShaderModelInstance, Renderable renderable) {
            shader.setUniform(location, camera.combined);
        }
    };
    public final static UniformRegistry.UniformSetter cameraPosition = new UniformRegistry.UniformSetter() {
        @Override
        public void set(BasicShader shader, int location, Camera camera, Environment environment, GraphShaderModelInstanceImpl graphShaderModelInstance, Renderable renderable) {
            shader.setUniform(location, camera.position.x, camera.position.y, camera.position.z,
                    1.1881f / (camera.far * camera.far));
        }
    };
    public final static UniformRegistry.UniformSetter cameraDirection = new UniformRegistry.UniformSetter() {
        @Override
        public void set(BasicShader shader, int location, Camera camera, Environment environment, GraphShaderModelInstanceImpl graphShaderModelInstance, Renderable renderable) {
            shader.setUniform(location, camera.direction);
        }
    };
    public final static UniformRegistry.UniformSetter worldTrans = new UniformRegistry.UniformSetter() {
        @Override
        public void set(BasicShader shader, int location, Camera camera, Environment environment, GraphShaderModelInstanceImpl graphShaderModelInstance, Renderable renderable) {
            shader.setUniform(location, renderable.worldTransform);
        }
    };

    public static class Bones implements UniformRegistry.UniformSetter {
        private final static Matrix4 idtMatrix = new Matrix4();
        public final float bones[];

        public Bones(final int numBones) {
            this.bones = new float[numBones * 16];
        }

        @Override
        public void set(BasicShader shader, int location, Camera camera, Environment environment, GraphShaderModelInstanceImpl graphShaderModelInstance, Renderable renderable) {
            Matrix4[] modelBones = renderable.bones;
            for (int i = 0; i < bones.length; i++) {
                final int idx = i / 16;
                bones[i] = (modelBones == null || idx >= modelBones.length || modelBones[idx] == null) ? idtMatrix.val[i % 16]
                        : modelBones[idx].val[i % 16];
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
        public void set(BasicShader shader, int location, Camera camera, Environment environment, GraphShaderModelInstanceImpl graphShaderModelInstance, Renderable renderable) {
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
        public void set(BasicShader shader, int location, Camera camera, Environment environment, GraphShaderModelInstanceImpl graphShaderModelInstance, Renderable renderable) {
            final TextureAttribute ta = (TextureAttribute) (renderable.material.get(type));
            shader.setUniform(location, ta.offsetU, ta.offsetV, ta.scaleU, ta.scaleV);
        }
    }
}
