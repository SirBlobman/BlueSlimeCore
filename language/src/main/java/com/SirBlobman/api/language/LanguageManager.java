package com.SirBlobman.api.language;

import java.io.File;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.SirBlobman.api.configuration.ConfigurationManager;
import com.SirBlobman.api.utility.MessageUtility;
import com.SirBlobman.api.utility.Validate;
import com.SirBlobman.api.utility.VersionUtility;

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
}