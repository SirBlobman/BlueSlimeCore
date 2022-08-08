package com.github.sirblobman.api.language;

import com.github.sirblobman.api.utility.MessageUtility;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent.Builder;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public final class ComponentHelper {
    public static LegacyComponentSerializer getLegacySerializer() {
        return LegacyComponentSerializer.legacySection();
    }

    public static PlainTextComponentSerializer getPlainSerializer() {
        return PlainTextComponentSerializer.plainText();
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

    public static Component wrapNoItalics(Component component) {
        Builder builder = Component.text();
        builder.decoration(TextDecoration.ITALIC, false);
        builder.append(component);
        return builder.build();
    }
}
