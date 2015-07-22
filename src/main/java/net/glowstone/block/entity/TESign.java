package net.glowstone.block.entity;

import net.glowstone.data.manipulator.tileentity.GlowSignData;
import net.glowstone.entity.player.GlowPlayer;
import net.glowstone.net.message.play.game.UpdateSignMessage;
import net.glowstone.text.TextUtils;
import net.glowstone.util.nbt.CompoundTag;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.tileentity.Sign;
import org.spongepowered.api.block.tileentity.TileEntityType;
import org.spongepowered.api.block.tileentity.TileEntityTypes;
import org.spongepowered.api.data.manipulator.tileentity.SignData;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;

import java.util.List;

public class TESign extends GlowSingleDataTileEntity<SignData> implements Sign {
    public TESign(Location block) {
        super(block, SignData.class);
        setSaveId("Sign");

        if (block.getBlockType() != BlockTypes.WALL_SIGN && block.getBlockType() != BlockTypes.STANDING_SIGN) {
            throw new IllegalArgumentException("Sign must be WALL_SIGN or SIGN_POST, got " + block.getBlockType());
        }
    }

    @Override
    public void update(GlowPlayer player) {
        if (getRawData() instanceof GlowSignData) {
            player.getSession().send(new UpdateSignMessage(x, y, z, ((GlowSignData) getRawData()).getLineArr()));
        } else {
            List<Text> lines = getRawData().getLines();
            player.getSession().send(new UpdateSignMessage(x, y, z, lines.toArray(new Text[lines.size()])));
        }
    }

    @Override
    public void loadNbt(CompoundTag tag) {
        super.loadNbt(tag);
        Text[] lines = new Text[4];
        for (int i = 0; i < lines.length; ++i) {
            String key = "Text" + (i + 1);
            if (tag.isString(key)) {
                lines[i] = TextUtils.fromJSONStr(tag.getString(key));
            }
        }

        ((GlowSignData)getRawData()).setLines(lines);
    }

    @Override
    public void saveNbt(CompoundTag tag) {
        super.saveNbt(tag);

        List<Text> lines = getRawData().getLines();
        for (int i = 0; i < lines.size(); ++i) {
            tag.putString("Text" + (i + 1), TextUtils.toJSON(lines.get(i)).toString());
        }
    }

    @Override
    public TileEntityType getType() {
        return TileEntityTypes.SIGN;
    }

    @Override
    protected SignData createNew() {
        return new GlowSignData();
    }
}
