package com.github.sirblobman.api.nms;

import org.bukkit.plugin.java.JavaPlugin;

import com.github.sirblobman.api.utility.Validate;

public abstract class Handler {
    private final JavaPlugin plugin;
    protected Handler(JavaPlugin plugin) {
        this.plugin = Validate.notNull(plugin, "plugin must not be null!");
    }

    public JavaPlugin getPlugin() {
        return this.plugin;
    }
}