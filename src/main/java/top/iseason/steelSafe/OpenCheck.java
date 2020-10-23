package top.iseason.steelSafe;

import org.bukkit.ChatColor;
import org.bukkit.block.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.block.data.type.Chest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import static org.bukkit.Material.CHEST;
import static org.bukkit.Material.TRAPPED_CHEST;
import static org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK;
import static top.iseason.steelSafe.OpenWithKey.isLocked;


public class OpenCheck implements Listener {
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void openChestCheck(PlayerInteractEvent e) { //todo：在打开的瞬间移开可能会绕过取消事件，待解决。
        if (e.getAction() == RIGHT_CLICK_BLOCK && ((e.getClickedBlock().getType() == CHEST) || (e.getClickedBlock().getType() == TRAPPED_CHEST))) {
            FileConfiguration steelSafeList = Main.getSteelSafesList();
            Block targetBlock = e.getClickedBlock();
            if (targetBlock.getType() != CHEST && targetBlock.getType() != TRAPPED_CHEST) {
                e.getPlayer().closeInventory();
                e.setCancelled(true);
                return;
            }
            BlockFace chestface = getRelativeChestFace(targetBlock);
            String World = targetBlock.getWorld().getName();
            String data1 = World + "," + targetBlock.getLocation().getBlockX() + "," + targetBlock.getLocation().getBlockY() + "," + targetBlock.getLocation().getBlockZ();
            if (chestface == null) {
                if (isLocked(targetBlock)) {
                    if (Main.getInstance().getConfig().getBoolean("OpOpen")) {
                        return;
                    }
                    e.getPlayer().closeInventory();
                    e.setCancelled(true);
                    String message = Message.replace(Main.getInstance().getConfig().getString("TryToOpen"), "%player%", steelSafeList.getString(data1.concat(".owner")));
                    e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                    return;
                }
                return;
            } else {
                Block relativechest = targetBlock.getRelative(chestface);
                if (isLocked(targetBlock)) {
                    if (Main.getInstance().getConfig().getBoolean("OpOpen")) {
                        return;
                    }
                    e.getPlayer().closeInventory();
                    e.setCancelled(true);
                    String message = Message.replace(Main.getInstance().getConfig().getString("TryToOpen"), "%player%", steelSafeList.getString(data1.concat(".owner")));
                    e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                    return;
                } else if (isLocked(relativechest)) {
                    if (Main.getInstance().getConfig().getBoolean("OpOpen")) {
                        return;
                    }
                    e.getPlayer().closeInventory();
                    e.setCancelled(true);
                    String data2 = World + "," + relativechest.getLocation().getBlockX() + "," + relativechest.getLocation().getBlockY() + "," + relativechest.getLocation().getBlockZ();
                    String message = Message.replace(Main.getInstance().getConfig().getString("TryToOpen"), "%player%", steelSafeList.getString(data2.concat(".owner")));
                    e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                    return;
                }
            }
        }
    }

    public static BlockFace getRelativeChestFace(Block block) {
        Chest chest = (Chest) block.getBlockData();
        BlockFace face = getFacing(block);
        BlockFace relativeFace = null;
        if (chest.getType() == Chest.Type.LEFT) {
            if (face == BlockFace.NORTH) {
                relativeFace = BlockFace.EAST;
            } else if (face == BlockFace.SOUTH) {
                relativeFace = BlockFace.WEST;
            } else if (face == BlockFace.WEST) {
                relativeFace = BlockFace.NORTH;
            } else if (face == BlockFace.EAST) {
                relativeFace = BlockFace.SOUTH;
            }
        } else if (chest.getType() == Chest.Type.RIGHT) {
            if (face == BlockFace.NORTH) {
                relativeFace = BlockFace.WEST;
            } else if (face == BlockFace.SOUTH) {
                relativeFace = BlockFace.EAST;
            } else if (face == BlockFace.WEST) {
                relativeFace = BlockFace.SOUTH;
            } else if (face == BlockFace.EAST) {
                relativeFace = BlockFace.NORTH;
            }
        }
        return relativeFace;
    }

    public static BlockFace getFacing(Block block) {
        BlockData data = block.getBlockData();
        BlockFace f = null;
        if (data instanceof Directional && data instanceof Waterlogged && ((Waterlogged) data).isWaterlogged()) {
            String str = ((Directional) data).toString();
            if (str.contains("facing=west")) {
                f = BlockFace.WEST;
            } else if (str.contains("facing=east")) {
                f = BlockFace.EAST;
            } else if (str.contains("facing=south")) {
                f = BlockFace.SOUTH;
            } else if (str.contains("facing=north")) {
                f = BlockFace.NORTH;
            }
        } else if (data instanceof Directional) {
            f = ((Directional) data).getFacing();
        }
        return f;
    }
}
