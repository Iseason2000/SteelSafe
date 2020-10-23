package top.iseason.steelSafe;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.DoubleChest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import static org.bukkit.Material.CHEST;
import static org.bukkit.Material.TRAPPED_CHEST;
import static top.iseason.steelSafe.OpenWithKey.isLocked;

public class MoveItemEvent implements Listener {
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onInventoryMove(InventoryMoveItemEvent event) {
        if(Main.getInstance().getConfig().getBoolean("Hopper")){
            return;
        }
        if (isInventoryLocked(event.getSource())) {
            event.setCancelled(true);
        }

    }

    public boolean isInventoryLocked(Inventory inventory) {
        InventoryHolder inventoryholder = inventory.getHolder();
        if (inventoryholder instanceof DoubleChest) {
            inventoryholder = ((DoubleChest) inventoryholder).getLeftSide();
        }
        if (inventoryholder instanceof BlockState) {
            Block block = ((BlockState) inventoryholder).getBlock();
            if (block.getType() != CHEST && block.getType() != TRAPPED_CHEST) {
                return false;
            }
            BlockFace chestface = OpenCheck.getRelativeChestFace(block);
            if (chestface == null) {
                return isLocked(block);
            } else {
                if (isLocked(block)) {
                    return true;
                } else {
                    Block relativechest = block.getRelative(chestface);
                    return isLocked(relativechest);
                }

            }
        }
        return false;
    }

}
