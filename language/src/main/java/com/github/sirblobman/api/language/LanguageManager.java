package com.github.sirblobman.api.language;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.sirblobman.api.configuration.ConfigurationManager;
import com.github.sirblobman.api.utility.MessageUtility;
import com.github.sirblobman.api.utility.Validate;
import com.github.sirblobman.api.utility.VersionUtility;

public final class LanguageManager {
    private final ConfigurationManager configurationManager;
    public LanguageManager(JavaPlugin plugin, ConfigurationManager configurationManager) {
        this.configurationManager = Validate.notNull(configurationManager, "plugin must not be null!");
    }

    /**
     * @param sender The sender that the message will be localized to (can be {@code null})
     * @param key The path to the message in the localization file
     * @return A message localized to the sender (may be empty)
     */
    public String getMessage(CommandSender sender, String key) {
        String localeFileName = getLocaleFileName(sender);
        YamlConfiguration config = this.configurationManager.get(localeFileName);
        String missingValue = ("{" + key + "}");

        if(config.isList(key)) {
            List<String> messageList = config.getStringList(key);
            return String.join("\n", messageList);
        }

        if(config.isString(key)) {
            String message = config.getString(key);
            return (message == null ? missingValue : message);
        }

        return missingValue;
    }

    /**
     * @param sender The sender that the message will be localized to (can be {@code null})
     * @param key The path to the message in the localization file
     * @return The same output from {@link #getMessage(CommandSender, String)}, but with color codes replaced
     */
    public String getMessageColored(CommandSender sender, String key) {
        String message = getMessage(sender, key);
        return MessageUtility.color(message);
    }

    /**
     * Send a localized message to something
     * @param sender The sender that the message will be localized to (can be {@code null})
     * @param key The path to the message in the localization file
     * @param replacer A variable replacer (can be {@code null})
     * @param color {@code true} to replace color codes, {@code false} to leave the message as-is
     */
    public void sendMessage(CommandSender sender, String key, Replacer replacer, boolean color) {
        String message = (color ? getMessageColored(sender, key) : getMessage(sender, key));
        if(message.isEmpty()) return;

        if(replacer != null) {
            String replaced = replacer.replace(message);
            sender.sendMessage(replaced);
            return;
        }

        sender.sendMessage(message);
    }

    /**
     * Send a localized message to all players and the server console
     * @param key The path to the message in the localization file
     * @param replacer A variable replacer (can be {@code null})
     * @param color {@code true} to replace color codes, {@code false} to leave the message as-is
     * @see #sendMessage(CommandSender, String, Replacer, boolean)
     */
    public void broadcastMessage(String key, Replacer replacer, boolean color) {
        CommandSender console = Bukkit.getConsoleSender();
        sendMessage(console, key, replacer, color);

        Collection<? extends Player> onlinePlayerCollection = Bukkit.getOnlinePlayers();
        onlinePlayerCollection.forEach(player -> sendMessage(player, key, replacer, color));
    }

    public void saveDefaultLocales() {
        try {
            JavaPlugin plugin = this.configurationManager.getPlugin();
            File pluginFolder = plugin.getDataFolder();
            File languageFolder = new File(pluginFolder, "language");
            if(languageFolder.exists()) return;

            Set<String> localeNameSet = getJarLocaleNames();
            localeNameSet.forEach(this.configurationManager::saveDefault);
        } catch(Exception ex) {
            Logger logger = this.configurationManager.getPlugin().getLogger();
            logger.log(Level.WARNING, "An error occurred while saving the default locale files:", ex);
        }
    }

    public void reloadLocales() {
        try {
            JavaPlugin javaPlugin = this.configurationManager.getPlugin();
            File dataFolder = javaPlugin.getDataFolder();
            File languageFolder = new File(dataFolder, "language");

            FilenameFilter filenameFilter = (folder, fileName) -> fileName.endsWith(".lang.yml");
            File[] localeFileArray = languageFolder.listFiles(filenameFilter);
            if(localeFileArray == null || localeFileArray.length == 0) throw new IllegalStateException("There are no locale files to reload.");

            for(File localeFile : localeFileArray) {
                String fileName = localeFile.getName();
                String reloadName = ("language/" + fileName);
                this.configurationManager.reload(reloadName);
            }
        } catch(Exception ex) {
            Logger logger = this.configurationManager.getPlugin().getLogger();
            logger.log(Level.WARNING, "An error occurred while reloading locale files:", ex);
        }
    }

    private String getDefaultLocale() {
        YamlConfiguration configuration = this.configurationManager.get("language.yml");
        return configuration.getString("default-locale");
    }

    private String getLocale(Player player) {
        int minorVersion = VersionUtility.getMinorVersion();
        if(minorVersion < 12) return getDefaultLocale();
        return player.getLocale();
    }

    private String getLocaleFileName(CommandSender sender) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            String locale = getLocale(player);
            return getLocaleFileName(locale);
        }

        return getDefaultLocaleFileName();
    }

    private String getLocaleFileName(String locale) {
        JavaPlugin javaPlugin = this.configurationManager.getPlugin();
        File dataFolder = javaPlugin.getDataFolder();
        File languageFolder = new File(dataFolder, "language");

        String localeFileName = (locale + ".lang.yml");
        File localeFile = new File(languageFolder, localeFileName);
        return (localeFile.exists() ? ("language/" + localeFileName) : getDefaultLocaleFileName());
    }

    private String getDefaultLocaleFileName() {
        String defaultLocale = getDefaultLocale();
        return ("language/" + defaultLocale + ".lang.yml");
    }

    private Set<String> getJarLocaleNames() {
        try {
            JavaPlugin javaPlugin = this.configurationManager.getPlugin();
            Class<?> class_JavaPlugin = Class.forName("org.bukkit.plugin.java.JavaPlugin");
            Method method_getFile = class_JavaPlugin.getDeclaredMethod("getFile");

            method_getFile.setAccessible(true);
            File pluginJarFile = (File) method_getFile.invoke(javaPlugin);
            JarFile jarFile = new JarFile(pluginJarFile);

            Set<String> jarLocaleNameSet = new HashSet<>();
            Enumeration<JarEntry> entries = jarFile.entries();
            while(entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement();
                String jarEntryName = jarEntry.getName();
                if(!jarEntryName.startsWith("language/")) continue;
                jarLocaleNameSet.add(jarEntryName);
            }

            return jarLocaleNameSet;
        } catch(Exception ex) {
            Logger logger = this.configurationManager.getPlugin().getLogger();
            logger.log(Level.WARNING, "An error occurred while listing the jar locale files:", ex);
            return Collections.emptySet();
        }
    }
}