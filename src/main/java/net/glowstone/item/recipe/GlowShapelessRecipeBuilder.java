package net.glowstone.item.recipe;

import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.recipe.ShapelessRecipe;
import org.spongepowered.api.item.recipe.ShapelessRecipeBuilder;

import java.util.ArrayList;
import java.util.Collection;

public class GlowShapelessRecipeBuilder implements ShapelessRecipeBuilder {
    private final Collection<ItemStack> ingredients = new ArrayList<>();
    private final Collection<ItemStack> results = new ArrayList<>();

    @Override
    public ShapelessRecipeBuilder addIngredient(ItemStack ingredient) {
        ingredients.add(ingredient);
        return this;
    }

    @Override
    public ShapelessRecipeBuilder addResult(ItemStack result) {
        results.add(result);
        return this;
    }

    @Override
    public ShapelessRecipe build() {
        return new GlowShapelessRecipe();
    }
}
