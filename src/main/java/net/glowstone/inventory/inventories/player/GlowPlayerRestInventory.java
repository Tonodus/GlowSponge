package net.glowstone.inventory.inventories.player;

import net.glowstone.inventory.inventories.base.GlowGridInventory;
import org.spongepowered.api.item.inventory.Inventory;

public class GlowPlayerRestInventory extends GlowGridInventory {

    public GlowPlayerRestInventory(Inventory parent) {
        super(parent, 9, 5);
    }
}
