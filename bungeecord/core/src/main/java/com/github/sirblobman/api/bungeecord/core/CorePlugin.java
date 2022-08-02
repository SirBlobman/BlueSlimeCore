package com.github.sirblobman.api.bungeecord.core;

import net.md_5.bungee.config.Configuration;

import com.github.sirblobman.api.bungeecord.bungeeperms.BungeePermsHook;
import com.github.sirblobman.api.bungeecord.configuration.ConfigurablePlugin;
import com.github.sirblobman.api.bungeecord.configuration.ConfigurationManager;
import com.github.sirblobman.api.bungeecord.core.hook.DefaultPermissionHook;
import com.github.sirblobman.api.bungeecord.core.hook.DefaultVanishHook;
import com.github.sirblobman.api.bungeecord.hook.permission.IPermissionHook;
import com.github.sirblobman.api.bungeecord.hook.vanish.IVanishHook;
import com.github.sirblobman.api.bungeecord.luckperms.LuckPermsHook;
import com.github.sirblobman.api.bungeecord.premiumvanish.PremiumVanishHook;

import org.jetbrains.annotations.NotNull;

public final class CorePlugin extends ConfigurablePlugin {
    private IPermissionHook permissionHook;
    private IVanishHook vanishHook;

    public CorePlugin() {
        super();
        this.permissionHook = null;
        this.vanishHook = null;
    }

    @Override
    public void onLoad() {
        ConfigurationManager configurationManager = getConfigurationManager();
        configurationManager.saveDefault("config.yml");
    }

    @Override
    public void onEnable() {
        ConfigurationManager configurationManager = getConfigurationManager();
        configurationManager.reload("config.yml");

        setupPermissionHook();
        setupVanishHook();
    }

    @Override
    public void onDisable() {
        // Do Nothing
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

        if(hooksSection.getBoolean("LuckPerms", false)) {
            IPermissionHook hook = new LuckPermsHook(this);
            if(!hook.isDisabled()) {
                this.permissionHook = hook;
                return;
            }
        }

        if(hooksSection.getBoolean("BungeePerms")) {
            IPermissionHook hook = new BungeePermsHook(this);
            if(!hook.isDisabled()) {
                this.permissionHook = hook;
                return;
            }
        }

        this.permissionHook = new DefaultPermissionHook(this);
    }

    public void setupVanishHook() {
        ConfigurationManager configurationManager = getConfigurationManager();
        Configuration configuration = configurationManager.get("config.yml");
        Configuration hooksSection = configuration.getSection("hooks");

        if(hooksSection.getBoolean("PremiumVanish", false)) {
            IVanishHook hook = new PremiumVanishHook(this);
            if(!hook.isDisabled()) {
                this.vanishHook = hook;
                return;
            }
        }

        this.vanishHook = new DefaultVanishHook(this);
    }
}
