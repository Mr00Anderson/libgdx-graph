Lighting getSpotLightContribution(vec3 pos, vec3 normal, float shininess, Lighting lighting) {
    vec3 viewVec = normalize(u_cameraPosition.xyz - pos.xyz);
    for (int i = 0; i < NUM_SPOT_LIGHTS; i++) {
        vec3 lightDir = u_spotLights[i].position - pos.xyz;
        float dist2 = dot(lightDir, lightDir);
        lightDir *= inversesqrt(dist2);

        // light direction
        vec3 l = normalize(-u_spotLights[i].direction);// Vector from surface point to light

        // from https://github.com/KhronosGroup/glTF/blob/master/extensions/2.0/Khronos/KHR_lights_punctual/README.md#inner-and-outer-cone-angles
        float lightAngleOffset = u_spotLights[i].cutoffAngle;
        float lightAngleScale = u_spotLights[i].exponent;

        float cd = dot(l, lightDir);
        float angularAttenuation = clamp(cd * lightAngleScale + lightAngleOffset, 0.0, 1.0);
        angularAttenuation *= angularAttenuation;

        float NdotL = clamp(dot(normal, lightDir), 0.0, 1.0);
        vec3 value = u_spotLights[i].color * (NdotL / (1.0 + dist2));
        lighting.diffuse += value * angularAttenuation;
        float halfDotView = max(0.0, dot(normal, normalize(lightDir + viewVec)));
        lighting.specular += value * angularAttenuation * pow(halfDotView, shininess);
    }
    return lighting;
}
