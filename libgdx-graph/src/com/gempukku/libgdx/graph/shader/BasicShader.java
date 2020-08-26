package com.gempukku.libgdx.graph.shader;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GLTexture;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.g3d.utils.TextureDescriptor;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.IntArray;

import java.util.HashMap;
import java.util.Map;

import static com.badlogic.gdx.graphics.GL20.GL_BACK;
import static com.badlogic.gdx.graphics.GL20.GL_FRONT;
import static com.badlogic.gdx.graphics.GL20.GL_NONE;

public abstract class BasicShader implements UniformRegistry, Disposable {
    public enum Culling {
        back(GL_BACK), none(GL_NONE), front(GL_FRONT);

        private int cullFace;

        Culling(int cullFace) {
            this.cullFace = cullFace;
        }

        public void run(RenderContext renderContext) {
            renderContext.setCullFace(cullFace);
        }
    }

    public enum Transparency {
        opaque, transparent;
    }

    public enum Blending {
        alpha(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA),
        additive(GL20.GL_SRC_ALPHA, GL20.GL_ONE),
        none(GL20.GL_ZERO, GL20.GL_ONE);

        private int sourceFactor;
        private int destinationFactor;

        Blending(int sourceFactor, int destinationFactor) {
            this.sourceFactor = sourceFactor;
            this.destinationFactor = destinationFactor;
        }

        public int getSourceFactor() {
            return sourceFactor;
        }

        public int getDestinationFactor() {
            return destinationFactor;
        }
    }

    private static class Attribute {
        private final String alias;
        private int location = -1;

        public Attribute(String alias) {
            this.alias = alias;
        }

        private void setLocation(int location) {
            this.location = location;
        }
    }

    private static class Uniform {
        private final String alias;
        private final boolean global;
        private final UniformSetter setter;
        private int location = -1;

        public Uniform(String alias, boolean global, UniformSetter setter) {
            this.alias = alias;
            this.global = global;
            this.setter = setter;
        }

        private void setUniformLocation(int location) {
            this.location = location;
        }
    }

    private static class StructArrayUniform {
        private final String alias;
        private final String[] fieldNames;
        private final boolean global;
        private final StructArrayUniformSetter setter;
        private int startIndex;
        private int size;
        private int[] fieldOffsets;

        public StructArrayUniform(String alias, String[] fieldNames, boolean global, StructArrayUniformSetter setter) {
            this.alias = alias;
            this.fieldNames = new String[fieldNames.length];
            System.arraycopy(fieldNames, 0, this.fieldNames, 0, fieldNames.length);
            this.global = global;
            this.setter = setter;
        }

        private void setUniformLocations(int startIndex, int size, int[] fieldOffsets) {
            this.startIndex = startIndex;
            this.size = size;
            this.fieldOffsets = fieldOffsets;
        }
    }

    private final Map<String, Attribute> attributes = new HashMap<>();
    private final Map<String, Uniform> uniforms = new HashMap<String, Uniform>();
    private final Map<String, StructArrayUniform> structArrayUniforms = new HashMap<String, StructArrayUniform>();

    private ShaderProgram program;
    private RenderContext context;
    private Camera camera;
    private Mesh currentMesh;
    private Culling culling = Culling.back;
    private Transparency transparency = Transparency.opaque;
    private Blending blending = Blending.alpha;

    private boolean initialized = false;

    @Override
    public void registerAttribute(String alias) {
        if (initialized) throw new GdxRuntimeException("Cannot register an uniform after initialization");
        validateNewAttribute(alias);
        attributes.put(alias, new Attribute(alias));
    }

    @Override
    public void registerUniform(final String alias, final boolean global, final UniformSetter setter) {
        if (initialized) throw new GdxRuntimeException("Cannot register an uniform after initialization");
        validateNewUniform(alias);
        uniforms.put(alias, new Uniform(alias, global, setter));
    }

    @Override
    public void registerStructArrayUniform(final String alias, String[] fieldNames, final boolean global, StructArrayUniformSetter setter) {
        if (initialized) throw new GdxRuntimeException("Cannot register an uniform after initialization");
        validateNewUniform(alias);
        structArrayUniforms.put(alias, new StructArrayUniform(alias, fieldNames, global, setter));
    }

    public RenderContext getContext() {
        return context;
    }

    public Camera getCamera() {
        return camera;
    }

    private void validateNewAttribute(String alias) {
        if (attributes.containsKey(alias))
            throw new GdxRuntimeException("Attribute already registered");
    }

    private void validateNewUniform(String alias) {
        if (uniforms.containsKey(alias) || structArrayUniforms.containsKey(alias))
            throw new GdxRuntimeException("Uniform already registered");
    }

    /**
     * Initialize this shader, causing all registered uniforms/attributes to be fetched.
     */
    protected void init(final ShaderProgram program) {
        if (initialized) throw new GdxRuntimeException("Already initialized");
        if (!program.isCompiled()) throw new GdxRuntimeException(program.getLog());
        this.program = program;

        for (Attribute attribute : attributes.values()) {
            final int location = program.getAttributeLocation(attribute.alias);
            if (location >= 0)
                attribute.setLocation(location);
        }

        for (Uniform uniform : uniforms.values()) {
            String alias = uniform.alias;
            int location = getUniformLocationSafely(program, alias);
            uniform.setUniformLocation(location);
        }

        for (StructArrayUniform uniform : structArrayUniforms.values()) {
            int startIndex = getUniformLocationSafely(program, uniform.alias + "[0]." + uniform.fieldNames[0]);
            int size = program.fetchUniformLocation(uniform.alias + "[1]." + uniform.fieldNames[0], false) - startIndex;
            int[] fieldOffsets = new int[uniform.fieldNames.length];
            // Starting at 1, as first field offset is 0 by default
            for (int i = 1; i < uniform.fieldNames.length; i++) {
                fieldOffsets[i] = getUniformLocationSafely(program, uniform.alias + "[0]." + uniform.fieldNames[i]) - startIndex;
            }
            uniform.setUniformLocations(startIndex, size, fieldOffsets);
        }
        initialized = true;
    }

    private int getUniformLocationSafely(ShaderProgram program, String alias) {
        int location = program.fetchUniformLocation(alias, false);
        if (location == -1)
            throw new GdxRuntimeException("Uniform not found in program - " + alias);
        return location;
    }

    private final IntArray tempArray = new IntArray();

    private final int[] getAttributeLocations(final VertexAttributes attrs) {
        tempArray.clear();
        final int n = attrs.size();
        for (int i = 0; i < n; i++) {
            Attribute attribute = attributes.get(attrs.get(i).alias);
            if (attribute != null)
                tempArray.add(attribute.location);
            else
                tempArray.add(-1);
        }
        return tempArray.items;
    }

    public void setCulling(Culling culling) {
        this.culling = culling;
    }

    public void setTransparency(Transparency transparency) {
        this.transparency = transparency;
    }

    public void setBlending(Blending blending) {
        this.blending = blending;
    }

    public void begin(Camera camera, Environment environment, RenderContext context) {
        this.camera = camera;
        this.context = context;
        program.begin();

        // Enable depth testing
        context.setDepthMask(true);
        context.setDepthTest(GL20.GL_LEQUAL, camera.near, camera.far);
        culling.run(context);
        setBlending(context, transparency, blending);

        for (Uniform uniform : uniforms.values()) {
            if (uniform.global)
                uniform.setter.set(this, uniform.location, null, null);
        }
        for (StructArrayUniform uniform : structArrayUniforms.values()) {
            if (uniform.global)
                uniform.setter.set(this, uniform.startIndex, uniform.fieldOffsets, uniform.size, null, null);
        }
    }

    private static void setBlending(RenderContext context, Transparency transparency, Blending blending) {
        boolean enabled = transparency == Transparency.transparent;
        context.setBlending(enabled, blending.getSourceFactor(), blending.getDestinationFactor());
    }

    public void render(Renderable renderable) {
        for (Uniform uniform : uniforms.values()) {
            if (!uniform.global)
                uniform.setter.set(this, uniform.location, renderable, renderable.material);
        }
        for (StructArrayUniform uniform : structArrayUniforms.values()) {
            if (!uniform.global)
                uniform.setter.set(this, uniform.startIndex, uniform.fieldOffsets, uniform.size, renderable, renderable.material);
        }

        if (currentMesh != renderable.meshPart.mesh) {
            if (currentMesh != null) currentMesh.unbind(program, tempArray.items);
            currentMesh = renderable.meshPart.mesh;
            currentMesh.bind(program, getAttributeLocations(renderable.meshPart.mesh.getVertexAttributes()));
        }
        renderable.meshPart.render(program, false);
    }

    public void end() {
        if (currentMesh != null) {
            currentMesh.unbind(program, tempArray.items);
            currentMesh = null;
        }
        program.end();
    }

    @Override
    public void dispose() {
        program = null;
        uniforms.clear();
        structArrayUniforms.clear();
        attributes.clear();
    }

    public void setUniform(final int location, final Matrix4 value) {
        program.setUniformMatrix(location, value);
    }

    public void setUniform(final int location, final Matrix3 value) {
        program.setUniformMatrix(location, value);
    }

    public void setUniform(final int location, final Vector3 value) {
        program.setUniformf(location, value);
    }

    public void setUniform(final int location, final Vector2 value) {
        program.setUniformf(location, value);
    }

    public void setUniform(final int location, final Color value) {
        program.setUniformf(location, value);
    }

    public void setUniform(final int location, final float value) {
        program.setUniformf(location, value);
    }

    public void setUniform(final int location, final float v1, final float v2) {
        program.setUniformf(location, v1, v2);
    }

    public void setUniform(final int location, final float v1, final float v2, final float v3) {
        program.setUniformf(location, v1, v2, v3);
    }

    public void setUniform(final int location, final float v1, final float v2, final float v3, final float v4) {
        program.setUniformf(location, v1, v2, v3, v4);
    }

    public void setUniform(final int location, final int value) {
        program.setUniformi(location, value);
    }

    public void setUniform(final int location, final int v1, final int v2) {
        program.setUniformi(location, v1, v2);
    }

    public void setUniform(final int location, final int v1, final int v2, final int v3) {
        program.setUniformi(location, v1, v2, v3);
    }

    public void setUniform(final int location, final int v1, final int v2, final int v3, final int v4) {
        program.setUniformi(location, v1, v2, v3, v4);
    }

    public void setUniform(final int location, final TextureDescriptor textureDesc) {
        program.setUniformi(location, context.textureBinder.bind(textureDesc));
    }

    public void setUniform(final int location, final GLTexture texture) {
        program.setUniformi(location, context.textureBinder.bind(texture));
    }

    public void setUniformMatrixArray(final int location, float[] values) {
        program.setUniformMatrix4fv(location, values, 0, values.length);
    }

    public void setUniformArray(final int location, float[] values) {
        program.setUniform3fv(location, values, 0, values.length);
    }
}
