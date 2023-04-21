package com.github.sirblobman.api.language;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import com.github.sirblobman.api.utility.ConfigurationHelper;
import com.github.sirblobman.api.language.custom.ModifiableMessage;
import com.github.sirblobman.api.language.custom.ModifiableMessageType;
import com.github.sirblobman.api.language.custom.PlayerListInfo;
import com.github.sirblobman.api.shaded.adventure.key.Key;
import com.github.sirblobman.api.shaded.adventure.sound.Sound;
import com.github.sirblobman.api.shaded.adventure.sound.Sound.Source;
import com.github.sirblobman.api.shaded.adventure.text.Component;
import com.github.sirblobman.api.shaded.adventure.text.minimessage.MiniMessage;
import com.github.sirblobman.api.shaded.adventure.title.Title;
import com.github.sirblobman.api.shaded.adventure.title.Title.Times;
import com.github.sirblobman.api.shaded.adventure.util.Ticks;

import org.intellij.lang.annotations.Subst;

public final class LanguageConfiguration {
    private final YamlConfiguration configuration;
    private final MiniMessage miniMessage;
    private final Map<String, String> rawMessageMap;
    private final Map<String, Component> messageMap;
    private final Map<String, List<Component>> messageListMap;
    private final Map<String, ModifiableMessage> modifiableMessageMap;
    private final Map<String, Sound> soundMap;
    private final Map<String, Title> titleMap;
    private final Map<String, PlayerListInfo> playerListInfoMap;
    private LanguageConfiguration parent;
    private DecimalFormat decimalFormat;

    public LanguageConfiguration(@NotNull YamlConfiguration configuration, @NotNull MiniMessage miniMessage) {
        this.parent = null;
        this.configuration = configuration;
        this.miniMessage = miniMessage;

        this.rawMessageMap = new ConcurrentHashMap<>();
        this.messageMap = new ConcurrentHashMap<>();
        this.messageListMap = new ConcurrentHashMap<>();
        this.modifiableMessageMap = new ConcurrentHashMap<>();
        this.soundMap = new ConcurrentHashMap<>();
        this.titleMap = new ConcurrentHashMap<>();
        this.playerListInfoMap = new ConcurrentHashMap<>();
    }

    public @NotNull Optional<LanguageConfiguration> getParent() {
        return Optional.ofNullable(this.parent);
    }

    public void setParent(@Nullable LanguageConfiguration parent) {
        this.parent = parent;
    }

    public @NotNull YamlConfiguration getOriginalConfiguration() {
        return this.configuration;
    }

    public @NotNull MiniMessage getMiniMessage() {
        return this.miniMessage;
    }

    public @NotNull String getRawMessage(@NotNull String path) {
        return this.rawMessageMap.computeIfAbsent(path, this::fetchRawMessage);
    }

    private @NotNull String fetchRawMessage(@NotNull String path) {
        YamlConfiguration originalConfiguration = getOriginalConfiguration();
        if (originalConfiguration.isList(path)) {
            List<String> messageList = originalConfiguration.getStringList(path);
            return String.join("\n", messageList);
        }

        String message = originalConfiguration.getString(path);
        if (message != null) {
            return message;
        }

        Optional<LanguageConfiguration> optionalParent = getParent();
        if (optionalParent.isPresent()) {
            LanguageConfiguration parent = optionalParent.get();
            return parent.getRawMessage(path);
        }

        return String.format(Locale.US, "{%s}", path);
    }

    public @NotNull Component getMessage(@NotNull String path) {
        return this.messageMap.computeIfAbsent(path, this::fetchMessage);
    }

    private @NotNull Component fetchMessage(@NotNull String path) {
        String rawMessage = getRawMessage(path);
        MiniMessage miniMessage = getMiniMessage();
        return miniMessage.deserialize(rawMessage);
    }

    public @NotNull List<Component> getMessageList(@NotNull String path) {
        return this.messageListMap.computeIfAbsent(path, this::fetchMessageList);
    }

    private @NotNull List<Component> fetchMessageList(@NotNull String path) {
        String baseMessage = getRawMessage(path);
        String[] rawMessages = baseMessage.split(Pattern.quote("\n"));
        List<Component> messages = new ArrayList<>();

        MiniMessage miniMessage = getMiniMessage();
        for (String rawMessage : rawMessages) {
            Component component = miniMessage.deserialize(rawMessage);
            messages.add(component);
        }

        return messages;
    }

    public @NotNull ModifiableMessage getModifiableMessage(@NotNull String path) {
        return this.modifiableMessageMap.computeIfAbsent(path, this::fetchModifiableMessage);
    }

    private @NotNull ModifiableMessage fetchModifiableMessage(@NotNull String path) {
        YamlConfiguration originalConfiguration = getOriginalConfiguration();
        if (originalConfiguration.isConfigurationSection(path)) {
            Component message = getMessage(path + ".content");
            ModifiableMessage modifiableMessage = new ModifiableMessage();
            modifiableMessage.setMessage(message);

            String messageTypeName = originalConfiguration.getString(path + ".type");
            ModifiableMessageType messageType = ConfigurationHelper.parseEnum(ModifiableMessageType.class,
                    messageTypeName, ModifiableMessageType.CHAT);
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

    public @Nullable Sound getSound(@NotNull String path) {
        return this.soundMap.computeIfAbsent(path, this::fetchSound);
    }

    private @Nullable Sound fetchSound(@NotNull String path) {
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

    public @NotNull Title getTitle(@NotNull String path) {
        return this.titleMap.computeIfAbsent(path, this::fetchTitle);
    }

    private @NotNull Title fetchTitle(@NotNull String path) {
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
        Times times = Times.times(ticks(fadeInTicks), ticks(stayTicks), ticks(fadeOutTicks));

        Component titleMessage = getMessage(path + ".title");
        Component subtitleMessage = getMessage(path + ".subtitle");
        return Title.title(titleMessage, subtitleMessage, times);
    }

    private @NotNull Duration ticks(int ticks) {
        return Ticks.duration(ticks);
    }

    public @NotNull PlayerListInfo getPlayerListInfo(@NotNull String path) {
        return this.playerListInfoMap.computeIfAbsent(path, this::fetchPlayerListInfo);
    }

    private @NotNull PlayerListInfo fetchPlayerListInfo(@NotNull String path) {
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

    public @NotNull DecimalFormat getDecimalFormat() {
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

    public void setDecimalFormat(@NotNull DecimalFormat format) {
        this.decimalFormat = format;
    }
}
