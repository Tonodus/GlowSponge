package net.glowstone.inventory;

import net.glowstone.util.nbt.CompoundTag;
import org.spongepowered.api.item.inventory.Inventory;

public interface InventorySerializer<T extends Inventory> {
    T deserialize(CompoundTag tag);

    void serialize(T inventory, CompoundTag tag);
}
