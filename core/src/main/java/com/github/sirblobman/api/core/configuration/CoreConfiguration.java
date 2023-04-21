package com.github.sirblobman.api.core.configuration;

import org.jetbrains.annotations.NotNull;

import org.bukkit.configuration.ConfigurationSection;

import com.github.sirblobman.api.configuration.IConfigurable;

public final class CoreConfiguration implements IConfigurable {
    private boolean updateCheckerEnabled;
    private boolean debugModeEnabled;
    private boolean commandLoggerEnabled;

    private boolean cacheLanguageOnJoin;
    private boolean removeCacheLanguageOnQuit;
    private boolean updateCacheLanguageOnChange;

    public CoreConfiguration() {
        this.updateCheckerEnabled = true;
        this.debugModeEnabled = false;
        this.commandLoggerEnabled = false;

        this.cacheLanguageOnJoin = true;
        this.removeCacheLanguageOnQuit = true;
        this.updateCacheLanguageOnChange = true;
    }

    @Override
    public void load(@NotNull ConfigurationSection section) {
        setUpdateCheckerEnabled(section.getBoolean("update-checker", true));
        setDebugModeEnabled(section.getBoolean("debug-mode", false));
        setCommandLoggerEnabled(section.getBoolean("command-logger", false));

        setCacheLanguageOnJoin(section.getBoolean("cache-language-on-join", true));
        setRemoveCacheLanguageOnQuit(section.getBoolean("cache-language-remove-on-quit", true));
        setUpdateCacheLanguageOnChange(section.getBoolean("cache-language-update-on-change", true));
    }

    public boolean isUpdateCheckerEnabled() {
        return this.updateCheckerEnabled;
    }

    public void setUpdateCheckerEnabled(boolean enabled) {
        this.updateCheckerEnabled = enabled;
    }

    public boolean isDebugModeEnabled() {
        return this.debugModeEnabled;
    }

    public void setDebugModeEnabled(boolean enabled) {
        this.debugModeEnabled = enabled;
    }

    public boolean isCommandLoggerEnabled() {
        return this.commandLoggerEnabled;
    }

    public void setCommandLoggerEnabled(boolean enabled) {
        this.commandLoggerEnabled = enabled;
    }

    public boolean isCacheLanguageOnJoin() {
        return this.cacheLanguageOnJoin;
    }

    public void setCacheLanguageOnJoin(boolean cacheLanguageOnJoin) {
        this.cacheLanguageOnJoin = cacheLanguageOnJoin;
    }

    public boolean isRemoveCacheLanguageOnQuit() {
        return this.removeCacheLanguageOnQuit;
    }

    public void setRemoveCacheLanguageOnQuit(boolean removeCacheLanguageOnQuit) {
        this.removeCacheLanguageOnQuit = removeCacheLanguageOnQuit;
    }

    public boolean isUpdateCacheLanguageOnChange() {
        return this.updateCacheLanguageOnChange;
    }

    public void setUpdateCacheLanguageOnChange(boolean updateCacheLanguageOnChange) {
        this.updateCacheLanguageOnChange = updateCacheLanguageOnChange;
    }
}
