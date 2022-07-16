package santas.spy.listeners;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import santas.spy.helpers.Config;
import santas.spy.helpers.ItemValidater;

public class CommandListener implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) 
    {
        if (args.length == 0) {
            sender.sendMessage("/santascrafting reload: Reload the plugin");
            sender.sendMessage("/santascrafting give <item> [count]: Gives you the custom item under that name");
            sender.sendMessage("/santascrafting list: Lists the names of all custom items");
        } else {
            switch (args[0])
            {
                case "reload":
                    if (sender instanceof Player)
                    {
                        Player player = (Player)sender;
                        if (player.hasPermission("santascrafting.reload"))
                        {
                            sender.sendMessage("reloading config");
                            Config.reload();
                        } else {
                            player.sendMessage("You do not have permission to use this command");
                        }
                    } else {
                        sender.sendMessage("reloading config");
                        Config.reload();
                    }
                    break;
                case "give":
                    if (sender instanceof Player)
                    {
                        Player player = (Player)sender;
                        if (player.hasPermission("santascrafting.give"))
                        {
                            if (ItemValidater.isNameOfCustomItem(args[1]))
                            {
                                ItemStack item = ItemValidater.giveCustomItem(args[1]);
                                if (args.length > 2) {
                                    try {
                                        item.setAmount(Integer.parseInt(args[2]));
                                    } catch (NumberFormatException e) {
                                        sender.sendMessage(args[2] + " is not a valid number");
                                    }
                                }
                                sender.sendMessage("You have been given " + args[1]);
                                player.getInventory().addItem(item);
                            } else {
                                player.sendMessage("[SantasCrafting] " + args[1] + " is not a valid item");
                            }
                        } else {
                            player.sendMessage("You do not have permission to use this command");
                        }
                    }
                    break;
                case "list":
                    for (String name : Config.getCustomItemKeys()) {
                        sender.sendMessage(name);
                    }
                    break;
                default:
                    sender.sendMessage("Unknown Command");
            }
        }
        return false;
    }
}
