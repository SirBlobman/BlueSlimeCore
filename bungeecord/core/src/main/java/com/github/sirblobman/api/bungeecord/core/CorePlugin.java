package com.github.sirblobman.api.bungeecord.core;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.config.Configuration;

import com.github.sirblobman.api.bungeecord.bungeeperms.BungeePermsHook;
import com.github.sirblobman.api.bungeecord.configuration.ConfigurablePlugin;
import com.github.sirblobman.api.bungeecord.configuration.ConfigurationManager;
import com.github.sirblobman.api.bungeecord.core.command.CommandSBCoreHide;
import com.github.sirblobman.api.bungeecord.core.command.CommandSBCoreReload;
import com.github.sirblobman.api.bungeecord.core.hook.DefaultPermissionHook;
import com.github.sirblobman.api.bungeecord.core.hook.DefaultVanishHook;
import com.github.sirblobman.api.bungeecord.hook.permission.IPermissionHook;
import com.github.sirblobman.api.bungeecord.hook.vanish.IVanishHook;
import com.github.sirblobman.api.bungeecord.luckperms.LuckPermsHook;
import com.github.sirblobman.api.bungeecord.premiumvanish.PremiumVanishHook;

import org.jetbrains.annotations.NotNull;

public final class CorePlugin extends ConfigurablePlugin {
    private final IPermissionHook defaultPermissionHook;
    private final IVanishHook defaultVanishHook;

    private IPermissionHook permissionHook;
    private IVanishHook vanishHook;

    public CorePlugin() {
        super();

        this.defaultPermissionHook = new DefaultPermissionHook(this);
        this.defaultVanishHook = new DefaultVanishHook(this);

        this.permissionHook = null;
        this.vanishHook = null;
    }

    @Override
    public void onLoad() {
        ConfigurationManager configurationManager = getConfigurationManager();
        configurationManager.saveDefault("config.yml");
        configurationManager.saveDefault("hidden.yml");
    }

    @Override
    public void onEnable() {
        onReload();

        ProxyServer proxy = getProxy();
        PluginManager pluginManager = proxy.getPluginManager();
        pluginManager.registerCommand(this, new CommandSBCoreReload(this));
        pluginManager.registerCommand(this, new CommandSBCoreHide(this));
    }

    @Override
    public void onDisable() {
        // Do Nothing
    }

    public void onReload() {
        ConfigurationManager configurationManager = getConfigurationManager();
        configurationManager.reload("config.yml");
        configurationManager.reload("hidden.yml");

        setupPermissionHook();
        setupVanishHook();
    }

    @NotNull
    public IPermissionHook getDefaultPermissionHook() {
        return this.defaultPermissionHook;
    }

    @NotNull
    public IVanishHook getDefaultVanishHook() {
        return this.defaultVanishHook;
    }

    @NotNull
    public IPermissionHook getPermissionHook() {
        return this.permissionHook;
    }

    @NotNull
    public IVanishHook getVanishHook() {
        return this.vanishHook;
    }

    public void setupPermissionHook() {
        ConfigurationManager configurationManager = getConfigurationManager();
        Configuration configuration = configurationManager.get("config.yml");
        Configuration hooksSection = configuration.getSection("hooks");

        if (hooksSection.getBoolean("LuckPerms", false)) {
            IPermissionHook hook = new LuckPermsHook(this);
            if (!hook.isDisabled()) {
                this.permissionHook = hook;
                return;
            }
        }

        if (hooksSection.getBoolean("BungeePerms")) {
            IPermissionHook hook = new BungeePermsHook(this);
            if (!hook.isDisabled()) {
                this.permissionHook = hook;
                return;
            }
        }

        this.permissionHook = defaultPermissionHook;
    }

    public void setupVanishHook() {
        ConfigurationManager configurationManager = getConfigurationManager();
        Configuration configuration = configurationManager.get("config.yml");
        Configuration hooksSection = configuration.getSection("hooks");

        if (hooksSection.getBoolean("PremiumVanish", false)) {
            IVanishHook hook = new PremiumVanishHook(this);
            if (!hook.isDisabled()) {
                this.vanishHook = hook;
                return;
            }
        }

        this.vanishHook = defaultVanishHook;
    }
}
