package net.glowstone.id;

import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;

public class OldIdItemManager implements ItemIdManager {
    @Override
    public ItemType getItemTypeById(short type, short durability) {
        return null;
    }

    @Override
    public short getItemId(ItemStack stack) {
        return 0;
    }
}
