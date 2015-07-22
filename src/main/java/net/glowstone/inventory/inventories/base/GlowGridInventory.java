package net.glowstone.inventory.inventories.base;

import org.spongepowered.api.item.inventory.type.GridInventory;

public class GlowGridInventory extends GlowBaseGridInventory<GlowSlot> implements GridInventory, OrderedContentInventory {

    public GlowGridInventory(GlowInventory parent, int width, int height) {
        super(parent, width, height);
    }

    @Override
    protected GlowSlot[] getChildren() {
        return new GlowSlot[getWidth() * getHeight()];
    }
}
