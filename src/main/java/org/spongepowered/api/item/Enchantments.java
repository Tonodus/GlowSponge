/*
 * This file is part of SpongeAPI, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.api.item;

import net.glowstone.item.GlowEnchantment;

/**
 * An enumeration of known {@link Enchantment} types.
 */
public final class Enchantments {

    public static final Enchantment PROTECTION = new GlowEnchantment("Protection", "minecraft:protection", /*TODO: weight*/1, 1, 4);
    public static final Enchantment FIRE_PROTECTION = null;
    public static final Enchantment FEATHER_FALLING = null;
    public static final Enchantment BLAST_PROTECTION = null;
    public static final Enchantment PROJECTILE_PROTECTION = null;
    public static final Enchantment RESPIRATION = null;
    public static final Enchantment AQUA_AFFINITY = null;
    public static final Enchantment THORNS = null;
    public static final Enchantment DEPTH_STRIDER = null;
    public static final Enchantment SHARPNESS = null;
    public static final Enchantment SMITE = null;
    public static final Enchantment BANE_OF_ARTHROPODS = null;
    public static final Enchantment KNOCKBACK = null;
    public static final Enchantment FIRE_ASPECT = null;
    public static final Enchantment LOOTING = null;
    public static final Enchantment EFFICIENCY = null;
    public static final Enchantment SILK_TOUCH = null;
    public static final Enchantment UNBREAKING = null;
    public static final Enchantment FORTUNE = null;
    public static final Enchantment POWER = null;
    public static final Enchantment PUNCH = null;
    public static final Enchantment FLAME = null;
    public static final Enchantment INFINITY = null;
    public static final Enchantment LUCK_OF_THE_SEA = null;
    public static final Enchantment LURE = null;

    private Enchantments() {
    }

    /*
    private static enum Impl {
        PROTECTION_ENVIRONMENTAL(0, "Protection", 4, EnchantmentTarget.ARMOR, GROUP_PROTECT),
        PROTECTION_FIRE(1, "Fire Protection", 4, EnchantmentTarget.ARMOR, GROUP_PROTECT),
        PROTECTION_FALL(2, "Feather Falling", 4, EnchantmentTarget.ARMOR_FEET, GROUP_PROTECT),
        PROTECTION_EXPLOSIONS(3, "Blast Protection", 4, EnchantmentTarget.ARMOR),
        PROTECTION_PROJECTILE(4, "Projectile Protection", 4, EnchantmentTarget.ARMOR, GROUP_PROTECT),
        OXYGEN(5, "Respiration", 3, EnchantmentTarget.ARMOR_HEAD),
        WATER_WORKER(6, "Aqua Affinity", 1, EnchantmentTarget.ARMOR_HEAD),
        THORNS(7, "Thorns", 3, EnchantmentTarget.ARMOR_TORSO, new MatcherAdapter(EnchantmentTarget.ARMOR)),
        DEPTH_STRIDER(8, "Depth Strider", 3, EnchantmentTarget.ARMOR_FEET),
        DAMAGE_ALL(16, "Sharpness", 5, EnchantmentTarget.WEAPON, SWORD_OR_AXE, GROUP_ATTACK),
        DAMAGE_UNDEAD(17, "Smite", 5, EnchantmentTarget.WEAPON, SWORD_OR_AXE, GROUP_ATTACK),
        DAMAGE_ARTHROPODS(18, "Bane of Arthropods", 5, EnchantmentTarget.WEAPON, SWORD_OR_AXE, GROUP_ATTACK),
        KNOCKBACK(19, "Knockback", 2, EnchantmentTarget.WEAPON),
        FIRE_ASPECT(20, "Fire Aspect", 2, EnchantmentTarget.WEAPON),
        LOOT_BONUS_MOBS(21, "Looting", 3, EnchantmentTarget.WEAPON),
        DIG_SPEED(32, "Efficiency", 5, EnchantmentTarget.TOOL, DIGGING_TOOLS),
        SILK_TOUCH(33, "Silk Touch", 1, EnchantmentTarget.TOOL, DIGGING_TOOLS, GROUP_DIG),
        DURABILITY(34, "Unbreaking", 3, EnchantmentTarget.TOOL, ALL_THINGS),
        LOOT_BONUS_BLOCKS(35, "Fortune", 3, EnchantmentTarget.TOOL, BASE_TOOLS, GROUP_DIG),
        ARROW_DAMAGE(48, "Power", 5, EnchantmentTarget.BOW),
        ARROW_KNOCKBACK(49, "Punch", 2, EnchantmentTarget.BOW),
        ARROW_FIRE(50, "Flame", 1, EnchantmentTarget.BOW),
        ARROW_INFINITE(51, "Infinity", 1, EnchantmentTarget.BOW),
        LUCK(61, "Luck of the Sea", 3, EnchantmentTarget.FISHING_ROD),
        LURE(62, "Lure", 3, EnchantmentTarget.FISHING_ROD);

        private final int id;
        private final String name;
        private final int maxLevel;
        private final EnchantmentTarget target;
        private final MaterialMatcher matcher;
        private final int group;

        Impl(int id, String name, int max, EnchantmentTarget target) {
            this(id, name, max, target, new MatcherAdapter(target), GROUP_NONE);
        }

        Impl(int id, String name, int max, EnchantmentTarget target, int group) {
            this(id, name, max, target, new MatcherAdapter(target), group);
        }

        Impl(int id, String name, int max, EnchantmentTarget target, MaterialMatcher matcher) {
            this(id, name, max, target, matcher, GROUP_NONE);
        }

        Impl(int id, String name, int max, EnchantmentTarget target, MaterialMatcher matcher, int group) {
            this.id = id;
            this.name = name;
            this.maxLevel = max;
            this.target = target;
            this.matcher = matcher;
            this.group = group;
        }
    }
     */
}
