package net.glowstone.block.behavior;

import net.glowstone.item.GlowItemStackBuilder;
import org.spongepowered.api.data.DataManipulator;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.world.Location;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class RandomDrops extends BaseBlockBehavior {
    private final Random random = new Random();

    private final ItemType dropType;
    private final DataManipulator<?>[] data;
    private final int minDrops;
    private final int maxDrops;

    public RandomDrops(ItemType dropType, int minDrops, int maxDrops, DataManipulator<?>... data) {
        this.dropType = dropType;
        this.minDrops = minDrops;
        this.maxDrops = maxDrops;
        this.data = data;
    }

    public RandomDrops(ItemType dropType, int minDrops, int maxDrops) {
        this(dropType, minDrops, maxDrops, null);
    }

    public RandomDrops(ItemType dropType, int maxDrops) {
        this(dropType, 1, maxDrops, null);
    }

    @Override
    public Collection<ItemStack> getDrops(Location block, ItemStack tool) {
        return Collections.unmodifiableList(Arrays.asList(
                (ItemStack) GlowItemStackBuilder.build(dropType, random.nextInt(maxDrops - minDrops + 1) + minDrops, data)
        ));
    }
}
