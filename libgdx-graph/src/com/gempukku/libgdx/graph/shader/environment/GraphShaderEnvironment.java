package com.gempukku.libgdx.graph.shader.environment;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.g3d.environment.SpotLight;
import com.badlogic.gdx.utils.Array;

public class GraphShaderEnvironment {
    private Color ambientColor;
    private Array<DirectionalLight> directionalLights = new Array<>();
    private Array<PointLight> pointLights = new Array<>();
    private Array<SpotLight> spotLights = new Array<>();

    public Color getAmbientColor() {
        return ambientColor;
    }

    public void setAmbientColor(Color ambientColor) {
        this.ambientColor = ambientColor;
    }

    public void addDirectionalLight(DirectionalLight directionalLight) {
        directionalLights.add(directionalLight);
    }

    public void removeDirectionalLight(DirectionalLight directionalLight) {
        directionalLights.removeValue(directionalLight, true);
    }

    public void addPointLight(PointLight pointLight) {
        pointLights.add(pointLight);
    }

    public void removePointLight(PointLight pointLight) {
        pointLights.removeValue(pointLight, true);
    }

    public void addSpotLight(SpotLight spotLight) {
        spotLights.add(spotLight);
    }

    public void removeSpotLight(SpotLight spotLight) {
        spotLights.removeValue(spotLight, true);
    }

    public Array<DirectionalLight> getDirectionalLights() {
        return directionalLights;
    }

    public Array<PointLight> getPointLights() {
        return pointLights;
    }

    public Array<SpotLight> getSpotLights() {
        return spotLights;
    }
}
