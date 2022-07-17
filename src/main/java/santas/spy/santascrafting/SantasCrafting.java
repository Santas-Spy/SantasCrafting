package santas.spy.santascrafting;

import java.util.ArrayList;
import java.util.List;

import org.bstats.bukkit.Metrics;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

import santas.spy.santascrafting.helpers.Config;
import santas.spy.santascrafting.listeners.CommandListener;
import santas.spy.santascrafting.recipes.RecipeRegister;

public class SantasCrafting extends JavaPlugin
{
    public static SantasCrafting PLUGIN;
    private static List<NamespacedKey> recipeList;

    @Override
    public void onEnable()
    {
        this.getLogger().info("Starting SantasCrafting");
        PLUGIN = this;
        this.getCommand("santascrafting").setExecutor(new CommandListener());
        Config.initialize();
        registerRecipes();
        getbstats();
    }

    @Override
    public void onDisable()
    {
        getLogger().info("Shutting down SantasCrafting");
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

    private void getbstats()
    {
        int pluginID = 15796;
        Metrics metrics = new Metrics(this, pluginID);
    }

    public static SantasCrafting getInstance()
    {
        return PLUGIN;
    }
}
