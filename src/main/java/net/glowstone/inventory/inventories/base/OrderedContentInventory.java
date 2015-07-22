package net.glowstone.inventory.inventories.base;

import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;

public interface OrderedContentInventory extends Inventory {
    void setRawContents(ItemStack... contents);

    ItemStack[] getRawContents();

    int getRawContentSize();
}
