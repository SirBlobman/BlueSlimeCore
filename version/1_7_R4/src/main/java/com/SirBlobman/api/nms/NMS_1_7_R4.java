package com.SirBlobman.api.nms;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class NMS_1_7_R4 extends NMS_Handler {
    @Override
    public void sendActionBar(Player player, String message) {
        return;
    }

    @Override
    public void sendNewBossBar(Player player, String title, double progress, String color, String style) {
        return;
    }

    @Override
    public void setTab(Player player, String header, String footer) {
        return;
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
        Objective obj = scoreboard.registerNewObjective(name, criteria);
        obj.setDisplayName(displayName);
        return obj;
    }
}