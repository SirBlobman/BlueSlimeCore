package com.github.sirblobman.api.update;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;

import com.github.sirblobman.api.folia.FoliaHelper;
import com.github.sirblobman.api.folia.IFoliaPlugin;
import com.github.sirblobman.api.folia.details.TaskDetails;
import com.github.sirblobman.api.folia.scheduler.TaskScheduler;

public final class HangarUpdateManager {
    private static final String BASE_API_URL;
    private static final String BASE_RESOURCE_URL;

    static {
        BASE_API_URL = "https://hangar.papermc.io/api/v1/projects/%s/%s/latestrelease";
        BASE_RESOURCE_URL = "https://hangar.papermc.io/%s/%s";
    }

    private final IFoliaPlugin plugin;
    private final Map<String, HangarInfo> pluginInfoMap;
    private final Map<String, String> hangarVersionCache;

    public HangarUpdateManager(@NotNull IFoliaPlugin plugin) {
        this.plugin = plugin;
        this.pluginInfoMap = new HashMap<>();
        this.hangarVersionCache = new HashMap<>();
    }

    /**
     * Register a plugin that uses update checking.
     *
     * @param plugin     The plugin that will be checked for updates.
     * @param hangarInfo The Hangar information for the plugin.
     */
    public void addProject(@NotNull Plugin plugin, @NotNull HangarInfo hangarInfo) {
        String pluginName = plugin.getName();
        this.pluginInfoMap.put(pluginName, hangarInfo);
    }

    /**
     * Remove a resource from this manager.
     *
     * @param plugin The plugin to remove.
     */
    public void removeProject(@NotNull Plugin plugin) {
        String pluginName = plugin.getName();
        this.pluginInfoMap.remove(pluginName);
    }

    /**
     * Schedule an async task that will check every plugin registered in this manager.
     */
    public void checkForUpdates() {
        if (!isEnabled()) {
            printDisabledInformation();
            return;
        }

        IFoliaPlugin plugin = getPlugin();
        TaskDetails task = new TaskDetails(plugin.getPlugin()) {
            @Override
            public void run() {
                fetchUpdates();
            }
        };

        FoliaHelper foliaHelper = plugin.getFoliaHelper();
        TaskScheduler scheduler = foliaHelper.getScheduler();
        scheduler.scheduleAsyncTask(task);
    }

    private @NotNull IFoliaPlugin getPlugin() {
        return this.plugin;
    }

    private @NotNull Logger getLogger() {
        Plugin plugin = getPlugin().getPlugin();
        return plugin.getLogger();
    }

    private boolean isEnabled() {
        Plugin plugin = getPlugin().getPlugin();
        FileConfiguration configuration = plugin.getConfig();
        return configuration.getBoolean("update-checker", false);
    }

    private void printDisabledInformation() {
        Logger logger = getLogger();
        logger.info("[Update Checker] The update checking feature is disabled.");
        logger.info("[Update Checker] No plugin update information is available.");
    }

    private void fetchUpdates() {
        fetchHangarVersions();
        printUpdateInformation();
    }

    private void fetchHangarVersions() {
        Set<String> pluginNameSet = this.pluginInfoMap.keySet();
        for (String pluginName : pluginNameSet) {
            fetchHangarVersion(pluginName);
        }
    }

    private @NotNull HangarInfo getHangarInfo(@NotNull String pluginName) {
        return this.pluginInfoMap.get(pluginName);
    }

    private void fetchHangarVersion(@NotNull String pluginName) {
        HangarInfo hangarInfo = getHangarInfo(pluginName);
        String authorName = hangarInfo.getAuthor();
        String projectName = hangarInfo.getProject();

        try {
            String updateUrlString = String.format(Locale.US, BASE_API_URL, authorName, projectName);
            URL updateUrl = new URL(updateUrlString);
            fetchHangarVersion(pluginName, updateUrl);
        } catch (MalformedURLException ex) {
            Logger logger = getLogger();
            logger.log(Level.WARNING, "Invalid Update Check URL:", ex);
        }
    }

    private void fetchHangarVersion(@NotNull String pluginName, @NotNull URL updateUrl) {
        try {
            URLConnection connection = updateUrl.openConnection();
            if (!(connection instanceof HttpURLConnection)) {
                throw new IOException("URL is not using 'http/https'.");
            }

            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            httpConnection.setRequestMethod("GET");
            fetchHangarVersion(pluginName, httpConnection);
        } catch (IOException ex) {
            Logger logger = getLogger();
            logger.log(Level.WARNING, "Failed to start an HTTP/HTTPS connection:", ex);
        }
    }

    private void fetchHangarVersion(@NotNull String pluginName, @NotNull HttpURLConnection connection) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String hangarVersion = reader.readLine();
            this.hangarVersionCache.put(pluginName, hangarVersion);
        } catch (IOException ex) {
            Logger logger = getLogger();
            logger.log(Level.WARNING, "Failed to fetch update data:", ex);
        }
    }

    private void printUpdateInformation() {
        Set<Entry<String, HangarInfo>> entrySet = this.pluginInfoMap.entrySet();
        for (Entry<String, HangarInfo> entry : entrySet) {
            String pluginName = entry.getKey();
            HangarInfo info = entry.getValue();
            String version = this.hangarVersionCache.get(pluginName);
            printUpdateInformation(pluginName, version, info);
        }
    }

    private void printUpdateInformation(@NotNull String pluginName, @Nullable String version, @NotNull HangarInfo info) {
        PluginManager pluginManager = Bukkit.getPluginManager();
        Plugin plugin = pluginManager.getPlugin(pluginName);
        if (plugin == null) {
            return;
        }

        PluginDescriptionFile description = plugin.getDescription();
        String pluginVersion = description.getVersion();

        Logger logger = plugin.getLogger();
        logger.info(" ");

        if (version == null) {
            printFailedInformation(pluginName, logger);
            return;
        }

        if (pluginVersion.equals(version)) {
            printSameVersion(pluginName, logger);
            return;
        }

        printMismatch(pluginName, logger, pluginVersion, version, info);
    }

    private void printFailedInformation(@NotNull String pluginName, @NotNull Logger logger) {
        logger.info("[Update Checker] Update check failed for plugin '" + pluginName + "'.");
        logger.info("[Updaye Checker] Is is possible that your server does not have access to the internet.");
    }

    private void printSameVersion(@NotNull String pluginName, @NotNull Logger logger) {
        logger.info("[Update Checker] There are no updates available for plugin '" + pluginName + "'.");
    }

    private void printMismatch(@NotNull String pluginName, @NotNull Logger logger, @NotNull String localVersion,
                               @NotNull String remoteVersion, @NotNull HangarInfo info) {
        logger.info("[Update Checker] Found a possible update for plugin '" + pluginName + "'.");
        logger.info("[Update Checker] Local Version: " + localVersion);
        logger.info("[Update Checker] Remote Version: " + remoteVersion);

        String downloadUrl = String.format(BASE_RESOURCE_URL, info.getAuthor(), info.getProject());
        logger.info("[Update Checker] Download Link: " + downloadUrl);
    }
}
