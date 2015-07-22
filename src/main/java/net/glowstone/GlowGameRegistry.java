package net.glowstone;

import com.google.common.base.Optional;
import net.glowstone.entity.meta.profile.PlayerProfile;
import net.glowstone.item.recipe.GlowRecipeRegistry;
import org.spongepowered.api.CatalogType;
import org.spongepowered.api.GameProfile;
import org.spongepowered.api.GameRegistry;
import org.spongepowered.api.attribute.AttributeBuilder;
import org.spongepowered.api.attribute.AttributeCalculator;
import org.spongepowered.api.attribute.AttributeModifierBuilder;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.data.DataManipulatorRegistry;
import org.spongepowered.api.data.ImmutableDataRegistry;
import org.spongepowered.api.data.type.Career;
import org.spongepowered.api.data.type.Profession;
import org.spongepowered.api.effect.particle.ParticleEffectBuilder;
import org.spongepowered.api.effect.particle.ParticleType;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.item.FireworkEffectBuilder;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStackBuilder;
import org.spongepowered.api.item.merchant.TradeOfferBuilder;
import org.spongepowered.api.potion.PotionEffectBuilder;
import org.spongepowered.api.resourcepack.ResourcePack;
import org.spongepowered.api.scoreboard.ScoreboardBuilder;
import org.spongepowered.api.scoreboard.TeamBuilder;
import org.spongepowered.api.scoreboard.displayslot.DisplaySlot;
import org.spongepowered.api.scoreboard.objective.ObjectiveBuilder;
import org.spongepowered.api.statistic.*;
import org.spongepowered.api.statistic.achievement.AchievementBuilder;
import org.spongepowered.api.status.Favicon;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.translation.Translation;
import org.spongepowered.api.util.rotation.Rotation;
import org.spongepowered.api.world.WorldBuilder;
import org.spongepowered.api.world.gen.PopulatorFactory;
import org.spongepowered.api.world.gen.WorldGeneratorModifier;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

public class GlowGameRegistry implements GameRegistry {
    private final Map<Class<?>, Object> builders = new HashMap<>();
    private final GlowGameDictionary gameDictionary;
    private final GlowRecipeRegistry recipeRegistry;

    public GlowGameRegistry(GlowGame game) {
        gameDictionary = new GlowGameDictionary();
        recipeRegistry = new GlowRecipeRegistry();

        registerCatalogTypes();
        registerBuilders();
    }

    private void registerBuilders() {
    }

    private void registerCatalogTypes() {

    }

    @Override
    public <T extends CatalogType> Optional<T> getType(Class<T> typeClass, String id) {
        return null;
    }

    @Override
    public <T extends CatalogType> Collection<T> getAllOf(Class<T> typeClass) {
        return null;
    }

    @Override
    public <T> Optional<T> getBuilderOf(Class<T> builderClass) {
        return Optional.fromNullable((T) builders.get(builderClass));
    }

    private <T> T getBuilder(Class<T> builderClass) {
        return (T) builders.get(builderClass);
    }

    @Override
    public ItemStackBuilder getItemBuilder() {
        return getBuilder(ItemStackBuilder.class);
    }

    @Override
    public TradeOfferBuilder getTradeOfferBuilder() {
        return getBuilder(TradeOfferBuilder.class);
    }

    @Override
    public FireworkEffectBuilder getFireworkEffectBuilder() {
        return getBuilder(FireworkEffectBuilder.class);
    }

    @Override
    public PotionEffectBuilder getPotionEffectBuilder() {
        return getBuilder(PotionEffectBuilder.class);
    }

    @Override
    public ObjectiveBuilder getObjectiveBuilder() {
        return getBuilder(ObjectiveBuilder.class);
    }

    @Override
    public TeamBuilder getTeamBuilder() {
        return getBuilder(TeamBuilder.class);
    }

    @Override
    public ScoreboardBuilder getScoreboardBuilder() {
        return getBuilder(ScoreboardBuilder.class);
    }

    @Override
    public StatisticBuilder getStatisticBuilder() {
        return getBuilder(StatisticBuilder.class);
    }

    @Override
    public StatisticBuilder.EntityStatisticBuilder getEntityStatisticBuilder() {
        return null;
    }

    @Override
    public StatisticBuilder.BlockStatisticBuilder getBlockStatisticBuilder() {
        return null;
    }

    @Override
    public StatisticBuilder.ItemStatisticBuilder getItemStatisticBuilder() {
        return null;
    }

    @Override
    public StatisticBuilder.TeamStatisticBuilder getTeamStatisticBuilder() {
        return null;
    }

    @Override
    public AchievementBuilder getAchievementBuilder() {
        return null;
    }

    @Override
    public AttributeModifierBuilder getAttributeModifierBuilder() {
        return null;
    }

    @Override
    public AttributeCalculator getAttributeCalculator() {
        return null;
    }

    @Override
    public AttributeBuilder getAttributeBuilder() {
        return null;
    }

    @Override
    public WorldBuilder getWorldBuilder() {
        return null;
    }

    @Override
    public ParticleEffectBuilder getParticleEffectBuilder(ParticleType particle) {
        return null;
    }

    @Override
    public Collection<Career> getCareers(Profession profession) {
        /*if (profession instanceof GlowProfession) {
            return ((GlowProfession) profession).get
        }*/
        return Collections.emptyList();
    }

    @Override
    public Collection<String> getDefaultGameRules() {
        return null;
    }

    @Override
    public Optional<EntityStatistic> getEntityStatistic(StatisticGroup statisticGroup, EntityType entityType) {
        return null;
    }

    @Override
    public Optional<ItemStatistic> getItemStatistic(StatisticGroup statisticGroup, ItemType itemType) {
        return null;
    }

    @Override
    public Optional<BlockStatistic> getBlockStatistic(StatisticGroup statisticGroup, BlockType blockType) {
        return null;
    }

    @Override
    public Optional<TeamStatistic> getTeamStatistic(StatisticGroup statisticGroup, TextColor teamColor) {
        return null;
    }

    @Override
    public Collection<Statistic> getStatistics(StatisticGroup statisticGroup) {
        return null;
    }

    @Override
    public void registerStatistic(Statistic stat) {

    }

    @Override
    public Optional<Rotation> getRotationFromDegree(int degrees) {
        return null;
    }

    @Override
    public GameProfile createGameProfile(UUID uuid, String name) {
        return new PlayerProfile(name, uuid);
    }

    @Override
    public Favicon loadFavicon(String raw) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Favicon loadFavicon(File file) throws IOException {
        return null;
    }

    @Override
    public Favicon loadFavicon(URL url) throws IOException {
        return null;
    }

    @Override
    public Favicon loadFavicon(InputStream in) throws IOException {
        return null;
    }

    @Override
    public Favicon loadFavicon(BufferedImage image) throws IOException {
        return null;
    }

    @Override
    public GlowGameDictionary getGameDictionary() {
        return gameDictionary;
    }

    @Override
    public GlowRecipeRegistry getRecipeRegistry() {
        return recipeRegistry;
    }

    @Override
    public DataManipulatorRegistry getManipulatorRegistry() {
        return null;
    }

    @Override
    public ImmutableDataRegistry getImmutableDataRegistry() {
        return null;
    }

    @Override
    public Optional<ResourcePack> getById(String id) {
        return null;
    }

    @Override
    public Optional<DisplaySlot> getDisplaySlotForColor(TextColor color) {
        return null;
    }

    @Override
    public void registerWorldGeneratorModifier(WorldGeneratorModifier modifier) {

    }

    @Override
    public PopulatorFactory getPopulatorFactory() {
        return null;
    }

    @Override
    public Optional<Translation> getTranslationById(String id) {
        return null;
    }
}
