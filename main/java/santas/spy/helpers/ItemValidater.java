package santas.spy.helpers;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import santas.spy.SantasCrafting;

public class ItemValidater
{

    /**
     * Check if the supplied ItemStack is a valid fuel for miners
     **/
    public static boolean isFuel(ItemStack fuel)
    {
        boolean isFuel = false;

        if (fuel != null)
        {
            //get a list of all the fuel sources keyNames
            Set<String> keys = Config.getKeys("fuel-sources");

            //iterate through the list
            for (String curr : keys)
            {
                if (!isFuel)
                {
                    String path = "fuel-sources." + curr + ".type";

                    //check each type to see if it contains the word "CUSTOM"
                    if (Config.isCustom(path))
                    {
                        //Strip the name and see if this ItemStack matches the ItemStack that keyName maps to
                        String fuelName = Config.getCustomName(path);
                        if (checkCustomItem(fuel, fuelName))
                        {
                            isFuel = true;
                        }

                    } else {
                        //if it was not a custom item, check to see if the type matches
                        if (Config.getMaterial(path).equals(fuel.getType()))
                        {
                            isFuel = true;
                        }
                    }
                }
            }
        }
        return isFuel;
    }

    /**
     * Check if the supplied ItemStack is an instance of the custom item "itemName"
     * @param inItem The item stack in question
     * @param itemName The name of the custom in config
     * @return True if the item is an instance of the item stored in config
     **/
    public static boolean checkCustomItem(ItemStack inItem, String itemName)
    {
        boolean match = false;
        ItemStack item = inItem.clone();
        item.setAmount(1);
        Set<String> items = Config.getCustomItemKeys();

        for (String curr : items)
        {
            if (itemName.equals(curr))
            {
                if (item.equals(giveCustomItem(itemName)))
                {
                    match = true;
                }
            }
        }
        return match;
    }

    public static ItemStack giveCustomItem(String itemName)
    {
        FileConfiguration config = Config.getCustomItemConfig();

        //get Material
        String searchKey = itemName + ".type";
        Material itemType = Material.getMaterial(config.getString(searchKey).toUpperCase());
        if (itemType == null)
        {
            itemType = Material.STONE;
        }
        ItemStack item = new ItemStack(itemType);
        ItemMeta itemMeta = item.getItemMeta();

        //get Enchantments
        searchKey = itemName + ".enchantments";
        ConfigurationSection enchantments = config.getConfigurationSection(searchKey);
        Set<String> keys = enchantments.getKeys(false);
        for (String cur : keys)
        {
            itemMeta.addEnchant(Enchantment.getByKey(NamespacedKey.minecraft(enchantments.getString(cur + ".type"))), enchantments.getInt(cur + ".level"), config.getBoolean("allow-op-enchants"));
        }

        //get Name
        searchKey = itemName + ".name";
        itemMeta.setDisplayName(config.getString(searchKey));

        //get Lore
        searchKey = itemName + ".lore";
        keys = config.getConfigurationSection(searchKey).getKeys(false);
        List<String> lore = (List<String>)(new LinkedList<String>());
        for (String cur : keys)
        {
            lore.add(config.getString(searchKey + "." + cur));
        }
        itemMeta.setLore(lore);

        //get Item Flags
        searchKey = itemName + ".flags";
        List<String> flags = config.getStringList(searchKey);
        for (String cur : flags)
        {
            itemMeta.addItemFlags(ItemFlag.valueOf(cur.toUpperCase()));
        }

        //build meta and ship item
        item.setItemMeta(itemMeta);
        return item;
    }

    public static boolean isNameOfCustomItem(String name)
    {
        return Config.getCustomItemKeys().contains(name);
    }

    public static boolean hasRecipe(String name)
    {
        return (Config.getCustomItemConfig().getConfigurationSection(name + ".recipe") != null);
    }

    /**
     * Check if the supplied item is a custom item defined in config and will return the adress if found
     * @return The address of the custom item if it was found or null if nothing was found
     **/
    public static String isCustomItem(ItemStack item)
    {
        boolean match = false;
        String location = null;
        Set<String> keys = Config.getCustomItemKeys();
        ItemStack testItem = item.clone();
        testItem.setAmount(1);
        
        SantasCrafting.PLUGIN.debugMessage("Checking if " + item.getType().toString() + " is a custom item", 2);
        for (String key : keys)
        {
            if (!match)
            {
                if (testItem.equals(giveCustomItem(key)))
                {
                    SantasCrafting.PLUGIN.debugMessage("Determined that supplied item of type: " + item.getType().toString() + " was an instance of " + key, 2);
                    match = true;
                    location = key;
                }
            }
        }
        SantasCrafting.PLUGIN.debugMessage("Returning " + location + " from isCustomItem", 2);
        return location;
    }
}
