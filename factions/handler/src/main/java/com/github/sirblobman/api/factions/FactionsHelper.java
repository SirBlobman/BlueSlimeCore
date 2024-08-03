package com.github.sirblobman.api.factions;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    @NotNull
    public JavaPlugin getPlugin() {
        return this.plugin;
    }

    @NotNull
    public Logger getLogger() {
        JavaPlugin plugin = getPlugin();
        return plugin.getLogger();
    }

    @Nullable
    public FactionsHandler getFactionsHandler() {
        if (this.factionsHandler != null) {
            return this.factionsHandler;
        }

        try {
            PluginManager manager = Bukkit.getPluginManager();
            if (manager.isPluginEnabled("FactionsX")) {
                printHookInfo("FactionsX", "FactionsX");
                this.factionsHandler = new FactionsHandler_X();
                return this.factionsHandler;
            }

            if (manager.isPluginEnabled("LegacyFactions")) {
                printHookInfo("LegacyFactions", "Legacy Factions");
                this.factionsHandler = new FactionsHandler_Legacy();
                return this.factionsHandler;
            }

            Plugin plugin = getPlugin("Factions");
            if (plugin == null) {
                throw new FactionsNotFoundException();
            }

            if (isSaberFactions(plugin)) {
                printHookInfo("Factions", "SaberFactions");
                this.factionsHandler = new FactionsHandler_Saber();
                return this.factionsHandler;
            }

            PluginDescriptionFile description = plugin.getDescription();
            List<String> pluginDependencyList = description.getDepend();
            if (pluginDependencyList.contains("MassiveCore")) {
                printHookInfo("Factions", "MassiveCore Factions");
                this.factionsHandler = new FactionsHandler_Massive();
                return this.factionsHandler;
            }

            String pluginVersion = description.getVersion();
            if (pluginVersion.startsWith("1.6.9.5-")) {
                String subVersion = pluginVersion.substring(8);
                if (subVersion.startsWith("U0.2")) {
                    printHookInfo("Factions", "Factions UUID Legacy");
                    this.factionsHandler = new FactionsHandler_UUID_Legacy();
                    return this.factionsHandler;
                }

                if (subVersion.startsWith("U0.6") || subVersion.startsWith("U0.7")) {
                    printHookInfo("Factions", "Factions UUID Modern");
                    this.factionsHandler = new FactionsHandler_UUID();
                    return this.factionsHandler;
                }
            }

            throw new FactionsNotFoundException();
        } catch (FactionsNotFoundException ex) {
            Logger logger = getLogger();
            logger.warning("Failed to find any of the following plugins:");
            logger.warning("[Factions, FactionsX, LegacyFactions]");
            logger.warning("Please contact SirBlobman if you believe this is mistake.");
            logger.warning("https://github.com/SirBlobman/BlueSlimeCore/issues/new/choose");
            return null;
        } catch (Exception ex) {
            Logger logger = getLogger();
            logger.log(Level.WARNING, "Failed to hook into a Factions plugin because an error occurred:", ex);
            return null;
        }
    }

    private Plugin getPlugin(String pluginName) {
        PluginManager pluginManager = Bukkit.getPluginManager();
        return pluginManager.getPlugin(pluginName);
    }

    private void printHookInfo(String pluginName, String hookName) {
        Plugin plugin = getPlugin(pluginName);
        if (plugin == null) {
            return;
        }

        PluginDescriptionFile description = plugin.getDescription();
        String pluginVersion = description.getVersion();

        Logger logger = this.plugin.getLogger();
        logger.info("Successfully hooked into '" + hookName + " v" + pluginVersion + "'.");
    }

    private boolean isSaberFactions(Plugin plugin) {
        PluginDescriptionFile pluginDescription = plugin.getDescription();
        String pluginVersion = pluginDescription.getVersion();
        List<String> pluginAuthorList = pluginDescription.getAuthors();

        if (pluginAuthorList.contains("Driftay") || pluginAuthorList.contains("SaberDev")) {
            if (pluginVersion.endsWith("-X")) {
                return true;
            }

            if (pluginVersion.startsWith("1.6.9.5")) {
                return (pluginVersion.endsWith("-RC") || pluginVersion.endsWith("-STABLE"));
            }
        }

        return false;
    }
}
