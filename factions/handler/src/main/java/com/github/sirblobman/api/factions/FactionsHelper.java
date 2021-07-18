package com.github.sirblobman.api.factions;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.sirblobman.api.utility.Validate;

public final class FactionsHelper {
    private final JavaPlugin plugin;
    private FactionsHandler factionsHandler;

    public FactionsHelper(JavaPlugin plugin) {
        this.plugin = Validate.notNull(plugin, "plugin must not be null!");
        this.factionsHandler = null;
    }

    public JavaPlugin getPlugin() {
        return this.plugin;
    }

    public FactionsHandler getFactionsHandler() {
        if(this.factionsHandler != null) {
            return this.factionsHandler;
        }

        try {
            PluginManager manager = Bukkit.getPluginManager();
            if(manager.isPluginEnabled("FactionsX")) {
                printHookInfo("FactionsX", "FactionsX");
                this.factionsHandler = new FactionsHandler_X(this.plugin);
                return this.factionsHandler;
            }

            if(manager.isPluginEnabled("LegacyFactions")) {
                printHookInfo("LegacyFactions", "Legacy Factions");
                this.factionsHandler = new FactionsHandler_Legacy(this.plugin);
                return this.factionsHandler;
            }

            Plugin plugin = getPlugin("Factions");
            if(plugin == null) throw new IllegalStateException("Could not find a plugin named 'Factions'.");

            PluginDescriptionFile description = plugin.getDescription();
            List<String> pluginAuthorList = description.getAuthors();
            String pluginVersion = description.getVersion();

            if(pluginVersion.startsWith("1.6.9.5")) {
                if(pluginVersion.startsWith("1.6.9.5-U0.2")) {
                    printHookInfo("Factions", "Factions UUID Legacy");
                    this.factionsHandler = new FactionsHandler_UUID_Legacy(this.plugin);
                    return this.factionsHandler;
                }

                if(pluginVersion.startsWith("1.6.9.5-U0.5")) {
                    printHookInfo("Factions", "Factions UUID");
                    this.factionsHandler = new FactionsHandler_UUID(this.plugin);
                    return this.factionsHandler;
                }

                if(pluginVersion.startsWith("1.6.9.5-2") && pluginAuthorList.contains("Driftay")) {
                    printHookInfo("Factions", "SaberFactions");
                    this.factionsHandler = new FactionsHandler_Saber(this.plugin);
                    return this.factionsHandler;
                }
            }

            printHookInfo("Factions", "MassiveCore Factions");
            this.factionsHandler = new FactionsHandler_Massive(this.plugin);
            return this.factionsHandler;
        } catch(Exception ex) {
            Logger logger = getPlugin().getLogger();
            logger.log(Level.WARNING,"Failed to hook into a Factions plugin because an error occurred:", ex);
            return null;
        }
    }

    private Plugin getPlugin(String pluginName) {
        PluginManager manager = Bukkit.getPluginManager();
        return manager.getPlugin(pluginName);
    }

    private void printHookInfo(String pluginName, String hookName) {
        Plugin plugin = getPlugin(pluginName);
        if(plugin == null) return;

        PluginDescriptionFile description = plugin.getDescription();
        String pluginVersion = description.getVersion();

        Logger logger = this.plugin.getLogger();
        logger.info("Successfully hooked into '" + hookName + " v" + pluginVersion + "'.");
    }
}
