package net.glowstone.id;

import org.spongepowered.api.data.type.DyeColor;
import org.spongepowered.api.data.type.NotePitch;
import org.spongepowered.api.data.type.SkullType;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.world.biome.BiomeType;

public class IdManagers {
    public static final IdManager<Byte, BiomeType> BIOMES = new BiomeIdManager();
    public static final IdManager<Byte, DyeColor> DYE_COLORS = new DyeColorIdManager();
    public static final IdManager<Byte, NotePitch> NOTE_PITCHES = NotePitchIdManager();
    public static final IdManager<Byte, SkullType> SKULL_TYPES = new SkullTypeManager();
    public static final IdManager<String, EntityType> ENTITY_TYPES = new EntityTypeIdManager();

    private IdManagers() {
    }
}
