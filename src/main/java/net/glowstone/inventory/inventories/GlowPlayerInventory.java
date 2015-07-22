package net.glowstone.inventory.inventories;

import com.google.common.base.Optional;
import net.glowstone.entity.player.GlowHumanEntity;
import net.glowstone.entity.player.GlowPlayer;
import net.glowstone.inventory.DragTracker;
import net.glowstone.inventory.GlowCraftingInventory;
import net.glowstone.inventory.inventories.base.GlowTopInventory;
import net.glowstone.net.message.play.inv.HeldItemMessage;
import org.bukkit.event.inventory.InventoryType;
import org.spongepowered.api.entity.ArmorEquipable;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.entity.HumanInventory;
import org.spongepowered.api.item.inventory.equipment.EquipmentType;
import org.spongepowered.api.item.inventory.type.CarriedInventory;

import java.util.Arrays;

/**
 * An Inventory representing the items a player is holding.
 */
public class GlowPlayerInventory extends GlowTopInventory implements HumanInventory, ArmorEquipable, CarriedInventory<Player> {

    private static final int SIZE = 36;

    private static final int BOOTS_SLOT = 36;
    private static final int LEGGINGS_SLOT = 37;
    private static final int CHESTPLATE_SLOT = 38;
    private static final int HELMET_SLOT = 39;

    /**
     * The armor contents.
     */
    private final ItemStack[] armor = new ItemStack[4];

    /**
     * The crafting inventory.
     */
    private final GlowCraftingInventory crafting;

    /**
     * The current held item slot.
     */
    private int heldSlot = 0;

    /**
     * Tracker for inventory drags.
     */
    private final DragTracker tracker = new DragTracker();

    public GlowPlayerInventory(GlowHumanEntity owner) {
        // all player inventories are ID 0
        // 36 = 4 rows of 9
        // + 4 = armor, completed inventory
        super(owner, InventoryType.PLAYER, SIZE);
        crafting = new GlowCraftingInventory(owner, InventoryType.CRAFTING);
        Arrays.fill(slotTypes, 0, 9, InventoryType.SlotType.QUICKBAR);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Internals

    /**
     * Get the crafting inventory.
     * @return The GlowCraftingInventory attached to this player
     */
    public GlowCraftingInventory getCraftingInventory() {
        return crafting;
    }

    public void setRawHeldItemSlot(int slot) {
        if (slot < 0 || slot > 8)
            throw new IllegalArgumentException(slot + " not in range 0..8");
        heldSlot = slot;
        setItemInHand(getItemInHand());  // send to player again just in case
    }

    @Override
    public InventoryType.SlotType getSlotType(int slot) {
        if (slot >= SIZE && slot - SIZE < 4) {
            return InventoryType.SlotType.ARMOR;
        } else {
            return slotTypes[slot];
        }
    }

    @Override
    public boolean itemShiftClickAllowed(int slot, ItemStack stack) {
        // todo: check armor slots
        return super.itemPlaceAllowed(slot, stack);
    }

    /**
     * Get the DragTracker associated with this player.
     * @return The DragTracker.
     */
    public DragTracker getDragTracker() {
        return tracker;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Overrides


    @Override
    public void setItem(int index, ItemStack item) {
        if (index >= SIZE) {
            armor[index - SIZE] = item;
        } else {
            super.setItem(index, item);
        }
    }

    @Override
    public ItemStack getItem(int index) {
        if (index >= SIZE) {
            return armor[index - SIZE];
        } else {
            return super.getItem(index);
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Interface implementation

    public ItemStack[] getArmorContents() {
        return armor;
    }

    public void setArmorContents(ItemStack[] items) {
        if (items.length != 4) {
            throw new IllegalArgumentException("Length of armor must be 4");
        }
        for (int i = 0; i < 4; ++i) {
            setItem(SIZE + i, items[i]);
        }
    }

    public ItemStack getHelmet() {
        return getItem(HELMET_SLOT);
    }


    public ItemStack getChestplate() {
        return getItem(CHESTPLATE_SLOT);
    }


    public ItemStack getLeggings() {
        return getItem(LEGGINGS_SLOT);
    }

    public ItemStack getBoots() {
        return getItem(BOOTS_SLOT);
    }

    public void setHelmet(ItemStack helmet) {
        setItem(HELMET_SLOT, helmet);
    }

    public void setChestplate(ItemStack chestplate) {
        setItem(CHESTPLATE_SLOT, chestplate);
    }

    public void setLeggings(ItemStack leggings) {
        setItem(LEGGINGS_SLOT, leggings);
    }

    public void setBoots(ItemStack boots) {
        setItem(BOOTS_SLOT, boots);
    }


    public ItemStack getItemInHand() {
        return getItem(heldSlot);
    }

    @Override
    public void setItemInHand(ItemStack stack) {
        setItem(heldSlot, stack);
    }

    @Override
    public int getHeldItemSlot() {
        return heldSlot;
    }

    @Override
    public void setHeldItemSlot(int slot) {
        setRawHeldItemSlot(slot);

        if (getHolder() instanceof GlowPlayer) {
            ((GlowPlayer) getHolder()).getSession().send(new HeldItemMessage(slot));
        }
    }

    @Override
    public int clear(int id, int data) {
        int cleared = 0;
        for (int i = 0; i < getSize(); ++i) {
            ItemStack stack = getItem(i);
            if (stack != null && (stack.getTypeId() == id || id == -1) && (stack.getDurability() == data || data == -1)) {
                setItem(i, null);
                ++cleared;
            }
        }
        return cleared;
    }

    ////////////////////////////////////////////////////////////////////////////
    // EntityEquipment implementation

    @Override
    public float getItemInHandDropChance() {
        return 1;
    }

    @Override
    public void setItemInHandDropChance(float chance) {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getHelmetDropChance() {
        return 1;
    }

    @Override
    public void setHelmetDropChance(float chance) {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getChestplateDropChance() {
        return 1;
    }

    @Override
    public void setChestplateDropChance(float chance) {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getLeggingsDropChance() {
        return 1;
    }

    @Override
    public void setLeggingsDropChance(float chance) {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getBootsDropChance() {
        return 1;
    }

    @Override
    public void setBootsDropChance(float chance) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean canEquip(EquipmentType type) {
        return false;
    }

    @Override
    public boolean canEquip(EquipmentType type, ItemStack equipment) {
        return false;
    }

    @Override
    public Optional<ItemStack> getEquipped(EquipmentType type) {
        return null;
    }

    @Override
    public boolean equip(EquipmentType type, ItemStack equipment) {
        return false;
    }
}
