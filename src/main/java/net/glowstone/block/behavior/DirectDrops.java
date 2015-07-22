package net.glowstone.block.behavior;

import net.glowstone.item.GlowItemStackBuilder;
import org.spongepowered.api.data.DataManipulator;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.world.Location;

import java.util.Arrays;
import java.util.Collection;

public class DirectDrops extends BaseBlockBehavior {
    private final ItemType type;
    private final int quantity;
    private final DataManipulator<?>[] data;

    public DirectDrops(ItemType type, int quantity, DataManipulator<?>... data) {
        this.type = type;
        this.quantity = quantity;
        this.data = data;
    }

    public DirectDrops(ItemType type, int amount) {
        this(type, amount, null);
    }

    public DirectDrops(ItemType type) {
        this(type, 1, null);
    }

    @Override
    public Collection<ItemStack> getDrops(Location block, ItemStack tool) {
        return Arrays.asList((ItemStack) GlowItemStackBuilder.build(type, quantity, data));
    }

}
