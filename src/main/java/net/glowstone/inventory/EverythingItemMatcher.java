package net.glowstone.inventory;

import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;

public final class EverythingItemMatcher implements ItemMatcher {

    @Override
    public boolean matches(ItemStack itemType) {
        return true;
    }

    @Override
    public boolean matches(ItemType type) {
        return true;
    }
}
