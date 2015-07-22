package net.glowstone.entity;

import com.flowpowered.networking.Message;
import net.glowstone.data.manipulator.entity.GlowHealthData;
import net.glowstone.inventory.EquipmentMonitor;
import net.glowstone.net.message.play.entity.EntityEquipmentMessage;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.entity.DamageableData;
import org.spongepowered.api.data.manipulator.entity.HealthData;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.potion.PotionEffect;
import org.spongepowered.api.potion.PotionEffectType;
import org.spongepowered.api.world.Location;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GlowLivingEntity extends GlowEntity implements Living {

    /**
     * Potion effects on the entity.
     */
    private final Map<PotionEffectType, PotionEffect> potionEffects = new HashMap<>();

    /**
     * The magnitude of the last damage the entity took.
     */
    private double lastDamage;

    /**
     * How long the entity has until it runs out of air.
     */
    // private int airTicks = 300;

    /**
     * The maximum amount of air the entity can hold.
     */
    //  private int maximumAir = 300;

    /**
     * The number of ticks remaining in the invincibility period.
     */
    private int noDamageTicks = 0;

    /**
     * The default length of the invincibility period.
     */
    private int maxNoDamageTicks = 10;

    /**
     * A custom overhead name to be shown for non-Players.
     */
    //   private String customName;

    /**
     * Whether the custom name is shown.
     */
//    private boolean customNameVisible;

    /**
     * Whether the entity should be removed if it is too distant from players.
     */
    private boolean removeDistance;

    /**
     * Whether the (non-Player) entity can pick up armor and tools.
     */
    private boolean pickupItems;

    /**
     * Monitor for the equipment of this entity.
     */
    private EquipmentMonitor equipmentMonitor = new EquipmentMonitor(this);

    /**
     * Creates a mob within the specified world.
     *
     * @param location The location.
     */
    public GlowLivingEntity(Location location) {
        super(location);
        offer(new GlowHealthData().setHealth(20).setMaxHealth(20));
    }

    @Override
    protected void serialize(DataView data) {
        data.set(DataQuery.of("HealthData"), getHealthData());
        data.set(DataQuery.of("DamageableData"), getMortalData());
    }

    ////////////////////////////////////////////////////////////////////////////
    // Internals

    @Override
    public void pulse() {
        super.pulse();
/*
        // invulnerability
        if (noDamageTicks > 0) {
            --noDamageTicks;
        }

        Material mat = getEyeLocation().getBlock().getType();
        // breathing
        if (mat == Material.WATER || mat == Material.STATIONARY_WATER) {
            if (canTakeDamage(EntityDamageEvent.DamageCause.DROWNING)) {
                --airTicks;
                if (airTicks <= -20) {
                    airTicks = 0;
                    damage(1, EntityDamageEvent.DamageCause.DROWNING);
                }
            }
        } else {
            airTicks = maximumAir;
        }

        if (isTouchingMaterial(Material.CACTUS) && canTakeDamage(EntityDamageEvent.DamageCause.CONTACT)) {
            damage(1, EntityDamageEvent.DamageCause.CONTACT);
        }
        if (location.getY() < -64) { // no canTakeDamage call - pierces through game modes
            damage(4, EntityDamageEvent.DamageCause.VOID);
        }

        // potion effects
        List<PotionEffect> effects = new ArrayList<>(potionEffects.values());
        for (PotionEffect effect : effects) {
            // pulse effect
            PotionEffectType type = effect.getType();
            GlowPotionEffect glowType = GlowPotionEffect.getEffect(type);
            if (glowType != null) {
                glowType.pulse(this, effect);
            }

            if (effect.getDuration() > 0) {
                // reduce duration and re-add
                addPotionEffect(new PotionEffect(type, effect.getDuration() - 1, effect.getAmplifier(), effect.isAmbient()), true);
            } else {
                // remove
                removePotionEffect(type);
            }
        }*/
    }

    @Override
    public void reset() {
        super.reset();
        equipmentMonitor.resetChanges();
    }

    @Override
    public List<Message> createUpdateMessage() {
        List<Message> messages = super.createUpdateMessage();

        for (EquipmentMonitor.Entry change : equipmentMonitor.getChanges()) {
            messages.add(new EntityEquipmentMessage(id, change.slot, change.item));
        }

        return messages;
    }


    ////////////////////////////////////////////////////////////////////////////
    // Health

  /*  public void damage(double amount) {
        // invincibility timer
        if (noDamageTicks > 0 || health <= 0) {
            return;
        } else {
            noDamageTicks = maxNoDamageTicks;
        }

        // fire resistance
        if (cause != null && hasPotionEffect(PotionEffectType.FIRE_RESISTANCE)) {
            switch (cause) {
                case PROJECTILE:
                    if (source == null || !(source instanceof Fireball)) {
                        break;
                    }
                case FIRE:
                case FIRE_TICK:
                case LAVA:
                    return;
            }
        }

        // fire event
        // todo: use damage modifier system
        EntityDamageEvent event;
        if (source == null) {
            event = new EntityDamageEvent(this, cause, amount);
        } else {
            event = new EntityDamageByEntityEvent(source, this, cause, amount);
        }
        EventFactory.callEvent(event);
        if (event.isCancelled()) {
            return;
        }

        // apply damage
        amount = event.getFinalDamage();
        lastDamage = amount;
        setHealth(health - amount);
        playEffect(EntityEffect.HURT);

        // play sounds, handle death
        if (health <= 0.0) {
            Sound deathSound = getDeathSound();
            if (deathSound != null) {
                world.playSound(location, deathSound, 1.0f, 1.0f);
            }
            // todo: drop items
        } else {
            Sound hurtSound = getHurtSound();
            if (hurtSound != null) {
                world.playSound(location, hurtSound, 1.0f, 1.0f);
            }
        }
    }*/

    @Override
    public HealthData getHealthData() {
        return getData(HealthData.class).get();
    }

    @Override
    public DamageableData getMortalData() {
        return getData(DamageableData.class).get();
    }

}
