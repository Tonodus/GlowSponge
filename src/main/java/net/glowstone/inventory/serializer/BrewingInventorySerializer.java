package net.glowstone.inventory.serializer;

import net.glowstone.inventory.BasicInventorySerializer;
import net.glowstone.inventory.InventorySerializer;
import net.glowstone.inventory.inventories.tileentity.BrewingInventory;
import net.glowstone.io.nbt.NbtSerialization;
import net.glowstone.util.nbt.CompoundTag;
import net.glowstone.util.nbt.TagType;
import org.spongepowered.api.block.tileentity.carrier.BrewingStand;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BrewingInventorySerializer extends BasicInventorySerializer<BrewingInventory> implements InventorySerializer<BrewingInventory> {
    private final BrewingStand brewingStand;

    public BrewingInventorySerializer(BrewingStand brewingStand) {
        this.brewingStand = brewingStand;
    }

    @Override
    public BrewingInventory deserialize(CompoundTag tag) {
        BrewingInventory brewingInventory = new BrewingInventory(brewingStand);
        if (tag.isList("Items", TagType.COMPOUND)) {
            List<CompoundTag> items = tag.getCompoundList("Items");
            ItemStack[] contents = NbtSerialization.readInventory(items, 0, 3);
            for (int i = 0; i < contents.length; i++) {
                brewingInventory.setOutputItem(i, contents[i]);
            }
        }
        return brewingInventory;
    }

    @Override
    public void serialize(BrewingInventory inventory, CompoundTag tag) {
        List<CompoundTag> items = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            items.add(NbtSerialization.writeItem(inventory.getOutputItem(i), i));
        }
        tag.putCompoundList("Items", items);
    }
}
