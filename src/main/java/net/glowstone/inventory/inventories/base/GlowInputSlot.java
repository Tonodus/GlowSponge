package net.glowstone.inventory.inventories.base;

import net.glowstone.inventory.ItemMatcher;
import org.spongepowered.api.item.inventory.slot.InputSlot;

public class GlowInputSlot extends GlowFilteringSlot implements InputSlot {

    public GlowInputSlot(GlowInventory parent, ItemMatcher matcher) {
        super(parent, matcher);
    }
}
