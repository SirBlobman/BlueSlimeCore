package com.github.sirblobman.api.language;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.MatchResult;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import net.md_5.bungee.api.chat.BaseComponent;

import com.github.sirblobman.api.configuration.ConfigurationManager;
import com.github.sirblobman.api.configuration.IResourceHolder;
import com.github.sirblobman.api.configuration.WrapperPluginResourceHolder;
import com.github.sirblobman.api.language.custom.ModifiableMessage;
import com.github.sirblobman.api.language.custom.ModifiableMessageType;
import com.github.sirblobman.api.language.custom.PlayerListInfo;
import com.github.sirblobman.api.language.listener.LanguageListener;
import com.github.sirblobman.api.language.replacer.Replacer;
import com.github.sirblobman.api.utility.Validate;
import com.github.sirblobman.api.utility.VersionUtility;
import com.github.sirblobman.api.shaded.adventure.audience.Audience;
import com.github.sirblobman.api.shaded.adventure.platform.AudienceProvider;
import com.github.sirblobman.api.shaded.adventure.platform.bukkit.BukkitAudiences;
import com.github.sirblobman.api.shaded.adventure.sound.Sound;
import com.github.sirblobman.api.shaded.adventure.text.Component;
import com.github.sirblobman.api.shaded.adventure.text.ComponentLike;
import com.github.sirblobman.api.shaded.adventure.text.TextComponent;
import com.github.sirblobman.api.shaded.adventure.text.TextReplacementConfig;
import com.github.sirblobman.api.shaded.adventure.text.minimessage.MiniMessage;
import com.github.sirblobman.api.shaded.adventure.text.serializer.legacy.LegacyComponentSerializer;
import com.github.sirblobman.api.shaded.adventure.title.Title;
import com.github.sirblobman.api.shaded.adventure.title.Title.Times;

import me.clip.placeholderapi.PlaceholderAPI;

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

    private final IResourceHolder plugin;
    private final ConfigurationManager configurationManager;

    private final Map<UUID, String> localeMap;
    private final Map<String, Language> languageMap;
    private final MiniMessage miniMessage;

    private String defaultLanguageName;
    private String consoleLanguageName;
    private boolean forceDefaultLanguage;
    private boolean usePlaceholderAPI;
    private boolean debugLanguage;

    private AudienceProvider audienceProvider;
    private Language defaultLanguage;
    private Language consoleLanguage;


    public LanguageManager(@NotNull ConfigurationManager configurationManager) {
        this.configurationManager = configurationManager;
        this.plugin = configurationManager.getResourceHolder();

        this.localeMap = new HashMap<>();
        this.languageMap = new HashMap<>();

        MiniMessage.Builder builder = MiniMessage.builder();
        builder.strict(false);
        builder.debug(this::printMiniMessageDebug);
        this.miniMessage = builder.build();
    }

    public @NotNull IResourceHolder getPlugin() {
        return this.plugin;
    }

    public @NotNull Logger getLogger() {
        IResourceHolder plugin = getPlugin();
        return plugin.getLogger();
    }

    public @NotNull MiniMessage getMiniMessage() {
        return this.miniMessage;
    }

    public @NotNull ConfigurationManager getConfigurationManager() {
        return this.configurationManager;
    }

    public @NotNull String getCachedLocale(@NotNull OfflinePlayer player) {
        UUID playerId = player.getUniqueId();
        return this.localeMap.get(playerId);
    }

    public void setLocale(@NotNull Player player, @NotNull String locale) {
        printDebug("Detected setLocale for player '" + player.getName() + "' and locale '" + locale + "'.");
        UUID playerId = player.getUniqueId();
        this.localeMap.put(playerId, locale);
    }

    public void removeLocale(@NotNull Player player) {
        printDebug("Detected removeLocale for player '" + player.getName() + "'.");
        UUID playerId = player.getUniqueId();
        this.localeMap.remove(playerId);
    }

    public @Nullable Language getDefaultLanguage() {
        if (this.defaultLanguage != null) {
            return this.defaultLanguage;
        }

        if (this.defaultLanguageName == null) {
            Logger logger = getLogger();
            logger.warning("The default language name is not properly defined.");
            return null;
        }

        this.defaultLanguage = this.languageMap.get(this.defaultLanguageName);
        return this.defaultLanguage;
    }

    public @Nullable Language getConsoleLanguage() {
        if (this.forceDefaultLanguage) {
            return getDefaultLanguage();
        }

        if (this.consoleLanguage != null) {
            return this.consoleLanguage;
        }

        Logger logger = getLogger();
        if (this.consoleLanguageName == null) {
            logger.warning("The console language name is not properly defined, using default.");
            return getDefaultLanguage();
        }

        this.consoleLanguage = getLanguage(this.consoleLanguageName);
        if (this.consoleLanguage == null) {
            logger.warning("The console language name '" + this.consoleLanguageName
                    + "' is not valid, using default.");
            return getDefaultLanguage();
        }

        return this.consoleLanguage;
    }

    public @Nullable Language getLanguage(@Nullable String name) {
        printDebug("Detected getLanguage for name '" + name + "'...");

        Language defaultLanguage = getDefaultLanguage();
        if (name == null || name.isEmpty() || name.equals("default")) {
            printDebug("Name is not valid, using default language.");
            return defaultLanguage;
        }

        printDebug("Getting name from language map.");
        return this.languageMap.getOrDefault(name, defaultLanguage);
    }

    private @Nullable Language getPlayerLanguage(@NotNull Player player) {
        printDebug("Detected getPlayerLanguage for player '" + player.getName() + "'.");
        String cachedLocale = getCachedLocale(player);
        printDebug("Cached Locale Name: " + cachedLocale);
        return getLanguage(cachedLocale);
    }

    public @Nullable Language getLanguage(@Nullable CommandSender sender) {
        if (this.forceDefaultLanguage) {
            return getDefaultLanguage();
        }

        if (sender == null || sender instanceof ConsoleCommandSender) {
            return getConsoleLanguage();
        }

        if (sender instanceof Player) {
            Player player = (Player) sender;
            return getPlayerLanguage(player);
        }

        return getDefaultLanguage();
    }

    public void onPluginEnable() {
        printDebug("Detected onPluginEnable...");

        IResourceHolder resourceHolder = getPlugin();
        if (resourceHolder instanceof WrapperPluginResourceHolder) {
            printDebug("Plugin is a resource holder.");
            WrapperPluginResourceHolder wrapper = (WrapperPluginResourceHolder) resourceHolder;
            Plugin plugin = wrapper.getPlugin();

            int minorVersion = VersionUtility.getMinorVersion();
            if (minorVersion >= 12) {
                printDebug("Version is 1.12 or greater, registering language listener...");
                new LanguageListener(plugin, this).register();
            }

            this.audienceProvider = BukkitAudiences.create(plugin);
            printDebug("Successfully created BukkitAudiences instance.");
        }
    }

    public void saveDefaultLanguageFiles() {
        ConfigurationManager configurationManager = getConfigurationManager();
        configurationManager.saveDefault("language.yml");

        File dataFolder = configurationManager.getBaseFolder();
        File languageFolder = new File(dataFolder, "language");
        if (languageFolder.exists()) {
            FilenameFilter languageOnly = (folder, fileName) -> fileName.endsWith(".lang.yml");
            File[] files = languageFolder.listFiles(languageOnly);
            if (files != null && files.length > 0) {
                return;
            }
        }

        boolean makeFolder = languageFolder.mkdirs();
        if (!makeFolder) {
            throw new IllegalStateException("Failed to create language folder at path '" + languageFolder + "'.");
        }

        for (String languageName : KNOWN_LANGUAGE_ARRAY) {
            String languageFileName = String.format(Locale.US, "language/%s.lang.yml", languageName);
            YamlConfiguration jarConfiguration = configurationManager.getInternal(languageFileName);
            if (jarConfiguration == null) {
                continue;
            }

            configurationManager.saveDefault(languageFileName);
        }
    }

    public void printDebug(@NotNull String message) {
        if (!this.debugLanguage) {
            return;
        }

        Logger logger = getLogger();
        logger.info("[Debug] [Language] " + message);
    }

    public void printMiniMessageDebug(@NotNull String message) {
        printDebug("[MiniMessage] " + message);
    }

    public void reloadLanguages() {
        reloadLanguageSettings();
        reloadLanguageFiles();

        int languageCount = this.languageMap.size();
        Logger logger = getLogger();
        logger.info("Successfully loaded " + languageCount + " language(s).");
    }

    private void reloadLanguageSettings() {
        ConfigurationManager configurationManager = getConfigurationManager();
        configurationManager.reload("language.yml");

        YamlConfiguration configuration = configurationManager.get("language.yml");
        this.defaultLanguageName = configuration.getString("default-locale", "en_us");
        this.consoleLanguageName = configuration.getString("console-locale", "en_us");
        this.forceDefaultLanguage = configuration.getBoolean("enforce-default-locale", false);
        this.debugLanguage = configuration.getBoolean("debug-mode", false);

        PluginManager pluginManager = Bukkit.getPluginManager();
        boolean configPlaceholderAPI = configuration.getBoolean("use-placeholder-api", false);
        boolean realPlaceholderAPI = pluginManager.isPluginEnabled("PlaceholderAPI");
        this.usePlaceholderAPI = (configPlaceholderAPI && realPlaceholderAPI);
    }

    private void reloadLanguageFiles() {
        this.defaultLanguage = null;
        this.consoleLanguage = null;
        this.languageMap.clear();

        File dataFolder = configurationManager.getBaseFolder();
        File languageFolder = new File(dataFolder, "language");
        if (!languageFolder.exists() || !languageFolder.isDirectory()) {
            return;
        }

        FilenameFilter filenameFilter = (folder, fileName) -> fileName.endsWith(".lang.yml");
        File[] fileArray = languageFolder.listFiles(filenameFilter);
        if (fileArray == null || fileArray.length < 1) {
            return;
        }

        List<YamlConfiguration> configurationList = new ArrayList<>();
        for (File languageFile : fileArray) {
            YamlConfiguration configuration = reloadLanguageFile(languageFile);
            if (configuration == null) {
                continue;
            }

            configurationList.add(configuration);
        }

        LanguageConfigurationComparator comparator = new LanguageConfigurationComparator();
        configurationList.sort(comparator);

        for (YamlConfiguration configuration : configurationList) {
            LanguageConfiguration languageConfiguration = reloadLanguage(configuration);
            String languageName = configuration.getString("language-name");
            Language language = new Language(languageName, languageConfiguration);
            this.languageMap.put(languageName, language);
        }
    }

    private @Nullable YamlConfiguration reloadLanguageFile(@NotNull File file) {
        String languageFileName = file.getName();
        String languageName = languageFileName.replace(".lang.yml", "");

        try {
            YamlConfiguration configuration = new YamlConfiguration();
            configuration.load(file);

            configuration.set("language-name", languageName);
            return configuration;
        } catch (IOException | InvalidConfigurationException ex) {
            Logger logger = getLogger();
            logger.log(Level.WARNING, "An error occurred while loading a language file:", ex);
            return null;
        }
    }

    private @NotNull LanguageConfiguration reloadLanguage(@NotNull YamlConfiguration configuration) {
        MiniMessage miniMessage = getMiniMessage();
        LanguageConfiguration languageConfiguration = new LanguageConfiguration(configuration, miniMessage);

        String parentName = configuration.getString("parent");
        if (parentName != null) {
            Language language = getLanguage(parentName);
            if (language != null) {
                LanguageConfiguration parent = language.getConfiguration();
                languageConfiguration.setParent(parent);
            }
        }

        String languageName = configuration.getString("language-name");
        if (!languageName.equals(this.defaultLanguageName) && !languageConfiguration.getParent().isPresent()) {
            Language defaultLanguage = getDefaultLanguage();
            if (defaultLanguage != null) {
                LanguageConfiguration defaultLanguageConfiguration = defaultLanguage.getConfiguration();
                languageConfiguration.setParent(defaultLanguageConfiguration);
            }
        }

        String decimalFormatString = configuration.getString("decimal-format");
        if (decimalFormatString != null && !decimalFormatString.isEmpty()) {
            DecimalFormat decimalFormat = new DecimalFormat(decimalFormatString);
            languageConfiguration.setDecimalFormat(decimalFormat);
        }

        return languageConfiguration;
    }

    private @NotNull String replacePlaceholderAPI(@Nullable CommandSender audience, @NotNull String message) {
        if (message.isEmpty()) {
            return "";
        }

        if (!(audience instanceof OfflinePlayer)) {
            return message;
        }

        OfflinePlayer player = (OfflinePlayer) audience;
        return PlaceholderAPI.setPlaceholders(player, message);
    }

    private @NotNull Component replacePlaceholderAPI(@Nullable CommandSender audience, @NotNull Component message) {
        if (!(audience instanceof OfflinePlayer)) {
            return message;
        }

        OfflinePlayer player = (OfflinePlayer) audience;
        TextReplacementConfig.Builder builder = TextReplacementConfig.builder();
        builder.match(PlaceholderAPI.getPlaceholderPattern());
        builder.replacement((matchResult, builderCopy) -> replacePlaceholderAPI(player, matchResult));

        TextReplacementConfig textReplacementConfig = builder.build();
        return message.replaceText(textReplacementConfig);
    }

    private @NotNull ComponentLike replacePlaceholderAPI(@NotNull OfflinePlayer player,
                                                         @NotNull MatchResult matchResult) {
        String match = matchResult.group();
        String replaced = PlaceholderAPI.setPlaceholders(player, match);

        if (replaced.contains("§")) {
            LegacyComponentSerializer serializer = LegacyComponentSerializer.legacySection();
            return serializer.deserialize(replaced);
        } else {
            MiniMessage miniMessage = getMiniMessage();
            return miniMessage.deserialize(replaced);
        }
    }

    public @NotNull String getMessageRaw(@Nullable CommandSender audience, @NotNull String key) {
        Validate.notEmpty(key, "key must not be empty!");

        Language language = getLanguage(audience);
        if (language == null) {
            Logger logger = getLogger();
            logger.warning("There are no languages available.");
            return String.format(Locale.US, "{%s}", key);
        }

        LanguageConfiguration configuration = language.getConfiguration();
        return configuration.getRawMessage(key);
    }

    public @NotNull String getMessageString(@Nullable CommandSender audience, @NotNull String key,
                                            Replacer @NotNull ... replacerArray) {
        String message = getMessageRaw(audience, key);

        if (this.usePlaceholderAPI) {
            message = replacePlaceholderAPI(audience, message);
        }

        for (Replacer replacer : replacerArray) {
            String target = replacer.getTarget();
            String replacement = replacer.getReplacementString();
            message = message.replace(target, replacement);
        }

        return message;
    }

    public @NotNull List<Component> getMessageList(@Nullable CommandSender audience, @NotNull String key,
                                                   Replacer @NotNull ... replacerArray) {
        Validate.notEmpty(key, "key must not be empty!");

        Language language = getLanguage(audience);
        if (language == null) {
            Logger logger = getLogger();
            logger.warning("There are no languages available.");
            return Collections.emptyList();
        }

        LanguageConfiguration configuration = language.getConfiguration();
        List<Component> messages = configuration.getMessageList(key);

        if (this.usePlaceholderAPI) {
            List<Component> newMessages = new ArrayList<>();
            for (Component message : messages) {
                message = replacePlaceholderAPI(audience, message);
                newMessages.add(message);
            }

            messages = newMessages;
        }

        for (Replacer replacer : replacerArray) {
            List<Component> newMessages = new ArrayList<>();
            TextReplacementConfig replacementConfig = replacer.asReplacementConfig();
            for (Component message : messages) {
                message = message.replaceText(replacementConfig);
                newMessages.add(message);
            }

            messages = newMessages;
        }

        return messages;
    }

    public @NotNull Component getMessage(@Nullable CommandSender audience, @NotNull String key,
                                         Replacer @NotNull ... replacerArray) {
        Validate.notEmpty(key, "key must not be empty!");

        Language language = getLanguage(audience);
        if (language == null) {
            Logger logger = getLogger();
            logger.warning("There are no languages available.");
            return Component.text(String.format(Locale.US, "{%s}", key));
        }

        LanguageConfiguration configuration = language.getConfiguration();
        Component message = configuration.getMessage(key);

        if (this.usePlaceholderAPI) {
            message = replacePlaceholderAPI(audience, message);
        }

        for (Replacer replacer : replacerArray) {
            TextReplacementConfig replacementConfig = replacer.asReplacementConfig();
            message = message.replaceText(replacementConfig);
        }

        return message;
    }

    public @NotNull Component getMessageWithPrefix(@Nullable CommandSender audience, @NotNull String key,
                                                   Replacer @NotNull ... replacerArray) {
        Component message = getMessage(audience, key, replacerArray);
        if (Component.empty().equals(message)) {
            return Component.empty();
        }

        Component prefix = getMessage(audience, "prefix", replacerArray);
        if (!Component.empty().equals(prefix)) {
            TextComponent.Builder builder = Component.text();
            builder.append(prefix);
            builder.append(Component.space());
            builder.append(message);
            return builder.build();
        }

        return message;
    }

    public @NotNull ModifiableMessage getMessageModifiable(@Nullable CommandSender audience, @NotNull String key,
                                                           Replacer @NotNull ... replacerArray) {
        Validate.notEmpty(key, "key must not be empty!");

        Language language = getLanguage(audience);
        if (language == null) {
            Logger logger = getLogger();
            logger.warning("There are no languages available.");
            ModifiableMessage modifiableMessage = new ModifiableMessage();
            modifiableMessage.setMessage(Component.text(String.format(Locale.US, "{%s}", key)));
            modifiableMessage.setType(ModifiableMessageType.CHAT);
            return modifiableMessage;
        }

        LanguageConfiguration configuration = language.getConfiguration();
        ModifiableMessage modifiable = configuration.getModifiableMessage(key);
        Component message = modifiable.getMessage();

        if (this.usePlaceholderAPI) {
            message = replacePlaceholderAPI(audience, message);
        }

        for (Replacer replacer : replacerArray) {
            TextReplacementConfig replacementConfig = replacer.asReplacementConfig();
            message = message.replaceText(replacementConfig);
        }

        ModifiableMessage newModifiable = new ModifiableMessage();
        newModifiable.setType(modifiable.getType());
        newModifiable.setMessage(message);
        return newModifiable;
    }

    public @NotNull Title getTitle(@Nullable CommandSender audience, @NotNull String key,
                                   Replacer @NotNull ... replacerArray) {
        Validate.notEmpty(key, "key must not be empty!");

        Language language = getLanguage(audience);
        if (language == null) {
            Logger logger = getLogger();
            logger.warning("There are no languages available.");
            return Title.title(Component.empty(), Component.empty());
        }

        LanguageConfiguration configuration = language.getConfiguration();
        Title title = configuration.getTitle(key);

        Component titleMessage = title.title();
        Component subtitleMessage = title.subtitle();

        if (this.usePlaceholderAPI) {
            titleMessage = replacePlaceholderAPI(audience, titleMessage);
            subtitleMessage = replacePlaceholderAPI(audience, subtitleMessage);
        }

        for (Replacer replacer : replacerArray) {
            TextReplacementConfig replacementConfig = replacer.asReplacementConfig();
            titleMessage = titleMessage.replaceText(replacementConfig);
            subtitleMessage = subtitleMessage.replaceText(replacementConfig);
        }

        Times times = title.times();
        return Title.title(titleMessage, subtitleMessage, times);
    }

    public @NotNull PlayerListInfo getPlayerListInfo(@Nullable CommandSender audience, @NotNull String key,
                                                     Replacer @NotNull ... replacerArray) {
        Validate.notEmpty(key, "key must not be empty!");

        Language language = getLanguage(audience);
        if (language == null) {
            Logger logger = getLogger();
            logger.warning("There are no languages available.");
            return new PlayerListInfo();
        }

        LanguageConfiguration configuration = language.getConfiguration();
        PlayerListInfo playerListInfo = configuration.getPlayerListInfo(key);

        Component headerMessage = playerListInfo.getHeader();
        Component footerMessage = playerListInfo.getFooter();

        if (this.usePlaceholderAPI) {
            headerMessage = replacePlaceholderAPI(audience, headerMessage);
            footerMessage = replacePlaceholderAPI(audience, footerMessage);
        }

        for (Replacer replacer : replacerArray) {
            TextReplacementConfig replacementConfig = replacer.asReplacementConfig();
            headerMessage = headerMessage.replaceText(replacementConfig);
            footerMessage = footerMessage.replaceText(replacementConfig);
        }

        PlayerListInfo newPlayerListInfo = new PlayerListInfo();
        newPlayerListInfo.setHeader(headerMessage);
        newPlayerListInfo.setFooter(footerMessage);
        return newPlayerListInfo;
    }

    public @Nullable Sound getSound(@Nullable CommandSender audience, @NotNull String key) {
        Validate.notEmpty(key, "key must not be empty!");

        Language language = getLanguage(audience);
        if (language == null) {
            Logger logger = getLogger();
            logger.warning("There are no languages available.");
            return null;
        }

        LanguageConfiguration configuration = language.getConfiguration();
        return configuration.getSound(key);
    }

    public @Nullable Audience getAudience(CommandSender sender) {
        if (this.audienceProvider == null) {
            return null;
        }

        if (sender instanceof Player) {
            Player player = (Player) sender;
            UUID playerId = player.getUniqueId();
            return this.audienceProvider.player(playerId);
        }

        return this.audienceProvider.console();
    }

    private void sendNoAudience(@NotNull CommandSender sender, @NotNull Component message) {
        BaseComponent[] bungeeComponents = ComponentBungeeConverter.toBungee(message);
        CommandSender.Spigot spigot = sender.spigot();
        spigot.sendMessage(bungeeComponents);
    }

    public void sendMessage(@NotNull CommandSender audience, @NotNull String key,
                            Replacer @NotNull ... replacerArray) {
        Component message = getMessage(audience, key, replacerArray);
        sendMessage(audience, message);
    }

    public void sendMessage(@NotNull CommandSender audience, @NotNull Component message) {
        if (Component.empty().equals(message)) {
            return;
        }

        Audience realAudience = getAudience(audience);
        if (realAudience == null) {
            sendNoAudience(audience, message);
            return;
        }

        realAudience.sendMessage(message);
    }

    public void sendMessageWithPrefix(@NotNull CommandSender audience, @NotNull String key,
                                      Replacer @NotNull ... replacerArray) {
        Component message = getMessageWithPrefix(audience, key, replacerArray);
        sendMessage(audience, message);
    }

    public void sendActionBar(@NotNull CommandSender audience, @NotNull String key,
                              Replacer @NotNull ... replacerArray) {
        Component message = getMessage(audience, key, replacerArray);
        sendActionBar(audience, message);
    }

    public void sendActionBar(@NotNull CommandSender audience, @NotNull Component message) {
        if (Component.empty().equals(message)) {
            return;
        }

        Audience realAudience = getAudience(audience);
        if (realAudience == null) {
            return;
        }

        realAudience.sendActionBar(message);
    }

    public void sendModifiableMessage(@NotNull CommandSender audience, @NotNull String key,
                                      Replacer @NotNull ... replacerArray) {
        ModifiableMessage modifiable = getMessageModifiable(audience, key, replacerArray);
        Component message = modifiable.getMessage();
        if (Component.empty().equals(message)) {
            return;
        }

        ModifiableMessageType type = modifiable.getType();
        switch (type) {
            case CHAT:
                sendMessage(audience, message);
                break;
            case ACTION_BAR:
                sendActionBar(audience, message);
                break;
            default:
                break;
        }
    }

    public void sendModifiableMessageWithPrefix(@NotNull CommandSender audience, @NotNull String key,
                                                Replacer @NotNull ... replacerArray) {
        ModifiableMessage modifiable = getMessageModifiable(audience, key, replacerArray);
        Component message = modifiable.getMessage();
        if (Component.empty().equals(message)) {
            return;
        }

        ModifiableMessageType type = modifiable.getType();
        if (type == ModifiableMessageType.CHAT) {
            Component prefix = getMessage(audience, "prefix");
            if (!Component.empty().equals(prefix)) {
                TextComponent.Builder builder = Component.text();
                builder.append(prefix);
                builder.append(Component.space());
                builder.append(message);
                message = builder.build();
            }
        }

        switch (type) {
            case CHAT:
                sendMessage(audience, message);
                break;
            case ACTION_BAR:
                sendActionBar(audience, message);
                break;
            default:
                break;
        }
    }

    public void sendTitle(@NotNull CommandSender audience, @NotNull String key,
                          Replacer @NotNull ... replacerArray) {
        Title title = getTitle(audience, key, replacerArray);
        sendTitle(audience, title);
    }

    public void sendTitle(@NotNull CommandSender audience, @NotNull Title title) {
        if (Component.empty().equals(title.title()) && Component.empty().equals(title.subtitle())) {
            return;
        }

        Audience realAudience = getAudience(audience);
        if (realAudience == null) {
            return;
        }

        realAudience.showTitle(title);
    }

    public void sendPlayerListInfo(@NotNull CommandSender audience, @NotNull String key,
                                   Replacer @NotNull ... replacerArray) {
        PlayerListInfo playerListInfo = getPlayerListInfo(audience, key, replacerArray);
        sendPlayerListInfo(audience, playerListInfo);
    }

    public void sendPlayerListInfo(@NotNull CommandSender audience, @NotNull PlayerListInfo info) {
        Audience realAudience = getAudience(audience);
        if (realAudience == null) {
            return;
        }

        Component header = info.getHeader();
        Component footer = info.getFooter();
        realAudience.sendPlayerListHeaderAndFooter(header, footer);
    }

    public void sendSound(@NotNull CommandSender audience, @NotNull String key) {
        Sound sound = getSound(audience, key);
        if (sound == null) {
            return;
        }

        sendSound(audience, sound);
    }

    public void sendSound(@NotNull CommandSender audience, @NotNull Sound sound) {
        Audience realAudience = getAudience(audience);
        if (realAudience == null) {
            return;
        }

        realAudience.playSound(sound);
    }

    public void broadcastMessage(@NotNull String key, @Nullable String permission,
                                 Replacer @NotNull ... replacerArray) {
        Collection<? extends Player> onlinePlayerCollection = Bukkit.getOnlinePlayers();
        broadcastMessage(onlinePlayerCollection, key, permission, replacerArray);
    }

    public void broadcastMessage(@NotNull Iterable<? extends Player> players, @NotNull String key,
                                 @Nullable String permission, Replacer @NotNull ... replacerArray) {
        CommandSender console = Bukkit.getConsoleSender();
        sendMessage(console, key, replacerArray);

        for (Player player : players) {
            if (hasPermission(player, permission)) {
                sendMessage(player, key, replacerArray);
            }
        }
    }

    private boolean hasPermission(@NotNull Player player, @Nullable String permission) {
        if (permission == null || permission.isEmpty()) {
            return true;
        }

        return player.hasPermission(permission);
    }

    public @NotNull DecimalFormat getDecimalFormat(@Nullable CommandSender sender) {
        Language language = getLanguage(sender);
        if (language == null) {
            DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance(Locale.US);
            return new DecimalFormat("0.00", symbols);
        }

        LanguageConfiguration configuration = language.getConfiguration();
        return configuration.getDecimalFormat();
    }
}
