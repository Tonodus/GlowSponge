package net.glowstone.inventory.inventories.tileentity;

import com.google.common.base.Optional;
import net.glowstone.inventory.EverythingItemMatcher;
import net.glowstone.inventory.ItemMatcher;
import net.glowstone.inventory.inventories.base.GlowBaseInventory;
import net.glowstone.inventory.inventories.base.GlowInputSlot;
import net.glowstone.inventory.inventories.base.GlowOutputSlot;
import org.spongepowered.api.block.tileentity.carrier.BrewingStand;
import org.spongepowered.api.entity.living.Human;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.slot.InputSlot;
import org.spongepowered.api.item.inventory.slot.OutputSlot;
import org.spongepowered.api.item.inventory.type.TileEntityInventory;

public class BrewingInventory extends GlowBaseInventory implements  TileEntityInventory<BrewingStand> {
    private static final ItemMatcher VALID_ITEMS = new EverythingItemMatcher();

    private final BrewingStand brewingStand;

    private final InputSlot brewingInput;
    private final OutputSlot[] brewingOutputs;


    public BrewingInventory(BrewingStand brewingStand) {
        this.brewingStand = brewingStand;

        brewingInput = new GlowInputSlot(this, VALID_ITEMS);
        brewingOutputs = new OutputSlot[]{
                new GlowOutputSlot(this),
                new GlowOutputSlot(this),
                new GlowOutputSlot(this)
        };
    }

    @Override
    public Inventory[] getChildren() {
        return new Inventory[]{
                brewingInput,
                brewingOutputs[0],
                brewingOutputs[1],
                brewingOutputs[2],
        };
    }

    public void setInputItem(ItemStack stack) {
        brewingInput.set(stack);
    }

    public void setOutputItem(int outputIndex, ItemStack stack) {
        brewingOutputs[outputIndex].set(stack);
    }

    public ItemStack getInputItem() {
        return brewingInput.peek().orNull();
    }

    public ItemStack getOutputItem(int index) {
        return brewingOutputs[index].peek().orNull();
    }

    @Override
    public Optional<BrewingStand> getCarrier() {
        return Optional.of(brewingStand);
    }

    @Override
    public Optional<BrewingStand> getTileEntity() {
        return getCarrier();
    }

    @Override
    public boolean canInteractWith(Human entity) {
        return true;
    }

    @Override
    public void markDirty() {

    }
}
