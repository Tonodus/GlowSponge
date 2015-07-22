package net.glowstone.inventory.inventories.tileentity;

import com.google.common.base.Optional;
import net.glowstone.inventory.inventories.base.GlowGridInventory;
import org.spongepowered.api.block.tileentity.carrier.Dropper;
import org.spongepowered.api.entity.living.Human;
import org.spongepowered.api.item.inventory.type.TileEntityInventory;

public class DropperInventory extends GlowGridInventory implements TileEntityInventory<Dropper>{
    private final Dropper dropper;

    public DropperInventory(Dropper dropper) {
        super(null, 3, 3);
        this.dropper = dropper;
    }

    @Override
    public Optional<Dropper> getTileEntity() {
        return Optional.of(dropper);
    }

    @Override
    public Optional<Dropper> getCarrier() {
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
