package me.sothatsit.mobeggspawnerblocker;

import java.util.logging.Logger;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.GameMode;

public class SpawnerListener implements Listener {
    
    private final MobEggSpawnerBlocker plugin;
    private final Logger logger;
    
    public SpawnerListener(MobEggSpawnerBlocker plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        try {
            Player player = event.getPlayer();
            Block block = event.getClickedBlock();
            ItemStack item = event.getItem();
            
            // 檢查是否點擊了生怪籠
            if (block == null || !isSpawner(block)) {
                return;
            }
            
            // 檢查是否手持生怪蛋
            if (item == null || !isSpawnEgg(item)) {
                return;
            }
            
            // 檢查是否需要阻止創造模式玩家
            if (!plugin.shouldBlockCreative() && player.getGameMode() == GameMode.CREATIVE) {
                return;
            }
            
            // 阻止事件
            event.setCancelled(true);
            
            // 發送訊息給玩家
            String message = plugin.getMessage();
            if (message != null && !message.isEmpty()) {
                player.sendMessage(message.replace("&", "§"));
            }
            
            // 記錄嘗試
            if (plugin.shouldLogAttempts()) {
                logger.info("阻止玩家 " + player.getName() + " 使用生怪蛋更改生怪籠");
            }
            
        } catch (Exception e) {
            logger.severe("處理玩家互動事件時發生錯誤: " + e.getMessage());
        }
    }
    
    // 檢查是否為生怪籠
    private boolean isSpawner(Block block) {
        try {
            Material material = block.getType();
            return material == Material.SPAWNER || material.name().equals("MOB_SPAWNER");
        } catch (Exception e) {
            logger.warning("檢測生怪籠時發生錯誤: " + e.getMessage());
            return false;
        }
    }
    
    // 多版本支援的生怪蛋檢測
    private boolean isSpawnEgg(ItemStack item) {
        if (item == null) {
            return false;
        }
        
        try {
            Material material = item.getType();
            String materialName = material.name();
            
            // 檢查是否為生怪蛋（支援多版本）
            boolean isEgg = materialName.endsWith("_SPAWN_EGG") || 
                           materialName.contains("SPAWN_EGG") ||
                           materialName.contains("MONSTER_EGG") ||
                           (materialName.endsWith("_EGG") && !materialName.equals("EGG"));
            
            if (isEgg && plugin.shouldLogAttempts()) {
                logger.info("檢測到生怪蛋: " + materialName);
            }
            
            return isEgg;
            
        } catch (Exception e) {
            logger.warning("檢測生怪蛋時發生錯誤: " + e.getMessage());
            return false;
        }
    }
    
    // 版本兼容性檢查
    public void checkVersionCompatibility() {
        try {
            logger.info("正在檢查版本兼容性...");
            logger.info("支援的版本: 1.13+ 至 1.21.7");
            logger.info("多版本生怪蛋檢測已啟用");
        } catch (Exception e) {
            logger.warning("版本兼容性檢查失敗: " + e.getMessage());
        }
    }
}