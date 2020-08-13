package com.gempukku.libgdx.graph.renderer.loader.node;

import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.renderer.loader.PipelineRenderingContext;

import java.util.HashMap;
import java.util.Map;

public abstract class OncePerFrameJobPipelineNode implements PipelineNode {
    private boolean executedInFrame;
    private PipelineNodeConfiguration configuration;
    private Map<String, FieldOutput<?>> inputFields;
    private Map<String, WorkerFieldOutput<Object>> workerFieldOutputs = new HashMap<>();

    public OncePerFrameJobPipelineNode(PipelineNodeConfiguration configuration, Map<String, FieldOutput<?>> inputFields) {
        this.configuration = configuration;
        this.inputFields = inputFields;
    }

    @Override
    public FieldOutput<?> getFieldOutput(String name) {
        WorkerFieldOutput<Object> fieldOutput = workerFieldOutputs.get(name);
        if (fieldOutput == null) {
            PropertyType propertyType = determineOutputType(name, inputFields);
            fieldOutput = new WorkerFieldOutput<>(propertyType);
            workerFieldOutputs.put(name, fieldOutput);
        }

        return fieldOutput;
    }

    protected PropertyType determineOutputType(String name, Map<String, FieldOutput<?>> inputFields) {
        return configuration.getNodeOutput(name).getProducablePropertyTypes().get(0);
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

    private class WorkerFieldOutput<T> implements FieldOutput<T>, OutputValue<T> {
        private PropertyType propertyType;
        private T value;

        public WorkerFieldOutput(PropertyType propertyType) {
            this.propertyType = propertyType;
        }

        @Override
        public void setValue(T value) {
            this.value = value;
        }

        @Override
        public PropertyType getPropertyType() {
            return propertyType;
        }

        @Override
        public T getValue(PipelineRenderingContext context) {
            if (!executedInFrame) {
                executeJob(context, workerFieldOutputs);
            }
            return value;
        }
    }

    protected interface OutputValue<T> {
        void setValue(T value);
    }
}
