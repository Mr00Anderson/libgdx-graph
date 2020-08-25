package com.gempukku.libgdx.graph.ui.shader.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.gempukku.libgdx.graph.data.Graph;
import com.gempukku.libgdx.graph.data.GraphConnection;
import com.gempukku.libgdx.graph.data.GraphNode;
import com.gempukku.libgdx.graph.data.GraphProperty;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderAttribute;
import com.gempukku.libgdx.graph.shader.GraphShaderBuilder;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

public class ShaderPreviewWidget extends Widget {
    private boolean shaderInitialized;
    private int width;
    private int height;

    private FrameBuffer frameBuffer;
    private GraphShader graphShader;
    private RenderContext renderContext;
    private Model model;
    private ModelInstance modelInstance;
    private Renderable renderable = new Renderable();
    private Camera camera;

    public ShaderPreviewWidget(int width, int height) {
        this.width = width;
        this.height = height;
        renderContext = new RenderContext(new DefaultTextureBinder(DefaultTextureBinder.LRU, 1));
        camera = new PerspectiveCamera();
        camera.position.set(1.5f, 0f, 0f);
        camera.up.set(0f, 1f, 0f);
        camera.lookAt(0, 0f, 0f);
        camera.update();
    }

    @Override
    protected void setStage(Stage stage) {
        super.setStage(stage);
        if (stage == null && shaderInitialized) {
            destroyShader();
        }
    }

    @Override
    public float getPrefWidth() {
        return width;
    }

    @Override
    public float getPrefHeight() {
        return height;
    }

    private void createShader(Graph<? extends GraphNode<ShaderFieldType>, ? extends GraphConnection, ? extends GraphProperty<ShaderFieldType>, ShaderFieldType> graph) {
        System.out.println("Create shader");
        graphShader = new GraphShader("");
        GraphShaderBuilder.buildShader(graphShader, graph, true);
        frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false);
        createModel();

        shaderInitialized = true;
    }

    private void createModel() {
        ModelBuilder modelBuilder = new ModelBuilder();
        Material material = new Material(new GraphShaderAttribute());
        model = modelBuilder.createSphere(1, 1, 1, 20, 20,
                material,
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);
        modelInstance = new ModelInstance(model);
    }

    private void destroyShader() {
        System.out.println("Destroy shader");
        model.dispose();
        frameBuffer.dispose();
        frameBuffer = null;
        graphShader.dispose();
        shaderInitialized = false;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        if (frameBuffer != null) {
            batch.end();

            Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST);
            frameBuffer.begin();
            camera.viewportWidth = width;
            camera.viewportHeight = height;
            camera.update();

            modelInstance.getRenderable(renderable);

            renderContext.begin();
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
            graphShader.begin(camera, null, renderContext);
            graphShader.render(renderable);
            graphShader.end();
            frameBuffer.end();
            renderContext.end();
            if (ScissorStack.peekScissors() != null)
                Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);

            batch.begin();
            batch.draw(frameBuffer.getColorBufferTexture(), getX(), getY(), width, height);
        }
    }

    public void graphChanged(boolean hasErrors, Graph<? extends GraphNode<ShaderFieldType>, ? extends GraphConnection, ? extends GraphProperty<ShaderFieldType>, ShaderFieldType> graph) {
        if (hasErrors && shaderInitialized) {
            destroyShader();
        } else if (!hasErrors && !shaderInitialized) {
            createShader(graph);
        }
    }
}
