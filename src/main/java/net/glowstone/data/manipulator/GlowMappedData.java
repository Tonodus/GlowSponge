package net.glowstone.data.manipulator;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.spongepowered.api.data.manipulator.MappedData;

import java.util.Map;
import java.util.Set;

public abstract class GlowMappedData<K, V, T extends GlowMappedData<K, V, T>> extends GlowDataManipulator<T> implements MappedData<K, V, T> {
    protected Map<K, V> map;

    public GlowMappedData(Class<T> manipulatorClass) {
        super(manipulatorClass);
    }

    @Override
    public Set<K> getKeys() {
        return ImmutableSet.copyOf(map.keySet());
    }

    @Override
    public Map<K, V> asMap() {
        return ImmutableMap.copyOf(map);
    }

    @Override
    public Optional<V> get(K key) {
        return Optional.fromNullable(map.get(key));
    }

    @Override
    public T setUnsafe(K key, V value) {
        map.put(key, value);
        return (T) this;
    }

    @Override
    public T setUnsafe(Map<K, V> mapped) {
        map.clear();
        for (Map.Entry<K, V> entry : mapped.entrySet()) {
            setUnsafe(entry.getKey(), entry.getValue());
        }
        return (T) this;
    }

    @Override
    public T remove(K key) {
        map.remove(key);
        return (T) this;
    }
}
