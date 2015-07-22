package net.glowstone.inventory.inventories.player;

import com.google.common.base.Optional;
import net.glowstone.inventory.inventories.base.GlowInventoryRow;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.Slot;
import org.spongepowered.api.item.inventory.entity.Hotbar;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.item.inventory.property.SlotPos;
import org.spongepowered.api.item.inventory.transaction.InventoryOperationResult;

public class GlowHotbar extends GlowInventoryRow implements Hotbar {

    public GlowHotbar(GlowPlayerInventory parent) {

    }

    @Override
    public int getSelectedSlotIndex() {
        return 0;
    }

    @Override
    public void setSelectedSlotIndex(int index) {

    }

    @Override
    public Optional<ItemStack> poll(SlotPos pos) {
        return null;
    }

    @Override
    public Optional<ItemStack> poll(SlotPos pos, int limit) {
        return null;
    }

    @Override
    public Optional<ItemStack> peek(SlotPos pos) {
        return null;
    }

    @Override
    public Optional<ItemStack> peek(SlotPos pos, int limit) {
        return null;
    }

    @Override
    public InventoryOperationResult set(SlotPos pos, ItemStack stack) {
        return null;
    }

    @Override
    public Optional<Slot> getSlot(SlotPos pos) {
        return null;
    }

    @Override
    public Optional<ItemStack> poll(SlotIndex index) {
        return null;
    }

    @Override
    public Optional<ItemStack> poll(SlotIndex index, int limit) {
        return null;
    }

    @Override
    public Optional<ItemStack> peek(SlotIndex index) {
        return null;
    }

    @Override
    public Optional<ItemStack> peek(SlotIndex index, int limit) {
        return null;
    }

    @Override
    public InventoryOperationResult set(SlotIndex index, ItemStack stack) {
        return null;
    }

    @Override
    public Optional<Slot> getSlot(SlotIndex index) {
        return null;
    }
}
