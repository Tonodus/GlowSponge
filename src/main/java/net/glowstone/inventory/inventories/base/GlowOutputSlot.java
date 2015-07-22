package net.glowstone.inventory.inventories.base;

import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.slot.OutputSlot;

public class GlowOutputSlot extends GlowSlot implements OutputSlot {

    public GlowOutputSlot(Inventory parent) {
        super(parent);
    }
}
