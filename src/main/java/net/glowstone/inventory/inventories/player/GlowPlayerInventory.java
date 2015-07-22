package net.glowstone.inventory.inventories.player;

import com.google.common.base.Optional;
import net.glowstone.entity.player.GlowPlayer;
import net.glowstone.inventory.inventories.base.GlowBaseInventory;
import net.glowstone.inventory.inventories.base.GlowInventory;
import org.spongepowered.api.entity.living.Human;
import org.spongepowered.api.item.inventory.entity.Hotbar;
import org.spongepowered.api.item.inventory.entity.HumanInventory;

public class GlowPlayerInventory extends GlowBaseInventory<GlowInventory> implements HumanInventory {
    private final GlowArmorInventory armorInventory;
    private final GlowPlayerMainInventory mainInventory;
    private final GlowPlayer player;

    public GlowPlayerInventory(GlowPlayer player) {
        super(null);
        this.player = player;
        armorInventory = new GlowArmorInventory(this);
        mainInventory = new GlowPlayerMainInventory(this);
    }


    @Override
    protected GlowInventory[] getChildren() {
        return new GlowInventory[]{
                armorInventory,
                mainInventory
        };
    }

    @Override
    public Hotbar getHotbar() {
        return mainInventory.getHotbar();
    }

    @Override
    public Optional<Human> getCarrier() {
        return Optional.fromNullable((Human) player);
    }


    /*@Override
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
    */
}
