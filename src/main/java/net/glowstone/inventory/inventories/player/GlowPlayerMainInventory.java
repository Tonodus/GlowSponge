package net.glowstone.inventory.inventories.player;

import net.glowstone.inventory.inventories.base.GlowBaseInventory;
import net.glowstone.inventory.inventories.base.GlowInventory;

public class GlowPlayerMainInventory extends GlowBaseInventory<GlowInventory> {
    private final GlowHotbar hotbar;
    private final GlowPlayerRestInventory restInventory;

    protected GlowPlayerMainInventory(GlowPlayerInventory parent) {
        super(parent);
        restInventory = new GlowPlayerRestInventory(this);
        hotbar = new GlowHotbar(this);
    }

    @Override
    protected GlowInventory[] getChildren() {
        return new GlowInventory[]{
                hotbar,
                restInventory
        };
    }

    public GlowHotbar getHotbar() {
        return hotbar;
    }
}
