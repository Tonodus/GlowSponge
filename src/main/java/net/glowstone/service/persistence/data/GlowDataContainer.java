package net.glowstone.service.persistence.data;

import com.google.common.base.Optional;
import org.spongepowered.api.service.persistence.DataSerializable;
import org.spongepowered.api.service.persistence.data.DataContainer;
import org.spongepowered.api.service.persistence.data.DataQuery;
import org.spongepowered.api.service.persistence.data.DataView;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class GlowDataContainer implements DataContainer {
    @Override
    public DataContainer getContainer() {
        return null;
    }

    @Override
    public DataQuery getCurrentPath() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Optional<DataView> getParent() {
        return null;
    }

    @Override
    public Set<DataQuery> getKeys(boolean deep) {
        return null;
    }

    @Override
    public Map<DataQuery, Object> getValues(boolean deep) {
        return null;
    }

    @Override
    public boolean contains(DataQuery path) {
        return false;
    }

    @Override
    public Optional<Object> get(DataQuery path) {
        return null;
    }

    @Override
    public void set(DataQuery path, Object value) {

    }

    @Override
    public void remove(DataQuery path) {

    }

    @Override
    public DataView createView(DataQuery path) {
        return null;
    }

    @Override
    public DataView createView(DataQuery path, Map<?, ?> map) {
        return null;
    }

    @Override
    public Optional<DataView> getView(DataQuery path) {
        return null;
    }

    @Override
    public Optional<Boolean> getBoolean(DataQuery path) {
        return null;
    }

    @Override
    public Optional<Integer> getInt(DataQuery path) {
        return null;
    }

    @Override
    public Optional<Long> getLong(DataQuery path) {
        return null;
    }

    @Override
    public Optional<Double> getDouble(DataQuery path) {
        return null;
    }

    @Override
    public Optional<String> getString(DataQuery path) {
        return null;
    }

    @Override
    public Optional<List<?>> getList(DataQuery path) {
        return null;
    }

    @Override
    public Optional<List<String>> getStringList(DataQuery path) {
        return null;
    }

    @Override
    public Optional<List<Character>> getCharacterList(DataQuery path) {
        return null;
    }

    @Override
    public Optional<List<Boolean>> getBooleanList(DataQuery path) {
        return null;
    }

    @Override
    public Optional<List<Byte>> getByteList(DataQuery path) {
        return null;
    }

    @Override
    public Optional<List<Short>> getShortList(DataQuery path) {
        return null;
    }

    @Override
    public Optional<List<Integer>> getIntegerList(DataQuery path) {
        return null;
    }

    @Override
    public Optional<List<Long>> getLongList(DataQuery path) {
        return null;
    }

    @Override
    public Optional<List<Float>> getFloatList(DataQuery path) {
        return null;
    }

    @Override
    public Optional<List<Double>> getDoubleList(DataQuery path) {
        return null;
    }

    @Override
    public Optional<List<Map<?, ?>>> getMapList(DataQuery path) {
        return null;
    }

    @Override
    public <T extends DataSerializable> Optional<T> getSerializable(DataQuery path, Class<T> clazz) {
        return null;
    }
}
