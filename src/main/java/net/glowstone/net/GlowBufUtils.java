package net.glowstone.net;

import com.flowpowered.math.vector.Vector3i;
import com.flowpowered.networking.util.ByteBufUtils;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import net.glowstone.GlowServer;
import net.glowstone.entity.meta.MetadataIndex;
import net.glowstone.entity.meta.MetadataMap;
import net.glowstone.entity.meta.MetadataType;
import net.glowstone.id.ItemIdManager;
import net.glowstone.item.ItemHelper;
import net.glowstone.text.TextUtils;
import net.glowstone.util.MutableVector;
import net.glowstone.util.nbt.CompoundTag;
import net.glowstone.util.nbt.NBTInputStream;
import net.glowstone.util.nbt.NBTOutputStream;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Contains several utility methods for writing special data types to @{link ByteBuf}s.
 */
public final class GlowBufUtils {

    private GlowBufUtils() {
    }

    /**
     * Read a list of mob metadata entries from the buffer.
     * @param buf The buffer.
     * @return The metadata.
     */
    public static List<MetadataMap.Entry> readMetadata(ByteBuf buf, ItemIdManager idManager) throws IOException {
        List<MetadataMap.Entry> entries = new ArrayList<>();
        byte item;
        while ((item = buf.readByte()) != 0x7F) {
            MetadataType type = MetadataType.byId(item >> 5);
            int id = item & 0x1f;
            MetadataIndex index = MetadataIndex.getIndex(id, type);

            switch (type) {
                case BYTE:
                    entries.add(new MetadataMap.Entry(index, buf.readByte()));
                    break;
                case SHORT:
                    entries.add(new MetadataMap.Entry(index, buf.readShort()));
                    break;
                case INT:
                    entries.add(new MetadataMap.Entry(index, buf.readInt()));
                    break;
                case FLOAT:
                    entries.add(new MetadataMap.Entry(index, buf.readFloat()));
                    break;
                case STRING:
                    entries.add(new MetadataMap.Entry(index, ByteBufUtils.readUTF8(buf)));
                    break;
                case ITEM:
                    entries.add(new MetadataMap.Entry(index, readSlot(buf, idManager)));
                    break;
            }
        }
        return entries;
    }

    /**
     * Write a list of mob metadata entries to the buffer.
     * @param buf The buffer.
     * @param entries The metadata.
     */
    public static void writeMetadata(ByteBuf buf, List<MetadataMap.Entry> entries, ItemIdManager idManager) throws IOException {
        for (MetadataMap.Entry entry : entries) {
            MetadataIndex index = entry.index;
            Object value = entry.value;

            if (value == null) continue;

            int type = index.getType().getId();
            int id = index.getIndex();
            buf.writeByte((type << 5) | id);

            switch (index.getType()) {
                case BYTE:
                    buf.writeByte((Byte) value);
                    break;
                case SHORT:
                    buf.writeShort((Short) value);
                    break;
                case INT:
                    buf.writeInt((Integer) value);
                    break;
                case FLOAT:
                    buf.writeFloat((Float) value);
                    break;
                case STRING:
                    ByteBufUtils.writeUTF8(buf, (String) value);
                    break;
                case ITEM:
                    writeSlot(buf, (ItemStack) value, idManager);
                    break;
            }
        }

        buf.writeByte(127);
    }

    /**
     * Read an uncompressed compound NBT tag from the buffer.
     * @param buf The buffer.
     * @return The tag read, or null.
     */
    public static CompoundTag readCompound(ByteBuf buf) {
        int idx = buf.readerIndex();
        if (buf.readByte() == 0) {
            return null;
        }

        buf.readerIndex(idx);
        try (NBTInputStream str = new NBTInputStream(new ByteBufInputStream(buf), false)) {
            return str.readCompound();
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Write an uncompressed compound NBT tag to the buffer.
     * @param buf The buffer.
     * @param data The tag to write, or null.
     */
    public static void writeCompound(ByteBuf buf, CompoundTag data) {
        if (data == null) {
            buf.writeByte(0);
            return;
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (NBTOutputStream str = new NBTOutputStream(out, false)) {
            str.writeTag(data);
        } catch (IOException e) {
            GlowServer.logger.log(Level.WARNING, "Error serializing NBT: " + data, e);
            return;
        }

        buf.writeBytes(out.toByteArray());
    }

    /**
     * Read an item stack from the buffer.
     * @param buf The buffer.
     * @return The stack read, or null.
     */
    public static ItemStack readSlot(ByteBuf buf, ItemIdManager idManager) {
        short type = buf.readShort();
        if (type == -1) {
            return null;
        }

        int amount = buf.readUnsignedByte();
        short durability = buf.readShort();

        ItemType material = idManager.getItemTypeById(type, durability);
        if (material == null) {
            return null;
        }

        CompoundTag tag = readCompound(buf);
        ItemStack stack = ItemHelper.buildFromOld(material, amount, durability, tag);
        return stack;
    }

    /**
     * Write an item stack to the buffer.
     * @param buf The buffer.
     * @param stack The stack to write, or null.
     */
    public static void writeSlot(ByteBuf buf, ItemStack stack, ItemIdManager idManager) {
        if (stack == null || stack.getItem() == null) {
            buf.writeShort(-1);
        } else {
            buf.writeShort(idManager.getItemId(stack));
            buf.writeByte(stack.getQuantity());
            buf.writeShort(0); //TODO: itemstack data stack.getData());


            /*
            TODO: itemmeta
            if (stack.hasItemMeta()) {
                CompoundTag tag = GlowItemFactory.instance().writeNbt(stack.getItemMeta());
                writeCompound(buf, tag);
            } else {
                writeCompound(buf, null);
            }*/
        }
    }

    /**
     * Read an encoded block vector (position) from the buffer.
     * @param buf The buffer.
     * @return The vector read.
     */
    public static MutableVector readBlockPosition(ByteBuf buf) {
        long val = buf.readLong();
        long x = (val >> 38); // signed
        long y = (val >> 26) & 0xfff; // unsigned
        // this shifting madness is used to preserve sign
        long z = (val << 38) >> 38; // signed
        return new MutableVector(x, y, z);
    }

    /**
     * Write an encoded block vector (position) to the buffer.
     * @param buf The buffer.
     * @param vector The vector to write.
     */
    public static void writeBlockPosition(ByteBuf buf, Vector3i vector) {
        writeBlockPosition(buf, vector.getX(), vector.getY(), vector.getZ());
    }

    /**
     * Write an encoded block vector (position) to the buffer.
     * @param buf The buffer.
     * @param x The x value.
     * @param y The y value.
     * @param z The z value.
     */
    public static void writeBlockPosition(ByteBuf buf, long x, long y, long z) {
        buf.writeLong(((x & 0x3ffffff) << 38) | ((y & 0xfff) << 26) | (z & 0x3ffffff));
    }

    /**
     * Read a UUID encoded as two longs from the buffer.
     * @param buf The buffer.
     * @return The UUID read.
     */
    public static UUID readUuid(ByteBuf buf) {
        return new UUID(buf.readLong(), buf.readLong());
    }

    /**
     * Write a UUID encoded as two longs to the buffer.
     * @param buf The buffer.
     * @param uuid The UUID to write.
     */
    public static void writeUuid(ByteBuf buf, UUID uuid) {
        buf.writeLong(uuid.getMostSignificantBits());
        buf.writeLong(uuid.getLeastSignificantBits());
    }

    /**
     * Read an encoded chat message from the buffer.
     * @param buf The buffer.
     * @return The chat message read.
     * @throws IOException on read failure.
     */
    public static Text readChat(ByteBuf buf) throws IOException {
        return TextUtils.fromJSONStr(ByteBufUtils.readUTF8(buf));
    }

    /**
     * Write an encoded chat message to the buffer.
     * @param buf The buffer.
     * @param text The chat message to write.
     * @throws IOException on write failure.
     */
    public static void writeChat(ByteBuf buf, JsonObject text) throws IOException {
        ByteBufUtils.writeUTF8(buf, text.toString());
    }

}
