package net.glowstone.inventory.inventories.tileentity;

import com.google.common.base.Optional;
import net.glowstone.inventory.inventories.base.GlowGridInventory;
import org.spongepowered.api.block.tileentity.carrier.Chest;
import org.spongepowered.api.entity.living.Human;
import org.spongepowered.api.item.inventory.type.GridInventory;
import org.spongepowered.api.item.inventory.type.TileEntityInventory;

public class ChestInventory extends GlowGridInventory implements GridInventory, TileEntityInventory<Chest> {
    public static final int WIDTH = 9;
    public static final int SINGLE_HEIGHT = 5;

    private final Chest chest;

    public ChestInventory(Chest chest) {
        super(null, WIDTH, SINGLE_HEIGHT);
        this.chest = chest;
    }

    @Override
    public Optional<Chest> getTileEntity() {
        return Optional.of(chest);
    }

    @Override
    public Optional<Chest> getCarrier() {
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
