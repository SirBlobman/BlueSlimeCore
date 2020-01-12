package com.SirBlobman.api.nms;

import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.entity.Player.Spigot;

public class FallbackPlayerHandler extends PlayerHandler {
    private boolean warningActionBar = false, warningTabHeader = false, warningTabFooter = false;
    
    @Override
    public void sendActionBar(Player player, String message) {
        if(!warningActionBar) {
            Logger logger = Logger.getLogger("SirBlobmanAPI");
            logger.warning("Could not find NMS Action Bar version. Sending action bar messages as text.");
            this.warningActionBar = true;
        }
        
        player.sendMessage("[Action Bar] " + message);
    }
    
    @Override
    public void setTabInfo(Player player, String header, String footer) {
        if(!warningTabHeader) {
            Logger logger = Logger.getLogger("SirBlobmanAPI");
            logger.warning("Could not find NMS TAB Header version. Sending tab messages as text.");
            this.warningTabHeader = true;
        }
        
        if(!warningTabFooter) {
            Logger logger = Logger.getLogger("SirBlobmanAPI");
            logger.warning("Could not find NMS TAB Footer version. Sending tab messages as text.");
            this.warningTabFooter = true;
        }
    
        player.sendMessage("[Tab Header] " + header);
        player.sendMessage("[Tab Footer] " + footer);
    }
    
    @Override
    public void forceRespawn(Player player) {
        Spigot spigot = player.spigot();
        spigot.respawn();
    }
    
    @Override
    public double getAbsorptionHearts(Player player) {
        return 0.0D;
    }
    
    @Override
    public void setAbsorptionHearts(Player player, double hearts) {
        // Do Nothing
    }
}