package top.iseason.steelSafe;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.Material.CHEST;
import static org.bukkit.Material.TRAPPED_CHEST;
import static top.iseason.steelSafe.OpenWithKey.isLocked;
import static top.iseason.steelSafe.OpenWithKey.removeChestWithCommand;

public class RemoveCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.isOp() | sender.hasPermission("steelsafe.admin")) {
            Block targetBlock = ((Player) sender).getTargetBlock(null,5);
            if (targetBlock.getType() == CHEST || targetBlock.getType() == TRAPPED_CHEST) {
                if (isLocked(targetBlock)) {
                    return removeChestWithCommand(sender, targetBlock);
                } else {
                    BlockFace chestface = OpenCheck.getRelativeChestFace(targetBlock);
                    if (chestface != null) {
                        Block relativechest = targetBlock.getRelative(chestface);
                        if (relativechest.getType() == CHEST || relativechest.getType() == TRAPPED_CHEST) {
                            if (isLocked(relativechest)) {
                                return removeChestWithCommand(sender, relativechest);
                            } else {
                                sender.sendMessage(ChatColor.YELLOW + "这个箱子没有上锁！");
                                return true;
                            }
                        } else {
                            sender.sendMessage(ChatColor.YELLOW + "请对着要删除的箱子操作！");
                            return true;
                        }
                    } else {
                        sender.sendMessage(ChatColor.YELLOW + "这个箱子没有上锁！");
                        return true;
                    }
                }
            } else {
                sender.sendMessage(ChatColor.YELLOW + "请对着要删除的箱子操作！");
                return true;
            }

        } else {
            sender.sendMessage(ChatColor.RED + "你没有权限这样做");
            return true;
        }
    }

}
