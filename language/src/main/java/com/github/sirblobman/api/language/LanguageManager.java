package com.github.sirblobman.api.language;

import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class LanguageManager {
    private final ConfigurationManager configurationManager;

    /**
     * @deprecated Used {@link #LanguageManager(ConfigurationManager)} instead.
     * @param plugin A Java plugin
     * @param configurationManager The configuration manager to use.
     */
    public LanguageManager(JavaPlugin plugin, ConfigurationManager configurationManager) {
        this(configurationManager);
    }

    /**
     * Create a language manager from a configuration manager
     * @param configurationManager The configuration manager to use.
     */
    public LanguageManager(ConfigurationManager configurationManager) {
        this.configurationManager = Validate.notNull(configurationManager, "plugin must not be null!");
    }

    /**
     * @param sender The sender that will be used to localize the message (null for default).
     * @param key The path to the message inside of the localization file.
     * @return The same output from {@link #getMessage(CommandSender, String)}, but with color codes replaced
     */
    public String getMessageColored(@Nullable CommandSender sender, @NotNull String key) {
        String message = getMessage(sender, key);
        return MessageUtility.color(message);
    }

    /**
     * @param sender The sender that will be used to localize the message (null for default).
     * @param key The path to the message inside of the localization file.
     * @return A localized message string, or {@code ""} if no message is set.
     */
    public String getMessage(@Nullable CommandSender sender, @NotNull String key) {
        YamlConfiguration configuration = getLocaleConfiguration(sender);
        String defaultValue = String.format(Locale.US,"{%s}", key);

        if(configuration.isList(key)) {
            List<String> messageList = configuration.getStringList(key);
            return String.join("\n", messageList);
        }

        if(configuration.isString(key)) {
            String message = configuration.getString(key);
            return (message == null ? defaultValue : message);
        }

        return defaultValue;
    }

    /**
     * @param sender The sender that receives and will be used to localize the message.
     * @param key The path to the message inside of the localization file.
     * @param replacer A variable replacer.
     * @param color {@code true} to replace color codes, {@code false} to leave the message as-is
     * @see #getMessageColored(CommandSender, String) 
     * @see #getMessage(CommandSender, String)
     */
    public void sendMessage(@NotNull CommandSender sender, @NotNull String key, @Nullable Replacer replacer, boolean color) {
        String message = (color ? getMessageColored(sender, key) : getMessage(sender, key));
        if(message.isEmpty()) return;

        if(replacer != null) message = replacer.replace(message);
        sender.sendMessage(message);
    }

    /**
     * Broadcast a localized message to all online players and the server console.
     * @param key The path to the message inside of the localization file.
     * @param replacer A variable replacer.
     * @param color {@code true} to replace color codes, {@code false} to leave the message as-is
     * @see #sendMessage(CommandSender, String, Replacer, boolean)
     */
    public void broadcastMessage(@NotNull String key, @Nullable Replacer replacer, boolean color) {
        CommandSender console = Bukkit.getConsoleSender();
        sendMessage(console, key, replacer, color);

        Collection<? extends Player> onlinePlayerCollection = Bukkit.getOnlinePlayers();
        for(Player player : onlinePlayerCollection) sendMessage(player, key, replacer, color);
    }

    /**
     * Save all of the default localization files.
     */
    public void saveDefaultLocales() {
        try {
            File baseFolder = this.configurationManager.getBaseFolder();
            File languageFolder = new File(baseFolder, "language");
            if(languageFolder.exists()) return; // If the language folder already exists, don't save a new one.

            Set<String> localeNameSet = getLocaleNamesFromResourceHolder();
            for(String localeName : localeNameSet) this.configurationManager.saveDefault(localeName);
        } catch(Exception ex) {
            Logger logger = this.configurationManager.getResourceHolder().getLogger();
            logger.log(Level.WARNING, "An error occurred while saving the default locale files:", ex);
        }
    }

    /**
     * Reload all of the localization files currently in the language folder.
     */
    public void reloadLocales() {
        try {
            File baseFolder = this.configurationManager.getBaseFolder();
            File languageFolder = new File(baseFolder, "language");

            FilenameFilter filenameFilter = (folder, fileName) -> fileName.endsWith(".lang.yml");
            File[] localeFileArray = languageFolder.listFiles(filenameFilter);
            if(localeFileArray == null || localeFileArray.length == 0) throw new IllegalStateException("There are no locale files to reload.");

            for(File localeFile : localeFileArray) {
                String fileName = localeFile.getName();
                String reloadName = ("language/" + fileName);
                this.configurationManager.reload(reloadName);
            }
        } catch(Exception ex) {
            Logger logger = this.configurationManager.getResourceHolder().getLogger();
            logger.log(Level.WARNING, "An error occurred while reloading locale files:", ex);
        }
    }

    private Set<String> getLocaleNamesFromResourceHolder() {
        try {
            Set<String> localeNameSet = new HashSet<>();
            Locale[] localeArray = Locale.getAvailableLocales();
            for(Locale locale : localeArray) {
                String language = locale.getLanguage();
                String country = locale.getCountry();
                String localeFileName = String.format(Locale.US,"language/%s_%s.lang.yml", language, country)
                        .toLowerCase(Locale.US);

                InputStream resource = this.configurationManager.getResourceHolder().getResource(localeFileName);
                if(resource != null) localeNameSet.add(localeFileName);
            }

            return localeNameSet;
        } catch(Exception ex) {
            Logger logger = this.configurationManager.getResourceHolder().getLogger();
            logger.log(Level.WARNING, "An error occurred while listing the jar locale files:", ex);
            return Collections.emptySet();
        }
    }

    private YamlConfiguration getDefaultLocaleConfiguration() {
        String defaultLocaleName = getDefaultLocale();
        return getLocaleConfiguration(defaultLocaleName);
    }

    private YamlConfiguration getLocaleConfiguration(String localeName) {
        String localeFileName = String.format(Locale.US, "language/%s.lang.yml", localeName);
        YamlConfiguration configuration = this.configurationManager.get(localeFileName);
        return (configuration == null ? new YamlConfiguration() : configuration);
    }

    private YamlConfiguration getLocaleConfiguration(CommandSender sender) {
        String localeName = getLocale(sender);
        YamlConfiguration configuration = getLocaleConfiguration(localeName);

        String defaultLocaleName = getDefaultLocale();
        if(!defaultLocaleName.equals(localeName)) {
            YamlConfiguration defaultConfiguration = getDefaultLocaleConfiguration();
            configuration.setDefaults(defaultConfiguration);
        }

        return configuration;
    }

    @NotNull
    private String getDefaultLocale() {
        YamlConfiguration configuration = this.configurationManager.get("language.yml");
        String defaultLocaleName = configuration.getString("default-locale");
        return (defaultLocaleName == null ? "en_us" : defaultLocaleName);
    }

    @NotNull
    private String getLocale(CommandSender sender) {
        int minorVersion = VersionUtility.getMinorVersion();
        if(minorVersion >= 12 && sender instanceof Player) {
            Player player = (Player) sender;
            String playerLocale = player.getLocale();
            if(playerLocale != null) return playerLocale;
        }

        return getDefaultLocale();
    }
}