package net.glowstone.item;

import net.glowstone.data.GlowDataHolder;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataManipulator;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.MemoryDataContainer;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;

public class GlowItemStack extends GlowDataHolder implements ItemStack {
    private final ItemType type;
    private int quantity;

    public GlowItemStack(ItemType type) {
        this.type = type;
    }

    public GlowItemStack(ItemType type, int quantity) {
        this.type = type;
        this.quantity = quantity;
    }

    public GlowItemStack(ItemStack stack) {
        this.type = stack.getItem();
        this.quantity = stack.getQuantity();
    }

    @Override
    public ItemType getItem() {
        return type;
    }

    @Override
    public int getQuantity() {
        return quantity;
    }

    @Override
    public void setQuantity(int quantity) throws IllegalArgumentException {
        this.quantity = quantity;
    }

    @Override
    public int getMaxStackQuantity() {
        return type.getMaxStackQuantity();
    }

    @Override
    public DataContainer toContainer() {
        DataContainer container = new MemoryDataContainer();
        for (DataManipulator manipulator : getManipulators()) {
            container.set(DataQuery.of(manipulator.getClass().getCanonicalName()), manipulator.toContainer());
        }
        DataContainer data = new MemoryDataContainer();
        data.set(DataQuery.of("ItemType"), this.getItem().getId());
        data.set(DataQuery.of("Quantity"), this.getQuantity());
        data.set(DataQuery.of("Data"), container);
        return data;
    }
}
