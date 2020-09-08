Lighting getPointLightContribution(vec3 pos, vec3 normal, float shininess, Lighting lighting) {
    vec3 viewVec = normalize(u_cameraPosition.xyz - pos.xyz);
    for (int i = 0; i < NUM_POINT_LIGHTS; i++) {
        vec3 lightDir = u_pointLights[i].position - pos.xyz;
        float dist2 = dot(lightDir, lightDir);
        lightDir *= inversesqrt(dist2);
        float NdotL = clamp(dot(normal, lightDir), 0.0, 1.0);
        vec3 value = u_pointLights[i].color * (NdotL / (1.0 + dist2));
        lighting.diffuse += value;
        float halfDotView = max(0.0, dot(normal, normalize(lightDir + viewVec)));
        lighting.specular += value * pow(halfDotView, shininess);
    }
    return lighting;
}
