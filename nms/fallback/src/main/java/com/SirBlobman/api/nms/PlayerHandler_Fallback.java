package com.SirBlobman.api.nms;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerHandler_Fallback extends PlayerHandler {
    public PlayerHandler_Fallback(JavaPlugin plugin) {
        super(plugin);
    }
    
    @Override
    public void sendActionBar(Player player, String message) {
        String realMessage = ("[Action Bar] " + message);
        player.sendMessage(realMessage);
    }
    
    @Override
    public void sendTabInfo(Player player, String header, String footer) {
        String headerMessage = ("[Tab Header] " + header);
        player.sendMessage(headerMessage);
        
        String footerMessage = ("[Tab Footer] " + footer);
        player.sendMessage(footerMessage);
    }
    
    @Override
    public void forceRespawn(Player player) {
        throw new UnsupportedOperationException("Unsupported NMS version!");
    }
    
    @Override
    public double getAbsorptionHearts(Player player) {
        throw new UnsupportedOperationException("Unsupported NMS version!");
    }
    
    @Override
    public void setAbsorptionHearts(Player player, double hearts) {
        throw new UnsupportedOperationException("Unsupported NMS version!");
    }
}