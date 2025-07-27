package org.bukkit;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.Plugin;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitScheduler;

public class Bukkit {
    private static PluginManager pluginManager = new PluginManager() {
        public void registerEvents(Listener listener, Plugin plugin) {
            // 事件註冊存根
        }
    };
    
    private static BukkitScheduler scheduler = new BukkitScheduler() {
        public int scheduleSyncDelayedTask(Object plugin, Runnable task) {
            // 延遲任務存根
            return 0;
        }
    };
    
    public static PluginManager getPluginManager() {
        return pluginManager;
    }
    
    public static BukkitScheduler getScheduler() {
        return scheduler;
    }
}