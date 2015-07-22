package net.glowstone.data.manipulator.item;

import net.glowstone.data.manipulator.GlowDataManipulator;
import org.spongepowered.api.data.manipulator.item.InventoryItemData;
import org.spongepowered.api.item.inventory.Carrier;
import org.spongepowered.api.item.inventory.type.CarriedInventory;

public class GlowInventoryItemData extends GlowDataManipulator<InventoryItemData> implements InventoryItemData {
    private final CarriedInventory<? extends Carrier> inventory;

    public GlowInventoryItemData(CarriedInventory<? extends Carrier> inventory) {
        super(InventoryItemData.class);
        this.inventory = inventory;
    }

    @Override
    public CarriedInventory<? extends Carrier> getInventory() {
        return inventory;
    }

    @Override
    public InventoryItemData copy() {
        return new GlowInventoryItemData(inventory);
    }

    @Override
    public int compareTo(InventoryItemData o) {
        return 0;
    }
}
