package com.gempukku.libgdx.graph.shader;

public class GraphShaderConfig {
    private static int MAX_NUMBER_OF_BONES_PER_MESH = 12;
    private static int MAX_NUMBER_OF_BONE_WEIGHTS = 5;
    private static boolean SET_IN_STONE = false;

    private GraphShaderConfig() {
    }

    public static void init(int maxNumberOfBonesPerMesh, int maxNumberOfBoneWeights) {
        if (SET_IN_STONE)
            throw new IllegalStateException();
        MAX_NUMBER_OF_BONES_PER_MESH = maxNumberOfBonesPerMesh;
        MAX_NUMBER_OF_BONE_WEIGHTS = maxNumberOfBoneWeights;
    }

    public static int getMaxNumberOfBonesPerMesh() {
        SET_IN_STONE = true;
        return MAX_NUMBER_OF_BONES_PER_MESH;
    }

    public static int getMaxNumberOfBoneWeights() {
        SET_IN_STONE = true;
        return MAX_NUMBER_OF_BONE_WEIGHTS;
    }
}
