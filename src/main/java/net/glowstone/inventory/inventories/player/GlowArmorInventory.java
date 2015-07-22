package net.glowstone.inventory.inventories.player;

import net.glowstone.inventory.inventories.base.GlowBaseInventory;
import net.glowstone.inventory.inventories.base.GlowFilteringSlot;
import org.spongepowered.api.item.inventory.Inventory;

public class GlowArmorInventory extends GlowBaseInventory {
    private final GlowFilteringSlot[] armor;

    public GlowArmorInventory(Inventory parent) {
        super(parent);
        armor = new GlowFilteringSlot[4];
    }

    @Override
    protected Inventory[] getChildren() {
        return armor;
    }
}
