package net.glowstone;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.spongepowered.api.GameDictionary;
import org.spongepowered.api.item.ItemType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GlowGameDictionary implements GameDictionary {
    private final Map<String, Set<ItemType>> data;

    public GlowGameDictionary() {
        data = new HashMap<>();
    }

    @Override
    public void register(String key, ItemType type) {
        if (!data.containsKey(key)) {
            data.put(key, new HashSet<ItemType>());
        }

        data.get(key).add(type);
    }

    @Override
    public Set<ItemType> get(String key) {
        return ImmutableSet.copyOf(data.get(key));
    }

    @Override
    public Map<String, Set<ItemType>> getAllItems() {
        //TODO: make immutable?
        return ImmutableMap.copyOf(data);
    }
}
