package com.SirBlobman.api.nms;

import java.util.logging.Logger;

import com.SirBlobman.api.nms.boss.bar.BossBarHandler;
import com.SirBlobman.api.nms.boss.bar.BossBarWrapper;

import org.bukkit.entity.Player;

public class FallbackBossBarHandler extends BossBarHandler {
    private boolean warningSent = false;
    
    @Override
    public BossBarWrapper getBossBar(Player player) {
        return null;
    }
    
    @Override
    public void updateBossBar(Player player, String message, double progress, String color, String style) {
        if(!warningSent) {
            Logger logger = Logger.getLogger("SirBlobmanAPI");
            logger.warning("Could not find NMS Boss Bar version. Sending boss bar messages as text.");
            this.warningSent = true;
        }
        
        player.sendMessage("[BossBar] " + message);
    }
    
    @Override
    public void removeBossBar(Player player) {
        // Do Nothing
    }
}