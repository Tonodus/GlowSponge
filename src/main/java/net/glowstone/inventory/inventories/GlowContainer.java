package net.glowstone.inventory.inventories;

import com.google.common.collect.ImmutableSet;
import net.glowstone.entity.player.GlowHumanEntity;
import net.glowstone.inventory.inventories.base.GlowBaseInventory;
import org.spongepowered.api.entity.living.Human;
import org.spongepowered.api.item.inventory.Container;

import java.util.HashSet;
import java.util.Set;

public class GlowContainer extends GlowBaseInventory implements Container {
    /**
     * The list of humans viewing this inventory.
     */
    private final Set<GlowHumanEntity> viewers = new HashSet<>();

    /**
     * Add a viewer to the inventory.
     * @param viewer The HumanEntity to add.
     */
    public void addViewer(GlowHumanEntity viewer) {
        if (!viewers.contains(viewer)) {
            viewers.add(viewer);
        }
    }

    /**
     * Remove a viewer from the inventory.
     * @param viewer The HumanEntity to remove.
     */
    public void removeViewer(GlowHumanEntity viewer) {
        if (viewers.contains(viewer)) {
            viewers.remove(viewer);
        }
    }

    @Override
    public Set<Human> getViewers() {
        return (Set) ImmutableSet.copyOf(viewers);
    }

    @Override
    public boolean hasViewers() {
        return !viewers.isEmpty();
    }

    @Override
    public void open(Human viewer) {
        viewer.openInventory(this);
    }

    @Override
    public void close(Human viewer) {
        if (viewer.isViewingInventory() && viewer.getOpenInventory().get() == this) {
            viewer.closeInventory();
        }
    }

    @Override
    public boolean canInteractWith(Human entity) {
        return true;
    }
}
