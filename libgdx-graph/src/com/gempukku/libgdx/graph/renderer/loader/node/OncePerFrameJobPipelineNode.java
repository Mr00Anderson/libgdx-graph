package com.gempukku.libgdx.graph.renderer.loader.node;

import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.renderer.loader.PipelineRenderingContext;
import com.google.common.base.Function;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class OncePerFrameJobPipelineNode implements PipelineNode {
    private boolean executedInFrame;
    private PipelineNodeConfiguration configuration;
    private Map<String, WorkerFunction<Object>> workerFunctions = new HashMap<>();

    public OncePerFrameJobPipelineNode(PipelineNodeConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public PropertyType determinePropertyType(String name, List<PropertyType> acceptedPropertyTypes) {
        List<PropertyType> producablePropertyTypes = configuration.getNodeOutput(name).getProducablePropertyTypes();
        for (PropertyType acceptedPropertyType : acceptedPropertyTypes) {
            if (producablePropertyTypes.contains(acceptedPropertyType))
                return acceptedPropertyType;
        }
        return null;
    }

    @Override
    public Function<PipelineRenderingContext, ?> getOutputSupplier(String name, PropertyType propertyType) {
        PipelineNodeOutput nodeOutput = configuration.getNodeOutput(name);
        if (nodeOutput == null)
            throw new IllegalArgumentException("This node does not have an output of: " + name);

        WorkerFunction<Object> workerFunction = workerFunctions.get(name);
        if (workerFunction == null) {
            workerFunction = new WorkerFunction<>();
            workerFunctions.put(name, workerFunction);
        }
        return workerFunction;
    }

    protected abstract void executeJob(PipelineRenderingContext pipelineRenderingContext, Map<String, ? extends OutputValue> outputValues);

    @Override
    public void startFrame(float delta) {

    }

    @Override
    public void endFrame() {
        executedInFrame = false;
    }

    @Override
    public void dispose() {

    }

    private class WorkerFunction<T> implements Function<PipelineRenderingContext, T>, OutputValue<T> {
        private T value;

        @Override
        public void setValue(T value) {
            this.value = value;
        }

        @Override
        public T apply(@Nullable PipelineRenderingContext pipelineRenderingContext) {
            if (!executedInFrame) {
                executeJob(pipelineRenderingContext, workerFunctions);
            }
            return value;
        }
    }

    protected interface OutputValue<T> {
        void setValue(T value);
    }
}
