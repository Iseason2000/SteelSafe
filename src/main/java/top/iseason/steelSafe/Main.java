package top.iseason.steelSafe;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Main extends JavaPlugin implements Listener {
    private static Main plugin;
    private static File steelSafesListFile;
    private static YamlConfiguration steelSafesOwner;
    private static File ownerListFile;
    private static YamlConfiguration ownerList;


    public static Main getInstance() {
        return plugin;
    }


    public static FileConfiguration getSteelSafesList() {
        return steelSafesOwner;
    }

    public static File getSteelSafesListFile() {
        return steelSafesListFile;
    }

    public static FileConfiguration getOwnerList() {
        return ownerList;
    }

    public static File getOwnerListFile() {
        return ownerListFile;
    }

    public void onEnable() { //启用插件
        plugin = this;
        new Dependency(this);
        this.getLogger().info(ChatColor.AQUA + "SteelSafes is enabled!");
        saveDefaultConfig();
        this.saveResource("steelSafes.yml", false);
        this.saveResource("owners.yml", false);
        steelSafesListFile = new File(getInstance().getDataFolder(), "steelSafes.yml");
        steelSafesOwner = YamlConfiguration.loadConfiguration(steelSafesListFile);
        ownerListFile = new File(getInstance().getDataFolder(), "owners.yml");
        ownerList = YamlConfiguration.loadConfiguration(ownerListFile);
        Bukkit.getPluginManager().registerEvents(new BreakingProtection(), this);//这里类是监听器, 将当前对象注册监听器
        Bukkit.getPluginManager().registerEvents(new OpenCheck(), this);
        Bukkit.getPluginManager().registerEvents(new MoveItemEvent(), this);
        Bukkit.getPluginCommand("steelsafe").setExecutor(new CreateCommand());
        Bukkit.getPluginCommand("steelsafereremove").setExecutor(new RemoveCommand());
        Bukkit.getPluginCommand("steelsafekey").setExecutor(new OpenWithKey());
        Bukkit.getPluginCommand("steelsafereload").setExecutor(new steelsafereload());
        Bukkit.getPluginCommand("steelsafeshowkey").setExecutor(new ShowMyChest());

    }


    public void onDisable() {//注销插件
        this.saveDefaultConfig();
        this.saveResource("steelSafes.yml", false);
        plugin = null;
        steelSafesListFile = null;
        steelSafesOwner = null;
        ownerListFile = null;
        ownerList = null;
        this.getLogger().info(ChatColor.GREEN + "SteelSafes is disabled！");

    }
}