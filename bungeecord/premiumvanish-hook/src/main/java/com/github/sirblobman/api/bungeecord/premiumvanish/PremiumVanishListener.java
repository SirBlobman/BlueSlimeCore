package com.github.sirblobman.api.bungeecord.premiumvanish;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.event.EventHandler;

import com.github.sirblobman.api.bungeecord.configuration.ConfigurablePlugin;
import com.github.sirblobman.api.bungeecord.configuration.IHookPlugin;
import com.github.sirblobman.api.bungeecord.hook.vanish.IVanishHook;

import de.myzelyam.api.vanish.BungeePlayerHideEvent;
import de.myzelyam.api.vanish.BungeePlayerShowEvent;

public final class PremiumVanishListener implements Listener {
    private final ConfigurablePlugin plugin;

    public PremiumVanishListener(ConfigurablePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onVanish(BungeePlayerHideEvent e) {
        ProxiedPlayer player = e.getPlayer();
        getDefaultVanishHook().setHidden(player, true);
    }

    @EventHandler
    public void onShow(BungeePlayerShowEvent e) {
        ProxiedPlayer player = e.getPlayer();
        getDefaultVanishHook().setHidden(player, false);
    }

    private ConfigurablePlugin getPlugin() {
        return this.plugin;
    }

    private ProxyServer getProxy() {
        ConfigurablePlugin plugin = getPlugin();
        return plugin.getProxy();
    }

    private IVanishHook getDefaultVanishHook() {
        ProxyServer proxy = getProxy();
        PluginManager pluginManager = proxy.getPluginManager();
        IHookPlugin corePlugin = (IHookPlugin) pluginManager.getPlugin("SirBlobmanBungeeCore");
        return corePlugin.getDefaultVanishHook();
    }
}
