package net.glowstone.item.recipe;

import com.google.common.base.Optional;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.type.GridInventory;
import org.spongepowered.api.item.recipe.ShapelessRecipe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GlowShapelessRecipe implements ShapelessRecipe {
    private final Collection<ItemStack> ingredients;
    private final List<ItemStack> results;

    public GlowShapelessRecipe() {
        ingredients = new ArrayList<>();
        results = new ArrayList<>();
    }

    @Override
    public List<ItemType> getResultTypes() {
        return null;
    }

    @Override
    public boolean isValid(GridInventory grid) {
        boolean[] accountedFor = new boolean[items.length];

        // Mark empty item slots accounted for
        for (int i = 0; i < items.length; ++i) {
            accountedFor[i] = items[i] == null;
        }

        // Make sure each ingredient in the recipe exists in the inventory
        ingredient:
        for (ItemStack ingredient : ingredients) {
            for (int i = 0; i < items.length; ++i) {
                // if this item is not already used and it matches this ingredient...
                if (!accountedFor[i] && matchesWildcard(ingredient, items[i])) {
                    // ... this item is accounted for and this ingredient is found.
                    accountedFor[i] = true;
                    continue ingredient;
                }
            }
            // no item matched this ingredient, so the recipe fails
            return false;
        }

        // Make sure inventory has no leftover items
        for (int i = 0; i < items.length; ++i) {
            if (!accountedFor[i]) {
                return false;
            }
        }

        return true;
    }

    @Override
    public Optional<List<ItemStack>> getResults(GridInventory grid) {
        return isValid(grid) ? Optional.of(results) : Optional.<List<ItemStack>>absent();
    }

    @Override
    public Collection<ItemStack> getIngredients() {
        return ingredients;
    }
}
