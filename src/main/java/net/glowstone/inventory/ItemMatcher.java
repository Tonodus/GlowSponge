package net.glowstone.inventory;

import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;

/**
 * An interface for checking predicates on Materials.
 */
public interface ItemMatcher {
    /**
     * Returns true if the given {@link ItemType} matches the conditions of this MaterialMatcher.
     * @param itemType the {@link ItemType} to check
     * @return true if it matches, false otherwise
     */
    boolean matches(ItemStack itemType);

    boolean matches(ItemType type);
}
