package net.glowstone.block.behavior;

import net.glowstone.item.GlowItemStackBuilder;
import org.spongepowered.api.data.manipulator.block.DirtData;
import org.spongepowered.api.data.type.DirtTypes;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.world.Location;

import java.util.Arrays;
import java.util.Collection;

public class DirtDrops extends BaseBlockBehavior {

    @Override
    public Collection<ItemStack> getDrops(Location block, ItemStack tool) {
        DirtData data = block.getData(DirtData.class).get();
        if (data.getValue() != DirtTypes.COARSE_DIRT) {
            data.setValue(DirtTypes.DIRT);
        }
        return Arrays.asList((ItemStack) GlowItemStackBuilder.build(ItemTypes.DIRT, 1, data));
    }
}
