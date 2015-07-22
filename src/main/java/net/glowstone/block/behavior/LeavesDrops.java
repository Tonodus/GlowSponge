package net.glowstone.block.behavior;

import net.glowstone.item.GlowItemStack;
import net.glowstone.item.GlowItemStackBuilder;
import org.spongepowered.api.data.manipulator.block.TreeData;
import org.spongepowered.api.data.type.TreeTypes;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.world.Location;

import java.util.*;

public class LeavesDrops extends BaseBlockBehavior {
    private final Random random = new Random();

    @Override
    public Collection<ItemStack> getDrops(Location block, ItemStack tool) {
        TreeData species = block.getData(TreeData.class).get();

        if (tool != null && tool.getItem().equals(ItemTypes.SHEARS)) {
            return Collections.unmodifiableList(Arrays.asList(
                    (ItemStack) GlowItemStackBuilder.build(block.getBlockType().getHeldItem().get(), 1, species)
            ));
        }

        List<ItemStack> drops = new ArrayList<>();
        if (random.nextFloat() < (species.getValue() == TreeTypes.JUNGLE ? .025f : .05f)) { // jungle leaves drop with 2.5% chance, others drop with 5%
            drops.add(GlowItemStackBuilder.build(ItemTypes.SAPLING, 1, species));
        }
        if (species.getValue() == TreeTypes.OAK && random.nextFloat() < .005) { // oak leaves have a .5% chance to drop an apple
            drops.add(new GlowItemStack(ItemTypes.APPLE));
        }
        return Collections.unmodifiableList(drops);
    }
}
