package top.iseason.steelSafe;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import static org.bukkit.Material.CHEST;
import static org.bukkit.Material.TRAPPED_CHEST;

import java.io.IOException;

public class OpenWithKey implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("只有玩家才能使用这个命令!");
            return false;
        }
        if (args.length != 1) {
            return false;
        }
        Block targetBlock = CreateCommand.getLookingAtBlock((Player) sender);
        if (targetBlock.getType() != CHEST && targetBlock.getType() != TRAPPED_CHEST) {
            String message = Main.getInstance().getConfig().getString("NotChest");
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',message));
            return true;
        }
        if (!Dependency.pluginCheck(targetBlock, ((Player) sender).getPlayer())) {
            String message = Main.getInstance().getConfig().getString("HaveNoPermission");
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',message));

            return true;
        }
        BlockFace chestface = OpenCheck.getRelativeChestFace(targetBlock);
        if (!isLocked(targetBlock)) {
            String unLocked = ChatColor.translateAlternateColorCodes('&',Main.getInstance().getConfig().getString("UnLocked"));

            if (chestface == null) {
                sender.sendMessage(unLocked);
                return true;
            }
            Block relative = targetBlock.getRelative(chestface);
            if (!isLocked(relative)) {
                sender.sendMessage(unLocked);
                return true;
            } else {
                if (tryWithKey(relative, args[0])) {
                    return removeChestWithCommand(sender, relative);
                } else {
                    String message = Main.getInstance().getConfig().getString("PassWordError");
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',message));
                    return true;
                }
            }
        } else {
            if (tryWithKey(targetBlock, args[0])) {
                return removeChestWithCommand(sender, targetBlock);
            } else {
                String message = Main.getInstance().getConfig().getString("PassWordError");
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',message));
                return true;
            }
        }

    }

    public static boolean removeChestWithCommand(CommandSender sender, Block targetBlock) {
        String key = targetBlock.getWorld().getName() + "," + targetBlock.getLocation().getBlockX() + "," + targetBlock.getLocation().getBlockY() + "," + targetBlock.getLocation().getBlockZ();
        if (!removeChest(sender.getName(), targetBlock, key)) {
            sender.sendMessage(ChatColor.RED + "删除操作错误！请通知管理员。");
            return true;
        }
        String message = Main.getInstance().getConfig().getString("PassWordCorrect");
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',message));
        return true;
    }

    public static boolean tryWithKey(Block chest, String key) {
        String World = chest.getWorld().getName();
        String data1 = World + "," + chest.getLocation().getBlockX() + "," + chest.getLocation().getBlockY() + "," + chest.getLocation().getBlockZ();
        FileConfiguration steelSafeList = Main.getSteelSafesList();
        if (!steelSafeList.contains(data1)) {
            return false;
        } else return steelSafeList.getString(data1.concat(".key")).equals(key);
    }

    private static boolean removeChest(String name, Block chest, String key) {
        FileConfiguration steelSafeList = Main.getSteelSafesList();
        FileConfiguration ownerList = Main.getOwnerList();
        String blockOwner = steelSafeList.getString(key.concat(".owner"));
        if (!name.equals(blockOwner)) {
            name = blockOwner;
        }
        steelSafeList.set(key, null);
        if (ownerList.getInt(name.concat(".num")) <= 1) {
            ownerList.set(name, null);
        } else {
            int n = ownerList.getInt(name.concat(".num"));
            ownerList.set(name + ".chests." + n, null);
            ownerList.set(name.concat(".num"), --n);
        }
        try {
            ownerList.save(Main.getOwnerListFile());
            steelSafeList.save(Main.getSteelSafesListFile());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean isLocked(Block block) {
        String key = block.getWorld().getName() + "," + block.getLocation().getBlockX() + "," + block.getLocation().getBlockY() + "," + block.getLocation().getBlockZ();
        FileConfiguration steelSafeList = Main.getSteelSafesList();
        return steelSafeList.contains(key);

    }
}
