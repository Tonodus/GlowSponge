package net.glowstone.block.entity;

import net.glowstone.constants.GlowBlockEntity;
import net.glowstone.data.manipulator.tileentity.GlowBannerData;
import net.glowstone.entity.player.GlowPlayer;
import net.glowstone.id.IdManagers;
import net.glowstone.util.nbt.CompoundTag;
import net.glowstone.util.nbt.TagType;
import org.spongepowered.api.block.tileentity.Banner;
import org.spongepowered.api.block.tileentity.TileEntityType;
import org.spongepowered.api.block.tileentity.TileEntityTypes;
import org.spongepowered.api.data.manipulator.tileentity.BannerData;
import org.spongepowered.api.world.Location;

import java.util.List;

public class TEBanner extends GlowSingleDataTileEntity<BannerData> implements Banner {
    public TEBanner(Location location) {
        super(location, BannerData.class);
        setSaveId("Banner");
    }

    @Override
    public TileEntityType getType() {
        return TileEntityTypes.BANNER;
    }

    @Override
    public void loadNbt(CompoundTag tag) {
        super.loadNbt(tag);

        GlowBannerData data = new GlowBannerData();
        if (tag.isList("Patterns", TagType.COMPOUND)) {
            List<CompoundTag> pattern = tag.getCompoundList("Patterns");
            for (CompoundTag patternTag : pattern) {
                //     data.addPatternLayer();
            }
        }

        if (tag.isInt("Base")) {
            data.setBaseColor(IdManagers.DYE_COLORS.getById((byte) tag.getInt("Base")));
        }
        setRawData(data);
    }

    @Override
    public void saveNbt(CompoundTag tag) {
        super.saveNbt(tag);
        //   tag.putCompoundList("Patterns", BlockBanner.toNBT(pattern));
        tag.putInt("Base", IdManagers.DYE_COLORS.getId(getRawData().getBaseColor()));
    }

    @Override
    public void update(GlowPlayer player) {
        super.update(player);
        CompoundTag nbt = new CompoundTag();
        saveNbt(nbt);
        player.sendTileEntityChange(x, y, z, GlowBlockEntity.BANNER, nbt);
    }

    @Override
    protected BannerData createNew() {
        return new GlowBannerData();
    }
}
