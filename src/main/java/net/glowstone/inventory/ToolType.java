package net.glowstone.inventory;

import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;

/**
 * A {@link ItemMatcher} implementation for basic tool types.
 */
public enum ToolType implements ItemMatcher {
    // Pickaxes
    DIAMOND_PICKAXE(ItemTypes.DIAMOND_PICKAXE, null),
    IRON_PICKAXE(ItemTypes.IRON_PICKAXE, DIAMOND_PICKAXE),
    STONE_PICKAXE(ItemTypes.STONE_PICKAXE, IRON_PICKAXE),
    GOLD_PICKAXE(ItemTypes.GOLDEN_PICKAXE, STONE_PICKAXE),
    PICKAXE(ItemTypes.WOODEN_PICKAXE, GOLD_PICKAXE),

    // Spades
    DIAMOND_SPADE(ItemTypes.DIAMOND_SHOVEL, null),
    IRON_SPADE(ItemTypes.IRON_SHOVEL, DIAMOND_SPADE),
    STONE_SPADE(ItemTypes.STONE_SHOVEL, IRON_PICKAXE),
    GOLD_SPADE(ItemTypes.GOLDEN_SHOVEL, STONE_SPADE),
    SPADE(ItemTypes.WOODEN_SHOVEL, GOLD_SPADE),

    // Swords
    DIAMOND_SWORD(ItemTypes.DIAMOND_SWORD, null),
    IRON_SWORD(ItemTypes.IRON_SWORD, DIAMOND_SWORD),
    STONE_SWORD(ItemTypes.STONE_SWORD, IRON_SWORD),
    GOLD_SWORD(ItemTypes.GOLDEN_SWORD, STONE_SWORD),
    SWORD(ItemTypes.WOODEN_SWORD, GOLD_SWORD);

    private final ItemType itemType;
    private final ToolType better;

    ToolType(ItemType itemType, ToolType better) {
        this.itemType = itemType;
        this.better = better;
    }

    /**
     * Checks the given {@link ItemType} is equal or better than this ToolType.
     * @param material The material to check
     * @return true if the material is equal or better, false otherwise
     */
    @Override
    public boolean matches(ItemType material) {
        return itemType == material || (better != null && better.matches(material));
    }

    @Override
    public boolean matches(ItemStack itemType) {
        return matches(itemType.getItem());
    }
}
