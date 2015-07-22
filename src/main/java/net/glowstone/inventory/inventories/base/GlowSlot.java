package net.glowstone.inventory.inventories.base;

import com.google.common.base.Optional;
import com.google.common.collect.Iterators;
import net.glowstone.item.GlowItemStackBuilder;
import net.glowstone.item.ItemHelper;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.InventoryProperty;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.Slot;
import org.spongepowered.api.item.inventory.transaction.InventoryOperationResult;
import org.spongepowered.api.text.translation.Translatable;

import java.util.Collection;
import java.util.Iterator;

public class GlowSlot extends GlowInventory implements Slot {
    private static final Optional<ItemStack> AIR = Optional.of(null);

    private ItemStack itemStack;

    public GlowSlot(GlowInventory parent) {
        super(parent);
    }

    @Override
    public int getStackSize() {
        return itemStack.getQuantity();
    }

    @Override
    public <T extends Inventory> Iterable<T> slots() {
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return Iterators.emptyIterator();
            }
        };
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
        if (itemStack == null) {
            return AIR;
        }
        ItemStack itemStack = this.itemStack;
        this.itemStack = null;
        notifyChange();
        return Optional.of(itemStack);
    }

    @Override
    public Optional<ItemStack> poll(int limit) {
        if (itemStack == null) {
            return AIR;
        }
        if (itemStack.getQuantity() > limit) {
            ItemStack returnStack = GlowItemStackBuilder.copy(itemStack);
            returnStack.setQuantity(limit);
            itemStack.setQuantity(itemStack.getQuantity() - limit);
            notifyChange();
            return Optional.of(returnStack);
        } else {
            return poll();
        }
    }

    @Override
    public Optional<ItemStack> peek() {
        if (itemStack == null) {
            return AIR;
        }
        return Optional.of(itemStack);
    }

    @Override
    public Optional<ItemStack> peek(int limit) {
        if (itemStack == null) {
            return AIR;
        }
        ItemStack returnStack = GlowItemStackBuilder.copy(itemStack);
        returnStack.setQuantity(Math.min(limit, returnStack.getQuantity()));
        return Optional.of(returnStack);
    }

    @Override
    public boolean offer(ItemStack stack) {
        if (!canAccept(stack)) {
            return false;
        }
        if (itemStack == null) {
            itemStack = GlowItemStackBuilder.copy(stack);
            notifyChange();
            stack.setQuantity(0);
            return true;
        }
        if (ItemHelper.matches(stack, itemStack)) {
            int maxAdd = itemStack.getMaxStackQuantity() - itemStack.getQuantity();
            int added = Math.min(itemStack.getQuantity(), maxAdd);
            itemStack.setQuantity(itemStack.getQuantity() + added);
            notifyChange();
            stack.setQuantity(stack.getQuantity() - added);
            return true;
        }

        return false;
    }

    private boolean canAccept(ItemStack stack) {
        return true;
    }

    @Override
    public InventoryOperationResult set(ItemStack stack) {
        if (!canAccept(stack)) {
            return null; //false
        }
        if (ItemHelper.matches(stack, itemStack)) {
            int both = itemStack.getQuantity() + stack.getQuantity();
            if (both > itemStack.getMaxStackQuantity()) {

            }
        }

        notifyChange();

        return null;
    }

    @Override
    public void clear() {
        itemStack = null;
        notifyChange();
    }

    @Override
    public int size() {
        return itemStack == null ? 0 : 1;
    }

    @Override
    public int totalItems() {
        return size();
    }

    @Override
    public int capacity() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return itemStack == null;
    }

    @Override
    public boolean contains(ItemStack stack) {
        return itemStack == null ? false : ItemHelper.matches(stack, itemStack);
    }

    @Override
    public boolean contains(ItemType type) {
        return itemStack == null ? false : itemStack.getItem().equals(type);
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    public void setMaxStackSize(int size) {
        //TODO
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
        return Iterators.emptyIterator();
    }

    @Override
    public Translatable getName() {
        return null;
    }
}
