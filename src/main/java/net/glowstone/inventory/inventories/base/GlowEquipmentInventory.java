package net.glowstone.inventory.inventories.base;

import com.google.common.base.Optional;
import org.spongepowered.api.entity.ArmorEquipable;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.Slot;
import org.spongepowered.api.item.inventory.equipment.EquipmentInventory;
import org.spongepowered.api.item.inventory.equipment.EquipmentType;
import org.spongepowered.api.item.inventory.property.EquipmentSlotType;
import org.spongepowered.api.item.inventory.transaction.InventoryOperationResult;
import org.spongepowered.api.item.inventory.type.InventoryColumn;

public abstract class GlowEquipmentInventory<C extends GlowEquipmentSlot> extends GlowBaseGridInventory<C> implements EquipmentInventory, InventoryColumn {

    public GlowEquipmentInventory(GlowInventory parent, int width, int height) {
        super(parent, width, height);
    }

    @Override
    public Optional<ItemStack> poll(EquipmentSlotType equipmentType) {
        return null;
    }

    @Override
    public Optional<ItemStack> poll(EquipmentSlotType equipmentType, int limit) {
        return null;
    }

    @Override
    public Optional<ItemStack> poll(EquipmentType equipmentType) {
        return null;
    }

    @Override
    public Optional<ItemStack> poll(EquipmentType equipmentType, int limit) {
        return null;
    }

    @Override
    public Optional<ItemStack> peek(EquipmentSlotType equipmentType) {
        return null;
    }

    @Override
    public Optional<ItemStack> peek(EquipmentSlotType equipmentType, int limit) {
        return null;
    }

    @Override
    public Optional<ItemStack> peek(EquipmentType equipmentType) {
        return null;
    }

    @Override
    public Optional<ItemStack> peek(EquipmentType equipmentType, int limit) {
        return null;
    }

    @Override
    public InventoryOperationResult set(EquipmentSlotType equipmentType, ItemStack stack) {
        return null;
    }

    @Override
    public InventoryOperationResult set(EquipmentType equipmentType, ItemStack stack) {
        return null;
    }

    @Override
    public Optional<Slot> getSlot(EquipmentSlotType equipmentType) {
        return null;
    }

    @Override
    public Optional<Slot> getSlot(EquipmentType equipmentType) {
        return null;
    }

    @Override
    public Optional<ArmorEquipable> getCarrier() {
        return null;
    }
}
