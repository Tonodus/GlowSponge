package net.glowstone.data.processor.block;

import com.google.common.base.Optional;
import net.glowstone.data.manipulator.block.GlowDirtData;
import net.glowstone.data.processor.GlowDataProcessor;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataPriority;
import org.spongepowered.api.data.DataTransactionResult;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.block.DirtData;
import org.spongepowered.api.service.persistence.InvalidDataException;

public class GlowDirtDataProcessor implements GlowDataProcessor<DirtData> {
    @Override
    public Optional<DirtData> getFrom(DataHolder dataHolder) {
        Optional<DirtData> data = dataHolder.getData(DirtData.class);
        if (data.isPresent()) {
            return Optional.of(new GlowDirtData().setValue(data.get().getValue()));
        } else {
            return Optional.absent();
        }
    }

    @Override
    public Optional<DirtData> fillData(DataHolder dataHolder, DirtData manipulator, DataPriority priority) {
        Optional<DirtData> data = dataHolder.getData(DirtData.class);
        if (data.isPresent() && priority == DataPriority.DATA_HOLDER) {
            manipulator.setValue(data.get().getValue());
        }

        return Optional.of(manipulator);
    }

    @Override
    public DataTransactionResult setData(DataHolder dataHolder, DirtData data, DataPriority priority) {
        return null;
    }

    @Override
    public boolean remove(DataHolder dataHolder) {
        return false;
    }

    @Override
    public Optional<DirtData> build(DataView container) throws InvalidDataException {
        return null;
    }

    @Override
    public DirtData create() {
        return new GlowDirtData();
    }

    @Override
    public Optional<DirtData> createFrom(DataHolder dataHolder) {
        return null;
    }
}
