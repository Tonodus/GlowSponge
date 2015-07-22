package net.glowstone.block.entity;

import net.glowstone.data.manipulator.tileentity.GlowNoteData;
import net.glowstone.id.IdManagers;
import net.glowstone.util.nbt.CompoundTag;
import org.spongepowered.api.block.tileentity.Note;
import org.spongepowered.api.block.tileentity.TileEntityType;
import org.spongepowered.api.block.tileentity.TileEntityTypes;
import org.spongepowered.api.data.manipulator.tileentity.NoteData;
import org.spongepowered.api.world.Location;

public class TENote extends GlowSingleDataTileEntity<NoteData> implements Note {
    public TENote(Location location) {
        super(location, NoteData.class);
    }

    @Override
    public void loadNbt(CompoundTag tag) {
        super.loadNbt(tag);
        getRawData().setNote(IdManagers.NOTE_PITCHES.getById(tag.getByte("note")));
    }

    @Override
    public void saveNbt(CompoundTag tag) {
        super.saveNbt(tag);
        tag.putByte("note", IdManagers.NOTE_PITCHES.getId(getRawData().getNote()));
    }

    @Override
    public void playNote() {

    }

    @Override
    public TileEntityType getType() {
        return TileEntityTypes.NOTE;
    }

    @Override
    protected NoteData createNew() {
        return new GlowNoteData();
    }
}
