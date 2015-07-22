package net.glowstone.inventory.inventories.base;

import com.flowpowered.math.vector.Vector2i;
import com.google.common.base.Optional;
import net.glowstone.item.GlowItemStack;
import net.glowstone.item.GlowItemStackBuilder;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.Slot;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.item.inventory.property.SlotPos;
import org.spongepowered.api.item.inventory.transaction.InventoryOperationResult;
import org.spongepowered.api.item.inventory.type.GridInventory;
import org.spongepowered.api.item.inventory.type.InventoryColumn;
import org.spongepowered.api.item.inventory.type.InventoryRow;

public class GlowGridInventory extends GlowBaseInventory implements GridInventory, OrderedContentInventory {
    private final int width;
    private final int height;
    private final ItemStack[] contents;

    private final InventoryRow[] rows;
    private final InventoryColumn[] columns;

    public GlowGridInventory(Inventory parent, int width, int height) {
        super(parent);

        rows = new InventoryRow[height];
        columns = new InventoryColumn[width];
        this.width = width;
        this.height = height;
        this.contents = new GlowItemStack[width * height];
    }

    @Override
    protected Inventory[] getChildren() {
        return children;
    }

    @Override
    public void setRawContents(ItemStack... contents) {
        if (contents.length != this.contents.length) {
            throw new IllegalArgumentException();
        }

        System.arraycopy(contents, 0, this.contents, 0, this.contents.length);
    }

    @Override
    public ItemStack[] getRawContents() {
        return contents;
    }

    @Override
    public int getRawContentSize() {
        return contents.length;
    }

    private int index(SlotPos pos) {
        return index(pos.getX(), pos.getY());
    }

    private int index(int x, int y) {
        if (x > width || y > height) {
            throw new IllegalArgumentException("x or y not in range!");
        }

        return y * width + x;
    }

    @Override
    public Optional<ItemStack> poll(SlotPos pos) {

    }

    @Override
    public Optional<ItemStack> poll(SlotPos pos, int limit) {

    }

    @Override
    public Optional<ItemStack> peek(SlotPos pos) {
        if (pos.getY() > rows.length) {
            return Optional.absent();
        }
        return rows[pos.getY()].peek(new SlotIndex(pos.getX()));
    }

    @Override
    public Optional<ItemStack> peek(SlotPos pos, int limit) {
        if (pos.getY() > rows.length) {
            return Optional.absent();
        }
        return rows[pos.getY()].peek(new SlotIndex(pos.getX()), limit);
    }

    @Override
    public InventoryOperationResult set(SlotPos pos, ItemStack stack) {
        if (pos.getY() > rows.length) {
            return null; //failure
        }
        return rows[pos.getY()].set(new SlotIndex(pos.getX()), stack);
    }

    @Override
    public Optional<Slot> getSlot(SlotPos pos) {
        if (pos.getY() > rows.length) {
            return Optional.absent();
        }
        return rows[pos.getY()].getSlot(new SlotIndex(pos.getX()));
    }

    @Override
    public Optional<ItemStack> poll(SlotIndex index) {

    }

    @Override
    public Optional<ItemStack> poll(SlotIndex index, int limit) {
        return null;
    }

    @Override
    public Optional<ItemStack> peek(SlotIndex index) {
        return null;
    }

    @Override
    public Optional<ItemStack> peek(SlotIndex index, int limit) {
        return null;
    }

    @Override
    public InventoryOperationResult set(SlotIndex index, ItemStack stack) {
        return null;
    }

    @Override
    public Optional<Slot> getSlot(SlotIndex index) {
        return null;
    }

    @Override
    public int getColumns() {
        return columns.length;
    }

    @Override
    public int getRows() {
        return rows.length;
    }

    @Override
    public Vector2i getDimensions() {
        return new Vector2i(width, height);
    }

    @Override
    public Optional<ItemStack> poll(int x, int y) {
        ItemStack stack = contents[index(x, y)];
        contents[index(x, y)] = null;
        return Optional.fromNullable(stack);
    }

    @Override
    public Optional<ItemStack> poll(int x, int y, int limit) {
        ItemStack stack = contents[index(x, y)];
        if (stack.getQuantity() > limit) {
            stack.setQuantity(stack.getQuantity() - limit);
            ItemStack withLimit = GlowItemStackBuilder.copy(stack);
            stack.setQuantity(limit);
            return Optional.of(withLimit);
        } else {
            contents[index(x, y)] = null;
            return Optional.fromNullable(stack);
        }
    }

    @Override
    public Optional<ItemStack> peek(int x, int y) {
        return null;
    }

    @Override
    public Optional<ItemStack> peek(int x, int y, int limit) {
        return null;
    }

    @Override
    public InventoryOperationResult set(int x, int y, ItemStack stack) {
        return null;
    }

    @Override
    public Optional<Slot> getSlot(int x, int y) {
        return null;
    }

    @Override
    public Optional<InventoryRow> getRow(int y) {
        if (y > rows.length) {
            return Optional.absent();
        }

        if (rows[y] == null) {
            rows[y] = new GridInventoryRow(this, y);
        }

        return Optional.of(rows[y]);
    }

    @Override
    public Optional<InventoryColumn> getColumn(int x) {
        return null;
    }

    private class GridInventoryRow implements InventoryRow {
        public GridInventoryRow(GlowGridInventory inventory, int y) {
        }
    }
}
