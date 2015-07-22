package net.glowstone.data.processor;

import com.google.common.base.Optional;
import org.spongepowered.api.data.*;
import org.spongepowered.api.service.persistence.DataBuilder;

public interface GlowDataProcessor<T extends DataManipulator<T>> extends DataBuilder<T>, DataManipulatorBuilder<T> {
    Optional<T> getFrom(DataHolder dataHolder);


    Optional<T> fillData(DataHolder dataHolder, T old, DataPriority priority);


    DataTransactionResult setData(DataHolder dataHolder, T old, DataPriority priority);


    boolean remove(DataHolder dataHolder);
}
