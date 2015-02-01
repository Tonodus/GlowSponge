package net.glowstone.senity;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3f;
import com.google.common.base.Optional;
import org.spongepowered.api.GameProfile;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.sound.SoundType;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntitySnapshot;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.entity.player.gamemode.GameMode;
import org.spongepowered.api.entity.projectile.Projectile;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.net.PlayerConnection;
import org.spongepowered.api.potion.PotionEffect;
import org.spongepowered.api.potion.PotionEffectType;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.service.permission.SubjectCollection;
import org.spongepowered.api.service.permission.SubjectData;
import org.spongepowered.api.service.permission.context.Context;
import org.spongepowered.api.service.persistence.DataSource;
import org.spongepowered.api.service.persistence.data.DataContainer;
import org.spongepowered.api.text.chat.ChatType;
import org.spongepowered.api.text.chat.ChatTypes;
import org.spongepowered.api.text.message.Message;
import org.spongepowered.api.text.message.Messages;
import org.spongepowered.api.text.title.Title;
import org.spongepowered.api.util.RelativePositions;
import org.spongepowered.api.util.Tristate;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.*;

public class GlowPlayer implements Player {
    private Message displayName;
    private boolean allowFlight;
    private Locale locale;
    private GameMode gameMode;
    private PlayerConnection connection;
    private float hunger;
    private float saturation;
    private String customName;

    @Override
    public Message getDisplayName() {
        return displayName == null ? Messages.of(getName()) : displayName;
    }

    @Override
    public boolean getAllowFlight() {
        return allowFlight;
    }

    @Override
    public void setAllowFlight(boolean allowFlight) {
        this.allowFlight = allowFlight;
    }

    @Override
    public Locale getLocale() {
        return locale;
    }

    @Override
    public void sendMessage(ChatType type, String... message) {
        for (String str : message) {
            sendMessage(type, Messages.of(str));
        }
    }

    @Override
    public void sendMessage(ChatType type, Message... messages) {
        //TODO
    }

    @Override
    public void sendMessage(ChatType type, Iterable<Message> messages) {
        for (Message msg : messages) {
            sendMessage(type, msg);
        }
    }

    @Override
    public void sendTitle(Title title) {

    }

    @Override
    public void resetTitle() {

    }

    @Override
    public void clearTitle() {

    }

    @Override
    public GameMode getGameMode() {
        return gameMode;
    }

    @Override
    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    @Override
    public PlayerConnection getConnection() {
        return connection;
    }

    @Override
    public void sendMessage(String... messages) {
        sendMessage(ChatTypes.CHAT, messages);
    }

    @Override
    public void sendMessage(Message... messages) {
        sendMessage(ChatTypes.CHAT, messages);
    }

    @Override
    public void sendMessage(Iterable<Message> messages) {
        sendMessage(ChatTypes.CHAT, messages);
    }

    @Override
    public DataContainer toContainer() {
        return null;
    }

    @Override
    public void serialize(DataSource source) {

    }

    @Override
    public float getHunger() {
        return hunger;
    }

    @Override
    public void setHunger(float hunger) {
        this.hunger = hunger;
    }

    @Override
    public float getSaturation() {
        return this.saturation;
    }

    @Override
    public void setSaturation(float saturation) {
        this.saturation = saturation;
    }

    @Override
    public double getExperience() {
        return 0;
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public double getTotalExperinece() {
        return 0;
    }

    @Override
    public void setExperience(double experience) {

    }

    @Override
    public void setLevel(int level) {

    }

    @Override
    public void setTotalExperience(double totalExperience) {

    }

    @Override
    public boolean isViewingInventory() {
        return false;
    }

    @Override
    public Optional<ItemStack> getHelmet() {
        return null;
    }

    @Override
    public void setHelmet(ItemStack helmet) {

    }

    @Override
    public Optional<ItemStack> getChestplate() {
        return null;
    }

    @Override
    public void setChestplate(ItemStack chestplate) {

    }

    @Override
    public Optional<ItemStack> getLeggings() {
        return null;
    }

    @Override
    public void setLeggings(ItemStack leggings) {

    }

    @Override
    public Optional<ItemStack> getBoots() {
        return null;
    }

    @Override
    public void setBoots(ItemStack boots) {

    }

    @Override
    public Optional<ItemStack> getItemInHand() {
        return null;
    }

    @Override
    public void setItemInHand(ItemStack itemInHand) {

    }

    @Override
    public void damage(double amount) {

    }

    @Override
    public double getHealth() {
        return 0;
    }

    @Override
    public void setHealth(double health) {

    }

    @Override
    public double getMaxHealth() {
        return 0;
    }

    @Override
    public void setMaxHealth(double maxHealth) {

    }

    @Override
    public void addPotionEffect(PotionEffect potionEffect, boolean force) {

    }

    @Override
    public void addPotionEffects(Collection<PotionEffect> potionEffects, boolean force) {

    }

    @Override
    public void removePotionEffect(PotionEffectType potionEffectType) {

    }

    @Override
    public boolean hasPotionEffect(PotionEffectType potionEffectType) {
        return false;
    }

    @Override
    public List<PotionEffect> getPotionEffects() {
        return null;
    }

    @Override
    public Optional<Living> getLastAttacker() {
        return null;
    }

    @Override
    public void setLastAttacker(Living lastAttacker) {

    }

    @Override
    public double getEyeHeight() {
        return 0;
    }

    @Override
    public Vector3f getEyeLocation() {
        return null;
    }

    @Override
    public int getRemainingAir() {
        return 0;
    }

    @Override
    public void setRemainingAir(int air) {

    }

    @Override
    public int getMaxAir() {
        return 0;
    }

    @Override
    public void setMaxAir(int air) {

    }

    @Override
    public double getLastDamage() {
        return 0;
    }

    @Override
    public void setLastDamage(double damage) {

    }

    @Override
    public int getInvulnerabilityTicks() {
        return 0;
    }

    @Override
    public void setInvulnerabilityTicks(int ticks) {

    }

    @Override
    public int getMaxInvulnerabilityTicks() {
        return 0;
    }

    @Override
    public void setMaxInvulnerabilityTicks(int ticks) {

    }

    @Override
    public String getCustomName() {
        return customName;
    }

    @Override
    public void setCustomName(String name) {

    }

    @Override
    public boolean isCustomNameVisible() {
        return false;
    }

    @Override
    public void setCustomNameVisible(boolean visible) {

    }

    @Override
    public boolean isInvisible() {
        return false;
    }

    @Override
    public void setInvisible(boolean invisible) {

    }

    @Override
    public World getWorld() {
        return null;
    }

    @Override
    public Location getLocation() {
        return null;
    }

    @Override
    public boolean setLocation(Location location) {
        return false;
    }

    @Override
    public boolean setLocationAndRotation(Location location, Vector3f rotation, EnumSet<RelativePositions> relativePositions) {
        return false;
    }

    @Override
    public Vector3f getRotation() {
        return null;
    }

    @Override
    public void setRotation(Vector3f rotation) {

    }

    @Override
    public Optional<Entity> getPassenger() {
        return null;
    }

    @Override
    public Optional<Entity> getVehicle() {
        return null;
    }

    @Override
    public Entity getBaseVehicle() {
        return null;
    }

    @Override
    public boolean setPassenger(Entity entity) {
        return false;
    }

    @Override
    public boolean setVehicle(Entity entity) {
        return false;
    }

    @Override
    public float getBase() {
        return 0;
    }

    @Override
    public float getHeight() {
        return 0;
    }

    @Override
    public float getScale() {
        return 0;
    }

    @Override
    public boolean isOnGround() {
        return false;
    }

    @Override
    public boolean isRemoved() {
        return false;
    }

    @Override
    public boolean isLoaded() {
        return false;
    }

    @Override
    public void remove() {

    }

    @Override
    public int getFireTicks() {
        return 0;
    }

    @Override
    public void setFireTicks(int ticks) {

    }

    @Override
    public int getFireDelay() {
        return 0;
    }

    @Override
    public boolean isPersistent() {
        return false;
    }

    @Override
    public void setPersistent(boolean persistent) {

    }

    @Override
    public <T> Optional<T> getData(Class<T> dataClass) {
        return null;
    }

    @Override
    public EntityType getType() {
        return null;
    }

    @Override
    public EntitySnapshot getSnapshot() {
        return null;
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<T> projectileClass) {
        return null;
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<T> projectileClass, Vector3f velocity) {
        return null;
    }

    @Override
    public GameProfile getProfile() {
        return null;
    }

    @Override
    public boolean hasJoinedBefore() {
        return false;
    }

    @Override
    public Date getFirstPlayed() {
        return null;
    }

    @Override
    public Date getLastPlayed() {
        return null;
    }

    @Override
    public boolean isBanned() {
        return false;
    }

    @Override
    public boolean isWhitelisted() {
        return false;
    }

    @Override
    public boolean isOnline() {
        return false;
    }

    @Override
    public Optional<Player> getPlayer() {
        return Optio;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public UUID getUniqueId() {
        return null;
    }

    @Override
    public String getIdentifier() {
        return null;
    }

    @Override
    public Optional<CommandSource> getCommandSource() {
        return null;
    }

    @Override
    public SubjectCollection getContainingCollection() {
        return null;
    }

    @Override
    public SubjectData getData() {
        return null;
    }

    @Override
    public SubjectData getTransientData() {
        return null;
    }

    @Override
    public boolean hasPermission(Set<Context> contexts, String permission) {
        return false;
    }

    @Override
    public boolean hasPermission(String permission) {
        return false;
    }

    @Override
    public Tristate getPermissionValue(Set<Context> contexts, String permission) {
        return null;
    }

    @Override
    public boolean isChildOf(Subject parent) {
        return false;
    }

    @Override
    public boolean isChildOf(Set<Context> contexts, Subject parent) {
        return false;
    }

    @Override
    public List<Subject> getParents() {
        return null;
    }

    @Override
    public List<Subject> getParents(Set<Context> contexts) {
        return null;
    }

    @Override
    public Set<Context> getActiveContexts() {
        return null;
    }

    @Override
    public void spawnParticles(ParticleEffect particleEffect, Vector3d position) {

    }

    @Override
    public void spawnParticles(ParticleEffect particleEffect, Vector3d position, int radius) {

    }

    @Override
    public void playSound(SoundType sound, Vector3d position, double volume) {

    }

    @Override
    public void playSound(SoundType sound, Vector3d position, double volume, double pitch) {

    }

    @Override
    public void playSound(SoundType sound, Vector3d position, double volume, double pitch, double minVolume) {

    }
}
