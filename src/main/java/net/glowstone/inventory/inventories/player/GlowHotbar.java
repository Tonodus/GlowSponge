package net.glowstone.inventory.inventories.player;

import net.glowstone.inventory.inventories.base.GlowGridInventory;
import org.spongepowered.api.item.inventory.entity.Hotbar;

public class GlowHotbar extends GlowGridInventory implements Hotbar {
    private int selectedSlot;

    public GlowHotbar(GlowPlayerInventory parent) {
        super(parent, 9, 1);
    }

    @Override
    public int getSelectedSlotIndex() {
        return selectedSlot;
    }

    @Override
    public void setSelectedSlotIndex(int index) {
        this.selectedSlot = index;
    }
}
