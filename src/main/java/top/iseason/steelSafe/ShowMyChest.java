package top.iseason.steelSafe;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Objects;

public class ShowMyChest implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String playerName = sender.getName();
        FileConfiguration ownerList = Main.getOwnerList();
        if (!ownerList.contains(playerName)) {
            sender.sendMessage(ChatColor.YELLOW + "你还没有保险箱,使用" + ChatColor.GREEN + "/ss create" + ChatColor.YELLOW + "来创建一个");
            return true;
        }
        sender.sendMessage(ChatColor.GREEN + "你的所有保险箱如下，一共" + ChatColor.GOLD + ownerList.getInt(playerName.concat(".num")) + ChatColor.GREEN + "个保险箱");
        sender.sendMessage(ChatColor.GREEN + "箱子所在世界," + ChatColor.YELLOW + "箱子坐标x,y,z:" + ChatColor.GOLD + "箱子密码");
        sender.sendMessage("↓↓↓");
        int n = 1;
        while (ownerList.contains(playerName.concat(".chests.") + n)) {
            sender.sendMessage(ChatColor.GRAY + "-" + Objects.requireNonNull(ownerList.getString(playerName.concat(".chests.") + n)));
            n++;
        }
        return true;
    }
}