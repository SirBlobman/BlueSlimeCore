package com.SirBlobman.api.nms.boss.bar;

import org.bukkit.entity.Player;

public abstract class BossBarHandler {
    public abstract BossBarWrapper getBossBar(Player player);
    public abstract void updateBossBar(Player player, String message, double progress, String color, String style);
    public abstract void removeBossBar(Player player);
}
