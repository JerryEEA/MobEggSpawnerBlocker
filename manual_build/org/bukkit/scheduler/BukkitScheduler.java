package org.bukkit.scheduler;

public interface BukkitScheduler {
    int scheduleSyncDelayedTask(Object plugin, Runnable task);
}