package net.glowstone.inventory.inventories.base;

import net.glowstone.inventory.ItemMatcher;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.equipment.EquipmentType;
import org.spongepowered.api.item.inventory.slot.EquipmentSlot;

public class GlowEquipmentSlot extends GlowFilteringSlot implements EquipmentSlot {
    private final EquipmentType[] types;

    public GlowEquipmentSlot(GlowInventory parent, EquipmentType... types) {
        super(parent, new ItemMatcher() {
            @Override
            public boolean matches(ItemStack itemType) {
                return matches(itemType.getItem());
            }

            @Override
            public boolean matches(ItemType type) {
                return false; //TODO
            }
        });
        this.types = types;
    }

    @Override
    public boolean isValidItem(EquipmentType item) {
        for (EquipmentType type : types) {
            if (type.equals(item)) {
                return true;
            }
        }

        return false;
    }
}
