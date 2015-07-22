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
package org.spongepowered.api.block;

import net.glowstone.block.BlockBehavior;
import net.glowstone.block.GlowBlockType;
import net.glowstone.block.behavior.*;
import net.glowstone.block.stateresolver.*;
import org.spongepowered.api.item.ItemTypes;

import java.util.Arrays;

/**
 * An enumeration of all possible {@link BlockType}s in vanilla minecraft.
 */
public final class BlockTypes {

    // These values will not be null at runtime

    public static final GlowBlockType AIR = of("air", 0);
    public static final GlowBlockType STONE = of("stone", 1, new StoneStateResolver(), new StoneDrops());
    public static final GlowBlockType GRASS = of("grass", 2, new DirectDrops(ItemTypes.DIRT));
    public static final GlowBlockType DIRT = of("dirt", 3, new DirtStateResolver(), new DirtDrops());
    public static final GlowBlockType COBBLESTONE = of("cobblestone", 4);
    public static final GlowBlockType PLANKS = of("planks", 5, new PlanksResolver());
    public static final GlowBlockType SAPLING = of("sapling", 6, new SaplingResolver());
    public static final GlowBlockType BEDROCK = null;
    public static final GlowBlockType FLOWING_WATER = null;
    public static final GlowBlockType WATER = null;
    public static final GlowBlockType FLOWING_LAVA = null;
    public static final GlowBlockType LAVA = null;
    public static final GlowBlockType SAND = null;
    public static final GlowBlockType GRAVEL = null;
    public static final GlowBlockType GOLD_ORE = null;
    public static final GlowBlockType IRON_ORE = null;
    public static final GlowBlockType COAL_ORE = null;
    public static final GlowBlockType LOG = of("log", 17, new LogsResolver());
    public static final GlowBlockType LOG2 = of("log2", 162, new LogsResolver());
    public static final GlowBlockType LEAVES = of("leaves", 18, new LeavesResolver(), new LeavesDrops());
    public static final GlowBlockType LEAVES2 = of("leaves2", 161, new LeavesResolver(), new LeavesDrops());
    public static final GlowBlockType SPONGE = null;
    public static final GlowBlockType GLASS = null;
    public static final GlowBlockType LAPIS_ORE = null;
    public static final GlowBlockType LAPIS_BLOCK = null;
    public static final GlowBlockType DISPENSER = null;
    public static final GlowBlockType SANDSTONE = null;
    public static final GlowBlockType NOTEBLOCK = null;
    public static final GlowBlockType BED = null;
    public static final GlowBlockType GOLDEN_RAIL = null;
    public static final GlowBlockType DETECTOR_RAIL = null;
    public static final GlowBlockType STICKY_PISTON = null;
    public static final GlowBlockType WEB = null;
    public static final GlowBlockType TALLGRASS = null;
    public static final GlowBlockType DEADBUSH = null;
    public static final GlowBlockType PISTON = null;
    public static final GlowBlockType PISTON_HEAD = null;
    public static final GlowBlockType WOOL = null;
    public static final GlowBlockType PISTON_EXTENSION = null;
    public static final GlowBlockType YELLOW_FLOWER = null;
    public static final GlowBlockType RED_FLOWER = null;
    public static final GlowBlockType BROWN_MUSHROOM = null;
    public static final GlowBlockType RED_MUSHROOM = null;
    public static final GlowBlockType GOLD_BLOCK = null;
    public static final GlowBlockType IRON_BLOCK = null;
    public static final GlowBlockType DOUBLE_STONE_SLAB = null;
    public static final GlowBlockType STONE_SLAB = null;
    public static final GlowBlockType BRICK_BLOCK = null;
    public static final GlowBlockType TNT = null;
    public static final GlowBlockType BOOKSHELF = null;
    public static final GlowBlockType MOSSY_COBBLESTONE = null;
    public static final GlowBlockType OBSIDIAN = null;
    public static final GlowBlockType TORCH = null;
    public static final GlowBlockType FIRE = null;
    public static final GlowBlockType MOB_SPAWNER = null;
    public static final GlowBlockType OAK_STAIRS = null;
    public static final GlowBlockType CHEST = null;
    public static final GlowBlockType REDSTONE_WIRE = null;
    public static final GlowBlockType DIAMOND_ORE = null;
    public static final GlowBlockType DIAMOND_BLOCK = null;
    public static final GlowBlockType CRAFTING_TABLE = null;
    public static final GlowBlockType WHEAT = null;
    public static final GlowBlockType FARMLAND = null;
    public static final GlowBlockType FURNACE = null;
    public static final GlowBlockType LIT_FURNACE = null;
    public static final GlowBlockType STANDING_SIGN = null;
    public static final GlowBlockType WOODEN_DOOR = null;
    public static final GlowBlockType SPRUCE_DOOR = null;
    public static final GlowBlockType BIRCH_DOOR = null;
    public static final GlowBlockType JUNGLE_DOOR = null;
    public static final GlowBlockType ACACIA_DOOR = null;
    public static final GlowBlockType DARK_OAK_DOOR = null;
    public static final GlowBlockType LADDER = null;
    public static final GlowBlockType RAIL = null;
    public static final GlowBlockType STONE_STAIRS = null;
    public static final GlowBlockType WALL_SIGN = null;
    public static final GlowBlockType LEVER = null;
    public static final GlowBlockType STONE_PRESSURE_PLATE = null;
    public static final GlowBlockType IRON_DOOR = null;
    public static final GlowBlockType WOODEN_PRESSURE_PLATE = null;
    public static final GlowBlockType REDSTONE_ORE = null;
    public static final GlowBlockType LIT_REDSTONE_ORE = null;
    public static final GlowBlockType UNLIT_REDSTONE_TORCH = null;
    public static final GlowBlockType REDSTONE_TORCH = null;
    public static final GlowBlockType STONE_BUTTON = null;
    public static final GlowBlockType SNOW_LAYER = null;
    public static final GlowBlockType ICE = null;
    public static final GlowBlockType SNOW = null;
    public static final GlowBlockType CACTUS = null;
    public static final GlowBlockType CLAY = null;
    public static final GlowBlockType REEDS = null;
    public static final GlowBlockType JUKEBOX = null;
    public static final GlowBlockType FENCE = null;
    public static final GlowBlockType SPRUCE_FENCE = null;
    public static final GlowBlockType BIRCH_FENCE = null;
    public static final GlowBlockType JUNGLE_FENCE = null;
    public static final GlowBlockType DARK_OAK_FENCE = null;
    public static final GlowBlockType ACACIA_FENCE = null;
    public static final GlowBlockType PUMPKIN = null;
    public static final GlowBlockType NETHERRACK = null;
    public static final GlowBlockType SOUL_SAND = null;
    public static final GlowBlockType GLOWSTONE = null;
    public static final GlowBlockType PORTAL = null;
    public static final GlowBlockType LIT_PUMPKIN = null;
    public static final GlowBlockType CAKE = null;
    public static final GlowBlockType UNPOWERED_REPEATER = null;
    public static final GlowBlockType POWERED_REPEATER = null;
    public static final GlowBlockType TRAPDOOR = null;
    public static final GlowBlockType MONSTER_EGG = null;
    public static final GlowBlockType STONEBRICK = null;
    public static final GlowBlockType BROWN_MUSHROOM_BLOCK = null;
    public static final GlowBlockType RED_MUSHROOM_BLOCK = null;
    public static final GlowBlockType IRON_BARS = null;
    public static final GlowBlockType GLASS_PANE = null;
    public static final GlowBlockType MELON_BLOCK = null;
    public static final GlowBlockType PUMPKIN_STEM = null;
    public static final GlowBlockType MELON_STEM = null;
    public static final GlowBlockType VINE = null;
    public static final GlowBlockType FENCE_GATE = null;
    public static final GlowBlockType SPRUCE_FENCE_GATE = null;
    public static final GlowBlockType BIRCH_FENCE_GATE = null;
    public static final GlowBlockType JUNGLE_FENCE_GATE = null;
    public static final GlowBlockType DARK_OAK_FENCE_GATE = null;
    public static final GlowBlockType ACACIA_FENCE_GATE = null;
    public static final GlowBlockType BRICK_STAIRS = null;
    public static final GlowBlockType STONE_BRICK_STAIRS = null;
    public static final GlowBlockType MYCELIUM = null;
    public static final GlowBlockType WATERLILY = null;
    public static final GlowBlockType NETHER_BRICK = null;
    public static final GlowBlockType NETHER_BRICK_FENCE = null;
    public static final GlowBlockType NETHER_BRICK_STAIRS = null;
    public static final GlowBlockType NETHER_WART = null;
    public static final GlowBlockType ENCHANTING_TABLE = null;
    public static final GlowBlockType BREWING_STAND = null;
    public static final GlowBlockType CAULDRON = null;
    public static final GlowBlockType END_PORTAL = null;
    public static final GlowBlockType END_PORTAL_FRAME = null;
    public static final GlowBlockType END_STONE = null;
    public static final GlowBlockType DRAGON_EGG = null;
    public static final GlowBlockType REDSTONE_LAMP = null;
    public static final GlowBlockType LIT_REDSTONE_LAMP = null;
    public static final GlowBlockType DOUBLE_WOODEN_SLAB = null;
    public static final GlowBlockType WOODEN_SLAB = null;
    public static final GlowBlockType COCOA = null;
    public static final GlowBlockType SANDSTONE_STAIRS = null;
    public static final GlowBlockType EMERALD_ORE = null;
    public static final GlowBlockType ENDER_CHEST = null;
    public static final GlowBlockType TRIPWIRE_HOOK = null;
    public static final GlowBlockType TRIPWIRE = null;
    public static final GlowBlockType EMERALD_BLOCK = null;
    public static final GlowBlockType SPRUCE_STAIRS = null;
    public static final GlowBlockType BIRCH_STAIRS = null;
    public static final GlowBlockType JUNGLE_STAIRS = null;
    public static final GlowBlockType COMMAND_BLOCK = null;
    public static final GlowBlockType BEACON = null;
    public static final GlowBlockType COBBLESTONE_WALL = null;
    public static final GlowBlockType FLOWER_POT = null;
    public static final GlowBlockType CARROTS = null;
    public static final GlowBlockType POTATOES = null;
    public static final GlowBlockType WOODEN_BUTTON = null;
    public static final GlowBlockType SKULL = null;
    public static final GlowBlockType ANVIL = null;
    public static final GlowBlockType TRAPPED_CHEST = null;
    public static final GlowBlockType LIGHT_WEIGHTED_PRESSURE_PLATE = null;
    public static final GlowBlockType HEAVY_WEIGHTED_PRESSURE_PLATE = null;
    public static final GlowBlockType UNPOWERED_COMPARATOR = null;
    public static final GlowBlockType POWERED_COMPARATOR = null;
    public static final GlowBlockType DAYLIGHT_DETECTOR = null;
    public static final GlowBlockType DAYLIGHT_DETECTOR_INVERTED = null;
    public static final GlowBlockType REDSTONE_BLOCK = null;
    public static final GlowBlockType QUARTZ_ORE = null;
    public static final GlowBlockType HOPPER = null;
    public static final GlowBlockType QUARTZ_BLOCK = null;
    public static final GlowBlockType QUARTZ_STAIRS = null;
    public static final GlowBlockType ACTIVATOR_RAIL = null;
    public static final GlowBlockType DROPPER = null;
    public static final GlowBlockType STAINED_HARDENED_CLAY = null;
    public static final GlowBlockType BARRIER = null;
    public static final GlowBlockType IRON_TRAPDOOR = null;
    public static final GlowBlockType HAY_BLOCK = null;
    public static final GlowBlockType CARPET = null;
    public static final GlowBlockType HARDENED_CLAY = null;
    public static final GlowBlockType COAL_BLOCK = null;
    public static final GlowBlockType PACKED_ICE = null;
    public static final GlowBlockType ACACIA_STAIRS = null;
    public static final GlowBlockType DARK_OAK_STAIRS = null;
    public static final GlowBlockType SLIME = null;
    public static final GlowBlockType DOUBLE_PLANT = null;
    public static final GlowBlockType STAINED_GLASS = null;
    public static final GlowBlockType STAINED_GLASS_PANE = null;
    public static final GlowBlockType PRISMARINE = null;
    public static final GlowBlockType SEA_LANTERN = null;
    public static final GlowBlockType STANDING_BANNER = null;
    public static final GlowBlockType WALL_BANNER = null;
    public static final GlowBlockType RED_SANDSTONE = null;
    public static final GlowBlockType RED_SANDSTONE_STAIRS = null;
    public static final GlowBlockType DOUBLE_STONE_SLAB2 = null;
    public static final GlowBlockType STONE_SLAB2 = null;

    private BlockTypes() {
    }

    private static GlowBlockType of(String name, int oldId) {
        return of(name, oldId, null, null);
    }

    private static GlowBlockType of(String name, int oldId, BlockBehavior... behaviors) {
        return of(name, oldId, null, behaviors);
    }

    private static GlowBlockType of(String name, int oldId, StateResolver resolver, BlockBehavior... behaviors) {
        if (behaviors != null && behaviors.length > 1) {
            return new GlowBlockType(name, "minecraft:" + name, oldId, new ListBlockBehavior(Arrays.asList(behaviors)), resolver);
        } else if (behaviors != null && behaviors.length == 1) {
            return new GlowBlockType(name, "minecraft:" + name, oldId, behaviors[0], resolver);
        } else {
            return new GlowBlockType(name, "minecraft:" + name, oldId, DefaultBlockBehavior.INSTANCE, resolver);
        }
    }

    public static void init() {
        //do nothing, just static init
    }
}