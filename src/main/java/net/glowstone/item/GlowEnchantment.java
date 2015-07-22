package net.glowstone.item;

import org.spongepowered.api.item.Enchantment;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.translation.Translation;

/**
 * Definitions of enchantment types.
 */
public final class GlowEnchantment implements Enchantment {
    private final String id;
    private final String name; //ex: minecraft:efficiency
    private final int weight;
    private final int minimumLevel;
    private final int maximumLevel;

    public GlowEnchantment(String id, String name, int weight, int minimumLevel, int maximumLevel) {
        this.id = id;
        this.name = name;
        this.weight = weight;
        this.minimumLevel = minimumLevel;
        this.maximumLevel = maximumLevel;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getWeight() {
        return weight;
    }

    @Override
    public int getMinimumLevel() {
        return minimumLevel;
    }

    @Override
    public int getMaximumLevel() {
        return maximumLevel;
    }

    @Override
    public int getMinimumEnchantabilityForLevel(int level) {
        return 0;
    }

    @Override
    public int getMaximumEnchantabilityForLevel(int level) {
        return 0;
    }

    @Override
    public boolean canBeAppliedToStack(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canBeAppliedByTable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isCompatibleWith(Enchantment ench) {
        return false;
    }

    @Override
    public Translation getTranslation() {
        return null;
    }

    /*
    private static final MaterialMatcher SWORD_OR_AXE = new MaterialMatcher() {
        @Override
        public boolean matches(Material item) {
            return EnchantmentTarget.WEAPON.includes(item)
                    || item.equals(Material.WOOD_AXE)
                    || item.equals(Material.STONE_AXE)
                    || item.equals(Material.IRON_AXE)
                    || item.equals(Material.DIAMOND_AXE)
                    || item.equals(Material.GOLD_AXE);
        }
    };

    private static final MaterialMatcher BASE_TOOLS = new MaterialMatcher() {
        @Override
        public boolean matches(Material item) {
            return item.equals(Material.WOOD_SPADE)
                    || item.equals(Material.STONE_SPADE)
                    || item.equals(Material.IRON_SPADE)
                    || item.equals(Material.DIAMOND_SPADE)
                    || item.equals(Material.GOLD_SPADE)
                    || item.equals(Material.WOOD_PICKAXE)
                    || item.equals(Material.STONE_PICKAXE)
                    || item.equals(Material.IRON_PICKAXE)
                    || item.equals(Material.DIAMOND_PICKAXE)
                    || item.equals(Material.GOLD_PICKAXE)
                    || item.equals(Material.WOOD_AXE)
                    || item.equals(Material.STONE_AXE)
                    || item.equals(Material.IRON_AXE)
                    || item.equals(Material.DIAMOND_AXE)
                    || item.equals(Material.GOLD_AXE);
        }
    };

    private static final MaterialMatcher DIGGING_TOOLS = new MaterialMatcher() {
        @Override
        public boolean matches(Material material) {
            return BASE_TOOLS.matches(material)
                    || material == Material.SHEARS;
        }
    };

    private static final MaterialMatcher ALL_THINGS = new MaterialMatcher() {
        @Override
        public boolean matches(Material material) {
            return EnchantmentTarget.TOOL.includes(material)
                    || EnchantmentTarget.WEAPON.includes(material)
                    || EnchantmentTarget.ARMOR.includes(material)
                    || material == Material.FISHING_ROD
                    || material == Material.BOW
                    || material == Material.CARROT_STICK;
        }
    };

    private static final int GROUP_NONE = 0;
    private static final int GROUP_PROTECT = 1;
    private static final int GROUP_ATTACK = 2;
    private static final int GROUP_DIG = 3;
*/
}
