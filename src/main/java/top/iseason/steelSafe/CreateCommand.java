package top.iseason.steelSafe;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.TabExecutor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;


import org.bukkit.util.BlockIterator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.bukkit.Material.*;
import static top.iseason.steelSafe.OpenWithKey.isLocked;


public class CreateCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String Name = sender.getName();
        if (!(sender instanceof Player)) {
            sender.sendMessage("只有玩家才能使用这个命令");
            return false;
        }
        if (args[0].equalsIgnoreCase("create")) {
            if (args.length != 3) {
                return false;
            }
            if (!args[1].equals(args[2])) {
                sender.sendMessage(ChatColor.RED + "两次密码不一致！");
                return true;
            }
            Player player;
            player = Bukkit.getPlayer(Name);
            if (player == null) {
                sender.sendMessage(ChatColor.DARK_RED + "获取玩家" + Name + "失败，请确认玩家是否在线!");
                return false;
            }
            Block targetBlock = getLookingAtBlock((Player) sender);
            if (targetBlock.getType() != CHEST && targetBlock.getType() != TRAPPED_CHEST) {
                String message = Main.getInstance().getConfig().getString("NotChest");
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',message));
                return true;
            }
            if (!Dependency.pluginCheck(targetBlock, ((Player) sender).getPlayer())) {
                String Message = Main.getInstance().getConfig().getString("HaveNoPermission");
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',Message));
                return true;
            }
            String World = targetBlock.getWorld().getName();
            BlockFace chestface = OpenCheck.getRelativeChestFace(targetBlock);
            FileConfiguration steelSafeList = Main.getSteelSafesList();


            if (chestface != null) {
                Block relativechest = targetBlock.getRelative(chestface);
                String data1 = World + "," + targetBlock.getLocation().getBlockX() + "," + targetBlock.getLocation().getBlockY() + "," + targetBlock.getLocation().getBlockZ();
                if (isLocked(targetBlock) | isLocked(relativechest)) {
                    String message = Main.getInstance().getConfig().getString("CreateFailure");
                    if (steelSafeList.getString(data1) != null) {
                        String messageChange = Message.replace(message,"%chest%",data1,"%player%",steelSafeList.getString(data1.concat(".owner")));
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',messageChange));
                        sender.sendMessage(ChatColor.AQUA + "请输入" + ChatColor.GOLD + "/ssk 密码 以解锁!");
                    } else {
                        String data2 = World + "," + relativechest.getLocation().getBlockX() + "," + relativechest.getLocation().getBlockY() + "," + relativechest.getLocation().getBlockZ();
                        String messageChange = Message.replace(message,"%chest%",data2,"%player%",steelSafeList.getString(data2.concat(".owner")));
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',messageChange));
                        sender.sendMessage(ChatColor.AQUA + "请输入" + ChatColor.GOLD + "/ssk 密码 以解锁!");
                    }
                    return true;
                }
                return createWithsystem(sender, args[1], steelSafeList, data1);

            } else {
                String data1 = World + "," + targetBlock.getLocation().getBlockX() + "," + targetBlock.getLocation().getBlockY() + "," + targetBlock.getLocation().getBlockZ();
                String message = Main.getInstance().getConfig().getString("CreateFailure");
                if (isLocked(targetBlock)) {
                    String messageChange = Message.replace(message,"%chest%",data1,"%player%",steelSafeList.getString(data1.concat(".owner")));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',messageChange));
                    sender.sendMessage(ChatColor.AQUA + "请输入" + ChatColor.GOLD + "/ssk 密码 以解锁!");
                    return true;
                } else {
                    return createWithsystem(sender, args[1], steelSafeList, data1);
                }

            }
        } else if (args[0].equalsIgnoreCase("help")) {
            sender.sendMessage(ChatColor.GREEN + "============>" + ChatColor.GOLD + "steelsafe" + ChatColor.AQUA + "(密码保险箱)" + ChatColor.GREEN + "<============");
            sender.sendMessage(ChatColor.YELLOW + "/steelsafe" + ChatColor.GREEN + " 缩写[ss]" + ChatColor.GOLD + " help");
            sender.sendMessage(ChatColor.GRAY + "                --" + ChatColor.ITALIC + "显示本插件所有命令");
            sender.sendMessage(ChatColor.YELLOW + "/steelsafe" + ChatColor.GREEN + " 缩写[ss]" + ChatColor.GOLD + " create" + ChatColor.AQUA + " 密码 重复密码");
            sender.sendMessage(ChatColor.GRAY + "                --" + ChatColor.ITALIC + "对着你的箱子，创建一个只认密码的保险箱");
            sender.sendMessage(ChatColor.YELLOW + "/steelsafekey" + ChatColor.GREEN + " 缩写[ssk]" + ChatColor.AQUA + " 密码");
            sender.sendMessage(ChatColor.GRAY + "                --" + ChatColor.ITALIC + "对着上锁的保险箱，解锁该保险箱" + ChatColor.BOLD + "（需要重新上锁）");
            sender.sendMessage(ChatColor.YELLOW + "/steelsafeshowkey" + ChatColor.GREEN + " 缩写[sssk]" + ChatColor.GOLD);
            sender.sendMessage(ChatColor.GRAY + "                --" + ChatColor.ITALIC + "显示自己所有的保险箱");
            sender.sendMessage(ChatColor.YELLOW + "/steelsafereload" + ChatColor.GREEN + " 缩写[ssr]");
            sender.sendMessage(ChatColor.GRAY + "                --" + ChatColor.ITALIC + "重载插件配置" + ChatColor.BOLD + "（管理员专属）");
            sender.sendMessage(ChatColor.YELLOW + "/steelsafereremove" + ChatColor.GREEN + " 缩写[ssre]");
            sender.sendMessage(ChatColor.GRAY + "                --" + ChatColor.ITALIC + "强制删除面前的保险箱" + ChatColor.BOLD + "（管理员专属）");


            return true;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            List<String> list = new ArrayList<>(Arrays.asList("create", "help"));
            list.removeIf(s -> !s.startsWith(args[0].toLowerCase()));
            return list;
        } else {
            return null;
        }
    }

    private boolean createWithsystem(CommandSender sender, String arg, FileConfiguration steelSafeList, String data) {
        steelSafeList.createSection(data);
        String owner = data.concat(".owner");
        steelSafeList.createSection(owner);
        String key = data.concat(".key");
        steelSafeList.createSection(key);
        steelSafeList.set(owner, sender.getName());
        steelSafeList.set(key, arg);
        String ownerName = sender.getName();
        FileConfiguration ownerList = Main.getOwnerList();
        if (Main.getInstance().getConfig().getBoolean("OpChests")) {
            if (sender.isOp()) {
                if (ownerList.contains(ownerName)) {
                    int n = ownerList.getInt(ownerName.concat(".num"));
                    ownerList.set(ownerName.concat(".num"), ++n);
                    ownerList.set(ownerName.concat(".chests." + n), data + ":" + arg);
                } else {
                    ownerList.createSection(ownerName);
                    ownerList.set(ownerName.concat(".num"), 1);
                    ownerList.set(ownerName.concat(".chests.1"), data + ":" + arg);
                }
            } else {
                if (!createCheck(sender, arg, data, ownerName, ownerList)) return false;
            }
        } else {
            if (!createCheck(sender, arg, data, ownerName, ownerList)) return false;
        }
        try {
            ownerList.save(Main.getOwnerListFile());
            steelSafeList.save(Main.getSteelSafesListFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        String successMessage = Message.replace(Main.getInstance().getConfig().getString("CreateSuccess"),"%chest%",data,"%key%",arg);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',successMessage));
        return true;
    }

    private boolean createCheck(CommandSender sender, String arg, String data, String ownerName, FileConfiguration ownerList) {
        if (ownerList.contains(ownerName)) {
            int n = ownerList.getInt(ownerName.concat(".num"));
            int max = Main.getInstance().getConfig().getInt("MaxChest");

            if (n >= max) {
                sender.sendMessage(ChatColor.RED + "已达最大创建数量:" + ChatColor.YELLOW + max);
                return false;
            }
            ownerList.set(ownerName.concat(".num"), ++n);
            ownerList.set(ownerName.concat(".chests." + n), data + ":" + arg);
        } else {
            int max = Main.getInstance().getConfig().getInt("MaxChest");
            if (max == 0) {
                sender.sendMessage(ChatColor.RED + "不允许创建保险箱！");
                return false;
            }
            ownerList.createSection(ownerName);
            ownerList.set(ownerName.concat(".num"), 1);
            ownerList.set(ownerName.concat(".chests.1"), data + ":" + arg);
        }
        return true;
    }


    public static Block getLookingAtBlock(Player event) {  //获取玩家所看的方块
        BlockIterator iter = new BlockIterator(event, 5);
        Block lastBlock = iter.next();
        while (iter.hasNext()) {
            lastBlock = iter.next();
            if (lastBlock.getType() == AIR || lastBlock.isLiquid())
                continue;
            break;
        }
        return lastBlock;
    }

}

