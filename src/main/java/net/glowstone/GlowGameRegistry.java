package net.glowstone;

import com.google.common.base.Optional;
import org.spongepowered.api.GameProfile;
import org.spongepowered.api.GameRegistry;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.meta.BannerPatternShape;
import org.spongepowered.api.block.meta.NotePitch;
import org.spongepowered.api.block.meta.SkullType;
import org.spongepowered.api.effect.particle.ParticleEffectBuilder;
import org.spongepowered.api.effect.particle.ParticleType;
import org.spongepowered.api.effect.sound.SoundType;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.hanging.art.Art;
import org.spongepowered.api.entity.living.meta.*;
import org.spongepowered.api.entity.living.villager.Career;
import org.spongepowered.api.entity.living.villager.Profession;
import org.spongepowered.api.entity.player.gamemode.GameMode;
import org.spongepowered.api.item.Enchantment;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStackBuilder;
import org.spongepowered.api.item.merchant.TradeOfferBuilder;
import org.spongepowered.api.potion.PotionEffectBuilder;
import org.spongepowered.api.potion.PotionEffectType;
import org.spongepowered.api.status.Favicon;
import org.spongepowered.api.util.rotation.Rotation;
import org.spongepowered.api.world.DimensionType;
import org.spongepowered.api.world.biome.BiomeType;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class GlowGameRegistry implements GameRegistry{
    private final GlowGame game;

    public GlowGameRegistry(GlowGame game) {
        this.game = game;
    }

    @Override
    public Optional<BlockType> getBlock(String id) {
        return null;
    }

    @Override
    public List<BlockType> getBlocks() {
        return null;
    }

    @Override
    public Optional<ItemType> getItem(String id) {
        return null;
    }

    @Override
    public List<ItemType> getItems() {
        return null;
    }

    @Override
    public Optional<BiomeType> getBiome(String id) {
        return null;
    }

    @Override
    public List<BiomeType> getBiomes() {
        return null;
    }

    @Override
    public ItemStackBuilder getItemBuilder() {
        return null;
    }

    @Override
    public TradeOfferBuilder getTradeOfferBuilder() {
        return null;
    }

    @Override
    public PotionEffectBuilder getPotionEffectBuilder() {
        return null;
    }

    @Override
    public Optional<ParticleType> getParticleType(String name) {
        return null;
    }

    @Override
    public List<ParticleType> getParticleTypes() {
        return null;
    }

    @Override
    public ParticleEffectBuilder getParticleEffectBuilder(ParticleType particle) {
        return null;
    }

    @Override
    public Optional<SoundType> getSound(String name) {
        return null;
    }

    @Override
    public List<SoundType> getSounds() {
        return null;
    }

    @Override
    public Optional<EntityType> getEntity(String id) {
        return null;
    }

    @Override
    public List<EntityType> getEntities() {
        return null;
    }

    @Override
    public Optional<Art> getArt(String id) {
        return null;
    }

    @Override
    public List<Art> getArts() {
        return null;
    }

    @Override
    public Optional<DyeColor> getDye(String id) {
        return null;
    }

    @Override
    public List<DyeColor> getDyes() {
        return null;
    }

    @Override
    public Optional<HorseColor> getHorseColor(String id) {
        return null;
    }

    @Override
    public List<HorseColor> getHorseColors() {
        return null;
    }

    @Override
    public Optional<HorseStyle> getHorseStyle(String id) {
        return null;
    }

    @Override
    public List<HorseStyle> getHorseStyles() {
        return null;
    }

    @Override
    public Optional<HorseVariant> getHorseVariant(String id) {
        return null;
    }

    @Override
    public List<HorseVariant> getHorseVariants() {
        return null;
    }

    @Override
    public Optional<OcelotType> getOcelotType(String id) {
        return null;
    }

    @Override
    public List<OcelotType> getOcelotTypes() {
        return null;
    }

    @Override
    public Optional<RabbitType> getRabbitType(String id) {
        return null;
    }

    @Override
    public List<RabbitType> getRabbitTypes() {
        return null;
    }

    @Override
    public Optional<SkeletonType> getSkeletonType(String id) {
        return null;
    }

    @Override
    public List<SkeletonType> getSkeletonTypes() {
        return null;
    }

    @Override
    public Optional<Career> getCareer(String id) {
        return null;
    }

    @Override
    public List<Career> getCareers() {
        return null;
    }

    @Override
    public List<Career> getCareers(Profession profession) {
        return null;
    }

    @Override
    public Optional<Profession> getProfession(String id) {
        return null;
    }

    @Override
    public List<Profession> getProfessions() {
        return null;
    }

    @Override
    public List<GameMode> getGameModes() {
        return null;
    }

    @Override
    public List<PotionEffectType> getPotionEffects() {
        return null;
    }

    @Override
    public Optional<Enchantment> getEnchantment(String id) {
        return null;
    }

    @Override
    public List<Enchantment> getEnchantments() {
        return null;
    }

    @Override
    public Collection<String> getDefaultGameRules() {
        return null;
    }

    @Override
    public Optional<DimensionType> getDimensionType(String name) {
        return null;
    }

    @Override
    public List<DimensionType> getDimensionTypes() {
        return null;
    }

    @Override
    public Optional<Rotation> getRotationFromDegree(int degrees) {
        return null;
    }

    @Override
    public List<Rotation> getRotations() {
        return null;
    }

    @Override
    public GameProfile createGameProfile(UUID uuid, String name) {
        return null;
    }

    @Override
    public Favicon loadFavicon(String raw) throws IOException {
        return null;
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
    public Optional<NotePitch> getNotePitch(String name) {
        return null;
    }

    @Override
    public List<NotePitch> getNotePitches() {
        return null;
    }

    @Override
    public Optional<SkullType> getSkullType(String name) {
        return null;
    }

    @Override
    public List<SkullType> getSkullTypes() {
        return null;
    }

    @Override
    public Optional<BannerPatternShape> getBannerPatternShape(String name) {
        return null;
    }

    @Override
    public Optional<BannerPatternShape> getBannerPatternShapeById(String id) {
        return null;
    }

    @Override
    public List<BannerPatternShape> getBannerPatternShapes() {
        return null;
    }
}
