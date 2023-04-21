package com.github.sirblobman.api.language;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.github.sirblobman.api.utility.MessageUtility;
import com.github.sirblobman.api.utility.VersionUtility;
import com.github.sirblobman.api.shaded.adventure.text.Component;
import com.github.sirblobman.api.shaded.adventure.text.TextComponent;
import com.github.sirblobman.api.shaded.adventure.text.format.TextDecoration;
import com.github.sirblobman.api.shaded.adventure.text.serializer.gson.GsonComponentSerializer;
import com.github.sirblobman.api.shaded.adventure.text.serializer.legacy.LegacyComponentSerializer;
import com.github.sirblobman.api.shaded.adventure.text.serializer.plain.PlainTextComponentSerializer;

public final class ComponentHelper {
    public static @NotNull LegacyComponentSerializer getLegacySerializer() {
        return LegacyComponentSerializer.legacySection();
    }

    public static @NotNull PlainTextComponentSerializer getPlainSerializer() {
        return PlainTextComponentSerializer.plainText();
    }

    public static @NotNull GsonComponentSerializer getGsonSerializer() {
        int minorVersion = VersionUtility.getMinorVersion();
        if (minorVersion < 16) {
            return GsonComponentSerializer.colorDownsamplingGson();
        } else {
            return GsonComponentSerializer.gson();
        }
    }

    public static @NotNull Component toComponent(@NotNull String legacy) {
        String colored = MessageUtility.color(legacy);
        LegacyComponentSerializer serializer = getLegacySerializer();
        return serializer.deserialize(colored);
    }

    public static @NotNull String toLegacy(@NotNull Component component) {
        LegacyComponentSerializer serializer = getLegacySerializer();
        return serializer.serialize(component);
    }

    public static @NotNull String toPlain(@NotNull Component component) {
        PlainTextComponentSerializer serializer = getPlainSerializer();
        return serializer.serialize(component);
    }

    public static @NotNull String toGson(@NotNull Component component) {
        GsonComponentSerializer serializer = getGsonSerializer();
        return serializer.serialize(component);
    }

    public static @NotNull Component wrapNoItalics(@NotNull Component component) {
        TextComponent.Builder builder = Component.text();
        builder.decoration(TextDecoration.ITALIC, false);
        builder.append(component);
        return builder.build();
    }

    public static @NotNull List<Component> wrapNoItalics(@NotNull Iterable<Component> components) {
        List<Component> newList = new ArrayList<>();
        for (Component component : components) {
            Component noItalics = wrapNoItalics(component);
            newList.add(noItalics);
        }

        return newList;
    }
}
