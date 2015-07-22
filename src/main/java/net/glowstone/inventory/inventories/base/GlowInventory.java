package net.glowstone.inventory.inventories.base;

import org.spongepowered.api.item.inventory.Inventory;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public abstract class GlowInventory implements Inventory {
    private final Consumer<Inventory> changeListener;
    private final GlowInventory parent;

    protected GlowInventory(GlowInventory parent) {
        this.parent = parent;
        this.changeListener = null;
    }

    protected void notifyChange() {
        if (changeListener != null) {
            changeListener.accept(this);
        }

        parent.notifyChange();
    }

    @Nullable
    @Override
    public Inventory parent() {
        return parent;
    }
}
