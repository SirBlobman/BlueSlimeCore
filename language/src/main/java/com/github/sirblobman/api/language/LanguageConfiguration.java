package com.github.sirblobman.api.language;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import com.github.sirblobman.api.adventure.adventure.key.Key;
import com.github.sirblobman.api.adventure.adventure.sound.Sound;
import com.github.sirblobman.api.adventure.adventure.sound.Sound.Source;
import com.github.sirblobman.api.adventure.adventure.text.Component;
import com.github.sirblobman.api.adventure.adventure.text.minimessage.MiniMessage;
import com.github.sirblobman.api.adventure.adventure.title.Title;
import com.github.sirblobman.api.adventure.adventure.title.Title.Times;
import com.github.sirblobman.api.language.custom.ModifiableMessage;
import com.github.sirblobman.api.language.custom.ModifiableMessageType;
import com.github.sirblobman.api.language.custom.PlayerListInfo;
import com.github.sirblobman.api.utility.Validate;

import me.clip.placeholderapi.libs.kyori.adventure.util.Ticks;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class LanguageConfiguration {
    private final YamlConfiguration configuration;
    private final MiniMessage miniMessage;
    private final Map<String, String> rawMessageMap;
    private final Map<String, Component> messageMap;
    private final Map<String, ModifiableMessage> modifiableMessageMap;
    private final Map<String, Sound> soundMap;
    private final Map<String, Title> titleMap;
    private final Map<String, PlayerListInfo> playerListInfoMap;
    private LanguageConfiguration parent;
    private DecimalFormat decimalFormat;

    public LanguageConfiguration(YamlConfiguration configuration, MiniMessage miniMessage) {
        this.parent = null;
        this.configuration = Validate.notNull(configuration, "configuration must not be null!");
        this.miniMessage = Validate.notNull(miniMessage, "miniMessage must not be null!");

        this.rawMessageMap = new HashMap<>();
        this.messageMap = new HashMap<>();
        this.modifiableMessageMap = new HashMap<>();
        this.soundMap = new HashMap<>();
        this.titleMap = new HashMap<>();
        this.playerListInfoMap = new HashMap<>();
    }

    public Optional<LanguageConfiguration> getParent() {
        return Optional.ofNullable(this.parent);
    }

    public void setParent(LanguageConfiguration parent) {
        this.parent = parent;
    }

    @NotNull
    public YamlConfiguration getOriginalConfiguration() {
        return this.configuration;
    }

    @NotNull
    public MiniMessage getMiniMessage() {
        return this.miniMessage;
    }

    @NotNull
    public String getRawMessage(String path) {
        return this.rawMessageMap.computeIfAbsent(path, this::fetchRawMessage);
    }

    @NotNull
    private String fetchRawMessage(String path) {
        YamlConfiguration originalConfiguration = getOriginalConfiguration();
        if (originalConfiguration.isList(path)) {
            List<String> messageList = originalConfiguration.getStringList(path);
            return String.join("\n", messageList);
        }

        String message = originalConfiguration.getString(path);
        if (message == null) {
            Optional<LanguageConfiguration> optionalParent = getParent();
            if (optionalParent.isPresent()) {
                LanguageConfiguration parent = optionalParent.get();
                return parent.getRawMessage(path);
            }

            return String.format(Locale.US, "{%s}", path);
        }

        return message;
    }

    @NotNull
    public Component getMessage(String path) {
        return this.messageMap.computeIfAbsent(path, this::fetchMessage);
    }

    @NotNull
    private Component fetchMessage(String path) {
        String rawMessage = getRawMessage(path);
        MiniMessage miniMessage = getMiniMessage();
        return miniMessage.deserialize(rawMessage);
    }

    @NotNull
    public ModifiableMessage getModifiableMessage(String path) {
        return this.modifiableMessageMap.computeIfAbsent(path, this::fetchModifiableMessage);
    }

    @NotNull
    private ModifiableMessage fetchModifiableMessage(String path) {
        YamlConfiguration originalConfiguration = getOriginalConfiguration();
        if (originalConfiguration.isConfigurationSection(path)) {
            Component message = getMessage(path + ".content");
            ModifiableMessage modifiableMessage = new ModifiableMessage();
            modifiableMessage.setMessage(message);

            String messageTypeName = originalConfiguration.getString(path + ".type");
            ModifiableMessageType messageType = ModifiableMessageType.parse(messageTypeName);
            modifiableMessage.setType(messageType);
            return modifiableMessage;
        } else {
            Optional<LanguageConfiguration> optionalParent = getParent();
            if (optionalParent.isPresent()) {
                LanguageConfiguration parent = optionalParent.get();
                return parent.getModifiableMessage(path);
            }

            Component message = getMessage(path);
            ModifiableMessage modifiableMessage = new ModifiableMessage();
            modifiableMessage.setMessage(message);
            return modifiableMessage;
        }
    }

    @Nullable
    public Sound getSound(String path) {
        return this.soundMap.computeIfAbsent(path, this::fetchSound);
    }

    @Nullable
    private Sound fetchSound(String path) {
        YamlConfiguration originalConfiguration = getOriginalConfiguration();
        ConfigurationSection section = originalConfiguration.getConfigurationSection(path);
        if (section == null) {
            Optional<LanguageConfiguration> optionalParent = getParent();
            if (optionalParent.isPresent()) {
                LanguageConfiguration parent = optionalParent.get();
                return parent.getSound(path);
            }

            return null;
        }

        @Subst("ignore")
        String soundKeyString = section.getString("sound");
        if (soundKeyString == null || soundKeyString.isEmpty()) {
            Optional<LanguageConfiguration> optionalParent = getParent();
            if (optionalParent.isPresent()) {
                LanguageConfiguration parent = optionalParent.get();
                return parent.getSound(path);
            }

            return null;
        }

        String categoryName = originalConfiguration.getString("category", "master");
        Source category = Source.valueOf(categoryName.toUpperCase(Locale.US));

        Key soundKey = Key.key(soundKeyString);
        float volume = (float) originalConfiguration.getDouble("volume", 1.0D);
        float pitch = (float) originalConfiguration.getDouble("pitch", 1.0D);

        Sound.Builder builder = Sound.sound();
        builder.type(soundKey);
        builder.source(category);
        builder.volume(volume);
        builder.pitch(pitch);
        return builder.build();
    }

    @NotNull
    public Title getTitle(String path) {
        return this.titleMap.computeIfAbsent(path, this::fetchTitle);
    }

    @NotNull
    private Title fetchTitle(String path) {
        YamlConfiguration originalConfiguration = getOriginalConfiguration();
        ConfigurationSection section = originalConfiguration.getConfigurationSection(path);
        if (section == null) {
            Optional<LanguageConfiguration> optionalParent = getParent();
            if (optionalParent.isPresent()) {
                LanguageConfiguration parent = optionalParent.get();
                return parent.getTitle(path);
            }

            return Title.title(Component.empty(), Component.empty());
        }

        int fadeInTicks = section.getInt("fade-in", 10);
        int stayTicks = section.getInt("stay", 70);
        int fadeOutTicks = section.getInt("fade-out", 20);
        Times times = Times.times(Ticks.duration(fadeInTicks), Ticks.duration(stayTicks), Ticks.duration(fadeOutTicks));

        Component titleMessage = getMessage(path + ".title");
        Component subtitleMessage = getMessage(path + ".subtitle");
        return Title.title(titleMessage, subtitleMessage, times);
    }

    @NotNull
    public PlayerListInfo getPlayerListInfo(String path) {
        return this.playerListInfoMap.computeIfAbsent(path, this::fetchPlayerListInfo);
    }

    @NotNull
    private PlayerListInfo fetchPlayerListInfo(String path) {
        YamlConfiguration originalConfiguration = getOriginalConfiguration();
        ConfigurationSection section = originalConfiguration.getConfigurationSection(path);
        if (section == null) {
            Optional<LanguageConfiguration> optionalParent = getParent();
            if (optionalParent.isPresent()) {
                LanguageConfiguration parent = optionalParent.get();
                return parent.getPlayerListInfo(path);
            }

            return new PlayerListInfo();
        }

        PlayerListInfo playerListHeader = new PlayerListInfo();
        Component header = getMessage(path + ".header");
        Component footer = getMessage(path + ".footer");
        playerListHeader.setHeader(header);
        playerListHeader.setFooter(footer);
        return playerListHeader;
    }

    @NotNull
    public DecimalFormat getDecimalFormat() {
        if (this.decimalFormat != null) {
            return this.decimalFormat;
        }

        Optional<LanguageConfiguration> optionalParent = getParent();
        if (optionalParent.isPresent()) {
            LanguageConfiguration parent = optionalParent.get();
            return parent.getDecimalFormat();
        }

        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance(Locale.US);
        this.decimalFormat = new DecimalFormat("0.00", symbols);
        return this.decimalFormat;
    }

    public void setDecimalFormat(DecimalFormat format) {
        this.decimalFormat = Validate.notNull(format, "format must not be null!");
    }
}
