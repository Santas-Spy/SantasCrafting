package santas.spy;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

import santas.spy.helpers.Config;
import santas.spy.listeners.CommandListener;
import santas.spy.recipes.RecipeRegister;

public class SantasCrafting extends JavaPlugin
{
    public static SantasCrafting PLUGIN;
    private static List<NamespacedKey> recipeList;

    @Override
    public void onEnable()
    {
        PLUGIN = this;
        this.getCommand("santascrafting").setExecutor(new CommandListener());
        Config.initialize();
        registerRecipes();
    }

    @Override
    public void onDisable()
    {

    }

    public void debugMessage(String message, int requiredLevel)
    {
        PLUGIN.getLogger().info(message);
    }

    private void registerRecipes()
    {
        recipeList = new ArrayList<>();
        getLogger().info("Running registerAll");
        RecipeRegister.registerAll();
    }

    public List<NamespacedKey> getRecipeKeyList()
    {
        return recipeList;
    }

    public void addRecipeToList(NamespacedKey key)
    {
        recipeList.add(key);
    }
}
