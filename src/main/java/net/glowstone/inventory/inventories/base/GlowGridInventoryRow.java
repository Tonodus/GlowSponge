package net.glowstone.inventory.inventories.base;

import com.google.common.base.Optional;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.InventoryProperty;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.Slot;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.item.inventory.property.SlotPos;
import org.spongepowered.api.item.inventory.transaction.InventoryOperationResult;
import org.spongepowered.api.item.inventory.type.GridInventory;
import org.spongepowered.api.item.inventory.type.InventoryRow;
import org.spongepowered.api.text.translation.Translatable;

import java.util.Collection;
import java.util.Iterator;

public class GlowGridInventoryRow extends GlowInventory implements InventoryRow {
    private final int y;
    private final GridInventory inventory;

    public GlowGridInventoryRow(int y, GlowGridInventory inventory) {
        super(inventory);
        this.y = y;
        this.inventory = inventory;
    }

    @Override
    public Optional<ItemStack> poll(SlotPos pos) {
        return inventory.poll(pos.getX(), y);
    }

    @Override
    public Optional<ItemStack> poll(SlotPos pos, int limit) {
        return inventory.poll(pos.getX(), y, limit);
    }

    @Override
    public Optional<ItemStack> peek(SlotPos pos) {
        return inventory.peek(pos.getX(), y);
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

    @Override
    public <T extends Inventory> Iterable<T> slots() {
        return null;
    }

    @Override
    public <T extends Inventory> T first() {
        return null;
    }

    @Override
    public <T extends Inventory> T next() {
        return null;
    }

    @Override
    public Optional<ItemStack> poll() {
        return null;
    }

    @Override
    public Optional<ItemStack> poll(int limit) {
        return null;
    }

    @Override
    public Optional<ItemStack> peek() {
        return null;
    }

    @Override
    public Optional<ItemStack> peek(int limit) {
        return null;
    }

    @Override
    public boolean offer(ItemStack stack) {
        return false;
    }

    @Override
    public InventoryOperationResult set(ItemStack stack) {
        return null;
    }

    @Override
    public void clear() {

    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public int totalItems() {
        return 0;
    }

    @Override
    public int capacity() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(ItemStack stack) {
        return false;
    }

    @Override
    public boolean contains(ItemType type) {
        return false;
    }

    @Override
    public int getMaxStackSize() {
        return 0;
    }

    @Override
    public void setMaxStackSize(int size) {

    }

    @Override
    public <T extends InventoryProperty<?, ?>> Collection<T> getProperties(Inventory child, Class<T> property) {
        return null;
    }

    @Override
    public <T extends InventoryProperty<?, ?>> Collection<T> getProperties(Class<T> property) {
        return null;
    }

    @Override
    public <T extends InventoryProperty<?, ?>> Optional<T> getProperty(Inventory child, Class<T> property, Object key) {
        return null;
    }

    @Override
    public <T extends InventoryProperty<?, ?>> Optional<T> getProperty(Class<T> property, Object key) {
        return null;
    }

    @Override
    public <T extends Inventory> T query(Class<?>... types) {
        return null;
    }

    @Override
    public <T extends Inventory> T query(ItemType... types) {
        return null;
    }

    @Override
    public <T extends Inventory> T query(ItemStack... types) {
        return null;
    }

    @Override
    public <T extends Inventory> T query(InventoryProperty<?, ?>... props) {
        return null;
    }

    @Override
    public <T extends Inventory> T query(Translatable... names) {
        return null;
    }

    @Override
    public <T extends Inventory> T query(String... names) {
        return null;
    }

    @Override
    public <T extends Inventory> T query(Object... args) {
        return null;
    }

    @Override
    public Iterator<Inventory> iterator() {
        return null;
    }

    @Override
    public Translatable getName() {
        return null;
    }
}
