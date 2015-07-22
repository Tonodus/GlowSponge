package net.glowstone.item;

import net.glowstone.util.nbt.CompoundTag;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;

public class ItemHelper {
    public static ItemStack buildFromOld(ItemType material, int amount, short durability, CompoundTag metaTag) {
        return null;
    }

    public static boolean matches(ItemStack one, ItemStack another) {
        //TODO
        return one.getItem().equals(another.getItem()) &&
                one.getManipulators().equals(another.getManipulators());
    }
}
