package com.github.sirblobman.api.language;

import net.md_5.bungee.api.chat.BaseComponent;

import com.github.sirblobman.api.shaded.adventure.text.Component;
import com.github.sirblobman.api.shaded.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import com.github.sirblobman.api.utility.VersionUtility;

public final class ComponentBungeeConverter {
    public static BungeeComponentSerializer getSerializer() {
        int minorVersion = VersionUtility.getMinorVersion();
        if (minorVersion < 16) {
            return BungeeComponentSerializer.legacy();
        }

        return BungeeComponentSerializer.get();
    }

    public static BaseComponent[] toBungee(Component component) {
        BungeeComponentSerializer serializer = getSerializer();
        return serializer.serialize(component);
    }

    public static Component fromBungee(BaseComponent... components) {
        BungeeComponentSerializer serializer = getSerializer();
        return serializer.deserialize(components);
    }
}
