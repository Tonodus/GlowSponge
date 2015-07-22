package net.glowstone.item.recipe;

import com.google.common.collect.ImmutableSet;
import org.spongepowered.api.item.recipe.Recipe;
import org.spongepowered.api.item.recipe.RecipeRegistry;

import java.util.HashSet;
import java.util.Set;

public class GlowRecipeRegistry implements RecipeRegistry {
    private final Set<Recipe> recipes = new HashSet<>();

    @Override
    public void register(Recipe recipe) {
        recipes.add(recipe);
    }

    @Override
    public void remove(Recipe recipe) {
        recipes.remove(recipe);
    }

    @Override
    public Set<Recipe> getRecipes() {
        return ImmutableSet.copyOf(recipes);
    }
}
