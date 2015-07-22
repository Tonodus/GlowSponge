package net.glowstone.block.behavior;

import com.google.common.collect.ImmutableMap;
import org.spongepowered.api.data.type.*;

import java.util.Map;

/**
 * Enum name mappings.
 */
public final class EnumNames {

    private EnumNames() {
    }

    public static Map<DirtType, String> dirt() {
        return ImmutableMap.of(
                DirtTypes.DIRT, "dirt",
                DirtTypes.COARSE_DIRT, "coarse_dirt",
                DirtTypes.PODZOL, "podzol");
    }

    public static Map<SandType, String> sand() {
        return ImmutableMap.of(
                SandTypes.NORMAL, "sand",
                SandTypes.RED, "red_sand");
    }

    public static Map<SandstoneType, String> sandstone() {
        return ImmutableMap.of(
                SandstoneTypes.DEFAULT, "sandstone",
                SandstoneTypes.CHISELED, "chiseled_sandstone",
                SandstoneTypes.SMOOTH, "smooth_sandstone");
    }

    public static Map<StoneType, String> stone() {
        return ImmutableMap.<StoneType, String>builder()
                .put(StoneTypes.STONE, "stone")
                .put(StoneTypes.GRANITE, "granite")
                .put(StoneTypes.SMOOTH_GRANITE, "smooth_granite")
                .put(StoneTypes.DIORITE, "diorite")
                .put(StoneTypes.SMOOTH_DIORITE, "smooth_diorite")
                .put(StoneTypes.ANDESITE, "andesite")
                .put(StoneTypes.SMOOTH_ANDESITE, "smooth_andesite")
                .build();
    }

    public static Map<TreeType, String> tree() {
        return ImmutableMap.<TreeType, String>builder()
                .put(TreeTypes.OAK, "oak")
                .put(TreeTypes.SPRUCE, "spruce")
                .put(TreeTypes.BIRCH, "birch")
                .put(TreeTypes.JUNGLE, "jungle")
                .put(TreeTypes.ACACIA, "acacia")
                .put(TreeTypes.DARK_OAK, "dark_oak")
                .build();
    }

}
