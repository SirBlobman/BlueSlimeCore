package com.github.sirblobman.api.bungeecord.premiumvanish;

import com.github.sirblobman.api.bungeecord.configuration.ConfigurablePlugin;
import com.github.sirblobman.api.bungeecord.hook.vanish.IVanishHook;
import de.myzelyam.api.vanish.BungeeVanishAPI;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public final class PremiumVanishHook implements IVanishHook {
    private final Plugin plugin;

    public PremiumVanishHook(@NotNull Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull Plugin plugin() {
        return this.plugin;
    }

    @Override
    public boolean isDisabled() {
        Plugin plugin = plugin();
        ProxyServer proxy = plugin.getProxy();
        PluginManager pluginManager = proxy.getPluginManager();

        Plugin luckPerms = pluginManager.getPlugin("PremiumVanish");
        return (luckPerms == null);
    }

    @Override
    public boolean isHidden(@NotNull ProxiedPlayer player) {
        return BungeeVanishAPI.isInvisible(player);
    }

    @Override
    public boolean isHidden(@NotNull UUID playerId) {
        List<UUID> invisiblePlayerList = BungeeVanishAPI.getInvisiblePlayers();
        return invisiblePlayerList.contains(playerId);
    }

    @Override
    public void setHidden(@NotNull ProxiedPlayer player, boolean hidden) {
        if (hidden) {
            BungeeVanishAPI.hidePlayer(player);
        } else {
            BungeeVanishAPI.showPlayer(player);
        }
    }

    @Override
    public void setHidden(@NotNull UUID playerId, boolean hidden) {
        Plugin plugin = plugin();
        ProxyServer proxy = plugin.getProxy();
        ProxiedPlayer player = proxy.getPlayer(playerId);
        setHidden(player, hidden);
    }

    @Override
    public boolean hasListener() {
        return true;
    }

    @Override
    public void registerListener() {
        Plugin plugin = plugin();
        if (!(plugin instanceof ConfigurablePlugin)) {
            return;
        }

        ProxyServer proxy = plugin.getProxy();
        PluginManager pluginManager = proxy.getPluginManager();
        pluginManager.registerListener(plugin, new PremiumVanishListener((ConfigurablePlugin) plugin));
    }
}
