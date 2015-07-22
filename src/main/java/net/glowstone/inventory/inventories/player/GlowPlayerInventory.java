package net.glowstone.inventory.inventories.player;

import com.google.common.base.Optional;
import net.glowstone.inventory.inventories.GlowEmptyInventory;
import net.glowstone.inventory.inventories.base.GlowBaseInventory;
import net.glowstone.item.GlowItemStackBuilder;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.InventoryProperty;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.transaction.InventoryOperationResult;
import org.spongepowered.api.text.translation.Translatable;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class GlowPlayerInventory extends GlowBaseInventory implements Inventory {
    private final GlowArmorInventory armorInventory;
    private final GlowPlayerRestInventory restInventory;
    private final GlowHotbar hotbar;

    public GlowPlayerInventory() {
        armorInventory = new GlowArmorInventory(this);
        restInventory = new GlowPlayerRestInventory(this);
        hotbar = new GlowHotbar(this);
    }

    @Nullable
    @Override
    public Inventory parent() {
        return null;
    }

    @Override
    public <T extends Inventory> Iterable<T> slots() {
        return (Iterable<T>) Arrays.asList(armorInventory, restInventory, hotbar);
    }

    @Override
    public <T extends Inventory> T first() {
        return (T) armorInventory;
    }

    @Override
    public <T extends Inventory> T next() {
        return (T) new GlowEmptyInventory(null);
    }

    @Override
    public Optional<ItemStack> poll() {
        Optional<ItemStack> item = hotbar.poll();
        if (!item.isPresent()) {
            item = restInventory.poll();
        }
        if (!item.isPresent()) {
            item = armorInventory.poll();
        }
        return item;
    }

    @Override
    public Optional<ItemStack> poll(int limit) {
        Optional<ItemStack> item = hotbar.poll(limit);
        if (item.isPresent()) {
            limit -= item.get().getQuantity();
            if (limit > 0) {
                ItemStack noSize = GlowItemStackBuilder.copy(item.get());
                noSize.setQuantity(-1);
                Optional<ItemStack> newItem = restInventory.query(noSize).poll(limit);
                if (newItem.isPresent()) {

                }
            }
        } else {
            item = restInventory.poll(limit);
        }

    }

    @Override
    public Optional<ItemStack> peek() {
        Optional<ItemStack> item = hotbar.peek();
        if (!item.isPresent()) {
            item = restInventory.peek();
        }
        if (!item.isPresent()) {
            item = armorInventory.peek();
        }
        return item;
    }

    @Override
    public Optional<ItemStack> peek(int limit) {
        return null;
    }

    @Override
    public boolean offer(ItemStack stack) {
        boolean result = armorInventory.offer(stack);
        if (result == false) {
            result = hotbar.offer(stack);
            if (result == false) {
                result = restInventory.offer(stack);
                return result;
            } else {
                if (stack.getQuantity() > 0) {
                    restInventory.offer(stack);
                }
                return true;
            }
        } else {
            if (stack.getQuantity() > 0) {
                hotbar.offer(stack);
                if (stack.getQuantity() > 0) {
                    restInventory.offer(stack);
                }
            }
            return true;
        }
    }

    @Override
    public InventoryOperationResult set(ItemStack stack) {
        InventoryOperationResult result = armorInventory.set(stack);
        Optional<Collection<ItemStack>> rejected = result.getRejectedItems();
    }

    @Override
    public void clear() {
        armorInventory.clear();
        hotbar.clear();
        restInventory.clear();
    }

    @Override
    public int size() {
        return armorInventory.size() + hotbar.size() + restInventory.size();
    }

    @Override
    public int totalItems() {
        return armorInventory.totalItems() + hotbar.totalItems() + restInventory.totalItems();
    }

    @Override
    public int capacity() {
        return armorInventory.capacity() + hotbar.capacity() + restInventory.capacity();
    }

    @Override
    public boolean isEmpty() {
        return armorInventory.isEmpty() && hotbar.isEmpty() && restInventory.isEmpty();
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
