package com.gempukku.libgdx.graph.shader;

public class GraphShaderConfig {
    private static int MAX_NUMBER_OF_UVS = 1;
    private static int MAX_NUMBER_OF_BONES_PER_MESH = 12;
    private static int MAX_NUMBER_OF_BONE_WEIGHTS = 5;
    private static int MAX_NUMBER_OF_DIRECTIONAL_LIGHTS = 2;
    private static int MAX_NUMBER_OF_POINT_LIGHTS = 5;
    private static int MAX_NUMBER_OF_SPOTLIGHTS = 5;

    private static boolean SET_IN_STONE = false;

    private GraphShaderConfig() {
    }

    public static void initUVs(int maxNumberOfUVs) {
        if (SET_IN_STONE)
            throw new IllegalStateException();
        MAX_NUMBER_OF_UVS = maxNumberOfUVs;
    }

    public static void initBones(int maxNumberOfBonesPerMesh, int maxNumberOfBoneWeights) {
        if (SET_IN_STONE)
            throw new IllegalStateException();
        MAX_NUMBER_OF_BONES_PER_MESH = maxNumberOfBonesPerMesh;
        MAX_NUMBER_OF_BONE_WEIGHTS = maxNumberOfBoneWeights;
    }

    public static void initLights(int maxNumberOfDirectionalLights, int maxNumberOfPointLights, int maxNumberOfSpotlights) {
        if (SET_IN_STONE)
            throw new IllegalStateException();
        MAX_NUMBER_OF_DIRECTIONAL_LIGHTS = maxNumberOfDirectionalLights;
        MAX_NUMBER_OF_POINT_LIGHTS = maxNumberOfPointLights;
        MAX_NUMBER_OF_SPOTLIGHTS = maxNumberOfSpotlights;
    }

    public static int getMaxNumberOfUVs() {
        freezeSettings();
        return MAX_NUMBER_OF_UVS;
    }

    public static int getMaxNumberOfBonesPerMesh() {
        freezeSettings();
        return MAX_NUMBER_OF_BONES_PER_MESH;
    }

    public static int getMaxNumberOfBoneWeights() {
        freezeSettings();
        return MAX_NUMBER_OF_BONE_WEIGHTS;
    }

    public static int getMaxNumberOfDirectionalLights() {
        freezeSettings();
        return MAX_NUMBER_OF_DIRECTIONAL_LIGHTS;
    }

    public static int getMaxNumberOfPointLights() {
        freezeSettings();
        return MAX_NUMBER_OF_POINT_LIGHTS;
    }

    public static int getMaxNumberOfSpotlights() {
        freezeSettings();
        return MAX_NUMBER_OF_SPOTLIGHTS;
    }

    private static void freezeSettings() {
        SET_IN_STONE = true;
    }
}
