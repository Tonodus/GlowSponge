package net.glowstone.inventory.serializer;

import net.glowstone.inventory.InventorySerializer;
import net.glowstone.inventory.inventories.base.OrderedContentInventory;
import net.glowstone.io.nbt.NbtSerialization;
import net.glowstone.util.nbt.CompoundTag;
import net.glowstone.util.nbt.TagType;

public abstract class OrderedContentInventorySerializer<T extends OrderedContentInventory> implements InventorySerializer<T> {
    @Override
    public T deserialize(CompoundTag tag) {
        T inventory = create(tag);
        if (tag.isList("Items", TagType.COMPOUND)) {
            inventory.setRawContents(NbtSerialization.readInventory(tag.getCompoundList("Items"), 0, inventory.getRawContentSize()));
        }
        return inventory;
    }

    protected abstract T create(CompoundTag tag);

    @Override
    public void serialize(T inventory, CompoundTag tag) {
        tag.putCompoundList("Items", NbtSerialization.writeInventory(inventory.getRawContents(), 0));
    }
}
