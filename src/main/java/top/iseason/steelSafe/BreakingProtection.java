package top.iseason.steelSafe;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Wither;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.world.StructureGrowEvent;

import java.util.Iterator;

import static org.bukkit.Material.CHEST;
import static org.bukkit.Material.TRAPPED_CHEST;
import static top.iseason.steelSafe.OpenWithKey.isLocked;

public class BreakingProtection implements Listener {
    @EventHandler
    public void breakEvent(BlockBreakEvent e) {
        if (e.isCancelled()) {
            return;
        }
        Block targetBlock = e.getBlock();
        if (targetBlock.getType() != CHEST && targetBlock.getType() != TRAPPED_CHEST) {
            return;
        }
        BlockFace chestface = OpenCheck.getRelativeChestFace(targetBlock);
        if (chestface != null) {
            Block relativechest = targetBlock.getRelative(chestface);
            if (isLocked(targetBlock) && ((targetBlock.getType() == CHEST) || (targetBlock.getType() == TRAPPED_CHEST))) {
                e.getPlayer().sendMessage(ChatColor.RED + "这个箱子受到了保护，请先解锁再尝试破坏！");
                e.setCancelled(true);
            } else if (isLocked(relativechest) && ((relativechest.getType() == CHEST) || (relativechest.getType() == TRAPPED_CHEST))) {
                e.getPlayer().sendMessage(ChatColor.RED + "这个箱子受到了保护，请先解锁再尝试破坏！");
                e.setCancelled(true);
            }
        } else {
            if (isLocked(targetBlock) && ((targetBlock.getType() == CHEST) || (targetBlock.getType() == TRAPPED_CHEST))) {
                e.getPlayer().sendMessage(ChatColor.RED + "这个箱子受到了保护，请先解锁再尝试破坏！");
                e.setCancelled(true);
            }

        }

    }

    @EventHandler
    public void entityExplodeEvent(EntityExplodeEvent e) {
        if (e.isCancelled()) {
            return;
        }
        if (!Main.getInstance().getConfig().getBoolean("EntityExplode")) {
            return;
        }
        Iterator<Block> it = e.blockList().iterator();
        preventExplode(it);
    }


    @EventHandler
    public void blockExplodeEvent(BlockExplodeEvent e) {
        if (e.isCancelled()) {
            return;
        }
        if (!Main.getInstance().getConfig().getBoolean("BlockExplode")) {
            return;
        }
        Iterator<Block> it = e.blockList().iterator();
        preventExplode(it);
    }

    @EventHandler
    public void structureGrowEvent(StructureGrowEvent e) {
        if (e.isCancelled()) {
            return;
        }
        for (BlockState blockstate : e.getBlocks()) {
            Block block = blockstate.getBlock();
            if (block.getType() == CHEST || block.getType() == TRAPPED_CHEST) {
                BlockFace chestface = OpenCheck.getRelativeChestFace(block);
                if (chestface == null) {
                    if ((block.getType() == CHEST || block.getType() == TRAPPED_CHEST) & isLocked(block)) {
                        e.setCancelled(true);
                    }
                } else {
                    Block relativechest = block.getRelative(chestface);
                    if ((block.getType() == CHEST || block.getType() == TRAPPED_CHEST) & isLocked(block)) {
                        e.setCancelled(true);
                    } else if ((relativechest.getType() == CHEST || relativechest.getType() == TRAPPED_CHEST) & isLocked(relativechest)) {
                        e.setCancelled(true);
                    }
                }
            }

        }
    }

    @EventHandler
    public void mobChangeBlock(EntityChangeBlockEvent e) {
        if (e.isCancelled()) {
            return;
        }
        FileConfiguration config = Main.getInstance().getConfig();
        if (!config.getBoolean("WitherDestroy")) {
            if (e.getEntity() instanceof Wither) {//凋零破坏事件
                mobDestroyCancel(e);
            }
        }
        if (!config.getBoolean("EnderDragonDestroy")) {
            if (e.getEntity() instanceof EnderDragon) {//末影龙破坏事件
                mobDestroyCancel(e);
            }
        }

    }

    private void preventExplode(Iterator<Block> it) {
        while (it.hasNext()) {
            Block block = it.next();
            if (block.getType() == CHEST || block.getType() == TRAPPED_CHEST) {
                BlockFace chestface = OpenCheck.getRelativeChestFace(block);
                if (chestface == null) {
                    if (isLocked(block)) {
                        it.remove();
                    }
                } else {
                    Block relativechest = block.getRelative(chestface);
                    if (isLocked(relativechest) || isLocked(block)) {
                        it.remove();
                    }
                }
            }
        }
    }

    private void mobDestroyCancel(EntityChangeBlockEvent e) {
        Block block = e.getBlock();
        if (block.getType() == CHEST || block.getType() == TRAPPED_CHEST) {
            BlockFace chestface = OpenCheck.getRelativeChestFace(block);
            if (chestface == null) {
                if (isLocked(block)) {
                    e.setCancelled(true);
                }
            } else {
                Block relativechest = block.getRelative(chestface);
                if (isLocked(relativechest)) {
                    e.setCancelled(true);
                }
            }
        }
    }
}


