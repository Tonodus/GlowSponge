package net.glowstone.inventory;

import com.google.common.collect.Iterators;
import net.glowstone.GlowServer;
import net.glowstone.item.recipe.GlowRecipeRegistry;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.spongepowered.api.item.recipe.Recipe;

import java.io.InputStream;
import java.util.*;

/**
 * Manager for crafting and smelting recipes
 */
public final class CraftingManager implements Iterable<Recipe> {
    private final GlowRecipeRegistry recipeRegistry;
    private final ArrayList<FurnaceRecipe> furnaceRecipes = new ArrayList<>();
    private final Map<Material, Integer> furnaceFuels = new HashMap<>();

    public CraftingManager(GlowRecipeRegistry recipeRegistry) {
        this.recipeRegistry = recipeRegistry;
    }

    public void initialize() {
        resetRecipes();

        // Report stats
        GlowServer.logger.info("Recipes: " +
                shapedRecipes.size() + " shaped, " +
                shapelessRecipes.size() + " shapeless, " +
                furnaceRecipes.size() + " furnace, " +
                furnaceFuels.size() + " fuels.");
    }

    /**
     * Get a furnace recipe from the crafting manager.
     * @param input The furnace input.
     * @return The FurnaceRecipe, or null if none is found.
     */
    public FurnaceRecipe getFurnaceRecipe(ItemStack input) {
        for (FurnaceRecipe recipe : furnaceRecipes) {
            if (matchesWildcard(recipe.getInput(), input)) {
                return recipe;
            }
        }
        return null;
    }

    /**
     * Get how long a given fuel material will burn for.
     * @param material The fuel material.
     * @return The time in ticks, or 0 if that material does not burn.
     */
    public int getFuelTime(Material material) {
        if (furnaceFuels.containsKey(material)) {
            return furnaceFuels.get(material);
        } else {
            return 0;
        }
    }

    /**
     * Remove enough items from the given item list to form the given recipe.
     * @param items The items to remove the ingredients from.
     * @param recipe A recipe known to match the items.
     */
    public void removeItems(ItemStack[] items, Recipe recipe) {
        // todo
    }

    /**
     * Get a shaped or shapeless recipe from the crafting manager.
     * @param items An array of items with null being empty slots. Length should be a perfect square.
     * @return The ShapedRecipe or ShapelessRecipe that matches the input, or null if none match.
     */
    public Recipe getCraftingRecipe(ItemStack[] items) {
        int size = (int) Math.sqrt(items.length);

        if (size * size != items.length) {
            throw new IllegalArgumentException("ItemStack list was not square (was " + items.length + ")");
        }

        ShapedRecipe result = getShapedRecipe(size, items);
        if (result != null) {
            return result;
        }

        ItemStack[] reversedItems = new ItemStack[items.length];
        for (int row = 0; row < size; ++row) {
            for (int col = 0; col < size; ++col) {
                int col2 = size - 1 - col;
                reversedItems[row * size + col] = items[row * size + col2];
            }
        }

        // this check saves the trouble of iterating through all the recipes again
        if (!Arrays.equals(items, reversedItems)) {
            result = getShapedRecipe(size, reversedItems);
            if (result != null) {
                return result;
            }
        }

        return getShapelessRecipe(items);
    }

    @Override
    public Iterator<Recipe> iterator() {
        return Iterators.concat(shapedRecipes.iterator(), shapelessRecipes.iterator(), furnaceRecipes.iterator());
    }

    private boolean isWildcard(short data) {
        // old-style wildcards (byte -1) not supported
        return data == Short.MAX_VALUE;
    }

    private boolean matchesWildcard(ItemStack expected, ItemStack actual) {
        return expected.getType() == actual.getType() && (isWildcard(expected.getDurability()) || expected.getDurability() == actual.getDurability());
    }

    /**
     * Get a list of all recipes for a given item. The stack size is ignored
     * in comparisons. If the durability is -1, it will match any data value.
     * @param result The item whose recipes you want
     * @return The list of recipes
     */
    public List<Recipe> getRecipesFor(ItemStack result) {
        // handling for old-style wildcards
        if (result.getDurability() == -1) {
            result = result.clone();
            result.setDurability(Short.MAX_VALUE);
        }

        List<Recipe> recipes = new LinkedList<>();
        for (Recipe recipe : this) {
            if (matchesWildcard(result, recipe.getResult())) {
                recipes.add(recipe);
            }
        }
        return recipes;
    }

    /**
     * Clear all recipes.
     */
    public void clearRecipes() {
        shapedRecipes.clear();
        shapelessRecipes.clear();
        furnaceRecipes.clear();
        furnaceFuels.clear();
    }

    /**
     * Reset the crafting recipe lists to their default states.
     */
    public void resetRecipes() {
        clearRecipes();
        loadRecipes();

        // Smelting fuels (time is in ticks)
        furnaceFuels.put(Material.COAL, 1600);
        furnaceFuels.put(Material.WOOD, 300);
        furnaceFuels.put(Material.SAPLING, 100);
        furnaceFuels.put(Material.STICK, 100);
        furnaceFuels.put(Material.FENCE, 300);
        furnaceFuels.put(Material.WOOD_STAIRS, 400);
        furnaceFuels.put(Material.TRAP_DOOR, 300);
        furnaceFuels.put(Material.LOG, 300);
        furnaceFuels.put(Material.WORKBENCH, 300);
        furnaceFuels.put(Material.BOOKSHELF, 300);
        furnaceFuels.put(Material.CHEST, 300);
        furnaceFuels.put(Material.JUKEBOX, 300);
        furnaceFuels.put(Material.NOTE_BLOCK, 300);
        furnaceFuels.put(Material.LOCKED_CHEST, 300);
        furnaceFuels.put(Material.LAVA_BUCKET, 20000);
    }

    /**
     * Load default recipes from built-in recipes.yml file.
     */
    @SuppressWarnings("unchecked")
    private void loadRecipes() {
        // Load recipes from recipes.yml file
        InputStream in = getClass().getClassLoader().getResourceAsStream("builtin/recipes.yml");
        if (in == null) {
            GlowServer.logger.warning("Could not find default recipes on classpath");
            return;
        }

        ConfigurationSection config = YamlConfiguration.loadConfiguration(in);

        // shaped
        for (Map<?, ?> data : config.getMapList("shaped")) {
            ItemStack resultStack = ItemStack.deserialize((Map<String, Object>) data.get("result"));
            ShapedRecipe recipe = new ShapedRecipe(resultStack);
            List<String> shape = (List<String>) data.get("shape");
            recipe.shape(shape.toArray(new String[shape.size()]));

            Map<String, Map<String, Object>> ingreds = (Map<String, Map<String, Object>>) data.get("ingredients");
            for (Map.Entry<String, Map<String, Object>> entry : ingreds.entrySet()) {
                ItemStack stack = ItemStack.deserialize(entry.getValue());
                recipe.setIngredient(entry.getKey().charAt(0), stack.getData());
            }

            shapedRecipes.add(recipe);
        }

        // shapeless
        for (Map<?, ?> data : config.getMapList("shapeless")) {
            ItemStack resultStack = ItemStack.deserialize((Map<String, Object>) data.get("result"));
            ShapelessRecipe recipe = new ShapelessRecipe(resultStack);

            List<Map<String, Object>> ingreds = (List<Map<String, Object>>) data.get("ingredients");
            for (Map<String, Object> entry : ingreds) {
                recipe.addIngredient(ItemStack.deserialize(entry).getData());
            }

            shapelessRecipes.add(recipe);
        }

        // furnace
        for (Map<?, ?> data : config.getMapList("furnace")) {
            ItemStack inputStack = ItemStack.deserialize((Map<String, Object>) data.get("input"));
            ItemStack resultStack = ItemStack.deserialize((Map<String, Object>) data.get("result"));
            furnaceRecipes.add(new FurnaceRecipe(resultStack, inputStack.getData()));
        }
    }

}
