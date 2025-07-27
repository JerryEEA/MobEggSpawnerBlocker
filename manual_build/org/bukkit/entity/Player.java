package org.bukkit.entity;

import org.bukkit.GameMode;

public interface Player {
    String getName();
    GameMode getGameMode();
    void setGameMode(GameMode gameMode);
    void sendMessage(String message);
}