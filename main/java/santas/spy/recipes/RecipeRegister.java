package santas.spy.recipes;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

import santas.spy.SantasCrafting;
import santas.spy.helpers.Config;
import santas.spy.helpers.ItemValidater;

public abstract class RecipeRegister 
{
    /**
    * construct a shaped recipe object
    * @param path the path to the item in config
    * @return NamespacedKey for the recipe, or null if it didnt exist
    */
    public static NamespacedKey createShapedRecipe(String path)
    {
        NamespacedKey key = null;
        ConfigurationSection config = Config.getCustomItemConfig().getConfigurationSection(path);
        if (config == null)
        {
            throw new NullPointerException("Cannot find item in items.yml");
        } else {
            if (config.getStringList(".recipe") == null)
            {
                throw new NullPointerException(path + " does not have a recipe");
            } else {
                List<String> line1 = config.getStringList("recipe.line1");
                List<String> line2 = config.getStringList("recipe.line2");
                List<String> line3 = config.getStringList("recipe.line3");
                ItemStack result = ItemValidater.giveCustomItem(path);
                
                if (config.getInt("recipe.gives") != 0)
                {
                    result.setAmount(config.getInt("recipe.gives"));
                }
                key = new NamespacedKey(SantasCrafting.PLUGIN, path);
                ShapedRecipe recipe = new ShapedRecipe(key, result);

                recipe.shape("123","456","789");

                int counter = 1;
                for (String item : line1)
                {
                    recipe = setIngredients(item, recipe, counter);
                    counter++;
                }

                for (String item : line2)
                {
                    recipe = setIngredients(item, recipe, counter);
                    counter++;
                }

                for (String item : line3)
                {
                    recipe = setIngredients(item, recipe, counter);
                    counter++;
                }

                registerRecipe(recipe);
            }
        }
        return key;
    }

    /**
    * Register this recipe with Bukkit
    * @param recipe the recipe to be registered
    */
    public static void registerRecipe(ShapedRecipe recipe)
    {
        Bukkit.getServer().addRecipe(recipe);
        SantasCrafting.PLUGIN.getLogger().info("added " + recipe.getKey().getKey());
        SantasCrafting.PLUGIN.addRecipeToList(recipe.getKey());
    }

    private static ShapedRecipe setIngredients(String item, ShapedRecipe recipe, int position)
    {
        if (item.equalsIgnoreCase("X"))
        {
            recipe.setIngredient(String.valueOf(position).charAt(0), Material.AIR);
        } else {
            if (item.contains("CUSTOM"))
            {
                recipe.setIngredient(String.valueOf(position).charAt(0), new RecipeChoice.ExactChoice(ItemValidater.giveCustomItem(item.split(":")[1])));
            } else {
                recipe.setIngredient(String.valueOf(position).charAt(0), Material.getMaterial(item.toUpperCase()));
            }
        }
        return recipe;
    }

    public static void registerAll()
    {
        SantasCrafting.PLUGIN.getLogger().info("Running registerAll");
        for (String key : Config.getCustomItemKeys())
        {
            if (ItemValidater.hasRecipe(key))
            {
                SantasCrafting.PLUGIN.getLogger().info(ChatColor.BLUE + "Registering " + key);
                createShapedRecipe(key);
            }
        }
    }

    public static void deregisterAll()
    {
        List<NamespacedKey> recipes = SantasCrafting.PLUGIN.getRecipeKeyList();
        SantasCrafting.PLUGIN.getLogger().info("Size of list = " + recipes.size());

        for (NamespacedKey key : recipes)
        {
            SantasCrafting.PLUGIN.getLogger().info("deregistering " + key.getKey());
            Bukkit.getServer().removeRecipe(key);
        }

        SantasCrafting.PLUGIN.getLogger().info("Size of list = " + recipes.size());
    }
}
