package me.sothatsit.mobeggspawnerblocker;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.logging.Logger;

public class MobEggSpawnerBlocker extends JavaPlugin {
    
    private String message;
    private boolean blockCreative;
    private boolean logAttempts;
    private int configVersion;
    private Logger logger;
    
    @Override
    public void onEnable() {
        this.logger = getLogger();
        
        // 註冊事件監聽器
        try {
            Bukkit.getPluginManager().registerEvents(new SpawnerListener(this), this);
            logger.info("事件監聽器已註冊");
        } catch (Exception e) {
            logger.warning("註冊事件監聽器時發生錯誤: " + e.getMessage());
        }
        
        reloadConfiguration();
        logger.info("MobEggSpawnerBlocker v2.0 已啟用 - 支援 Minecraft 1.21.7 及多版本！");
    }
    
    @Override
    public void onDisable() {
        logger.info("MobEggSpawnerBlocker v2.0 已停用");
    }
    
    public void reloadConfiguration() {
        try {
            this.saveDefaultConfig();
            this.reloadConfig();
            
            // 檢查配置版本
            this.configVersion = getConfig().getInt("config-version", 1);
            if (configVersion < 2) {
                logger.info("正在更新配置到版本 2...");
                getConfig().set("config-version", 2);
                getConfig().set("log-attempts", true);
                saveConfig();
            }
            
            if (!getConfig().isSet("message") || !getConfig().isString("message")) {
                logger.warning("配置中的 \"message\" 未設置或無效，重置為預設值");
                getConfig().set("message", "&c在此伺服器上禁止使用生怪蛋更改生怪籠");
                saveConfig();
            }
            
            if (!getConfig().isSet("block-creative") || !getConfig().isBoolean("block-creative")) {
                logger.warning("配置中的 \"block-creative\" 未設置或無效，重置為預設值");
                getConfig().set("block-creative", false);
                saveConfig();
            }
            
            if (!getConfig().isSet("log-attempts") || !getConfig().isBoolean("log-attempts")) {
                logger.warning("配置中的 \"log-attempts\" 未設置或無效，重置為預設值");
                getConfig().set("log-attempts", true);
                saveConfig();
            }
            
            this.message = getConfig().getString("message");
            this.blockCreative = getConfig().getBoolean("block-creative");
            this.logAttempts = getConfig().getBoolean("log-attempts");
            
            logger.info("MobEggSpawnerBlocker v2.0 配置已載入 - 支援 Minecraft 1.21.7 及多版本！");
            logger.info("配置: 阻止創造模式=" + blockCreative + ", 記錄嘗試=" + logAttempts);
            
        } catch (Exception e) {
            logger.severe("載入配置時發生錯誤: " + e.getMessage());
        }
    }
    
    // 提供給監聽器使用的方法
    public String getMessage() {
        return message;
    }
    
    public boolean shouldBlockCreative() {
        return blockCreative;
    }
    
    public boolean shouldLogAttempts() {
        return logAttempts;
    }
    
    // 命令處理
    public boolean onCommand(Object sender, Object command, String label, String[] args) {
        if (label.equalsIgnoreCase("mobeggspawnerblocker")) {
            try {
                reloadConfiguration();
                logger.info("插件配置已重載");
                return true;
            } catch (Exception e) {
                logger.severe("重載配置時發生錯誤: " + e.getMessage());
                return false;
            }
        }
        return false;
    }
}