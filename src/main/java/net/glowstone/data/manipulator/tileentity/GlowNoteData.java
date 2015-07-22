package net.glowstone.data.manipulator.tileentity;

import net.glowstone.data.manipulator.GlowSingleValueData;
import org.spongepowered.api.data.manipulator.tileentity.NoteData;
import org.spongepowered.api.data.type.NotePitch;
import org.spongepowered.api.data.type.NotePitches;

public class GlowNoteData extends GlowSingleValueData<NotePitch, NoteData> implements NoteData {
    public GlowNoteData() {
        super(NoteData.class, NotePitches.A1);
    }

    @Override
    public NotePitch getNote() {
        return getValue();
    }

    @Override
    public NoteData setNote(NotePitch note) {
        return setValue(note);
    }

    @Override
    public NoteData copy() {
        return new GlowNoteData().setNote(getNote());
    }

    @Override
    public int compareTo(NoteData o) {
        return 0;
    }
}
