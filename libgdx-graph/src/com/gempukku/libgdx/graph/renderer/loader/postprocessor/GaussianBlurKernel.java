package com.gempukku.libgdx.graph.renderer.loader.postprocessor;

public class GaussianBlurKernel {
    private static final float SIGMA_HELPER = 3.5676f;

    private GaussianBlurKernel() {
    }

    public static float[] create1DBlurKernel(int size) {
        float[] result = new float[1 + size];
        float sigma = size / SIGMA_HELPER;
        float norm = 1f / ((float) Math.sqrt(2 * Math.PI) * sigma);
        float coeff = 2 * sigma * sigma;
        float total = 0;
        for (int i = 0; i <= size; i++) {
            float value = norm * (float) Math.exp(-i * i / coeff);
            result[i] = value;
            total += value;
            if (i > 0)
                total += value;
        }
        for (int i = 0; i <= size; i++) {
            result[i] /= total;
        }
        return result;
    }
}
