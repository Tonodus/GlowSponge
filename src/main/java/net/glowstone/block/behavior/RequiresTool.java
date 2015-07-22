package net.glowstone.block.behavior;

import net.glowstone.inventory.ItemMatcher;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.world.Location;

import java.util.Collection;

public class RequiresTool extends BaseBlockBehavior {

    private final ItemMatcher matcher;

    public RequiresTool(ItemMatcher matcher) {
        this.matcher = matcher;
    }

    @Override
    public Collection<? extends ItemStack> getDrops(Location block, ItemStack tool) {
        if (matcher != null && (tool == null || !matcher.matches(tool.getItem()))) {
            return NoDrops.EMPTY;
        }
        return super.getDrops(block, tool);
    }
}
