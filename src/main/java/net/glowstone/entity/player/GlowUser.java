package net.glowstone.entity.player;

import com.google.common.base.Optional;
import net.glowstone.GlowServer;
import net.glowstone.io.PlayerDataService;
import org.spongepowered.api.GameProfile;
import org.spongepowered.api.data.*;
import org.spongepowered.api.data.manipulator.entity.AchievementData;
import org.spongepowered.api.data.manipulator.entity.BanData;
import org.spongepowered.api.data.manipulator.entity.StatisticData;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.entity.player.User;
import org.spongepowered.api.item.inventory.Carrier;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.equipment.EquipmentType;
import org.spongepowered.api.item.inventory.type.CarriedInventory;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.service.permission.SubjectCollection;
import org.spongepowered.api.service.permission.SubjectData;
import org.spongepowered.api.service.permission.context.Context;
import org.spongepowered.api.service.persistence.InvalidDataException;
import org.spongepowered.api.util.Tristate;
import org.spongepowered.api.util.command.CommandSource;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class GlowUser implements User {
    private final GlowServer server;
    private final GameProfile gameProfile;
    private final PlayerDataService.PlayerReader reader;
    private boolean isLoaded;

    public GlowUser(GlowServer server, GameProfile gameProfile, PlayerDataService.PlayerReader reader) {
        this.server = server;
        this.gameProfile = gameProfile;
        this.reader = reader;
        this.isLoaded = false;
    }

    private void load() {
        if (isLoaded) {
            return;
        }
        isLoaded = true;
        reader.readData(this);
    }

    @Override
    public GameProfile getProfile() {
        return gameProfile;
    }

    @Override
    public String getName() {
        return gameProfile.getName();
    }

    @Override
    public boolean isOnline() {
        return server.getPlayer(gameProfile.getUniqueId()).isPresent();
    }

    @Override
    public Optional<Player> getPlayer() {
        return server.getPlayer(gameProfile.getUniqueId());
    }

    @Override
    public AchievementData getAchievementData() {
        load();
        return null;
    }

    @Override
    public StatisticData getStatisticData() {
        return null;
    }

    @Override
    public BanData getBanData() {
        return null;
    }

    @Override
    public Optional<ItemStack> getHelmet() {
        load();
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
    public CarriedInventory<? extends Carrier> getInventory() {
        return null;
    }

    @Override
    public boolean canEquip(EquipmentType type) {
        return false;
    }

    @Override
    public boolean canEquip(EquipmentType type, ItemStack equipment) {
        return false;
    }

    @Override
    public Optional<ItemStack> getEquipped(EquipmentType type) {
        return null;
    }

    @Override
    public boolean equip(EquipmentType type, ItemStack equipment) {
        return false;
    }

    @Override
    public UUID getUniqueId() {
        return gameProfile.getUniqueId();
    }

    @Override
    public String getIdentifier() {
        return gameProfile.getUniqueId().toString();
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
    public SubjectData getSubjectData() {
        return null;
    }

    @Override
    public SubjectData getTransientSubjectData() {
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
    public <T extends DataManipulator<T>> Optional<T> getData(Class<T> dataClass) {
        return null;
    }

    @Override
    public <T extends DataManipulator<T>> Optional<T> getOrCreate(Class<T> manipulatorClass) {
        return null;
    }

    @Override
    public <T extends DataManipulator<T>> boolean remove(Class<T> manipulatorClass) {
        return false;
    }

    @Override
    public <T extends DataManipulator<T>> boolean isCompatible(Class<T> manipulatorClass) {
        return false;
    }

    @Override
    public <T extends DataManipulator<T>> DataTransactionResult offer(T manipulatorData) {
        return null;
    }

    @Override
    public <T extends DataManipulator<T>> DataTransactionResult offer(T manipulatorData, DataPriority priority) {
        return null;
    }

    @Override
    public Collection<DataManipulator<?>> getManipulators() {
        return null;
    }

    @Override
    public <T extends Property<?, ?>> Optional<T> getProperty(Class<T> propertyClass) {
        return null;
    }

    @Override
    public Collection<Property<?, ?>> getProperties() {
        return null;
    }

    @Override
    public boolean validateRawData(DataContainer container) {
        return false;
    }

    @Override
    public void setRawData(DataContainer container) throws InvalidDataException {

    }

    @Override
    public DataContainer toContainer() {
        return null;
    }
}
