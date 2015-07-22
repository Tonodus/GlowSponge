package net.glowstone.id;

import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;

public interface ItemIdManager {
    ItemType getItemTypeById(short type, short durability);

    short getItemId(ItemStack stack);
}
