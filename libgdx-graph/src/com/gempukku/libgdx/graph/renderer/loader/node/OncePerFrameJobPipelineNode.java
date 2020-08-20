package com.gempukku.libgdx.graph.renderer.loader.node;

import com.gempukku.libgdx.graph.renderer.PipelineFieldType;
import com.gempukku.libgdx.graph.renderer.loader.PipelineRenderingContext;

import java.util.HashMap;
import java.util.Map;

public abstract class OncePerFrameJobPipelineNode implements PipelineNode {
    private boolean executedInFrame;
    private PipelineNodeConfiguration<PipelineFieldType> configuration;
    private Map<String, FieldOutput<?>> inputFields;
    private Map<String, WorkerFieldOutput<Object>> workerFieldOutputs = new HashMap<>();

    public OncePerFrameJobPipelineNode(PipelineNodeConfiguration<PipelineFieldType> configuration, Map<String, FieldOutput<?>> inputFields) {
        this.configuration = configuration;
        this.inputFields = inputFields;
    }

    @Override
    public FieldOutput<?> getFieldOutput(String name) {
        WorkerFieldOutput<Object> fieldOutput = workerFieldOutputs.get(name);
        if (fieldOutput == null) {
            PipelineFieldType fieldType = determineOutputType(name, inputFields);
            fieldOutput = new WorkerFieldOutput<>(fieldType);
            workerFieldOutputs.put(name, fieldOutput);
        }

        return fieldOutput;
    }

    protected PipelineFieldType determineOutputType(String name, Map<String, FieldOutput<?>> inputFields) {
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
        private PipelineFieldType fieldType;
        private T value;

        public WorkerFieldOutput(PipelineFieldType fieldType) {
            this.fieldType = fieldType;
        }

        @Override
        public void setValue(T value) {
            this.value = value;
        }

        @Override
        public PipelineFieldType getPropertyType() {
            return fieldType;
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
