package com.github.sirblobman.api.language;

import java.util.ArrayList;
import java.util.List;

import com.github.sirblobman.api.utility.MessageUtility;
import com.github.sirblobman.api.utility.VersionUtility;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent.Builder;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public final class ComponentHelper {
    public static LegacyComponentSerializer getLegacySerializer() {
        return LegacyComponentSerializer.legacySection();
    }

    public static PlainTextComponentSerializer getPlainSerializer() {
        return PlainTextComponentSerializer.plainText();
    }

    public static GsonComponentSerializer getGsonSerializer() {
        int minorVersion = VersionUtility.getMinorVersion();
        if (minorVersion < 16) {
            return GsonComponentSerializer.colorDownsamplingGson();
        } else {
            return GsonComponentSerializer.gson();
        }
    }

    public static Component toComponent(String legacy) {
        String colored = MessageUtility.color(legacy);
        LegacyComponentSerializer serializer = getLegacySerializer();
        return serializer.deserialize(colored);
    }

    public static String toLegacy(Component component) {
        LegacyComponentSerializer serializer = getLegacySerializer();
        return serializer.serialize(component);
    }

    public static String toPlain(Component component) {
        PlainTextComponentSerializer serializer = getPlainSerializer();
        return serializer.serialize(component);
    }

    public static String toGson(Component component) {
        GsonComponentSerializer serializer = getGsonSerializer();
        return serializer.serialize(component);
    }

    public static Component wrapNoItalics(Component component) {
        Builder builder = Component.text();
        builder.decoration(TextDecoration.ITALIC, false);
        builder.append(component);
        return builder.build();
    }

    public static List<Component> wrapNoItalics(Iterable<Component> components) {
        List<Component> newList = new ArrayList<>();
        for (Component component : components) {
            Component noItalics = wrapNoItalics(component);
            newList.add(noItalics);
        }

        return newList;
    }
}
