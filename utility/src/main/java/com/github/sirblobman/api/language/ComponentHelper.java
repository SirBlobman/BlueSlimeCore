package com.github.sirblobman.api.language;

import java.util.ArrayList;
import java.util.List;

import com.github.sirblobman.api.shaded.adventure.text.Component;
import com.github.sirblobman.api.shaded.adventure.text.TextComponent;
import com.github.sirblobman.api.shaded.adventure.text.format.TextDecoration;
import com.github.sirblobman.api.shaded.adventure.text.serializer.gson.GsonComponentSerializer;
import com.github.sirblobman.api.shaded.adventure.text.serializer.legacy.LegacyComponentSerializer;
import com.github.sirblobman.api.shaded.adventure.text.serializer.plain.PlainTextComponentSerializer;
import com.github.sirblobman.api.utility.MessageUtility;
import com.github.sirblobman.api.utility.VersionUtility;

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
        TextComponent.Builder builder = Component.text();
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
