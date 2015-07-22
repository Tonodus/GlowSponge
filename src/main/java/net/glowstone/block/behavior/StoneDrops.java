package net.glowstone.block.behavior;

import net.glowstone.item.GlowItemStack;
import net.glowstone.item.GlowItemStackBuilder;
import org.spongepowered.api.data.manipulator.block.StoneData;
import org.spongepowered.api.data.type.StoneTypes;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.world.Location;

import java.util.Arrays;
import java.util.Collection;

public class StoneDrops extends BaseBlockBehavior {
    @Override
    public Collection<? extends ItemStack> getDrops(Location block, ItemStack tool) {
        StoneData data = block.getData(StoneData.class).get();
        if (data.getValue() == StoneTypes.STONE) {
            return Arrays.asList(new GlowItemStack(ItemTypes.COBBLESTONE));
        } else {
            return Arrays.asList(new GlowItemStackBuilder().itemType(ItemTypes.STONE).itemData(data).build());
        }
    }
}
