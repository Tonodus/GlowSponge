package net.glowstone.world;

import com.flowpowered.math.vector.Vector3i;
import com.google.common.base.Optional;
import net.glowstone.util.GameRuleManager;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.entity.player.gamemode.GameMode;
import org.spongepowered.api.world.DimensionType;
import org.spongepowered.api.world.GeneratorType;
import org.spongepowered.api.world.GeneratorTypes;
import org.spongepowered.api.world.WorldBorder;
import org.spongepowered.api.world.difficulty.Difficulty;
import org.spongepowered.api.world.gen.WorldGeneratorModifier;
import org.spongepowered.api.world.storage.WorldProperties;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public class GlowWorldProperties implements WorldProperties {
    private long seed;
    private final UUID uid;
    private boolean keepSpawnLoaded;
    private final String name;
    private boolean isEnabled;
    private final GameRuleManager gameRuleManager;
    private Vector3i spawnPosition;
    private Difficulty difficulty;
    private final WorldBorder worldBorder;
    private boolean isHardcore;
    private GeneratorType generatorType;

    public GlowWorldProperties(String name, long seed, UUID uid) {
        this.name = name;
        this.seed = seed;
        this.uid = uid;
        this.worldBorder = null; //TODO
        this.gameRuleManager = new GameRuleManager();
        this.generatorType = GeneratorTypes.DEFAULT;
        this.isEnabled = true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public void setEnabled(boolean state) {
        this.isEnabled = state;
    }

    @Override
    public boolean loadOnStartup() {
        return false;
    }

    @Override
    public void setLoadOnStartup(boolean state) {

    }

    @Override
    public boolean doesKeepSpawnLoaded() {
        return keepSpawnLoaded;
    }

    @Override
    public void setKeepSpawnLoaded(boolean state) {
        this.keepSpawnLoaded = state;
    }

    @Override
    public String getWorldName() {
        return name;
    }

    @Override
    public UUID getUniqueId() {
        return uid;
    }

    @Override
    public Vector3i getSpawnPosition() {
        return spawnPosition;
    }

    @Override
    public void setSpawnPosition(Vector3i position) {
        this.spawnPosition = position;
    }

    @Override
    public GeneratorType getGeneratorType() {
        return generatorType;
    }

    @Override
    public void setGeneratorType(GeneratorType type) {
        this.generatorType = type;
    }

    @Override
    public long getSeed() {
        return seed;
    }

    @Override
    public long getTotalTime() {
        return 0;
    }

    @Override
    public long getWorldTime() {
        return 0;
    }

    @Override
    public void setWorldTime(long time) {

    }

    @Override
    public DimensionType getDimensionType() {
        return null;
    }

    @Override
    public boolean isRaining() {
        return false;
    }

    @Override
    public void setRaining(boolean state) {

    }

    @Override
    public int getRainTime() {
        return 0;
    }

    @Override
    public void setRainTime(int time) {

    }

    @Override
    public boolean isThundering() {
        return false;
    }

    @Override
    public void setThundering(boolean state) {

    }

    @Override
    public int getThunderTime() {
        return 0;
    }

    @Override
    public void setThunderTime(int time) {

    }

    @Override
    public GameMode getGameMode() {
        return null;
    }

    @Override
    public void setGameMode(GameMode gamemode) {

    }

    @Override
    public boolean usesMapFeatures() {
        return false;
    }

    @Override
    public void setMapFeaturesEnabled(boolean state) {

    }

    @Override
    public boolean isHardcore() {
        return isHardcore;
    }

    @Override
    public void setHardcore(boolean state) {
        this.isHardcore = state;
    }

    @Override
    public boolean areCommandsAllowed() {
        return false;
    }

    @Override
    public void setCommandsAllowed(boolean state) {

    }

    @Override
    public boolean isInitialized() {
        return false;
    }

    @Override
    public Difficulty getDifficulty() {
        return difficulty;
    }

    @Override
    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public WorldBorder getWorldBorder() {
        return worldBorder;
    }

    public GameRuleManager getGameRuleManager() {
        return gameRuleManager;
    }

    @Override
    public Optional<String> getGameRule(String gameRule) {
        return Optional.fromNullable(gameRuleManager.getString(gameRule));
    }

    @Override
    public Map<String, String> getGameRules() {
        return gameRuleManager.getGameRules();
    }

    @Override
    public void setGameRule(String gameRule, String value) {
        gameRuleManager.setValue(gameRule, value);
    }

    @Override
    public DataContainer getAdditionalProperties() {
        return null;
    }

    @Override
    public Optional<DataView> getPropertySection(DataQuery path) {
        return null;
    }

    @Override
    public void setPropertySection(DataQuery path, DataView data) {

    }

    @Override
    public Collection<WorldGeneratorModifier> getGeneratorModifiers() {
        return null;
    }

    @Override
    public void setGeneratorModifiers(Collection<WorldGeneratorModifier> modifiers) {

    }

    @Override
    public DataContainer getGeneratorSettings() {
        return null;
    }

    @Override
    public DataContainer toContainer() {
        return null;
    }

}
