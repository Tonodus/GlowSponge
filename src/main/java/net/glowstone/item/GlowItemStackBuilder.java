package net.glowstone.item;

import com.google.common.collect.Sets;
import org.spongepowered.api.data.DataManipulator;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackBuilder;

import java.util.Arrays;
import java.util.Set;

public class GlowItemStackBuilder implements ItemStackBuilder {
    private Set<DataManipulator<?>> itemDataSet;
    private ItemType type;
    private int quantity;
    private int maxQuantity;

    public GlowItemStackBuilder() {
        reset();
    }

    public static GlowItemStack build(ItemType type) {
        return build(type, 1, (Iterable) null);
    }

    public static GlowItemStack build(ItemType itemType, int quantity, Iterable<DataManipulator<?>> data) {
        return new GlowItemStackBuilder().itemType(itemType).quantity(quantity).itemData(data).build();
    }

    public static GlowItemStack build(ItemType itemType, int quantity, DataManipulator<?>... data) {
        if (data == null) {
            return new GlowItemStackBuilder().itemType(itemType).quantity(quantity).build();
        } else {
            return new GlowItemStackBuilder().itemType(itemType).quantity(quantity).itemData(Arrays.asList(data)).build();
        }
    }

    @Override
    public GlowItemStackBuilder itemType(ItemType itemType) {
        this.type = itemType;
        return this;
    }

    @Override
    public GlowItemStackBuilder quantity(int quantity) throws IllegalArgumentException {
        this.quantity = quantity;
        return this;
    }

    @Override
    public GlowItemStackBuilder itemData(final DataManipulator<?> itemData) throws IllegalArgumentException {
        // DataTransactionResult result = validateData(this.type, itemData);
        //  if (result.getType() != DataTransactionResult.Type.SUCCESS) {
        //     throw new IllegalArgumentException("The item data is not compatible with the current item type!");
        // } else {
        if (this.itemDataSet == null) {
            this.itemDataSet = Sets.newHashSet();
        }
        this.itemDataSet.add(itemData);
        return this;
        // }
    }

    public GlowItemStackBuilder itemData(Iterable<DataManipulator<?>> itemData) throws IllegalArgumentException {
        if (itemData != null) {
            for (DataManipulator<?> manipulator : itemData) {
                itemData(manipulator);
            }
        }

        return this;
    }

    @Override
    public GlowItemStackBuilder fromItemStack(ItemStack itemStack) {
        // Assumes the item stack's values don't need to be validated
        this.type = itemStack.getItem();
        this.quantity = itemStack.getQuantity();
        this.maxQuantity = itemStack.getMaxStackQuantity();
        return this;
    }

    @Override
    public GlowItemStackBuilder reset() {
        this.type = null;
        this.quantity = 1;
        this.maxQuantity = 64;
        this.itemDataSet = Sets.newHashSet();
        return this;
    }

    @Override
    public GlowItemStack build() throws IllegalStateException {
        GlowItemStack stack = new GlowItemStack(type, quantity);

        if (this.itemDataSet != null) {
            for (DataManipulator<?> data : this.itemDataSet) {
                stack.offer((DataManipulator) data);
            }
        }

        return stack;
    }

    public static GlowItemStack copy(ItemStack stack) {
        return build(stack.getItem(), stack.getQuantity(), stack.getManipulators());
    }
}
