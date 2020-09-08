package com.gempukku.libgdx.graph.shader.models;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.model.Animation;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.model.NodePart;
import com.badlogic.gdx.utils.Disposable;
import com.gempukku.libgdx.graph.shader.GraphShaderConfig;

import java.util.UUID;

public class GraphShaderModel implements Disposable {
    private static VertexAttributes attributes;
    private Model internalModel;

    public GraphShaderModel(Model model) {
        init(model);
    }

    private void init(Model model) {
        internalModel = new Model();
        // copy nodes and animations only
        for (Node node : model.nodes) {
            internalModel.nodes.add(copyNode(node));
        }
        for (Animation animation : model.animations) {
            internalModel.animations.add(copyAnimation(animation));
        }
        standardizeMeshes();
    }

    private void standardizeMeshes() {
        for (Node node : internalModel.nodes) {
            standardizeMeshes(node);
        }
    }

    private void standardizeMeshes(Node node) {
        for (NodePart part : node.parts) {
            Mesh mesh = standardizeMesh(part.meshPart.mesh);
            internalModel.manageDisposable(mesh);
            part.meshPart.mesh = mesh;
        }

        int childCount = node.getChildCount();
        for (int i = 0; i < childCount; i++) {
            standardizeMeshes(node.getChild(i));
        }
    }

    private Mesh standardizeMesh(Mesh mesh) {
        if (attributes == null) {
            int maxNumberOfBoneWeights = GraphShaderConfig.getMaxNumberOfBoneWeights();
            int maxNumberOfUVs = GraphShaderConfig.getMaxNumberOfUVs();

            VertexAttribute[] vertexAttributeArr = new VertexAttribute[3 + maxNumberOfUVs + maxNumberOfBoneWeights];
            vertexAttributeArr[0] = VertexAttribute.Position();
            vertexAttributeArr[1] = VertexAttribute.Normal();
            vertexAttributeArr[2] = VertexAttribute.Tangent();
            for (int i = 0; i < maxNumberOfUVs; i++) {
                vertexAttributeArr[3 + i] = VertexAttribute.TexCoords(i);
            }
            for (int i = 0; i < maxNumberOfBoneWeights; i++) {
                vertexAttributeArr[3 + maxNumberOfUVs + i] = VertexAttribute.BoneWeight(i);
            }
            attributes = new VertexAttributes(vertexAttributeArr);
        }

        VertexAttributes vertexAttributes = mesh.getVertexAttributes();
        int[] offsetMap = new int[attributes.size()];
        int index = 0;
        for (VertexAttribute attribute : attributes) {
            int offset = findAttributeOffset(vertexAttributes, attribute);
            offsetMap[index++] = offset;
        }

        int numVertices = mesh.getNumVertices();
        int sourceVertexLength = vertexAttributes.vertexSize / 4;
        float[] sourceVboData = new float[numVertices * sourceVertexLength];
        mesh.getVertices(sourceVboData);
        float[] newVboData = new float[numVertices * attributes.vertexSize / 4];
        int arrayIndex = 0;
        for (int vertexIndex = 0; vertexIndex < numVertices; vertexIndex++) {
            for (int attributeIndex = 0; attributeIndex < offsetMap.length; attributeIndex++) {
                VertexAttribute attribute = attributes.get(attributeIndex);
                if (offsetMap[attributeIndex] != -1) {
                    System.arraycopy(sourceVboData, sourceVertexLength * vertexIndex + offsetMap[attributeIndex],
                            newVboData, arrayIndex, attribute.numComponents);
                } else if (attribute.usage == VertexAttributes.Usage.BoneWeight && attribute.unit == 0) {
                    // For bone-weightless vertices, create a dummy one to have correctly created skinning matrix
                    newVboData[arrayIndex + 0] = 0f;
                    newVboData[arrayIndex + 1] = 1f;
                }
                arrayIndex += attribute.numComponents;
            }
        }

        int numIndices = mesh.getNumIndices();
        short[] sourceIboData = new short[numIndices];
        mesh.getIndices(sourceIboData);

        Mesh result = new Mesh(true, numVertices, numIndices, attributes);
        result.setIndices(sourceIboData);
        result.setVertices(newVboData);
        return result;
    }

    private int findAttributeOffset(VertexAttributes vertexAttributes, VertexAttribute attribute) {
        for (VertexAttribute vertexAttribute : vertexAttributes) {
            if (vertexAttribute.usage == attribute.usage && vertexAttribute.unit == attribute.unit)
                return vertexAttribute.offset / 4;
        }
        return -1;
    }


    private Animation copyAnimation(Animation animation) {
        return animation;
    }

    private Node copyNode(Node node) {
        return node.copy();
    }

    public GraphShaderModelInstanceImpl createInstance() {
        String id = UUID.randomUUID().toString().replace("-", "");
        return new GraphShaderModelInstanceImpl(id, this, new ModelInstance(internalModel));
    }

    @Override
    public void dispose() {
        internalModel.dispose();
    }
}
