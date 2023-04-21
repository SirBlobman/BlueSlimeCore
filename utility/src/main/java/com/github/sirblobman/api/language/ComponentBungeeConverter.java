package com.github.sirblobman.api.language;

import org.jetbrains.annotations.NotNull;

import net.md_5.bungee.api.chat.BaseComponent;

import com.github.sirblobman.api.utility.VersionUtility;
import com.github.sirblobman.api.shaded.adventure.text.Component;
import com.github.sirblobman.api.shaded.adventure.text.serializer.bungeecord.BungeeComponentSerializer;

public final class ComponentBungeeConverter {
    public static @NotNull BungeeComponentSerializer getSerializer() {
        int minorVersion = VersionUtility.getMinorVersion();
        if (minorVersion < 16) {
            return BungeeComponentSerializer.legacy();
        }

        return BungeeComponentSerializer.get();
    }

    public static BaseComponent @NotNull [] toBungee(Component component) {
        BungeeComponentSerializer serializer = getSerializer();
        return serializer.serialize(component);
    }

    public static @NotNull Component fromBungee(BaseComponent @NotNull ... componentArray) {
        BungeeComponentSerializer serializer = getSerializer();
        return serializer.deserialize(componentArray);
    }
}
