package net.glowstone.entity;

import com.flowpowered.networking.Message;
import com.google.common.base.Optional;
import net.glowstone.net.message.play.entity.SpawnObjectMessage;
import net.glowstone.util.Position;
import org.spongepowered.api.data.manipulator.entity.FuseData;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.explosive.PrimedTNT;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.world.Location;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class GlowTNTPrimed extends GlowExplosive implements PrimedTNT {

    private int fuseTicks;
    private Entity source;

    public GlowTNTPrimed(Location location, Entity source) {
        super(location);
        this.fuseTicks = 80;
        this.source = source;
    }

    public void setIgnitedByExplosion(boolean ignitedByExplosion) {
        if (ignitedByExplosion) {
            // if ignited by an explosion, the fuseTicks should be a random number between 10 and 30 ticks
            fuseTicks = new Random().nextInt(21) + 10;
        }
    }

    @Override
    public void pulse() {
        super.pulse();

        fuseTicks--;
        if (fuseTicks <= 0) {
            detonate();
        } else {
            //world.showParticle(location.clone().add(0, 0.5D, 0), Particle.SMOKE, 0, 0, 0, 0, 0);
        }
    }

   /* private void explode() {
        ExplosionPrimeEvent event = EventFactory.callEvent(new ExplosionPrimeEvent(this));

        if (!event.isCancelled()) {
            Location location = getLocation();
            double x = location.getX() + 0.49, y = location.getY() + 0.49, z = location.getZ() + 0.49;
            world.createExplosion(this, x, y, z, event.getRadius(), event.getFire(), true);
        }

        remove();
    }*/

    @Override
    public List<Message> createSpawnMessage() {
        int x = Position.getIntX(location),
                y = Position.getIntY(location),
                z = Position.getIntZ(location),
                pitch = Position.getIntPitch(this.pitch),
                yaw = Position.getIntYaw(this.yaw);

        LinkedList<Message> result = new LinkedList<>();
        result.add(new SpawnObjectMessage(id, 50, x, y, z, pitch, yaw));
        return result;
    }


    @Override
    public Optional<Living> getDetonator() {
        if (source instanceof Living) {
            return Optional.fromNullable((Living) source);
        } else {
            return Optional.absent();
        }
    }

    @Override
    public FuseData getFuseData() {
        return getData(FuseData.class).get();
    }

    @Override
    public EntityType getType() {
        return EntityTypes.PRIMED_TNT;
    }
}
