package net.glowstone.inventory.inventories.base;

import net.glowstone.inventory.ItemMatcher;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.slot.FilteringSlot;

public class GlowFilteringSlot extends GlowSlot implements FilteringSlot {
    private final ItemMatcher matcher;

    public GlowFilteringSlot(GlowInventory parent, ItemMatcher matcher) {
        super(parent);
        this.matcher = matcher;
    }

    @Override
    public boolean isValidItem(ItemStack stack) {
        return matcher.matches(stack);
    }

    @Override
    public boolean isValidItem(ItemType type) {
        return matcher.matches(type);
    }
}
