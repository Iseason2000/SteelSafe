package top.iseason.steelSafe;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.containers.Flags;
import com.bekvon.bukkit.residence.protection.FlagPermissions;
import com.plotsquared.core.api.PlotAPI;
import com.plotsquared.core.player.PlotPlayer;
import com.plotsquared.core.plot.Plot;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;


import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.UUID;


public class Dependency {
    protected static WorldGuardPlugin worldguard = null;
    protected static Residence residence = null;
    protected static Plugin plotsquared = null;
    protected static PlotAPI plotAPI = null;

    public Dependency(Plugin plugin) {
        Plugin worldguardplugin = plugin.getServer().getPluginManager().getPlugin("WorldGuard");
        if (worldguardplugin instanceof WorldGuardPlugin) {
            worldguard = (WorldGuardPlugin) worldguardplugin;
        } else {
            worldguard = null;
        }
        Plugin residenceplugin = plugin.getServer().getPluginManager().getPlugin("Residence");
        if (residenceplugin instanceof Residence) {
            residence = (Residence) residenceplugin;
        } else {
            residence = null;
        }
        plotsquared = plugin.getServer().getPluginManager().getPlugin("PlotSquared");
        if (plotsquared != null) {
            plotAPI = new PlotAPI();
        }
    }

    public static boolean pluginCheck(Block block, Player player) {
        if (residence != null) {
            Location loc = block.getLocation();
            FlagPermissions res = Residence.getInstance().getPermsByLoc(loc);
            if (res != null) {
                if (!res.playerHas(player, Flags.valueOf("container"), true)) {
                    return false;
                }
            }
        }

        if (plotAPI != null) {
            PlotPlayer plotPlayer = plotAPI.wrapPlayer(player.getName());
            Plot plot = plotPlayer.getCurrentPlot();
            if (plot != null) {
                UUID uuid = plotPlayer.getUUID();
                if (!(plot.isOwner(uuid) || plot.getTrusted().contains(uuid) || plot.getMembers().contains(uuid))) {
                    return false;
                }
            }
        }
        if (worldguard != null) {
            if (!worldguard.createProtectionQuery().testBlockPlace(player, block.getLocation(), block.getType())) {
                return false;
            }
        }
        return true;
    }
}
