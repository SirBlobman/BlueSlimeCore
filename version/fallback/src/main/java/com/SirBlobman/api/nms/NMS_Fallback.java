package com.SirBlobman.api.nms;

import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.logging.Logger;

public class NMS_Fallback extends NMS_Handler {
    @Override
    public void sendActionBar(Player player, String message) {
        Logger.getLogger("SirBlobmanAPI").warning("NMS not found, sending action bar as chat");
        
        String color = ChatColor.translateAlternateColorCodes('&', message);
        player.sendMessage(color);
    }

    @Override
    public void sendNewBossBar(Player player, String title, double progress, String colorName, String styleName) {
        Logger.getLogger("SirBlobmanAPI").warning("NMS not found, sending boss bar as chat");
        
        String color = ChatColor.translateAlternateColorCodes('&', title);
        player.sendMessage(color);
    }

    @Override
    public void setTab(Player player, String header, String footer) {
        Logger.getLogger("SirBlobmanAPI").warning("NMS not found, ignoring tab list change.");
    }

    @Override
    public double getMaxHealth(LivingEntity entity) {
        return entity.getMaxHealth();
    }

    @Override
    public void setMaxHealth(LivingEntity entity, double maxHealth) {
        entity.setMaxHealth(maxHealth);
    }

    @Override
    public Objective createScoreboardObjective(Scoreboard scoreboard, String name, String criteria, String displayName) {
        Objective objective = scoreboard.registerNewObjective(name, criteria);
        objective.setDisplayName(displayName);
        return objective;
    }

    @Override
    public void forceRespawn(Player player) {
        player.spigot().respawn();
    }
}