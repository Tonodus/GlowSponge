package net.glowstone.id;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public abstract class BasicIdManager<I, T> implements IdManager<I, T> {
    private final BiMap<I, T> map;

    public BasicIdManager() {
        map = HashBiMap.create();
        fill(map);
    }

    protected abstract void fill(BiMap<I, T> map);

    @Override
    public T getById(I id) {
        return map.get(id);
    }

    @Override
    public I getId(T type) {
        return map.inverse().get(type);
    }
}
