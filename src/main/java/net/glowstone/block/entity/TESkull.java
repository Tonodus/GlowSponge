package net.glowstone.block.entity;

import net.glowstone.constants.GlowBlockEntity;
import net.glowstone.data.manipulator.tileentity.GlowSkullData;
import net.glowstone.entity.meta.profile.PlayerProfile;
import net.glowstone.entity.player.GlowPlayer;
import net.glowstone.util.nbt.CompoundTag;
import org.spongepowered.api.block.tileentity.Skull;
import org.spongepowered.api.block.tileentity.TileEntityType;
import org.spongepowered.api.block.tileentity.TileEntityTypes;
import org.spongepowered.api.data.manipulator.SkullData;
import org.spongepowered.api.world.Location;

public class TESkull extends GlowSingleDataTileEntity<SkullData> implements Skull {
    private byte rotation;
    private PlayerProfile owner;

    public TESkull(Location location) {
        super(location, SkullData.class);
        setSaveId("Skull");
    }

    @Override
    public void loadNbt(CompoundTag tag) {
        super.loadNbt(tag);
       /* SkullType type = IdManagers.SKULL_TYPES.getById(tag.getByte("SkullType"));

        if (BlockSkull.canRotate((Skull) getBlock().getState().getData())) {
            rotation = tag.getByte("Rot");
        }
        if (tag.containsKey("Owner")) {
            CompoundTag ownerTag = tag.getCompound("Owner");
            owner = PlayerProfile.fromNBT(ownerTag);
        } else if (tag.containsKey("ExtraType")) {
            // Pre-1.8 uses just a name, instead of a profile object
            String name = tag.getString("ExtraType");
            if (name != null && !name.isEmpty()) {
                owner = PlayerProfile.getProfile(name);
            }
        }*/
    }

    @Override
    public void saveNbt(CompoundTag tag) {
        super.saveNbt(tag);
      /*  tag.putByte("SkullType", type);
        if (BlockSkull.canRotate((Skull) getBlock().getState().getData())) {
            tag.putByte("Rot", rotation);
        }
        if (type == BlockSkull.getType(SkullType.PLAYER) && owner != null) {
            tag.putCompound("Owner", owner.toNBT());
        }*/
    }

    @Override
    public void update(GlowPlayer player) {
        super.update(player);
        CompoundTag nbt = new CompoundTag();
        saveNbt(nbt);
        player.sendTileEntityChange(x, y, z, GlowBlockEntity.SKULL, nbt);
    }

    @Override
    protected SkullData createNew() {
        return new GlowSkullData();
    }

    @Override
    public TileEntityType getType() {
        return TileEntityTypes.SKULL;
    }
}
