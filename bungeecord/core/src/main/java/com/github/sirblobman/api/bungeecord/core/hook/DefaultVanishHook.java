package com.github.sirblobman.api.bungeecord.core.hook;

import java.util.UUID;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import com.github.sirblobman.api.bungeecord.core.CorePlugin;
import com.github.sirblobman.api.bungeecord.hook.vanish.IVanishHook;
import com.github.sirblobman.api.utility.Validate;

public final class DefaultVanishHook implements IVanishHook {
    private final CorePlugin plugin;

    public DefaultVanishHook(CorePlugin plugin) {
        this.plugin = Validate.notNull(plugin, "plugin must not be null!");
    }

    @Override
    public CorePlugin getPlugin() {
        return this.plugin;
    }

    @Override
    public boolean isDisabled() {
        return false;
    }

    @Override
    public boolean isHidden(ProxiedPlayer player) {
        return false;
    }

    @Override
    public boolean isHidden(UUID playerId) {
        return false;
    }
}
