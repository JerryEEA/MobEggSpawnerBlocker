package me.sothatsit.mobeggspawnerblocker;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class MobEggSpawnerBlocker extends JavaPlugin implements Listener
{
    
    private String message;
    private boolean blockCreative;
    private boolean logAttempts;
    private int configVersion;
    
    @Override
    public void onEnable()
    {
        Bukkit.getPluginManager().registerEvents(this, this);
        
        reloadConfiguration();
    }
    
    public void reloadConfiguration() {
        this.saveDefaultConfig();
        this.reloadConfig();
        
        // 檢查配置版本
        this.configVersion = getConfig().getInt("config-version", 1);
        if (configVersion < 2) {
            getLogger().info("Updating config to version 2...");
            getConfig().set("config-version", 2);
            getConfig().set("log-attempts", true);
            saveConfig();
        }
        
        if (!getConfig().isSet("message") || !getConfig().isString("message")) {
            getLogger().warning("\"message\" not set or invalid in config, resetting to default");
            getConfig().set("message", "&cChanging spawners using mob eggs is disabled on this server");
            saveConfig();
        }
        
        if (!getConfig().isSet("block-creative") || !getConfig().isBoolean("block-creative")) {
            getLogger().warning("\"block-creative\" not set or invalid in config, resetting to default");
            getConfig().set("block-creative", false);
            saveConfig();
        }
        
        if (!getConfig().isSet("log-attempts") || !getConfig().isBoolean("log-attempts")) {
            getLogger().warning("\"log-attempts\" not set or invalid in config, resetting to default");
            getConfig().set("log-attempts", true);
            saveConfig();
        }
        
        this.message = getConfig().getString("message");
        this.blockCreative = getConfig().getBoolean("block-creative");
        this.logAttempts = getConfig().getBoolean("log-attempts");
        
        getLogger().info("MobEggSpawnerBlocker v2.0 loaded - Supporting Minecraft 1.21.8 and all spawn eggs!");
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e)
    {
        if (!blockCreative && e.getPlayer().getGameMode() == GameMode.CREATIVE)
            return;
        
        if (e.getPlayer().isOp() || e.getPlayer().hasPermission("mobeggspawnerblocker.override"))
            return;
        
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK && e.getAction() != Action.LEFT_CLICK_BLOCK)
            return;
        
        Block b = e.getClickedBlock();
        
        if (b == null)
            return;
        
        if (b.getType() != Material.MOB_SPAWNER)
            return;
        
        ItemStack i = e.getItem();
        
        if (i == null)
            return;
        
        if (!isSpawnEgg(i))
            return;
        
        CreatureSpawner cs = (CreatureSpawner) b.getState();
        
        final Location loc = cs.getLocation();
        final EntityType type = cs.getSpawnedType();
        
        // 記錄被阻止的嘗試
        if (logAttempts) {
            String playerName = e.getPlayer().getName();
            String itemType = i.getType().name();
            String spawnerLocation = String.format("(%d, %d, %d)", 
                loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
            getLogger().info(String.format("Blocked %s from changing spawner at %s using %s", 
                playerName, spawnerLocation, itemType));
        }
        
        if (message != null && !message.isEmpty())
            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        
        e.setCancelled(true);
        
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable()
        {
            @Override
            public void run()
            {
                Block b = loc.getBlock();
                
                if (b == null || b.getType() != Material.MOB_SPAWNER)
                    return;
                
                CreatureSpawner cs = (CreatureSpawner) b.getState();
                
                cs.setSpawnedType(type);
            }
        });
    }
    
    /**
     * 檢查物品是否為生怪蛋
     * 支援 1.21.8 之前的所有生物生怪蛋
     */
    private boolean isSpawnEgg(ItemStack item) {
        if (item == null || item.getType() == null) {
            return false;
        }
        
        Material type = item.getType();
        String typeName = type.name();
        
        // 檢查是否為任何類型的生怪蛋（新版本）
        if (typeName.endsWith("_SPAWN_EGG")) {
            return true;
        }
        
        // 檢查舊版本的生怪蛋材料
        try {
            if (type == Material.LEGACY_MONSTER_EGG || type == Material.LEGACY_MONSTER_EGGS) {
                return true;
            }
        } catch (NoSuchFieldError e) {
            // 在較新版本中，LEGACY 材料可能不存在
        }
        
        // 額外檢查：如果物品有 SpawnEggMeta，那它就是生怪蛋
        try {
            return item.getItemMeta() instanceof SpawnEggMeta;
        } catch (Exception e) {
            // 如果出現異常，回退到基本檢查
            return false;
        }
    }
    
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("mobeggspawnerblocker")) {
            if (!sender.isOp() && !sender.hasPermission("mobeggspawnerblocker.reload")) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4Error > &cYou do not have permission to run this command."));
                return true;
            }
            
            reloadConfiguration();
            sender.sendMessage(ChatColor.GREEN + "MobEggSpawnerBlocker config reloaded");
            
            return true;
        }
        
        return false;
    }
}