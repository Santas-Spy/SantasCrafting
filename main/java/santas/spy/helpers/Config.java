package santas.spy.helpers;

import java.io.File;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import santas.spy.SantasCrafting;
import santas.spy.recipes.RecipeRegister;

public class Config 
{
    private static SantasCrafting plugin = SantasCrafting.PLUGIN;
    static String itemFile = "items.yml";
    private static FileConfiguration config;

    public static FileConfiguration getConfig()
    {
        return config;
    }

    public static int getDebug()
    {
        return config.getInt("debug-level");
    }

    public static Set<String> getCustomItemKeys()
    {
        return YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), itemFile)).getKeys(false);
    }
    
    public static Set<String> getKeys(String path)
    {
        return config.getConfigurationSection(path).getKeys(false);
    }

    /**
     * Goes to location of path and checks if it contains the word "CUSTOM"
     * @param path The location to lookup
     **/
    public static boolean isCustom(String path)
    {
        return config.getString(path).contains("CUSTOM");
    }

    public static String getCustomName(String path)
    {
        return config.getString(path).split(":")[1];
    }

    public static Material getMaterial(String path)
    {
        return Material.getMaterial(config.getString(path).toUpperCase());
    }

    public static void initialize()
    {
        plugin.getDataFolder();
        plugin.saveResource(itemFile, false);
    }

    public static void reload()
    {
        RecipeRegister.deregisterAll();
        RecipeRegister.registerAll();
    }

    public static FileConfiguration getCustomItemConfig()
    {
        return YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), itemFile));
    }
}
