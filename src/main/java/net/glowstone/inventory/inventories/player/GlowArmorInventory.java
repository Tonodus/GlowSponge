package net.glowstone.inventory.inventories.player;

import net.glowstone.inventory.inventories.base.GlowEquipmentInventory;
import net.glowstone.inventory.inventories.base.GlowEquipmentSlot;
import net.glowstone.inventory.inventories.base.GlowInventory;
import org.spongepowered.api.item.inventory.equipment.EquipmentTypes;

public class GlowArmorInventory extends GlowEquipmentInventory<GlowEquipmentSlot> {
    private final GlowEquipmentSlot[] armor;

    public GlowArmorInventory(GlowInventory parent) {
        super(parent, 1, 4);

        armor = new GlowEquipmentSlot[]{
                new GlowEquipmentSlot(this, EquipmentTypes.HEADWEAR),
                new GlowEquipmentSlot(this, EquipmentTypes.CHESTPLATE),
                new GlowEquipmentSlot(this, EquipmentTypes.LEGGINGS),
                new GlowEquipmentSlot(this, EquipmentTypes.BOOTS)
        };
    }

    @Override
    protected GlowEquipmentSlot[] getChildren() {
        return armor;
    }
}
