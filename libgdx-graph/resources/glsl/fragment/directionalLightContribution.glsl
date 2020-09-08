Lighting getDirectionalLightContribution(vec3 pos, vec3 normal, float shininess, Lighting lighting) {
    vec3 viewVec = normalize(u_cameraPosition.xyz - pos.xyz);
    for (int i = 0; i < NUM_DIRECTIONAL_LIGHTS; i++) {
        vec3 lightDir = -u_dirLights[i].direction;
        float NdotL = clamp(dot(normal, lightDir), 0.0, 1.0);
        vec3 value = u_dirLights[i].color * NdotL;
        lighting.diffuse += value;
        float halfDotView = max(0.0, dot(normal, normalize(lightDir + viewVec)));
        lighting.specular += value * pow(halfDotView, shininess);
    }
    return lighting;
}
