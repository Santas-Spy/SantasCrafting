package santas.spy.santascrafting.listeners;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import santas.spy.santascrafting.helpers.Config;
import santas.spy.santascrafting.helpers.ItemValidater;

public class CommandListener implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) 
    {
        if (args.length == 0) {
            help(sender);
        } else {
            switch (args[0])
            {
                case "reload":
                    if (checkPerms("santascrafting.reload", sender)) {
                        sender.sendMessage("Reloading Config");
                        Config.reload();
                    } else {
                        sender.sendMessage("You do not have permission to use this command");
                    }
                    break;
                case "give":
                    if (checkPerms("santascrafting.give", sender)) {
                        Player player = Bukkit.getPlayer(args[1]);
                        if (player == null) {
                            sender.sendMessage("Could not find player " + args[1]);
                        } else {
                            if (ItemValidater.isNameOfCustomItem(args[2])) {
                                ItemStack item = ItemValidater.giveCustomItem(args[2]);
                                if (args.length > 3) {
                                    try {
                                        item.setAmount(Integer.parseInt(args[3]));
                                        player.sendMessage("You have been given " + args[2] + " x" + args[3]);
                                        player.getInventory().addItem(item);
                                    } catch (NumberFormatException e) {
                                        sender.sendMessage(args[3] + " is not a valid number");
                                    }
                                } else {
                                    player.sendMessage("You have been given " + args[2] + " x" + args[3]);
                                    player.getInventory().addItem(item);
                                }
                            } else {
                                sender.sendMessage(args[2] + " is not a valid item");
                            }
                        }
                    } else {
                        sender.sendMessage("You do not have permission to use this command");
                    }
                    break;
                case "list":
                    if (checkPerms("santascrafting.list", sender)) {
                        for (String name : Config.getCustomItemKeys()) {
                            sender.sendMessage(name);
                        }
                    } else {
                        sender.sendMessage("You do not have permission to use this command");
                    }
                    break;
                case "help":
                    help(sender);
                    break;
                default:
                    sender.sendMessage("Unknown Command");
            }
        }
        return false;
    }

    private boolean checkPerms(String perm, CommandSender sender)
    {
        boolean hasPerm = true;
        if (sender instanceof Player) {
            if (!((Player)sender).hasPermission(perm)) {
                hasPerm = false;
            }
        }

        return hasPerm;
    }

    private void help(CommandSender sender)
    {
        sender.sendMessage("/santascrafting help: Displays this list");
        sender.sendMessage("/santascrafting reload: Reload the plugin");
        sender.sendMessage("/santascrafting give <player> <item> [count]: Gives you the custom item under that name");
        sender.sendMessage("/santascrafting list: Lists the names of all custom items");
    }
}
