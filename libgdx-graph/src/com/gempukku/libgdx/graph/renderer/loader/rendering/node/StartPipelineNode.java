package com.gempukku.libgdx.graph.renderer.loader.rendering.node;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.gempukku.libgdx.graph.renderer.RenderPipeline;
import com.gempukku.libgdx.graph.renderer.impl.RenderPipelineImpl;
import com.gempukku.libgdx.graph.renderer.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.renderer.loader.node.OncePerFrameJobPipelineNode;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfiguration;
import com.google.common.base.Function;

import javax.annotation.Nullable;
import java.util.Map;

public class StartPipelineNode extends OncePerFrameJobPipelineNode {
    private Function<PipelineRenderingContext, Color> color;
    private Function<PipelineRenderingContext, Vector2> size;

    private RenderPipelineImpl renderPipeline;

    public StartPipelineNode(PipelineNodeConfiguration configuration,
                             Function<PipelineRenderingContext, Color> color,
                             Function<PipelineRenderingContext, Vector2> size) {
        super(configuration);
        if (color == null) {
            final Color defaultColor = Color.BLACK;
            color = new Function<PipelineRenderingContext, Color>() {
                @Override
                public Color apply(@Nullable PipelineRenderingContext pipelineRenderingContext) {
                    return defaultColor;
                }
            };
        }
        if (size == null) {
            size = new Function<PipelineRenderingContext, Vector2>() {
                @Override
                public Vector2 apply(@Nullable PipelineRenderingContext o) {
                    return new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                }
            };
        }
        this.color = color;
        this.size = size;
        renderPipeline = new RenderPipelineImpl();
    }

    @Override
    protected void executeJob(PipelineRenderingContext pipelineRenderingContext, Map<String, ? extends OutputValue> outputValues) {
        Vector2 bufferSize = size.apply(pipelineRenderingContext);
        Color backgroundColor = color.apply(pipelineRenderingContext);

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
