package net.glowstone.inventory.inventories.tileentity;

import com.google.common.base.Optional;
import net.glowstone.inventory.inventories.base.GlowGridInventory;
import org.spongepowered.api.block.tileentity.carrier.Hopper;
import org.spongepowered.api.entity.living.Human;
import org.spongepowered.api.item.inventory.type.TileEntityInventory;

public class HopperInventory extends GlowGridInventory implements TileEntityInventory<Hopper>{
    private final Hopper hopper;

    public HopperInventory(Hopper hopper) {
        super(null, 5, 1);
        this.hopper = hopper;
    }

    @Override
    public Optional<Hopper> getTileEntity() {
        return Optional.of(hopper);
    }

    @Override
    public Optional<Hopper> getCarrier() {
        return getTileEntity();
    }

    @Override
    public boolean canInteractWith(Human entity) {
        return false;
    }

    @Override
    public void markDirty() {

    }
}
