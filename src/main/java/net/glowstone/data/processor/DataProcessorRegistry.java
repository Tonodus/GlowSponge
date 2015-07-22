package net.glowstone.data.processor;

import org.spongepowered.api.data.DataManipulator;

public class DataProcessorRegistry {
    private DataProcessorRegistry() {
    }

    public static <T extends DataManipulator<T>> GlowDataProcessor<T> getProcessor(Class<T> manipulatorClass) {
        return null;
    }
}
