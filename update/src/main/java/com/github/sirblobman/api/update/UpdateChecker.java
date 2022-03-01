package com.github.sirblobman.api.update;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.sirblobman.api.utility.Validate;

/**
 * @deprecated Your plugin should depend on SirBlobmanCore and register the version with the {@link UpdateManager}
 * instead.
 */
@Deprecated
public final class UpdateChecker extends BukkitRunnable {
    private final JavaPlugin plugin;
    private final long resourceId;
    private String pluginVersion;
    private String spigotVersion;
    
    public UpdateChecker(JavaPlugin plugin, long resourceId) {
        this.plugin = Validate.notNull(plugin, "plugin must not be null!");
        this.resourceId = resourceId;
    }
    
    @Override
    public void run() {
        Logger logger = this.plugin.getLogger();
        logger.info("Checking for a plugin update...");
        
        String pluginVersion = getPluginVersion();
        String spigotVersion = getSpigotVersion();
        logger.info("Plugin Version: " + pluginVersion);
        logger.info("Spigot Version: " + spigotVersion);
        
        if(pluginVersion.equals(spigotVersion)) {
            logger.info("The plugin is on the latest version!");
            return;
        }
        
        logger.info("The plugin version does not match the spigot version.");
        logger.info("You might need to download an update from here:");
        
        String resourceLink = getResourceLink();
        logger.info(resourceLink);
    }
    
    public void runCheck() {
        FileConfiguration config = this.plugin.getConfig();
        if(!config.getBoolean("update-checker")) return;
        runTaskAsynchronously(this.plugin);
    }
    
    public String getUpdateLink() {
        String resourceString = Long.toString(this.resourceId);
        return ("https://api.spigotmc.org/legacy/update.php?resource=" + resourceString);
    }
    
    public String getResourceLink() {
        String resourceString = Long.toString(this.resourceId);
        return ("https://www.spigotmc.org/resources/" + resourceString + "/");
    }
    
    public URL getUpdateURL() throws MalformedURLException {
        String updateLink = getUpdateLink();
        return new URL(updateLink);
    }
    
    public String getPluginVersion() {
        if(this.pluginVersion != null) return this.pluginVersion;
        PluginDescriptionFile description = this.plugin.getDescription();
        return (this.pluginVersion = description.getVersion());
    }
    
    public String getSpigotVersion() {
        if(this.spigotVersion != null) return this.spigotVersion;
        FileConfiguration config = this.plugin.getConfig();
        if(!config.getBoolean("update-checker")) return (this.spigotVersion = "Update Checker Disabled!");
        
        Logger logger = this.plugin.getLogger();
        logger.info("Checking for updates using the Spigot API...");
        
        try {
            URL url = getUpdateURL();
            URLConnection urlConnection = url.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            httpURLConnection.setRequestMethod("GET");
            
            InputStream inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            
            this.spigotVersion = bufferedReader.readLine();
            bufferedReader.close();
            return this.spigotVersion;
        } catch(Exception ex) {
            logger.log(Level.WARNING, "An error occurred while trying to check for an update:", ex);
            return null;
        }
    }
}
