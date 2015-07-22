package net.glowstone.data.manipulator;

import com.google.common.base.Optional;
import net.glowstone.data.processor.DataProcessorRegistry;
import org.spongepowered.api.data.*;

public abstract class GlowDataManipulator<T extends DataManipulator<T>> implements DataManipulator<T> {
    private final Class<T> manipulatorClass;

    public GlowDataManipulator(Class<T> manipulatorClass) {
        this.manipulatorClass = manipulatorClass;
    }

    public Class<T> getManipulatorClass() {
        return manipulatorClass;
    }

    @Override
    public Optional<T> fill(DataHolder dataHolder) {
        return fill(dataHolder, DataPriority.DATA_HOLDER);
    }

    @Override
    public Optional<T> fill(DataHolder dataHolder, DataPriority overlap) {
        return DataProcessorRegistry.getProcessor(manipulatorClass).fillData(dataHolder, (T) this, overlap);
    }

    @Override
    public Optional<T> from(DataContainer container) {
        return DataProcessorRegistry.getProcessor(manipulatorClass).build(container);
    }

    @Override
    public DataContainer toContainer() {
        return new MemoryDataContainer();
    }
}
