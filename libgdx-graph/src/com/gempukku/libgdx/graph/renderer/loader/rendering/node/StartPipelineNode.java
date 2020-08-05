package com.gempukku.libgdx.graph.renderer.loader.rendering.node;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.gempukku.libgdx.graph.renderer.impl.RenderPipelineImpl;
import com.gempukku.libgdx.graph.renderer.loader.PipelineNode;
import com.gempukku.libgdx.graph.renderer.loader.rendering.WorkerSupplier;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;

import java.util.HashMap;
import java.util.Map;

public class StartPipelineNode implements PipelineNode {
    private Supplier<Color> color;
    private Supplier<Vector2> size;
    private Map<String, WorkerSupplier<Object>> workerSuppliers = new HashMap<>();

    private RenderPipelineImpl renderPipeline;

    public StartPipelineNode(Supplier<Color> color, Supplier<Vector2> size) {
        if (color == null) {
            color = Suppliers.ofInstance(new Color(0, 0, 0, 1));
        }
        if (size == null) {
            size = new Supplier<Vector2>() {
                @Override
                public Vector2 get() {
                    return new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                }
            };
        }
        this.color = color;
        this.size = size;
        renderPipeline = new RenderPipelineImpl();
    }

    @Override
    public void startFrame() {

    }

    public void executeNode() {
        Vector2 bufferSize = size.get();
        Color backgroundColor = color.get();

        FrameBuffer frameBuffer = renderPipeline.getNewFrameBuffer(MathUtils.round(bufferSize.x), MathUtils.round(bufferSize.y), Pixmap.Format.RGBA8888);
        renderPipeline.setCurrentBuffer(frameBuffer);

        frameBuffer.begin();
        Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        frameBuffer.end();

        WorkerSupplier<Object> output = workerSuppliers.get("output");
        if (output != null)
            output.setValue(renderPipeline);
    }

    @Override
    public void endFrame() {
        renderPipeline.ageOutBuffers();
        for (WorkerSupplier<?> value : workerSuppliers.values()) {
            value.resetValue();
        }
    }

    @Override
    public Supplier<?> getOutputSupplier(String name) {
        if (!name.equals("output"))
            throw new IllegalArgumentException();
        WorkerSupplier<Object> workerSupplier = workerSuppliers.get(name);
        if (workerSupplier == null) {
            workerSupplier = new WorkerSupplier<>(
                    new Runnable() {
                        @Override
                        public void run() {
                            executeNode();
                        }
                    });
            workerSuppliers.put(name, workerSupplier);
        }
        return workerSupplier;
    }

    @Override
    public void dispose() {
        renderPipeline.dispose();
    }
}
