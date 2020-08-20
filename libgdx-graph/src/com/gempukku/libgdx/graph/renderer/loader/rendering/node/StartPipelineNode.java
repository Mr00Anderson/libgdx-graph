package com.gempukku.libgdx.graph.renderer.loader.rendering.node;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.renderer.PipelineFieldType;
import com.gempukku.libgdx.graph.renderer.RenderPipeline;
import com.gempukku.libgdx.graph.renderer.impl.RenderPipelineImpl;
import com.gempukku.libgdx.graph.renderer.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.renderer.loader.node.OncePerFrameJobPipelineNode;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfiguration;

import java.util.Map;

public class StartPipelineNode extends OncePerFrameJobPipelineNode {
    private FieldOutput<Color> color;
    private FieldOutput<Vector2> size;

    private RenderPipelineImpl renderPipeline;

    public StartPipelineNode(PipelineNodeConfiguration configuration, Map<String, FieldOutput<?>> inputFields) {
        super(configuration, inputFields);
        color = (FieldOutput<Color>) inputFields.get("background");
        size = (FieldOutput<Vector2>) inputFields.get("size");

        if (color == null) {
            final Color defaultColor = Color.BLACK;
            color = new FieldOutput<Color>() {
                @Override
                public FieldType getPropertyType() {
                    return PipelineFieldType.Color;
                }

                @Override
                public Color getValue(PipelineRenderingContext context) {
                    return defaultColor;
                }
            };
        }
        if (size == null) {
            size = new FieldOutput<Vector2>() {
                @Override
                public FieldType getPropertyType() {
                    return PipelineFieldType.Vector2;
                }

                @Override
                public Vector2 getValue(PipelineRenderingContext context) {
                    return new Vector2(context.getRenderWidth(), context.getRenderHeight());
                }
            };
        }
        renderPipeline = new RenderPipelineImpl();
    }

    @Override
    protected void executeJob(PipelineRenderingContext pipelineRenderingContext, Map<String, ? extends OutputValue> outputValues) {
        Vector2 bufferSize = size.getValue(pipelineRenderingContext);
        Color backgroundColor = color.getValue(pipelineRenderingContext);

        FrameBuffer frameBuffer = renderPipeline.getNewFrameBuffer(MathUtils.round(bufferSize.x), MathUtils.round(bufferSize.y), Pixmap.Format.RGBA8888);
        renderPipeline.setCurrentBuffer(frameBuffer);

        frameBuffer.begin();
        Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        frameBuffer.end();

        OutputValue<RenderPipeline> output = outputValues.get("output");
        if (output != null)
            output.setValue(renderPipeline);
    }

    @Override
    public void endFrame() {
        super.endFrame();
        renderPipeline.ageOutBuffers();
    }

    @Override
    public void dispose() {
        renderPipeline.dispose();
    }
}
