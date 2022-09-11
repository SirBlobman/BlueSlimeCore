package com.github.sirblobman.api.language;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import com.github.sirblobman.api.configuration.ConfigurationManager;
import com.github.sirblobman.api.configuration.IResourceHolder;
import com.github.sirblobman.api.configuration.WrapperPluginResourceHolder;
import com.github.sirblobman.api.language.sound.CustomSoundInfo;
import com.github.sirblobman.api.language.sound.SoundInfo;
import com.github.sirblobman.api.language.sound.XSoundInfo;
import com.github.sirblobman.api.utility.Validate;

import com.cryptomorin.xseries.XSound;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.Title.Times;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class LanguageManager {
    private static final String[] KNOWN_LANGUAGE_ARRAY;

    static {
        // Last Updated: June 28, 2022 18:03
        KNOWN_LANGUAGE_ARRAY = new String[] {
                "af_za", "ar_sa", "ast_es", "az_az", "ba_ru", "bar", "be_by", "bg_bg", "br_fr", "brb", "bs_ba",
                "ca_es", "cs_cz", "cy_gb", "da_dk", "de_at", "de_ch", "de_de", "el_gr", "en_au", "en_ca", "en_gb",
                "en_nz", "en_pt", "en_ud", "en_us", "enp", "enws", "eo_uy", "es_ar", "es_cl", "es_ec", "es_es",
                "es_mx", "es_uy", "es_ve", "esan", "et_ee", "eu_es", "fa_ir", "fi_fi", "fil_ph", "fo_fo", "fr_ca",
                "fr_fr", "fra_de", "fy_nl", "ga_ie", "gd_gb", "gl_es", "got_de", "gv_im", "haw_us", "he_il",
                "hi_in", "hr_hr", "hu_hu", "hy_am", "id_id", "ig_ng", "io_en", "is_is", "isv", "it_it", "ja_jp",
                "jbo_en", "ka_ge", "kab_kab", "kk_kz", "kn_in", "ko_kr", "ksh", "kw_gb", "la_la", "lb_lu", "li_li",
                "lol_us", "lt_lt", "lv_lv", "mi_nz", "mk_mk", "mn_mn", "moh_ca", "ms_my", "mt_mt", "nds_de", "nl_be",
                "nl_nl", "nn_no", "no_no", "nb_no", "nuk", "oc_fr", "oj_ca", "ovd", "pl_pl", "pt_br", "pt_pt",
                "qya_aa", "ro_ro", "rpr", "ru_ru", "scn", "se_no", "sk_sk", "sl_si", "so_so", "sq_al", "sr_sp",
                "sv_se", "swg", "sxu", "szl", "ta_in", "th_th", "tl_ph", "tlh_aa", "tr_tr", "tt_ru", "tzl_tzl",
                "uk_ua", "val_es", "vec_it", "vi_vn", "yi_de", "yo_ng", "zh_cn", "zh_hk", "zh_tw"
        };
    }

    private final ConfigurationManager configurationManager;
    private final Map<String, Language> languageMap;
    private final MiniMessage miniMessage;
    private BukkitAudiences audiences;
    private String defaultLanguageName;
    private String consoleLanguageName;
    private Language defaultLanguage;
    private Language consoleLanguage;
    private boolean forceDefaultLanguage;
    private boolean usePlaceholderAPI;

    public LanguageManager(ConfigurationManager configurationManager) {
        Validate.notNull(configurationManager, "configurationManager must not be null!");
        this.configurationManager = configurationManager;
        this.languageMap = new ConcurrentHashMap<>();
        this.miniMessage = MiniMessage.miniMessage();

        this.defaultLanguageName = null;
        this.consoleLanguageName = null;
        this.defaultLanguage = null;
        this.consoleLanguage = null;
        this.forceDefaultLanguage = false;
        this.usePlaceholderAPI = false;
    }

    @NotNull
    public ConfigurationManager getConfigurationManager() {
        return this.configurationManager;
    }

    @NotNull
    public IResourceHolder getResourceHolder() {
        ConfigurationManager configurationManager = getConfigurationManager();
        return configurationManager.getResourceHolder();
    }

    @NotNull
    public Logger getLogger() {
        IResourceHolder resourceHolder = getResourceHolder();
        return resourceHolder.getLogger();
    }

    private boolean isDebugModeDisabled() {
        ConfigurationManager configurationManager = getConfigurationManager();
        YamlConfiguration configuration = configurationManager.get("config.yml");
        return !configuration.getBoolean("debug-mode", false);
    }

    private void printDebug(String message) {
        if (isDebugModeDisabled()) {
            return;
        }

        Logger logger = getLogger();
        logger.info("[Debug] [Language] " + message);
    }

    @NotNull
    public MiniMessage getMiniMessage() {
        return this.miniMessage;
    }

    @Nullable
    public BukkitAudiences getAudiences() {
        return this.audiences;
    }

    @Nullable
    public Language getDefaultLanguage() {
        if(this.defaultLanguage != null) {
            return this.defaultLanguage;
        }

        if(this.defaultLanguageName == null) {
            Logger logger = getLogger();
            logger.warning("Default language name is not properly defined.");
            return null;
        }

        this.defaultLanguage = this.languageMap.get(this.defaultLanguageName);
        if(this.defaultLanguage == null) {
            Logger logger = getLogger();
            logger.warning("Missing default language with name '" + this.defaultLanguageName + "'.");
            return null;
        }

        return this.defaultLanguage;
    }

    @Nullable
    public Language getConsoleLanguage() {
        if(isForceDefaultLanguage()) {
            return getDefaultLanguage();
        }

        if(this.consoleLanguage != null) {
            return this.consoleLanguage;
        }

        if(this.consoleLanguageName == null) {
            Logger logger = getLogger();
            logger.warning("Console language name is not properly defined, using default.");
            return getDefaultLanguage();
        }

        this.consoleLanguage = this.languageMap.get(this.consoleLanguageName);
        if(this.consoleLanguage == null) {
            Logger logger = getLogger();
            logger.warning("Missing console language with name '" + this.consoleLanguageName
                    + "', using default.");
            return getDefaultLanguage();
        }

        return this.consoleLanguage;
    }

    public boolean isForceDefaultLanguage() {
        return this.forceDefaultLanguage;
    }

    public boolean isUsePlaceholderAPI() {
        return this.usePlaceholderAPI;
    }

    @NotNull
    public String replacePlaceholderAPI(CommandSender commandSender, String message) {
        if (message == null || message.isEmpty()) {
            return "";
        }

        if(!(commandSender instanceof OfflinePlayer)) {
            return message;
        }

        OfflinePlayer player = (OfflinePlayer) commandSender;
        return PlaceholderAPI.setPlaceholders(player, message);
    }

    public void saveDefaultLanguageFiles() {
        printDebug("Save Default Language Files Start");

        ConfigurationManager configurationManager = getConfigurationManager();
        configurationManager.saveDefault("language.yml");
        printDebug("Triggered saveDefault for 'language.yml'.");

        File dataFolder = configurationManager.getBaseFolder();
        File languageFolder = new File(dataFolder, "language");
        if (languageFolder.exists()) {
            printDebug("language folder already exists, not necessary to create any default language files.");
            return;
        }

        boolean makeFolder = languageFolder.mkdirs();
        if (!makeFolder) {
            throw new IllegalStateException("Failed to create language folder '" + languageFolder + "'.");
        }

        for (String languageName : LanguageManager.KNOWN_LANGUAGE_ARRAY) {
            printDebug("Checking if jar contains language for '" + languageName + "'.");
            String languageFileName = String.format(Locale.US, "language/%s.lang.yml", languageName);
            YamlConfiguration jarLanguageConfiguration = configurationManager.getInternal(languageFileName);
            if (jarLanguageConfiguration != null) {
                printDebug("Jar contains default '" + languageFileName + "'. Saving...");
                configurationManager.saveDefault(languageFileName);
            } else {
                printDebug("Language '" + languageName + "' is missing from jar.");
            }
        }

        printDebug("Save Default Language Files End");
    }

    public void reloadLanguageFiles() {
        printDebug("Reload Language Files Start");

        this.languageMap.clear();
        this.defaultLanguage = null;
        this.consoleLanguage = null;
        printDebug("Cleared current language map and default languages.");

        IResourceHolder resourceHolder = getResourceHolder();
        Logger logger = getLogger();

        File dataFolder = resourceHolder.getDataFolder();
        File languageFolder = new File(dataFolder, "language");
        if (!languageFolder.exists() || !languageFolder.isDirectory()) {
            logger.warning("'language' folder does not exist or is not a directory.");
            return;
        }

        FilenameFilter filenameFilter = (folder, fileName) -> fileName.endsWith(".lang.yml");
        File[] fileArray = languageFolder.listFiles(filenameFilter);
        if (fileArray == null || fileArray.length == 0) {
            logger.warning("Failed to find any '.lang.yml' files in the language folder.");
            return;
        }

        List<YamlConfiguration> configurationList = new ArrayList<>();
        for (File languageFile : fileArray) {
            try {
                printDebug("Trying to load configuration file '" + languageFile + "'.");
                YamlConfiguration configuration = new YamlConfiguration();
                configuration.load(languageFile);

                String languageFileName = languageFile.getName();
                String languageName = languageFileName.replace(".lang.yml", "");
                configuration.set("language-name", languageName);

                configurationList.add(configuration);
                printDebug("Successfully loaded configuration from file '" + languageFile + "'.");
            } catch (IOException | InvalidConfigurationException ex) {
                logger.log(Level.WARNING, "An error occurred while loading a language file:", ex);
            }
        }

        LanguageConfigurationComparator languageConfigurationComparator = new LanguageConfigurationComparator();
        configurationList.sort(languageConfigurationComparator);

        for (YamlConfiguration configuration : configurationList) {
            try {
                printDebug("Trying to load a language from a configuration...");

                Language language = loadLanguage(configuration);
                if (language != null) {
                    String languageCode = language.getLanguageCode();
                    this.languageMap.put(languageCode, language);
                    printDebug("Successfully loaded language '" + languageCode + "'.");
                } else {
                    printDebug("Language was null for some reason.");
                }
            } catch (InvalidConfigurationException ex) {
                logger.log(Level.WARNING, "An error occurred while loading a language configuration:", ex);
            }
        }

        ConfigurationManager configurationManager = getConfigurationManager();
        configurationManager.reload("language.yml");
        printDebug("Successfully reloaded language.yml configuration.");

        YamlConfiguration configuration = configurationManager.get("language.yml");
        this.forceDefaultLanguage = configuration.getBoolean("enforce-default-locale");
        this.defaultLanguageName = configuration.getString("default-locale");
        this.consoleLanguageName = configuration.getString("console-locale");
        printDebug("Forced Default: " + this.forceDefaultLanguage);
        printDebug("Default Language Name: " + this.defaultLanguageName);
        printDebug("Console Language Name: " + this.consoleLanguageName);

        if (resourceHolder instanceof WrapperPluginResourceHolder) {
            WrapperPluginResourceHolder pluginHolder = (WrapperPluginResourceHolder) resourceHolder;
            Plugin plugin = pluginHolder.getPlugin();
            this.audiences = BukkitAudiences.create(plugin);
            printDebug("Successfully setup adventures audiences.");
        } else {
            this.audiences = null;
            printDebug("Current resource holder is not a plugin, ignoring adventure audiences.");
        }

        PluginManager pluginManager = Bukkit.getPluginManager();
        boolean usePlaceholderAPI = configuration.getBoolean("use-placeholder-api", false);
        boolean existsPlaceholderAPI = pluginManager.isPluginEnabled("PlaceholderAPI");
        this.usePlaceholderAPI = (usePlaceholderAPI && existsPlaceholderAPI);
        printDebug("Config Use PlaceholderAPI: " + usePlaceholderAPI);
        printDebug("PlaceholderAPI exists: " + existsPlaceholderAPI);
        printDebug("Use PlaceholderAPI: " + this.usePlaceholderAPI);

        int languageCount = this.languageMap.size();
        printDebug("Successfully loaded " + languageCount + " language(s).");
        printDebug("Reload Language Files End");
    }

    @Nullable
    private Language loadLanguage(YamlConfiguration configuration) throws InvalidConfigurationException {
        Logger logger = getLogger();
        String languageName = configuration.getString("language-name");
        if (languageName == null) {
            logger.warning("Missing 'language-name' setting in configuration.");
            return null;
        }

        Language parentLanguage = null;
        String parentLanguageName = configuration.getString("parent");
        if (parentLanguageName != null) {
            parentLanguage = this.languageMap.get(parentLanguageName);
            if (parentLanguage == null) {
                throw new InvalidConfigurationException("parent language not loaded correctly.");
            }
        }

        Language language = new Language(parentLanguage, languageName, configuration);
        Set<String> keySet = configuration.getKeys(true);
        for (String key : keySet) {
            if (configuration.isList(key)) {
                List<String> messageList = configuration.getStringList(key);
                if (!messageList.isEmpty()) {
                    String message = String.join("\n", messageList);
                    language.addTranslation(key, message);
                }
            }

            if (configuration.isString(key)) {
                String message = configuration.getString(key, key);
                if (message != null) {
                    language.addTranslation(key, message);
                }
            }
        }

        return language;
    }

    @Nullable
    public Language getLanguage(String localeName) {
        if (localeName == null || localeName.isEmpty() || localeName.equals("default")) {
            return getDefaultLanguage();
        }

        return this.languageMap.getOrDefault(localeName, getDefaultLanguage());
    }

    @Nullable
    public Language getLanguage(CommandSender commandSender) {
        if (isForceDefaultLanguage()) {
            return getDefaultLanguage();
        }

        if (commandSender == null || commandSender instanceof ConsoleCommandSender) {
            return getConsoleLanguage();
        }

        if (commandSender instanceof Player) {
            String cachedLocale = LanguageCache.getCachedLocale((Player) commandSender);
            return getLanguage(cachedLocale);
        }

        return getDefaultLanguage();
    }

    @NotNull
    public String getMessageRaw(@Nullable CommandSender commandSender, @NotNull String key) {
        Validate.notEmpty(key, "key must not be empty!");

        Language language = getLanguage(commandSender);
        if (language == null) {
            Logger logger = getLogger();
            logger.warning("There are no languages available.");
            return "";
        }

        return language.getTranslation(key);
    }

    @NotNull
    public String getMessageString(@Nullable CommandSender commandSender, @NotNull String key,
                                   @Nullable Replacer replacer) {
        String messageRaw = getMessageRaw(commandSender, key);
        if(messageRaw.isEmpty()) {
            return "";
        }

        String message = (isUsePlaceholderAPI() ? replacePlaceholderAPI(commandSender, messageRaw) : messageRaw);
        return (replacer == null ? message : replacer.replace(message));
    }

    @NotNull
    public Component getMessage(@Nullable CommandSender commandSender, @NotNull String key,
                                @Nullable Replacer replacer) {
        String messageString = getMessageString(commandSender, key, replacer);
        if (messageString.isEmpty()) {
            return Component.empty();
        }

        MiniMessage miniMessageHandler = getMiniMessage();
        return miniMessageHandler.deserialize(messageString);
    }

    @NotNull
    public Component getMessageWithPrefix(@Nullable CommandSender commandSender, @NotNull String key,
                                          @Nullable Replacer replacer) {
        String messageString = getMessageString(commandSender, key, replacer);
        if (messageString.isEmpty()) {
            return Component.empty();
        }

        String prefixString = getMessageString(commandSender, "prefix", replacer);
        if (!prefixString.isEmpty()) {
            messageString = (prefixString + " " + messageString);
        }

        MiniMessage miniMessageHandler = getMiniMessage();
        return miniMessageHandler.deserialize(messageString);
    }

    @NotNull
    @Deprecated
    public String getMessageLegacy(@Nullable CommandSender commandSender, @NotNull String key,
                                   @Nullable Replacer replacer) {
        Component component = getMessage(commandSender, key, replacer);
        return ComponentHelper.toLegacy(component);
    }

    public void sendMessage(@NotNull CommandSender commandSender, @NotNull Component message) {
        if (Component.empty().equals(message)) {
            return;
        }

        BukkitAudiences audiences = getAudiences();
        if (audiences == null) {
            return;
        }

        Audience audience = audiences.sender(commandSender);
        audience.sendMessage(message);
    }

    public void sendMessage(@NotNull CommandSender commandSender, @NotNull String key, @Nullable Replacer replacer) {
        Component message = getMessage(commandSender, key, replacer);
        sendMessage(commandSender, message);
    }

    public void sendMessageWithPrefix(@NotNull CommandSender commandSender, @NotNull String key,
                                      @Nullable Replacer replacer) {
        Component message = getMessageWithPrefix(commandSender, key, replacer);
        sendMessage(commandSender, message);
    }

    public void broadcastMessage(@NotNull Component message, @Nullable String permission) {
        CommandSender console = Bukkit.getConsoleSender();
        sendMessage(console, message);

        Collection<? extends Player> onlinePlayerCollection = Bukkit.getOnlinePlayers();
        for (Player player : onlinePlayerCollection) {
            if (hasPermission(player, permission)) {
                sendMessage(player, message);
            }
        }
    }

    public void broadcastMessage(@NotNull String key, @Nullable Replacer replacer, @Nullable String permission) {
        CommandSender console = Bukkit.getConsoleSender();
        sendMessage(console, key, replacer);

        Collection<? extends Player> onlinePlayerCollection = Bukkit.getOnlinePlayers();
        for (Player player : onlinePlayerCollection) {
            if (hasPermission(player, permission)) {
                sendMessage(player, key, replacer);
            }
        }
    }

    private boolean hasPermission(@NotNull Player player, @Nullable String permission) {
        if (permission == null || permission.isEmpty()) {
            return true;
        }

        return player.hasPermission(permission);
    }

    public void sendActionBar(@NotNull Player player, @NotNull Component message) {
        if (Component.empty().equals(message)) {
            return;
        }

        BukkitAudiences audiences = getAudiences();
        if (audiences == null) {
            return;
        }

        Audience audience = audiences.player(player);
        audience.sendActionBar(message);
    }

    public void sendActionBar(@NotNull Player player, @NotNull String key, @Nullable Replacer replacer) {
        Component message = getMessage(player, key, replacer);
        sendActionBar(player, message);
    }

    @NotNull
    public String formatDecimal(@Nullable CommandSender commandSender, @NotNull Number decimal) {
        Language language = getLanguage(commandSender);
        if (language == null) {
            DecimalFormatSymbols usSymbols = DecimalFormatSymbols.getInstance(Locale.US);
            DecimalFormat decimalFormat = new DecimalFormat("0.00", usSymbols);
            return decimalFormat.format(decimal);
        }

        DecimalFormat decimalFormat = language.getDecimalFormat();
        return decimalFormat.format(decimal);
    }

    @NotNull
    public Title getTitle(@Nullable CommandSender commandSender, @NotNull String path, @Nullable Replacer replacer) {
        String titleKey = (path + ".title");
        String subtitleKey = (path + ".subtitle");
        Component title = getMessage(commandSender, titleKey, replacer);
        Component subtitle = getMessage(commandSender, subtitleKey, replacer);

        Times times;
        try {
            String fadeInString = getMessageString(commandSender, path + ".fade-in", replacer);
            String stayString = getMessageString(commandSender, path + ".stay", replacer);
            String fadeOutString = getMessageString(commandSender, path + ".fade-out", replacer);

            int fadeInTicks = Integer.parseInt(fadeInString);
            int stayTicks = Integer.parseInt(stayString);
            int fadeOutTicks = Integer.parseInt(fadeOutString);

            Duration fadeIn = ofTicks(fadeInTicks);
            Duration stay = ofTicks(stayTicks);
            Duration fadeOut = ofTicks(fadeOutTicks);
            times = Times.times(fadeIn, stay, fadeOut);
        } catch (NumberFormatException ex) {
            if (!isDebugModeDisabled()) {
                printDebug("Invalid language title timings at path '" + path + "'.");
                ex.printStackTrace();
            }

            Duration defaultFadeIn = ofTicks(10);
            Duration defaultStay = ofTicks(70);
            Duration defaultFadeOut = ofTicks(20);
            times = Times.times(defaultFadeIn, defaultStay, defaultFadeOut);
        }

        return Title.title(title, subtitle, times);
    }

    public void sendTitle(@NotNull Player player, @NotNull String path, @Nullable Replacer replacer) {
        Title title = getTitle(player, path, replacer);
        sendTitle(player, title);
    }

    public void sendTitle(@NotNull Player player, @NotNull Title title) {
        BukkitAudiences audiences = getAudiences();
        if (audiences == null) {
            return;
        }

        Audience audience = audiences.player(player);
        audience.showTitle(title);
    }

    private Duration ofTicks(int ticks) {
        long millis = (ticks * 50L);
        return Duration.ofMillis(millis);
    }

    @Nullable
    public SoundInfo getSound(@Nullable CommandSender commandSender, @NotNull String path) {
        printDebug("Loading sound for sender '" + commandSender + "' and path '" + path + "'");

        String nameKey = (path + ".name");
        String volumeKey = (path + ".volume");
        String pitchKey = (path + ".pitch");
        printDebug("Key: Name: " + nameKey + ", Volume: " + volumeKey + ", Pitch: " + pitchKey);

        String soundName = getMessageString(commandSender, nameKey, null);
        String volumeString = getMessageString(commandSender, volumeKey, null);
        String pitchString = getMessageString(commandSender, pitchKey, null);
        printDebug("Value: Name: " + soundName + ", Volume: " + volumeString + ", Pitch: " + pitchString);

        try {
            SoundInfo soundInfo;
            if (soundName.startsWith("custom:")) {
                String realSoundName = soundName.substring("custom:".length());
                soundInfo = new CustomSoundInfo(realSoundName);
                printDebug("Custom Sound: Name: " + realSoundName);
            } else {
                Optional<XSound> optionalSound = XSound.matchXSound(soundName);
                if (!optionalSound.isPresent()) {
                    throw new IllegalArgumentException("Invalid sound name '" + soundName + "'.");
                }

                XSound sound = optionalSound.get();
                soundInfo = new XSoundInfo(sound);
                printDebug("XSound: Name: " + sound);
            }

            float volume = Float.parseFloat(volumeString);
            float pitch = Float.parseFloat(pitchString);
            printDebug("Volume: " + volume + ", Pitch: " + pitch);

            soundInfo.setVolume(volume);
            soundInfo.setPitch(pitch);
            return soundInfo;
        } catch (IllegalArgumentException ex) {
            if (!isDebugModeDisabled()) {
                printDebug("Invalid language sound at path '" + path + "'.");
                ex.printStackTrace();
            }

            return null;
        }
    }

    public void sendSound(@NotNull Player player, @NotNull SoundInfo soundInfo) {
        soundInfo.play(player);
    }

    public void sendSound(@NotNull Player player, @NotNull String path) {
        SoundInfo soundInfo = getSound(player, path);
        if (soundInfo != null) {
            sendSound(player, soundInfo);
        }
    }
}
