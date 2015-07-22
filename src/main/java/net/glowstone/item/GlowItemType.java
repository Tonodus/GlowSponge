package net.glowstone.item;

import com.google.common.base.Optional;
import net.glowstone.item.behavior.ItemBehavior;
import org.spongepowered.api.data.Property;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.text.translation.Translation;

public class GlowItemType implements ItemType {
    private final String id, name;
    private final int maxStackQuantity;
    private final ItemBehavior behavior;

    public GlowItemType(String id, String name, ItemBehavior behavior) {
        this.id = id;
        this.name = name;
        this.behavior = behavior;
        this.maxStackQuantity = 64;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getMaxStackQuantity() {
        return maxStackQuantity;
    }

    @Override
    public <T extends Property<?, ?>> Optional<T> getDefaultProperty(Class<T> propertyClass) {
        return Optional.absent();
    }

    @Override
    public Translation getTranslation() {
        return null;
    }
}
