package com.github.sirblobman.bossbar.legacy;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import com.github.sirblobman.api.utility.Validate;

public final class BossBarListener implements Listener {
    private final JavaPlugin plugin;

    public BossBarListener(JavaPlugin plugin) {
        this.plugin = Validate.notNull(plugin, "plugin must not be null!");
    }
    
    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        BossBarAPI.removeBar(player);
    }
    
    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onKick(PlayerKickEvent e) {
        Player player = e.getPlayer();
        BossBarAPI.removeBar(player);
    }
    
    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onTeleport(PlayerTeleportEvent e) {
        Player player = e.getPlayer();
        handlePlayerTeleport(player);
    }
    
    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onRespawn(PlayerRespawnEvent e) {
        Player player = e.getPlayer();
        handlePlayerTeleport(player);
    }
    
    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        BossBar bossBar = BossBarAPI.getBossBar(player);
        if(bossBar == null) return;

        Runnable task = () -> {
            if(!player.isOnline()) return;
            bossBar.updateMovement();
        };

        BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.scheduleSyncDelayedTask(this.plugin, task);
    }

    private void handlePlayerTeleport(final Player player) {
        BossBar bossBar = BossBarAPI.getBossBar(player);
        if(bossBar == null) return;
        bossBar.setVisible(false);

        Runnable task = () -> bossBar.setVisible(true);
        BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.scheduleSyncDelayedTask(this.plugin, task, 2L);
    }
}
