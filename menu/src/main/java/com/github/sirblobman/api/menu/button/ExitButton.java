package com.github.sirblobman.api.menu.button;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import com.github.sirblobman.api.menu.IMenu;
import com.github.sirblobman.api.utility.Validate;

public final class ExitButton extends QuickButton {
    private final JavaPlugin plugin;

    public ExitButton(IMenu menu) {
        Validate.notNull(menu, "menu must not be null!");
        this.plugin = menu.getPlugin();
    }

    @Override
    public void onLeftClick(Player player, boolean shift) {
        JavaPlugin plugin = getPlugin();
        BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.runTaskLater(plugin, player::closeInventory, 2L);
    }

    private JavaPlugin getPlugin() {
        return this.plugin;
    }
}
