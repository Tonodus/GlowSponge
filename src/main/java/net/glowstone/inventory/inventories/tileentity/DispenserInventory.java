package net.glowstone.inventory.inventories.tileentity;

import com.google.common.base.Optional;
import net.glowstone.inventory.inventories.base.GlowGridInventory;
import org.spongepowered.api.block.tileentity.carrier.Dispenser;
import org.spongepowered.api.entity.living.Human;
import org.spongepowered.api.item.inventory.type.TileEntityInventory;

public class DispenserInventory extends GlowGridInventory implements TileEntityInventory<Dispenser> {
    private final Dispenser dispenser;

    public DispenserInventory(Dispenser dispenser) {
        super(null, 3, 3);
        this.dispenser = dispenser;
    }

    @Override
    public Optional<Dispenser> getTileEntity() {
        return Optional.of(dispenser);
    }

    @Override
    public Optional<Dispenser> getCarrier() {
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
