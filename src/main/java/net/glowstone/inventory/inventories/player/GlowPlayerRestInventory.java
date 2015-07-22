package net.glowstone.inventory.inventories.player;

import net.glowstone.inventory.inventories.base.GlowGridInventory;

public class GlowPlayerRestInventory extends GlowGridInventory {
    private static final int WIDTH = 9, HEIGHT = 5;

    public GlowPlayerRestInventory(GlowPlayerMainInventory parent) {
        super(parent, WIDTH, HEIGHT);
    }
}
