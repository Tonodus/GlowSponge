package net.glowstone.inventory.inventories.base;

import com.google.common.base.Optional;
import com.google.common.collect.Iterators;
import net.glowstone.inventory.inventories.GlowEmptyInventory;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.InventoryProperty;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.transaction.InventoryOperationResult;
import org.spongepowered.api.text.translation.Translatable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public abstract class GlowBaseInventory<C extends Inventory> extends GlowInventory implements Inventory {

    protected GlowBaseInventory(GlowInventory parent) {
        super(parent);
    }

    protected abstract C[] getChildren();

    @Override
    public <T extends Inventory> Iterable<T> slots() {
        return null;
    }

    @Override
    public <T extends Inventory> T first() {
        return (T) (children.length >= 1 ? children[0] : this);
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
        for (Inventory child : children) {
            child.clear();
        }
    }

    @Override
    public int size() {
        int size = 0;
        for (int i = 0; i < children.length; i++) {
            size += children[i].size();
        }
        return size;
    }

    @Override
    public int totalItems() {
        int totalItems = 0;
        for (int i = 0; i < children.length; i++) {
            totalItems += children[i].totalItems();
        }
        return totalItems;
    }

    @Override
    public int capacity() {
        int capacity = 0;
        for (int i = 0; i < children.length; i++) {
            capacity += children[i].capacity();
        }
        return capacity;
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < children.length; i++) {
            if (!children[i].isEmpty()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean contains(ItemStack stack) {
        for (int i = 0; i < children.length; i++) {
            if (children[i].contains(stack)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean contains(ItemType type) {
        for (int i = 0; i < children.length; i++) {
            if (children[i].contains(type)) {
                return true;
            }
        }

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
        List<Inventory> found = new ArrayList<>();

        for (int i = 0; i < children.length; i++) {
            for (Class<?> clazz : types) {
                if (clazz.isAssignableFrom(children[i].getClass())) {
                    found.add(children[i]);
                }
            }
        }

        if (found.size() == 1) {
            return (T) found.get(0);
        } else if (found.size() == 0) {
            return (T) new GlowEmptyInventory(this);
        } else {
            return null; //new InventoryQueryResult(found);
        }
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
        return Iterators.forArray(children);
    }

    @Override
    public Translatable getName() {
        return null;
    }
}
