package top.iseason.steelSafe;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;


public class steelsafereload implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.isOp() && !sender.hasPermission("steelsafe.admin")) {
            sender.sendMessage("你没有权限使用这个命令");
            return true;
        }
        Main.getInstance().reloadConfig();
        FileConfiguration config = Main.getInstance().getConfig();
        sender.sendMessage(ChatColor.GREEN + "[SteelSafe]" + ChatColor.YELLOW + " 重载成功！");
        sender.sendMessage(ChatColor.GRAY + "MaxChest: " + ChatColor.YELLOW + config.getInt("MaxChest"));
        sender.sendMessage(ChatColor.GRAY + "OpChests: " + ChatColor.YELLOW + config.getBoolean("OpChests"));
        sender.sendMessage(ChatColor.GRAY + "Hopper: " + ChatColor.YELLOW + config.getBoolean("Hopper"));
        sender.sendMessage(ChatColor.GRAY + "EntityExplode: " + ChatColor.YELLOW + config.getBoolean("EntityExplode"));
        sender.sendMessage(ChatColor.GRAY + "BlockExplode: " + ChatColor.YELLOW + config.getBoolean("BlockExplode"));
        sender.sendMessage(ChatColor.GRAY + "WitherDestroy: " + ChatColor.YELLOW + config.getBoolean("WitherDestroy"));
        sender.sendMessage(ChatColor.GRAY + "EnderDragonDestroy: " + ChatColor.YELLOW + config.getBoolean("EnderDragonDestroy"));
        sender.sendMessage(ChatColor.GRAY + "OpOpen: " + ChatColor.YELLOW + config.getBoolean("OpOpen"));
        Main.getInstance().saveDefaultConfig();

        return true;

    }
}
