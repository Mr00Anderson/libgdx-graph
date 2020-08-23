package com.gempukku.libgdx.graph.ui.producer.shader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxInputConnector;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxOutputConnector;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxPart;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class GraphShadersBoxPart extends Table implements GraphBoxPart<PipelineFieldType> {
    private static final int GRAPH_WIDTH = 50;
    private static final int ACTIONS_WIDTH = 50;
    private final VerticalGroup shadersGroup;
    private final Skin skin;
    private List<ShaderInfo> shaders = new LinkedList<>();

    public GraphShadersBoxPart(Skin skin) {
        this.skin = skin;

        shadersGroup = new VerticalGroup();
        shadersGroup.top();
        shadersGroup.grow();

        Table table = new Table(skin);
        table.add("Tag").growX();
        table.add("Graph").width(GRAPH_WIDTH);
        table.add("Actions").width(ACTIONS_WIDTH);
        table.row();
        shadersGroup.addActor(table);

        ScrollPane scrollPane = new ScrollPane(shadersGroup);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setForceScroll(false, true);

        add(scrollPane).grow().row();

        TextButton newShader = new TextButton("New Shader", skin);
        newShader.addListener(
                new ClickListener(Input.Buttons.LEFT) {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        String id = UUID.randomUUID().toString().replace("-", "");
                        try {
                            JSONParser parser = new JSONParser();
                            InputStream is = Gdx.files.internal("template/empty-shader.json").read();
                            try {
                                JSONObject shader = (JSONObject) parser.parse(new InputStreamReader(is));
                                addShaderGraph(id, "", shader);
                            } finally {
                                is.close();
                            }
                        } catch (IOException exp) {

                        } catch (ParseException exp) {

                        }
                    }
                });

        Table buttonTable = new Table(skin);
        buttonTable.add(newShader);

        add(buttonTable).growX().row();
    }

    public void initialize(JSONObject data) {
        JSONArray shaderArray = (JSONArray) data.get("shaders");
        for (JSONObject shaderObject : (List<JSONObject>) shaderArray) {
            String id = (String) shaderObject.get("id");
            String tag = (String) shaderObject.get("tag");
            JSONObject shader = (JSONObject) shaderObject.get("shader");
            addShaderGraph(id, tag, shader);
        }
    }

    @Override
    public float getPrefWidth() {
        return 300;
    }

    @Override
    public float getPrefHeight() {
        return 200;
    }

    public void addShaderGraph(String id, String tag, JSONObject shader) {
        ShaderInfo shaderInfo = new ShaderInfo(id, tag, shader);
        shaders.add(shaderInfo);
        shadersGroup.addActor(shaderInfo.getTable());
    }

    @Override
    public Actor getActor() {
        return this;
    }

    @Override
    public GraphBoxOutputConnector<PipelineFieldType> getOutputConnector() {
        return null;
    }

    @Override
    public GraphBoxInputConnector<PipelineFieldType> getInputConnector() {
        return null;
    }

    @Override
    public void serializePart(JSONObject object) {
        JSONArray shaderArray = new JSONArray();
        for (ShaderInfo shader : shaders) {
            JSONObject shaderObject = new JSONObject();
            shaderObject.put("id", shader.getId());
            shaderObject.put("tag", shader.getTag());
            shaderObject.put("shader", shader.getShaderJson());
            shaderArray.add(shaderObject);
        }

        object.put("shaders", shaderArray);
    }

    private class ShaderInfo {
        private String id;
        private Table table;
        private TextField textField;
        private JSONObject shaderJson;

        public ShaderInfo(String id, String tag, JSONObject shaderJson) {
            this.id = id;
            this.shaderJson = shaderJson;
            table = new Table(skin);
            textField = new TextField(tag, skin);
            textField.setMessageText("Shader Tag");
            table.add(textField).growX();
            TextButton textButton = new TextButton("Edit", skin);
            textButton.fire(new RequestGraphOpen(id, shaderJson));
            table.add(textButton).width(ACTIONS_WIDTH);
            table.add().width(ACTIONS_WIDTH);
            table.row();
        }

        public String getId() {
            return id;
        }

        public Table getTable() {
            return table;
        }

        public String getTag() {
            return textField.getText();
        }

        public JSONObject getShaderJson() {
            return shaderJson;
        }
    }
}
