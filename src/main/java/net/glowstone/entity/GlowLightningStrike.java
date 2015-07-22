package net.glowstone.entity;

import com.flowpowered.networking.Message;
import net.glowstone.net.message.play.entity.SpawnLightningStrikeMessage;
import net.glowstone.util.Position;
import org.bukkit.Location;
import org.spongepowered.api.data.manipulator.entity.ExpirableData;
import org.spongepowered.api.effect.sound.SoundTypes;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.weather.Lightning;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * A GlowLightning strike is an entity produced during thunderstorms.
 */
public class GlowLightningStrike extends GlowWeatherEntity implements Lightning {

    /**
     * Whether the lightning strike is just for effect.
     */
    private boolean effect;

    /**
     * How long this lightning strike has to remain in the world.
     */
    private final int ticksToLive;

    private final Random random;

    public GlowLightningStrike(Location location, boolean effect, Random random) {
        super(location);
        this.effect = effect;
        this.ticksToLive = 30;
        this.random = random;
    }

    @Override
    public boolean isEffect() {
        return effect;
    }

    @Override
    public void setEffect(boolean effect) {
        this.effect = effect;
    }

    @Override
    public ExpirableData getExpiringData() {
        return getData(ExpirableData.class).get();
    }

    @Override
    public void pulse() {
        super.pulse();
        if (getTicksLived() >= ticksToLive) {
            remove();
        }
        if (getTicksLived() == 1) {
            world.playSound(location, SoundTypes.AMBIENCE_THUNDER, 10000, 0.8F + random.nextFloat() * 0.2F);
            world.playSound(location, SoundTypes.EXPLODE, 2, 0.5F + random.nextFloat() * 0.2F);
        }
    }

    @Override
    public List<Message> createSpawnMessage() {
        int x = Position.getIntX(location);
        int y = Position.getIntY(location);
        int z = Position.getIntZ(location);
        return Arrays.<Message>asList(new SpawnLightningStrikeMessage(id, x, y, z));
    }

    @Override
    public List<Message> createUpdateMessage() {
        return Arrays.asList();
    }

    @Override
    public EntityType getType() {
        return EntityTypes.LIGHTNING;
    }
}
