package net.glowstone.inventory.inventories.tileentity;

import com.google.common.base.Optional;
import net.glowstone.inventory.inventories.base.GlowBaseInventory;
import net.glowstone.inventory.inventories.base.OrderedContentInventory;
import org.spongepowered.api.block.tileentity.carrier.Furnace;
import org.spongepowered.api.entity.living.Human;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.type.TileEntityInventory;

public class FurnaceInventory extends GlowBaseInventory implements OrderedContentInventory, TileEntityInventory<Furnace>{
    private final Furnace furnace;

    public FurnaceInventory(Furnace furnace) {
        super(null);
        this.furnace = furnace;
    }

    @Override
    public Optional<Furnace> getTileEntity() {
        return Optional.of(furnace);
    }

    @Override
    public Optional<Furnace> getCarrier() {
        return getTileEntity();
    }

    @Override
    public boolean canInteractWith(Human entity) {
        return false;
    }

    @Override
    public void markDirty() {

    }

    @Override
    public void setRawContents(ItemStack... contents) {

    }

    @Override
    public ItemStack[] getRawContents() {
        return new ItemStack[0];
    }

    @Override
    public int getRawContentSize() {
        return 0;
    }
}
