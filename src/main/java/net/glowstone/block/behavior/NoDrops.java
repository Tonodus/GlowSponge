package net.glowstone.block.behavior;

import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.world.Location;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class NoDrops extends BaseBlockBehavior {
    public static final Collection<ItemStack> EMPTY = Collections.unmodifiableList(Arrays.asList(new ItemStack[0]));

    @Override
    public Collection<ItemStack> getDrops(Location block, ItemStack tool) {
        return EMPTY;
    }
}
