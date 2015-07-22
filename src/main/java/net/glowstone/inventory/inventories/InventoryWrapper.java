package net.glowstone.inventory.inventories;

import com.google.common.base.Optional;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.InventoryProperty;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.transaction.InventoryOperationResult;
import org.spongepowered.api.text.translation.Translatable;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Iterator;

public class InventoryWrapper implements Inventory {
    private final Inventory inventory;

    public InventoryWrapper(Inventory inventory) {
        this.inventory = inventory;
    }

    @Nullable
    @Override
    public Inventory parent() {
        return inventory.parent();
    }

    @Override
    public <T extends Inventory> Iterable<T> slots() {
        return inventory.slots();
    }

    @Override
    public <T extends Inventory> T first() {
        return inventory.first();
    }

    @Override
    public <T extends Inventory> T next() {
        return inventory.next();
    }

    @Override
    public Optional<ItemStack> poll() {
        return inventory.poll();
    }

    @Override
    public Optional<ItemStack> poll(int limit) {
        return inventory.poll();
    }

    @Override
    public Optional<ItemStack> peek() {
        return inventory.peek();
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
