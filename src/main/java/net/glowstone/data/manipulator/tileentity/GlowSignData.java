package net.glowstone.data.manipulator.tileentity;

import net.glowstone.data.manipulator.GlowDataManipulator;
import org.spongepowered.api.data.manipulator.tileentity.SignData;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;

import java.util.Arrays;
import java.util.List;

public class GlowSignData extends GlowDataManipulator<SignData> implements SignData {
    private final Text[] lines;

    public GlowSignData() {
        super(SignData.class);
        lines = new Text[4];
        Arrays.fill(lines, Texts.of());
    }

    public GlowSignData(Text[] arr) {
        super(SignData.class);
        lines = arr;
    }

    public Text[] getLineArr() {
        return lines;
    }

    @Override
    public List<Text> getLines() {
        return Arrays.asList(lines);
    }

    @Override
    public Text getLine(int index) throws IndexOutOfBoundsException {
        if (index >= lines.length) {
            throw new IndexOutOfBoundsException();
        }

        return lines[index];
    }

    @Override
    public SignData setLine(int index, Text text) throws IndexOutOfBoundsException {
        if (index >= lines.length) {
            throw new IndexOutOfBoundsException();
        }

        lines[index] = text;
        return this;
    }

    @Override
    public SignData reset() {
        for (int i = 0; i < lines.length; i++) {
            lines[i] = Texts.of();
        }

        return this;
    }

    @Override
    public SignData copy() {
        return new GlowSignData(lines.clone());
    }

    @Override
    public int compareTo(SignData o) {
        return 0;
    }

    public void setLines(Text[] lines) {
        System.arraycopy(lines, 0, this.lines, 0, this.lines.length);
    }
}
