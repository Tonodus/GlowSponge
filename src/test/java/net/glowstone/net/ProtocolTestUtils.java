package net.glowstone.net;

import com.google.gson.JsonObject;
import net.glowstone.entity.meta.MetadataIndex;
import net.glowstone.entity.meta.MetadataMap;
import net.glowstone.util.nbt.CompoundTag;
import net.glowstone.util.nbt.TagType;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Utilities for helping with the protocol test.
 */
public final class ProtocolTestUtils {

    private ProtocolTestUtils() {
    }

    @SuppressWarnings("unchecked")
    public static JsonObject getJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("key", "value");
        return obj;
    }

    public static Text getTextMessage() {
        return Texts.of("text");
    }

    public static List<MetadataMap.Entry> getMetadataEntry() {
        List<MetadataMap.Entry> list = new ArrayList<>();
        list.add(new MetadataMap.Entry(MetadataIndex.AGE, 1));
        return list;
    }

    public static CompoundTag getTag() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("int", 5);
        tag.putString("string", "text");
        tag.putList("list", TagType.FLOAT, Arrays.asList(1.f, 2.f, 3.f));
        tag.putCompound("compound", new CompoundTag());
        return tag;
    }
}
