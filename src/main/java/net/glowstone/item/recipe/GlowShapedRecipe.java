package net.glowstone.item.recipe;

import com.google.common.base.Optional;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.type.GridInventory;
import org.spongepowered.api.item.recipe.Recipe;

import java.util.List;
import java.util.Map;

public class GlowShapedRecipe implements Recipe {
    @Override
    public List<ItemType> getResultTypes() {
        return null;
    }

    @Override
    public boolean isValid(GridInventory grid) {
        Map<Character, ItemStack> ingredients = recipe.getIngredientMap();
        String[] shape = recipe.getShape();

        int rows = shape.length, cols = 0;
        for (String row : shape) {
            if (row.length() > cols) {
                cols = row.length();
            }
        }

        if (rows == 0 || cols == 0) return false;

        // outer loop: try at each possible starting position
        for (int rStart = 0; rStart <= size - rows; ++rStart) {
            position:
            for (int cStart = 0; cStart <= size - cols; ++cStart) {
                // inner loop: verify recipe against this position
                for (int row = 0; row < rows; ++row) {
                    for (int col = 0; col < cols; ++col) {
                        ItemStack given = items[(rStart + row) * size + cStart + col];
                        char ingredientChar = shape[row].length() > col ? shape[row].charAt(col) : ' ';
                        ItemStack expected = ingredients.get(ingredientChar);

                        // check for mismatch in presence of an item in that slot at all
                        if (expected == null) {
                            if (given != null) {
                                continue position;
                            } else {
                                continue; // good match
                            }
                        } else if (given == null) {
                            continue position;
                        }

                        // check for type and data match
                        if (!matchesWildcard(expected, given)) {
                            continue position;
                        }
                    }
                }

                // also check that no items outside the recipe size are present
                for (int row = 0; row < size; row++) {
                    for (int col = 0; col < size; col++) {
                        // if this position is outside the recipe and non-null, fail
                        if ((row < rStart || row >= rStart + rows || col < cStart || col >= cStart + cols) &&
                                items[row * size + col] != null) {
                            continue position;
                        }
                    }
                }

                // recipe matches and zero items outside the recipe part.
                return false;
            }
        } // end position loop

        return true;
    }

    @Override
    public Optional<List<ItemStack>> getResults(GridInventory grid) {
        return null;
    }
}
