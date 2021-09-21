package com.github.sirblobman.api.language;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.github.sirblobman.api.configuration.ConfigurationManager;
import com.github.sirblobman.api.configuration.IResourceHolder;
import com.github.sirblobman.api.utility.MessageUtility;
import com.github.sirblobman.api.utility.Validate;
import com.github.sirblobman.api.utility.VersionUtility;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class LanguageManager {
    /** Last Updated: June 04, 2021 19:30 */
    private static final String[] KNOWN_LANGUAGE_ARRAY = { "af_za", "ar_sa", "ast_es", "az_az", "ba_ru", "bar",
            "be_by", "bg_bg", "br_fr", "brb", "bs_ba", "ca_es", "cs_cz", "cy_gb", "da_dk", "de_at", "de_ch",
            "de_de", "el_gr", "en_au", "en_ca", "en_gb", "en_nz", "en_pt", "en_ud", "en_us", "enp", "enws",
            "eo_uy", "es_ar", "es_cl", "es_ec", "es_es", "es_mx", "es_uy", "es_ve", "esan", "et_ee", "eu_es",
            "fa_ir", "fi_fi", "fil_ph", "fo_fo", "fr_ca", "fr_fr", "fra_de", "fy_nl", "ga_ie", "gd_gb", "gl_es",
            "got_de", "gv_im", "haw_us", "he_il", "hi_in", "hr_hr", "hu_hu", "hy_am", "id_id", "ig_ng", "io_en",
            "is_is", "isv", "it_it", "ja_jp", "jbo_en", "ka_ge", "kab_kab", "kk_kz", "kn_in", "ko_kr", "ksh",
            "kw_gb", "la_la", "lb_lu", "li_li", "lol_us", "lt_lt", "lv_lv", "mi_nz", "mk_mk", "mn_mn", "moh_ca",
            "ms_my", "mt_mt", "nds_de", "nl_be", "nl_nl", "nn_no", "no_no", "nb_no", "nuk", "oc_fr", "oj_ca",
            "ovd", "pl_pl", "pt_br", "pt_pt", "qya_aa", "ro_ro", "rpr", "ru_ru", "scn", "se_no", "sk_sk", "sl_si",
            "so_so", "sq_al", "sr_sp", "sv_se", "swg", "sxu", "szl", "ta_in", "th_th", "tl_ph", "tlh_aa", "tr_tr",
            "tt_ru", "tzl_tzl", "uk_ua", "val_es", "vec_it", "vi_vn", "yi_de", "yo_ng", "zh_cn", "zh_hk", "zh_tw"
    };

    private final ConfigurationManager configurationManager;
    private final Map<String, Language> languageMap;
    private final Map<UUID, Language> playerLanguageMap;
    private Language defaultLanguage;

    /**
     * (Constructor) Create a language manager from a configuration manager
     * @param configurationManager The {@link ConfigurationManager} to use.
     */
    public LanguageManager(ConfigurationManager configurationManager) {
        this.configurationManager = Validate.notNull(configurationManager, "plugin must not be null!");
        this.languageMap = new HashMap<>();
        this.playerLanguageMap = new HashMap<>();
        this.defaultLanguage = null;
    }

    public ConfigurationManager getConfigurationManager() {
        return this.configurationManager;
    }

    public void saveDefaultLanguages() {
        ConfigurationManager configurationManager = getConfigurationManager();
        configurationManager.saveDefault("language.yml");
        
        for(String languageName : LanguageManager.KNOWN_LANGUAGE_ARRAY) {
            String languageFileName = String.format(Locale.US, "language/%s.lang.yml", languageName);
            YamlConfiguration jarLanguageConfiguration = configurationManager.getInternal(languageFileName);
            if(jarLanguageConfiguration != null) {
                configurationManager.saveDefault(languageFileName);
            }
        }
        
        if(this.defaultLanguage == null) {
            reloadLanguages();
            if(this.defaultLanguage == null) {
                throw new IllegalStateException("Missing default locale translation file!");
            }
        }
    }

    public void reloadLanguages() {
        this.languageMap.clear();
        this.playerLanguageMap.clear();
        IResourceHolder resourceHolder = configurationManager.getResourceHolder();
        Logger logger = resourceHolder.getLogger();

        File dataFolder = resourceHolder.getDataFolder();
        File languageFolder = new File(dataFolder, "language");
        if(!languageFolder.exists() || !languageFolder.isDirectory()) {
            return;
        }

        FilenameFilter filenameFilter = (folder, fileName) -> fileName.endsWith(".lang.yml");
        File[] fileArray = languageFolder.listFiles(filenameFilter);
        if(fileArray == null || fileArray.length == 0) {
            return;
        }

        List<YamlConfiguration> languageConfigurationList = new ArrayList<>();
        for(File file : fileArray) {
            try {
                YamlConfiguration configuration = new YamlConfiguration();
                configuration.load(file);

                configuration.set("language-name", file.getName().replace(".lang.yml", ""));
                languageConfigurationList.add(configuration);
            } catch(IOException | InvalidConfigurationException ex) {
                logger.log(Level.WARNING, "Failed to load a language. Reason:", ex);
            }
        }
        languageConfigurationList.sort(new LanguageConfigurationComparator());

        for(YamlConfiguration configuration : languageConfigurationList) {
            try {
                Language language = loadLanguage(configuration);
                if(language != null) {
                    String languageCode = language.getLanguageCode();
                    this.languageMap.put(languageCode, language);
                }
            } catch(Exception ex) {
                logger.log(Level.WARNING, "Failed to load a language configuration because an error occurred:",
                        ex);
            }
        }

        ConfigurationManager configurationManager = getConfigurationManager();
        configurationManager.reload("language.yml");

        YamlConfiguration languageConfiguration = configurationManager.get("language.yml");
        String defaultLanguageName = languageConfiguration.getString("default-locale");
        if(this.languageMap.containsKey(defaultLanguageName)) {
            this.defaultLanguage = this.languageMap.getOrDefault(defaultLanguageName, null);
        } else {
            logger.warning("Your default language configuration doesn't match any of the existing languages!");
            logger.warning("If you believe this is an error, please contact SirBlobman!");
            logger.warning("Using 'en_us' as default language.");

            this.defaultLanguage = this.languageMap.getOrDefault("en_us", null);
            if(this.defaultLanguage == null) {
                throw new IllegalStateException("Missing 'en_us' translation file!");
            }
        }

        int languageCount = this.languageMap.size();
        logger.info("Successfully loaded " + languageCount + " language(s)");
    }

    public void removeCachedLanguage(OfflinePlayer player) {
        UUID playerId = player.getUniqueId();
        this.playerLanguageMap.remove(playerId);
    }

    @NotNull
    public String getMessage(@Nullable CommandSender sender, @NotNull String key, @Nullable Replacer replacer,
                             boolean color) {
        Validate.notEmpty(key, "key must not be empty!");
        Language language = getLanguage(sender);

        String message = language.getTranslation(key);
        if(message.isEmpty()) return "";

        if(replacer != null) message = replacer.replace(message);
        return (color ? MessageUtility.color(message) : message);
    }

    @NotNull
    @Deprecated
    public String getMessageColored(@Nullable CommandSender sender, @NotNull String key,
                                    @Nullable Replacer replacer) {
        return getMessage(sender, key, replacer, true);
    }

    @NotNull
    @Deprecated
    public String getMessageColored(@Nullable CommandSender sender, @NotNull String key) {
        return getMessageColored(sender, key, null);
    }

    public void sendMessage(@NotNull CommandSender sender, @NotNull String key, @Nullable Replacer replacer,
                            boolean color) {
        Validate.notNull(sender, "sender must not be null!");
        String message = getMessage(sender, key, replacer, color);
        if(!message.isEmpty()) sender.sendMessage(message);
    }

    public void broadcastMessage(@NotNull String key, @Nullable Replacer replacer, boolean color) {
        CommandSender console = Bukkit.getConsoleSender();
        sendMessage(console, key, replacer, color);

        Collection<? extends Player> onlinePlayerList = Bukkit.getOnlinePlayers();
        for(Player player : onlinePlayerList) {
            sendMessage(player, key, replacer, color);
        }
    }

    @NotNull
    public Language getLanguage(CommandSender sender) {
        if(this.defaultLanguage == null) {
            reloadLanguages();
            if(this.defaultLanguage == null) {
                throw new IllegalStateException("Missing default locale translation file!");
            }
        }

        if(!(sender instanceof Player)) {
            return this.defaultLanguage;
        }

        Player player = (Player) sender;
        UUID playerId = player.getUniqueId();
        Language cachedLanguage = this.playerLanguageMap.getOrDefault(playerId, null);
        if(cachedLanguage != null) {
            return cachedLanguage;
        }

        int minorVersion = VersionUtility.getMinorVersion();
        if(minorVersion >= 12) {
            String localeName = player.getLocale();
            if(localeName != null) {
                Language language = this.languageMap.getOrDefault(localeName, null);
                if(language != null) {
                    this.playerLanguageMap.put(playerId, language);
                    return language;
                }
            }
        }

        this.playerLanguageMap.put(playerId, this.defaultLanguage);
        return this.defaultLanguage;
    }

    private Language loadLanguage(YamlConfiguration configuration) throws InvalidConfigurationException {
        String languageName = configuration.getString("language-name");
        if(languageName == null) return null;

        String parentLanguageName = configuration.getString("parent");
        Language parentLanguage = null;
        if(parentLanguageName != null) {
            parentLanguage = this.languageMap.getOrDefault(parentLanguageName, null);
            if(parentLanguage == null) {
                throw new InvalidConfigurationException("parent language not loaded correctly.");
            }
        }

        Language language = (parentLanguage == null ? new Language(languageName)
                : new Language(parentLanguage, languageName));

        Set<String> keySet = configuration.getKeys(true);
        for(String key : keySet) {
            String message;
            if(configuration.isList(key)) {
                List<String> messageList = configuration.getStringList(key);
                message = String.join("\n", messageList);
            } else {
                message = configuration.getString(key);
            }

            if(message != null) {
                language.addTranslation(key, message);
            }
        }

        return language;
    }
}
