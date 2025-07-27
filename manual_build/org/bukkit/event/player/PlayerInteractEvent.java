package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractEvent {
    private Player player;
    private Block clickedBlock;
    private ItemStack item;
    private boolean cancelled = false;
    
    public PlayerInteractEvent(Player player, Block clickedBlock, ItemStack item) {
        this.player = player;
        this.clickedBlock = clickedBlock;
        this.item = item;
    }
    
    public Player getPlayer() {
        return player;
    }
    
    public Block getClickedBlock() {
        return clickedBlock;
    }
    
    public ItemStack getItem() {
        return item;
    }
    
    public boolean isCancelled() {
        return cancelled;
    }
    
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}