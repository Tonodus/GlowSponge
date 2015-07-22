package net.glowstone.block.entity;

import net.glowstone.data.manipulator.item.GlowInventoryItemData;
import net.glowstone.inventory.InventorySerializer;
import net.glowstone.inventory.inventories.tileentity.ChestInventory;
import net.glowstone.inventory.serializer.OrderedContentInventorySerializer;
import net.glowstone.util.nbt.CompoundTag;
import org.spongepowered.api.block.tileentity.TileEntityType;
import org.spongepowered.api.block.tileentity.TileEntityTypes;
import org.spongepowered.api.block.tileentity.carrier.Chest;
import org.spongepowered.api.data.manipulator.item.InventoryItemData;
import org.spongepowered.api.world.Location;

/**
 * Tile entity for Chests.
 */
public class TEChest extends TEContainer<ChestInventory, InventoryItemData> implements Chest {

    public TEChest(Location location) {
        super(location, InventoryItemData.class);
        setSaveId("Chest");
    }


    @Override
    protected InventorySerializer<ChestInventory> getSerializer() {
        return new OrderedContentInventorySerializer<ChestInventory>() {
            @Override
            protected ChestInventory create(CompoundTag tag) {
                return new ChestInventory(TEChest.this);
            }
        };
    }

    @Override
    protected InventoryItemData createNew() {
        return new GlowInventoryItemData(getInventory());
    }

    @Override
    public TileEntityType getType() {
        return TileEntityTypes.CHEST;
    }
}
