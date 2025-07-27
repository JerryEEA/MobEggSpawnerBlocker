package org.bukkit.plugin.java;

import org.bukkit.plugin.Plugin;

public abstract class JavaPlugin implements Plugin {
    public void onEnable() {}
    public void onDisable() {}
    public void saveDefaultConfig() {}
    public void reloadConfig() {}
    public void saveConfig() {}
    public org.bukkit.configuration.file.FileConfiguration getConfig() {
        return new org.bukkit.configuration.file.FileConfiguration() {
            public String getString(String path) { return ""; }
            public String getString(String path, String def) { return def; }
            public boolean getBoolean(String path) { return false; }
            public boolean getBoolean(String path, boolean def) { return def; }
            public int getInt(String path, int def) { return def; }
            public boolean isSet(String path) { return true; }
            public boolean isString(String path) { return true; }
            public boolean isBoolean(String path) { return true; }
            public void set(String path, Object value) {}
        };
    }
    public java.util.logging.Logger getLogger() {
        return java.util.logging.Logger.getLogger("MobEggSpawnerBlocker");
    }
    
    // Plugin 介面方法實現
    public String getName() {
        return "MobEggSpawnerBlocker";
    }
    
    public boolean isEnabled() {
        return true;
    }
}